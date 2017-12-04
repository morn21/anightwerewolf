package online.morn.anightwerewolf.controller;

import online.morn.anightwerewolf.DO.RoomDO;
import online.morn.anightwerewolf.service.RoomService;
import online.morn.anightwerewolf.util.MyException;
import online.morn.anightwerewolf.util.enumeration.SessionKey;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 页面
 * @auther Horner 2017/11/26 0:22
 */
@Controller
public class IndexController {

    @Autowired
    private RoomService roomService;

    @RequestMapping(value = {"/","/index"})
    public String index() {
        return "index";
    }

    @RequestMapping("/start")
    public String start() {
        return "start";
    }

    @RequestMapping("/room")
    public String room() {
        return "room";
    }

    @RequestMapping("/activity")
    public String activity() {
        return "activity";
    }

    @RequestMapping("/result")
    public String result() {
        return "result";
    }

    @RequestMapping("/intoRoom")
    public String intoRoom(HttpServletRequest request, String id) {
        try {
            if(StringUtils.isBlank(id)){
                throw new MyException("ID不能为空");
            }
            RoomDO roomDO = roomService.findRoomById(id);
            request.getSession().setAttribute(SessionKey.ROOM, roomDO);//设置房间实例
        } catch (MyException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:room";
    }
}
