package com.xkj.redpacket.dao;

import com.xkj.redpacket.domain.UserRedPacket;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

/**
 * @author 渣小宇
 */

@Mapper
@Repository
public interface UserRedPacketDao {
    String TABLE_NAME = " t_user_red_packet ";
    String INSERT_FIELDS = " red_packet_id, user_id, amount, grab_time, note ";
    /**
     * 插入抢红包信息
     * @param userRedPacket 抢红包信息
     * @return 影响记录数
     * 注解@Options 获取插入的主键信息
     */
    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS,") values (#{redPacketId},#{userId},#{amount},now(),#{note})"})
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int grabRedPacket(UserRedPacket userRedPacket);
}
