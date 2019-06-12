package com.xkj.redpacket.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 用户抢红包表
 * @author 渣小宇
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRedPacket implements Serializable {
    /**
     * 编号
     */
    private Long id;
    /**
     * 红包编号
     */
    private Long redPacketId;
    /**
     * 抢红包用户编号
     */
    private Long userId;
    /**
     * 单个红包金额
     */
    private Double amount;
    /**
     * 抢红包时间
     */
    private Timestamp grabDate;
    /**
     * 备注
     */
    private String note;

}
