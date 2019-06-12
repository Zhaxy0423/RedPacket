package com.xkj.redpacket.dao;

import com.xkj.redpacket.domain.RedPacket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;


/**
 * @author 渣小宇
 */

@Mapper
@Repository
public interface RedPacketDao {
    String TABLE_NAME = " t_red_packet ";
    String SELECT_FIELDS = " id , user_id, amount, send_date, total, unit_amount, stock, version, note ";
    /**
     * 获取红包信息
     * @param id 红包编号
     * @return 红包的具体信息
     */
    @Select({" select ",SELECT_FIELDS," from ",TABLE_NAME," where id = #{id}"})
    RedPacket getRedPacket(Long id);

    /**
     * 扣减红包数
     * @param id 红包编号
     * @return 更新记录数
     */
    @Update({" update ",TABLE_NAME," set stock = stock-1 where id = #{id}"})
    int decreaseRedPacket(Long id);
}
