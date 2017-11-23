package online.morn.anightwerewolf.controller.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import online.morn.anightwerewolf.DO.RoleCardDO;
import online.morn.anightwerewolf.DO.RoomDO;
import online.morn.anightwerewolf.DO.RoomRoleCardDO;
import online.morn.anightwerewolf.mapper.RoomMapper;
import online.morn.anightwerewolf.mapper.RoomRoleCardMapper;
import online.morn.anightwerewolf.util.IdUtil;
import online.morn.anightwerewolf.util.SessionKey;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.List;

@RequestMapping(value = "/room")
@RestController
public class RoomController {
    private Logger logger = Logger.getLogger(RoomController.class);

    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private RoomRoleCardMapper roomRoleCardMapper;

    /**
     * 进入房间
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
                throw new Exception("房间号不能为空");
            }
            if(StringUtils.isBlank(password)){
                throw new Exception("密码不能为空");
            }
            RoomDO roomDO = roomMapper.selectRoomByNameAndPassword(name, password);
            if(roomDO == null){
                throw new Exception("房间号或密码错误");
            }
            request.getSession().setAttribute(SessionKey.ROOM_ID,roomDO.getId());//设置房间ID
            modelMap.put("success",true);
        } catch (Exception e) {
            modelMap.put("success",false);
            modelMap.put("msg",e.getMessage());
            e.printStackTrace();
        }
        return JSONObject.toJSONString(modelMap);
    }

    /**
     * 创建房间
     * @param modelMap
     * @param request
     * @param password
     * @param roleCardListStr
     * @return
     */
    @RequestMapping(value = "/createRoom.json", method = {RequestMethod.GET , RequestMethod.POST})
    public String createRoom(ModelMap modelMap, HttpServletRequest request, String password, String roleCardListStr) {
        try {
            if(StringUtils.isBlank(password)){
                throw new Exception("密码不能为空");
            }
            if(StringUtils.isBlank(roleCardListStr)){
                throw new Exception("roleCardListStr不能为空");
            }
            List<RoleCardDO> roleCardDOList = JSON.parseArray(roleCardListStr,RoleCardDO.class);
            int peopleCount = 0;
            for(RoleCardDO roleCardDO : roleCardDOList){
                if(roleCardDO.getIsSelected() == 1){
                    peopleCount += roleCardDO.getPeopleCount();//累加人数
                }
            }
            if(peopleCount - 3 <= 3){
                throw new Exception("创建房间最少需要三人");
            }
            Integer nameValue = roomMapper.selectMaxNameValue();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMM-");
            String roomName = "";
            if(nameValue == null){
                roomName = df.format(System.currentTimeMillis()) + "1";
            } else {
                roomName = df.format(System.currentTimeMillis()) + (nameValue + 1);
            }
            String roomId = IdUtil.getId();
            RoomDO roomDO = new RoomDO();
            roomDO.setId(roomId);
            roomDO.setName(roomName);
            roomDO.setPassword(password);
            Integer effectRows = roomMapper.insert(roomDO);
            if(effectRows != null && effectRows > 0){
                for (RoleCardDO roleCardDO : roleCardDOList){
                    if(roleCardDO.getIsSelected() == 1){
                        RoomRoleCardDO roomRoleCardDO = new RoomRoleCardDO();
                        roomRoleCardDO.setId(IdUtil.getId());
                        roomRoleCardDO.setRoomId(roomId);
                        roomRoleCardDO.setRoleCardId(roleCardDO.getId());
                        roomRoleCardMapper.insert(roomRoleCardDO);
                    }
                }
                request.getSession().setAttribute(SessionKey.ROOM_ID,roomId);//设置房间ID
                modelMap.put("success",true);
            } else {
                throw new Exception("创建房间失败");
            }
        } catch (Exception e) {
            modelMap.put("success",false);
            modelMap.put("msg",e.getMessage());
            e.printStackTrace();
        }
        return JSONObject.toJSONString(modelMap);
    }
}
