package com.xkj.redpacket.control;

import com.xkj.redpacket.service.UserRedPacketService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 渣小宇
 */
@RestController
@RequestMapping("/userRedPacket")
public class UserRedPacketController {
    private final UserRedPacketService userRedPacketService;

    @Autowired
    public UserRedPacketController(UserRedPacketService userRedPacketService) {
        this.userRedPacketService = userRedPacketService;
    }
    @RequestMapping(value="/grabRedPacket")
    @ResponseBody
    public Map<String,Object> grabRedPacketForUpdate(@Param("redPacketId") Long redPacketId, @Param("userId") Long userId){
        int result = userRedPacketService.grabRedPacket(redPacketId, userId);
        Map<String,Object> redPacketMap = new HashMap<>(16);
        boolean flag = result > 0;
        redPacketMap.put("success",flag);
        redPacketMap.put("message",flag ? "抢红包成功":"抢红包失败");
        return redPacketMap;
    }
}
