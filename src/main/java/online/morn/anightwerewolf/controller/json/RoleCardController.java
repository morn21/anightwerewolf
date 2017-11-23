package online.morn.anightwerewolf.controller.json;

import com.alibaba.fastjson.JSONObject;
import online.morn.anightwerewolf.DO.RoleCardDO;
import online.morn.anightwerewolf.mapper.RoleCardMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping(value = "/roleCard")
@RestController
public class RoleCardController {
    private Logger logger = Logger.getLogger(RoleCardController.class);

    @Autowired
    private RoleCardMapper roleCardMapper;

    @RequestMapping(value = "/getAllList.json", method = {RequestMethod.GET , RequestMethod.POST})
    public String getAllList(ModelMap modelMap) {
        try{
            List<RoleCardDO> roleCardDOList = roleCardMapper.selectAllList();
            if(roleCardDOList != null){
                modelMap.put("data",roleCardDOList);
                modelMap.put("success",true);
            }
        } catch (Exception e){
            modelMap.put("success",false);
            e.printStackTrace();
        }
        return JSONObject.toJSONString(modelMap);
    }
}
