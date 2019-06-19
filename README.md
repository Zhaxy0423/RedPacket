# 高并发业务实现——抢红包
> ProjectGoal:模拟20万元的红包，共分2万可抢的小红包，有3万人抢夺的场景
> 
> ProjectLink:<https://github.com/Zhaxy0423/RedPacket>

### 1.MySql数据库建表
***(1)红包表t_red_packet***

字段|参数范围|字段释义
-|-|-
id|int(12)|红包编号pk
user_id|int(12)|发红包用户
amount|decimal(16,2)|红包金额
send_date|timestamp|发红包时间
total|int(12)|小红包总数
unit_amount|decimal(12)|单个红包金额
stock|int(12)|剩余小红包个数
version|int(12)|版本
note|varchar(256)|备注

***(2)用户抢红包表t_user_red_packet***

字段|参数范围|字段释义
-|-|-
id|int(12)|红包编号pk
red_packet_id|int(12)|抢红包用户id
user_id|int(12)|发红包用户
amount|decimal(16,2)|红包金额
grab_date|timestamp|抢红包时间
note|varchar(256)|备注

建好表后向t_red_packet表插入一个20万元金额，2万个小红包，每个10元的红包数据
### 2.Domain层新建RedPacket和UserRedPacket两个POJO类
（1）RedPacket保存红包信息  

（2）UserRedPacket保存抢红包信息  

注意事项：使用lombok插件注解@Getter、@Setter及@Controller简化代码
### 3.使用MyBatis实现Dao层进行数据库操作
（1）大红包查询及扣减红包数目RedPacketDao
```java
//获取红包信息
RedPacket getRedPacket(Long id);
//扣减抢红包数
int decreaseRedPacket(Long id);
```
备注：上述数据库操作基于SpringBoot和MyBatis通过注解@Select({"sql语句"})的方式实现

（2）抢红包信息记录UserRedPacketDao
```java
//插入抢红包具体信息，并返回影响记录数
int grabRedPacket(UserRedPacket userRedPacket);
```
备注：使用@Insert()注解时需要设置useGeneratedKeys和keyProperty，返回数据库生成的主键信息，从而根据主键获取抢红包的具体信息
### 4.业务逻辑接口及实现类
（1）RedPacketServiceImpl实现类中需要配置事务注解@Transactionnal，让程序能在事务中运行，以保证数据的一致性

* 程序中采用Isolation.READ_COMMITTED读/写提交的隔离级别，主要是提高数据库的并发能力；
* 而传播行为采用Propagation.REQUIRED，如果存在事务则沿用当前事务，若没有则创建事务。

（2） UserRedPacketServiceImpl核心方法grabRedPacket的实现逻辑:

* 获取红包信息
* 若红包库存大于0，抢夺红包并将生成的额红包信息保存到数据库
* 若红包库存没有存量，则返回影响记录数为0，抢红包失败
### 4.业务逻辑接口及实现类
（1）UserRedPacketController控制器
```java
@RestController
@RequestMapping("/userRedPacket")
public class UserRedPacketController {
    private final UserRedPacketService userRedPacketService;
    @Autowired
    public UserRedPacketController(UserRedPacketService userRedPacketService) {
        this.userRedPacketService = userRedPacketService;
    }
    @RequestMapping(value="/grabRedPacket")
    @ResponseBody
    public Map<String,Object> grabRedPacket(@Param("redPacketId") Long redPacketId, @Param("userId") Long userId){
        int result = userRedPacketService.grabRedPacket(redPacketId, userId);
        Map<String,Object> redPacketMap = new HashMap<>(16);
        boolean flag = result > 0;
        redPacketMap.put("success",flag);
        redPacketMap.put("message",flag ? "抢红包成功":"抢红包失败");
        return redPacketMap;
    }
}
```
需要说明的是，Alibaba检测代码规范插件显示以如下方式注入(变量注入)不规范，原因是加载顺序的问题，@Autowired写在变量上的注入要等到类完全加载完，才会将相应的bean注入，而变量是在加载类的时候按照相应顺序加载的，所以变量的加载要早于@Autowired变量的加载
```java
@Autowired
private UserRedPacketService userRedPacketService;
```
更改为当前类的构造器注入，@Autowired一定要等本类构造完成后，才能从外部引用设置进来，所以@Autowired的注入时间一定会晚于构造函数的执行时间
```java
private final UserRedPacketService userRedPacketService;
@Autowired
public UserRedPacketController(UserRedPacketService userRedPacketService) {
    this.userRedPacketService = userRedPacketService;
}
```
***补充：构造器注入的方式能够保证注入的组件不可变，并且确保需要的依赖不为空，且构造器注入的依赖总是能够在返回客户端（组件）代码的时候保证完全初始化的状态。***

