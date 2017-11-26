package online.morn.anightwerewolf.controller.json;

import com.alibaba.fastjson.JSONObject;
import online.morn.anightwerewolf.DO.RoleCardDO;
import online.morn.anightwerewolf.service.RoleCardService;
import online.morn.anightwerewolf.util.MyException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 角色卡
 * @auther Horner 2017/11/26 0:21
 */
@RequestMapping(value = "/roleCard")
@RestController
public class RoleCardController {
    private Logger logger = Logger.getLogger(RoleCardController.class);

    @Autowired
    private RoleCardService roleCardService;

    /**
     * 加载角色卡列表
     * @auther Horner 2017/11/26 11:40
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/loadRoleCardList.json", method = {RequestMethod.GET , RequestMethod.POST})
    public String loadRoleCardList(ModelMap modelMap) {
        try{
            List<RoleCardDO> roleCardDOList = roleCardService.findRoleCardList();
            /*for(RoleCardDO ro : roleCardDOList){
                System.out.println("public static final String " +ro.getId()+ " = \"" +ro.getId()+ "\";//" + ro.getName() + " orderNum:"+ro.getOrderNum() + " peopleCount:" +ro.getPeopleCount());
                String roleId = ro.getId();
                StringBuffer strBuffer = new StringBuffer();
                for(int i = 0 ; i < roleId.length() ; i++){
                    char chr = roleId.charAt(i);
                    if(Character.isUpperCase(chr) && i != 0){
                        strBuffer.append("_" + chr);
                    } else if(Character.isDigit(chr)){
                        strBuffer.append("_" + chr);
                    } else {
                        strBuffer.append(String.valueOf(chr).toUpperCase());
                    }
                }
                System.out.println("update role_card set id='" +strBuffer.toString() + "' where id='" + roleId + "';");
            }*/
            if(roleCardDOList == null){
                throw new MyException("列表未找到");
            }
            modelMap.put("data",roleCardDOList);
            modelMap.put("success",true);
        } catch (MyException e) {
            modelMap.put("success",false);
            modelMap.put("msg",e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return JSONObject.toJSONString(modelMap);
    }
}
