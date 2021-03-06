package online.morn.anightwerewolf.service.impl;

import online.morn.anightwerewolf.DO.RoleCardDO;
import online.morn.anightwerewolf.DO.RoomRoleCardDO;
import online.morn.anightwerewolf.mapper.RoleCardMapper;
import online.morn.anightwerewolf.mapper.RoomRoleCardMapper;
import online.morn.anightwerewolf.service.RoleCardService;
import online.morn.anightwerewolf.service.RoomRoleCardService;
import online.morn.anightwerewolf.util.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色牌服务
 * @auther Horner 2017/11/26 1:08
 */
@Service
public class RoleCardServiceImpl implements RoleCardService {

    @Autowired
    private RoleCardMapper roleCardMapper;
    @Autowired
    private RoomRoleCardService roomRoleCardService;

    @Override
    public List<RoleCardDO> findRoleCardList() throws MyException {
        return roleCardMapper.selectRoleCardList();
    }

    @Override
    public List<RoleCardDO> findRoleCardByRoomId(String roomId) throws MyException{
        List<RoleCardDO> roleCardDOList = null;
        List<RoomRoleCardDO> roomRoleCardDOList = roomRoleCardService.findRoomRoleCardListByRoomId(roomId);
        if(!CollectionUtils.isEmpty(roomRoleCardDOList) && roomRoleCardDOList.size() > 0){
            List<String> roleCardIdList = new ArrayList<>();
            for(RoomRoleCardDO roomRoleCardDO : roomRoleCardDOList){
                roleCardIdList.add(roomRoleCardDO.getRoleCardId());
            }
            roleCardDOList = roleCardMapper.selectRoleCardListByIdList(roleCardIdList);
        }
        return roleCardDOList;
    }

    @Override
    public RoleCardDO findRoleCardById(String id) throws MyException {
        List<String> roleCardIdList = new ArrayList<>();
        roleCardIdList.add(id);
        List<RoleCardDO> roleCardDOList = roleCardMapper.selectRoleCardListByIdList(roleCardIdList);
        if(!CollectionUtils.isEmpty(roleCardDOList)){
            return roleCardDOList.get(0);
        }
        return null;
    }

    @Override
    public List<RoleCardDO> findRoleCardListByIdList(List<String> roleCardIdList) {
        return roleCardMapper.selectRoleCardListByIdList(roleCardIdList);
    }
}
