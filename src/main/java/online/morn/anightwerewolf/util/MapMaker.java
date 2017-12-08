package online.morn.anightwerewolf.util;

import online.morn.anightwerewolf.DO.RoleCardDO;
import online.morn.anightwerewolf.DO.RoleDO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Map制作者
 * @auther Horner 2017/12/9 2:42
 */
public class MapMaker {

    /**
     * 制作角色Map --- key为角色ID
     * @auther Horner 2017/11/29 19:58
     * @param roleDOList
     * @return
     */
    public static Map<String,RoleDO> makeRoleMap(List<RoleDO> roleDOList){
        Map<String,RoleDO> roleMap = new HashMap<>();
        for(RoleDO roleDO : roleDOList){
            roleMap.put(roleDO.getId(),roleDO);
        }
        return roleMap;
    }

    /**
     * 制作角色牌Map --- key为角色牌ID
     * @auther Horner 2017/11/29 19:58
     * @param roleCardDOList
     * @return
     */
    public static Map<String,RoleCardDO> makeRoleCardMap(List<RoleCardDO> roleCardDOList){
        Map<String,RoleCardDO> roleCardMap = new HashMap<>();
        for(RoleCardDO roleCardDO : roleCardDOList){
            roleCardMap.put(roleCardDO.getId(),roleCardDO);
        }
        return roleCardMap;
    }
}
