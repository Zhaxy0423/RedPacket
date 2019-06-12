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
     * 获取红包信息
     * 如果红包库存大于0，扣减剩余红包数目，生成抢红包的信息         *
     * @param redPacketId 红包编号
     * @param userId 抢红包用户信息
     * @return 影响记录数
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public int grabRedPacket(Long redPacketId, Long userId) {
        RedPacket redPacket = redPacketDao.getRedPacket(redPacketId);
        if(redPacket.getStock()>0){
            redPacketDao.decreaseRedPacket(redPacketId);
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
