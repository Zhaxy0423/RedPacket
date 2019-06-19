package com.xkj.redpacket.service;

import com.xkj.redpacket.dao.RedPacketDao;
import com.xkj.redpacket.dao.UserRedPacketDao;
import com.xkj.redpacket.domain.RedPacket;
import com.xkj.redpacket.domain.UserRedPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 渣小宇
 */

@Service
public class UserRedPacketServiceImpl implements UserRedPacketService {

    private final UserRedPacketDao userRedPacketDao;
    private final RedPacketDao redPacketDao;
    @Autowired
    public UserRedPacketServiceImpl(UserRedPacketDao userRedPacketDao, RedPacketDao redPacketDao) {
        this.userRedPacketDao = userRedPacketDao;
        this.redPacketDao = redPacketDao;
    }
    private static final int FAILED = 0;

    /**
     *
     * v3.0加入乐观锁（版本号）获取红包信息
     * 如果红包库存大于0，扣减剩余红包数目，生成抢红包的信息
     *              * 获取红包信息后，再次传入线程保存的version旧值给SQL判断，
     *              * 是否有其他线程修改过数据
     * v4.0针对乐观锁造成大量数据更新失败的问题，考虑重入机制（按时间戳）
     *              * 记录开始时间，获取当前循环时间，等待成功或者时间超出100ms则退出
     * @param redPacketId 红包编号
     * @param userId 抢红包用户信息
     * @return 影响记录数
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public int grabRedPacket(Long redPacketId, Long userId) {
        Long start =System.currentTimeMillis();
        while(true){
            Long end = System.currentTimeMillis();
            if(end-start>100){
                return FAILED;
            }
            RedPacket redPacket = redPacketDao.getRedPacket(redPacketId);
            if(redPacket.getStock()>0){
                int update = redPacketDao.decreaseRedPacketForVersion(redPacketId,redPacket.getVersion());
                if(update==0){
                    continue;
                }
                UserRedPacket userRedPacket = new UserRedPacket();
                userRedPacket.setRedPacketId(redPacketId);
                userRedPacket.setUserId(userId);
                userRedPacket.setAmount(redPacket.getUnitAmount());
                userRedPacket.setNote("抢红包 "+ redPacketId);
                return userRedPacketDao.grabRedPacket(userRedPacket);
            }
            return FAILED;
        }

    }

}
