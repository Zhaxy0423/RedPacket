package com.xkj.redpacket.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 红包表
 * @author 渣小宇
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RedPacket implements Serializable {
    /**
     * 红包编号
     */
    private Long id;
    /**
     * 发红包用户
     */
    private Long userId;
    /**
     * 红包金额
     */
    private Double amount;
    /**
     * 发红包时间
     */
    private Timestamp sendDate;
    /**
     * 小红包总数
     */
    private Integer total;
    /**
     * 单个红包金额
     */
    private Double unitAmount;
    /**
     * 剩余小红包个数
     */
    private Integer stock;
    /**
     * 版本
     */
    private Integer version;
    /**
     * 备注
     */
    private String note;


}
