package online.morn.anightwerewolf.service.impl;

import online.morn.anightwerewolf.DO.ActivityDetailDO;
import online.morn.anightwerewolf.DO.RoleCardDO;
import online.morn.anightwerewolf.DO.RoleDO;
import online.morn.anightwerewolf.mapper.RoleMapper;
import online.morn.anightwerewolf.service.ActivityDetailService;
import online.morn.anightwerewolf.service.RoleCardService;
import online.morn.anightwerewolf.service.RoleService;
import online.morn.anightwerewolf.util.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色服务
 * @auther Horner 2017/11/29 19:38
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleCardService roleCardService;
    @Autowired
    private ActivityDetailService activityDetailService;

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

    @Override
    public RoleDO findRoleByActivityId(String activityId) throws MyException {
        /**根据场次ID查所有场次明细*/
        List<ActivityDetailDO> activityDetailDOList = activityDetailService.findActivityDetailListByActivityId(activityId);
        /**根据角色牌ID列表查角色牌对象列表*/
        List<String> roleCardIdList = new ArrayList<>();
        for(ActivityDetailDO detailDO : activityDetailDOList){
            if(detailDO.getSkillStatus() == 0){
                roleCardIdList.add(detailDO.getInitialRoleCardId());
            }
        }
        List<RoleCardDO> roleCardDOList = roleCardService.findRoleCardListByIdList(roleCardIdList);
        /**根据角色ID列表查角色对象列表*/
        List<String> roleIdList = new ArrayList<>();
        for(RoleCardDO roleCardDO : roleCardDOList){
            roleIdList.add(roleCardDO.getRoleId());
        }
        List<RoleDO> roleDOList = roleMapper.selectRoleListByIdList(roleIdList);
        if(roleDOList.size() > 0){
            return roleDOList.get(0);
        } else {
            return null;
        }
    }
}
