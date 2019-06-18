package com.xkj.redpacket.service;

/**
 *@author 渣小宇
 */

public interface UserRedPacketService {
    /**
     * 保存抢红包信息
     * @param redPacketId 红包编号
     * @param userId 抢红包用户信息
     * @return 影响记录数
     */
     int grabRedPacket(Long redPacketId,Long userId);

}
