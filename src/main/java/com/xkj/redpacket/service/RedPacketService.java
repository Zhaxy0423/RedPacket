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
     * 扣减红包
     * @param id 编号
     * @return 影响记录数
     */
   int decreaseRedPacket(Long id);
}
