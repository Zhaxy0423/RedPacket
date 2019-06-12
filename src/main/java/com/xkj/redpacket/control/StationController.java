package com.xkj.redpacket.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StationController {
    @RequestMapping("/station")
    public String station(){
        return "grabRedPacket";
    }
}
