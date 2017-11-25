package online.morn.anightwerewolf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面
 * @auther Horner 2017/11/26 0:22
 */
@Controller
public class IndexController {

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/room")
    public String room() {
        return "room";
    }

}
