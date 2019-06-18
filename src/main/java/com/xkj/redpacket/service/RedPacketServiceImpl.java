package com.xkj.redpacket.service;

import com.xkj.redpacket.dao.RedPacketDao;
import com.xkj.redpacket.domain.RedPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 红包服务实现类
 * @author 渣小宇
 */
@Service
public class RedPacketServiceImpl implements RedPacketService{
    /**
     * 不建议使用(变量)属性输入的方法，而是使用构造器注入
     * 如果这个类使用了依赖注入的类，那么这个类摆脱了这几个依赖必须也能正常运行
     */
    private final RedPacketDao redPacketDao;

    @Autowired
    public RedPacketServiceImpl(RedPacketDao redPacketDao) {
        this.redPacketDao = redPacketDao;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public RedPacket getRedPacket(Long id) {

        return redPacketDao.getRedPacket(id);
    }

    @Override
    public RedPacket getRedPacketForUpdate(Long id) {
        return redPacketDao.getRedPacketForUpdate(id);
    }

    /**
     * 使用@Transactional事务注解需要配置回滚或在方法中显式回滚
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public int decreaseRedPacket(Long id) {
        return redPacketDao.decreaseRedPacket(id);
    }

    @Override
    public int decreaseRedPacketForVersion(Long id, Integer version) {
        return redPacketDao.decreaseRedPacketForVersion(id,version);
    }
}
