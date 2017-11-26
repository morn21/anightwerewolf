package online.morn.anightwerewolf.controller.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import online.morn.anightwerewolf.DO.*;
import online.morn.anightwerewolf.service.*;
import online.morn.anightwerewolf.util.MyException;
import online.morn.anightwerewolf.util.SessionKey;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 房间
 * @auther Horner 2017/11/26 0:07
 */
@RequestMapping(value = "/room")
@RestController
public class RoomController {
    private Logger logger = Logger.getLogger(RoomController.class);

    @Autowired
    private RoomService roomService;
    @Autowired
    private RoleCardService roleCardService;
    @Autowired
    private RoomRoleCardService roomRoleCardService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityDetailService activityDetailService;

    /**
     * 进入房间
     * @auther Horner 2017/11/26 0:06
     * @param modelMap
     * @param request
     * @param name
     * @param password
     * @return
     */
    @RequestMapping(value = "/intoRoom.json", method = {RequestMethod.GET , RequestMethod.POST})
    public String intoRoom(ModelMap modelMap, HttpServletRequest request, String name, String password) {
        try {
            if(StringUtils.isBlank(name)){
                throw new MyException("房间号不能为空");
            }
            if(StringUtils.isBlank(password)){
                throw new MyException("密码不能为空");
            }
            RoomDO roomDO = roomService.loginRoom(name, password);
            request.getSession().setAttribute(SessionKey.ROOM,roomDO);//设置房间实例
            modelMap.put("success",true);
        } catch (MyException e) {
            modelMap.put("success",false);
            modelMap.put("msg",e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return JSONObject.toJSONString(modelMap);
    }

    /**
     * 创建房间
     * @auther Horner 2017/11/26 0:06
     * @param modelMap
     * @param request
     * @param password
     * @param roleCardListStr
     * @return
     */
    @RequestMapping(value = "/createRoom.json", method = {RequestMethod.GET , RequestMethod.POST})
    public String createRoom(ModelMap modelMap, HttpServletRequest request, String password, String roleCardListStr) {
        try {
            /**验证*/
            if(StringUtils.isBlank(password)){
                throw new MyException("密码不能为空");
            }
            if(StringUtils.isBlank(roleCardListStr)){
                throw new MyException("roleCardListStr不能为空");
            }
            List<RoleCardDO> roleCardDOList = JSON.parseArray(roleCardListStr,RoleCardDO.class);
            int cardCount = 0;
            for(RoleCardDO roleCardDO : roleCardDOList){
                if(roleCardDO.getIsSelected() == 1){
                    cardCount += roleCardDO.getCardCount();//累加人数
                }
            }
            if(cardCount - 3 < 3){
                throw new MyException("创建房间最少需要三人");
            }
            /**生成房间*/
            RoomDO roomDO = roomService.generateRoom(password,cardCount - 3);
            request.getSession().setAttribute(SessionKey.ROOM,roomDO);//设置房间实例
            roomRoleCardService.generateRoomRoleCard(roomDO.getId(),roleCardDOList);//生成房间角色卡列表
            modelMap.put("success",true);
        } catch (MyException e) {
            modelMap.put("success",false);
            modelMap.put("msg",e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return JSONObject.toJSONString(modelMap);
    }

    /**
     * 加载房间
     * @auther Horner 2017/11/26 0:08
     * @param modelMap
     * @param request
     * @return
     */
    @RequestMapping(value = "/loadRoom.json", method = {RequestMethod.GET , RequestMethod.POST})
    public String loadRoom(ModelMap modelMap, HttpServletRequest request) {
        try {
            /**验证并生成用户*/
            RoomDO roomDO = (RoomDO)request.getSession().getAttribute(SessionKey.ROOM);//获得房间实例
            if(roomDO == null){
                throw new MyException("房间未登录");
            }
            UserDO userDO = (UserDO)request.getSession().getAttribute(SessionKey.USER);//获得用户实例
            if(userDO == null){
                throw new MyException("用户未登录");
            }
            /**获得房间 以及 房间角色卡信息*/
            List<RoleCardDO> roleCardDOList = roleCardService.findRoleCardByRoomId(roomDO.getId());
            if(roomDO == null){
                throw new MyException("房间没找到");
            }
            if(roleCardDOList == null){
                throw new MyException("房间角色卡列表没找到");
            }
            Map<String,Object> dataMap = new HashMap<>();
            dataMap.put("user",userDO);
            dataMap.put("room",roomDO);
            dataMap.put("roleCardList",roleCardDOList);
            modelMap.put("success",true);
            modelMap.put("data",dataMap);
        } catch (MyException e) {
            modelMap.put("success",false);
            modelMap.put("msg",e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return JSONObject.toJSONString(modelMap);
    }

    /**
     * 加载座号
     * @auther Horner 2017/11/26 11:42
     * @param modelMap
     * @param request
     * @return
     */
    @RequestMapping(value = "/loadSeatNum.json", method = {RequestMethod.GET , RequestMethod.POST})
    public String loadSeatNum(ModelMap modelMap, HttpServletRequest request) {
        try {
            RoomDO roomDO = (RoomDO)request.getSession().getAttribute(SessionKey.ROOM);//获得房间实例
            if(roomDO == null){
                throw new MyException("房间未登录");
            }
            UserDO userDO = (UserDO)request.getSession().getAttribute(SessionKey.USER);//获得用户实例
            if(userDO == null){
                throw new MyException("用户未登录");
            }
            ActivityDO activityDO = activityService.findUnfinishedActivityByRoomId(roomDO.getId());
            List<ActivityDetailDO> activityDetailDOList = activityDetailService.findActivityDetailListByActivityId(activityDO.getId());

            Map<String,Object> dataMap = new HashMap<>();
            dataMap.put("room",roomDO);
            dataMap.put("activity",activityDO);
            dataMap.put("activityDetailList",activityDetailDOList);
            modelMap.put("success",true);
            modelMap.put("data",dataMap);
        } catch (MyException e) {
            modelMap.put("success",false);
            modelMap.put("msg",e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return JSONObject.toJSONString(modelMap);
    }
}
