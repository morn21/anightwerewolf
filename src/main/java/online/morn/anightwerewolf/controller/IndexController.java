package online.morn.anightwerewolf.controller;

import online.morn.anightwerewolf.DO.UserDO;
import online.morn.anightwerewolf.service.UserService;
import online.morn.anightwerewolf.util.MyException;
import online.morn.anightwerewolf.util.SessionKey;
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
    private UserService userService;

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/room")
    public String room(HttpServletRequest request) {
        try {
            UserDO userDO = (UserDO)request.getSession().getAttribute(SessionKey.USER);//获得用户实例
            if(userDO == null){
                userDO = userService.generateUser();
                request.getSession().setAttribute(SessionKey.USER, userDO);//设置用户实例
            }
        } catch (MyException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return "room";
    }

}
