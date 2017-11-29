package online.morn.anightwerewolf.service.impl;

import online.morn.anightwerewolf.DO.RoleCardDO;
import online.morn.anightwerewolf.DO.RoleDO;
import online.morn.anightwerewolf.mapper.RoleMapper;
import online.morn.anightwerewolf.service.RoleCardService;
import online.morn.anightwerewolf.service.RoleService;
import online.morn.anightwerewolf.util.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色服务
 * @auther Horner 2017/11/29 19:38
 */
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleCardService roleCardService;

    @Override
    public List<RoleDO> findRoleList() throws MyException {
        return roleMapper.selectRoleList();
    }

    @Override
    public List<RoleDO> findRoleByRoomId(String roomId) throws MyException{
        List<RoleDO> roleDOList = null;
        List<RoleCardDO> roleCardDOList = roleCardService.findRoleCardByRoomId(roomId);
        if(!CollectionUtils.isEmpty(roleCardDOList) && roleCardDOList.size() > 0){
            List<String> roleIdList = new ArrayList<>();
            for(RoleCardDO roleCardDO : roleCardDOList){
                roleIdList.add(roleCardDO.getRoleId());
            }
            roleDOList = roleMapper.selectRoleListByIdList(roleIdList);
        }
        return roleDOList;
    }

    @Override
    public RoleDO findRoleById(String id) throws MyException {
        List<String> roleIdList = new ArrayList<>();
        roleIdList.add(id);
        List<RoleDO> roleDOList = roleMapper.selectRoleListByIdList(roleIdList);
        if(!CollectionUtils.isEmpty(roleDOList)){
            return roleDOList.get(0);
        }
        return null;
    }
}