* 依赖不可变：final关键字
* 依赖不为空：当要实例化Controller控制器的时候，由于自己实现了有参数的构造函数，所以不会调用默认构造函数，那么就需要Spring容器传入所需要的参数，所以就两种情况：1. 有该类型的参数->传入；2. 无该类型的参数->报错，所以保证不会为空。
* 完全初始化的状态：向构造器传参之前，要确保注入的内容不为空，那么肯定要调用依赖组件的构造方法完成实例化。而在Java类加载实例化的过程中，构造方法是最后一步，所以返回来的都是初始化之后的状态。

（2）JSP页面使用JavaScript模拟3万人同时抢红包的场景
```java
 <!--加载Query文件-->
    <script type="text/javascript" src="https://code.jquery.com/jquery-3.2.0.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            //模拟30000个异步请求，进行并发
            var max = 30000;
            for (var i = 1; i <= max; i++) {
                //jQuery的post请求，请注意这是异步请求
                $.post({
                    //请求抢id为1的红包
                    url: "./userRedPacket/grabRedPacket?redPacketId=i&userId=" + i,
                    //成功后的方法
                    success: function (result) {
                    }
                });
            }
        });
    </script>
```
### 5.悲观锁解决超发问题
（1）***超发现象：*** 多线程下数据不一致造成

（2）***悲观锁(对更新的数据枷锁)：***在SQL查询语句中加入for update，将持有对数据库记录的行更新锁；这就意味着在高并发场景下，当一个事务持有了这个更新锁才能继续操作，其他线程要等到该事务提交并释放资源才能竞争资源在获取锁后更新记录

（3）***性能下降：*** 对于悲观锁来说，当一条线程抢占了资源后，其他的线程将得不到资源，而CPU会将这些得不到资源的线程挂起，它们也会消耗CPU资源。一旦持有锁的线程提交事务释放锁，被挂起的线程则会竞争线程资源，竞争到的线程就会被CPU恢复到运行状态继续运行。因而使用悲观锁会造成大量的线程被频繁挂起和恢复，十分消化资源，其只有一个线程能独占这个资源使得悲观锁也称为独占锁(阻塞锁)
### 6.乐观锁解决性能下降问题
（1）***CAS原理：*** 对于多个线程共同的资源，先保存一个旧值(读入线程共享数据保存)，在进行业务逻辑操作时，先比较数据库当前的值与旧值是否一致，如果一致则进行当前业务操作，如果不一致则认为旧值已经被其他线程修改过了，不再进行操作

（2）***ABA问题：*** 业务逻辑存在回退的可能性

（3）***乐观锁：*** 在红包表中引入版本号，在扣减红包时，增加对版本号的判断，每次扣减红包都会对版本号加1，保证每次更新在版本号上有记录，从而避免ABA问题，对于查询语句不适用for update语句避免锁的发生，也就没有线程阻塞的问题存在。
```java
if(redPacket.getStock()>0){
    int update = redPacketDao.decreaseRedPacketForVersion(redPacketId,redPacket.getVersion());
    if(update==0){
        return FAILED;
    }
}
```
（4）***版本号：*** version值一开始就保存在对象中，当扣减时，再次传递给SQL，让SQL对数据库中的version和当前线程的旧值version进行比较，如果一致则插入红包数据，否则不进行操作

（5）***问题：*** 存在大量因为版本不一致的原因造成抢红包失败的请求
### 7.重入锁解决版本不一致更新失败的问题
（1）***按时间戳重入：*** 在一定时间戳内，更新失败的数据会循环到成功为止，直到超过时间戳，不成功才会退出，返回失败
```java
 Long start =System.currentTimeMillis();
 while(true){
    Long end = System.currentTimeMillis();
    if(end-start>100){
       return FAILED;
    }
}
```
（2）***按次数重入：*** 按时间戳重入并不稳定，会随着系统的空闲或繁忙导致重入次数不一致，而且过多的重试会导致过多SQL被执行的问题，影响数据库操作的性能，因而考虑对更新失败的数据尝试限定的次数

