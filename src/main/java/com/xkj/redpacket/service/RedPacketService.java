package com.xkj.redpacket.service;

import com.xkj.redpacket.domain.RedPacket;
/**
 * @author 渣小宇
 */

public interface RedPacketService {
    /**
     * 获取红包
     * @param id 红包编号
     * @return 红包信息
     */
    RedPacket getRedPacket(Long id);

    /**
     * 加入悲观锁后的获取红包信息
     * @param id 红包编号
     * @return 红包具体信息
     */
    RedPacket getRedPacketForUpdate(Long id);

    /**
     * 扣减红包
     * @param id 编号
     * @return 影响记录数
     */
   int decreaseRedPacket(Long id);

    /**
     * 引入乐观锁扣减红包
     * @param id 红包编号
     * @param version 版本号
     * @return 影响记录数
     */
   int decreaseRedPacketForVersion(Long id,Integer version);
}
