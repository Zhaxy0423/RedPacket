# 高并发业务实现——抢红包
```
Goal:模拟20万元的红包，共分2万可抢的小红包，有3万人抢夺的场景
```
### 1.数据库建表
-|红包表t_red_packet|-
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

---
-|用户抢红包表t_user_red_packet|-
-|-|-
id|int(12)|红包编号pk
red_packet_id|int(12)|抢红包用户id
user_id|int(12)|发红包用户
amount|decimal(16,2)|红包金额
grab_date|timestamp|抢红包时间
note|varchar(256)|备注
