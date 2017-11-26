package online.morn.anightwerewolf.service.impl;

import online.morn.anightwerewolf.DO.RoleCardDO;
import online.morn.anightwerewolf.DO.RoomRoleCardDO;
import online.morn.anightwerewolf.mapper.RoomRoleCardMapper;
import online.morn.anightwerewolf.service.RoomRoleCardService;
import online.morn.anightwerewolf.util.IdUtil;
import online.morn.anightwerewolf.util.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 房间角色卡服务
 * @auther Horner 2017/11/26 15:39
 */
@Service
public class RoomRoleCardServiceImpl implements RoomRoleCardService {

    @Autowired
    private RoomRoleCardMapper roomRoleCardMapper;

    @Override
    public List<RoomRoleCardDO> generateRoomRoleCard(String roomId, List<RoleCardDO> roleCardDOList) throws MyException {
        List<RoomRoleCardDO> roomRoleCardDOList = new ArrayList<>();
        for (RoleCardDO roleCardDO : roleCardDOList){
            if(roleCardDO.getIsSelected() == 1){
                RoomRoleCardDO roomRoleCardDO = new RoomRoleCardDO();
                roomRoleCardDO.setId(IdUtil.getId());
                roomRoleCardDO.setRoomId(roomId);
                roomRoleCardDO.setRoleCardId(roleCardDO.getId());
                Integer rows = roomRoleCardMapper.insert(roomRoleCardDO);
                if(rows == null || rows == 0){
                    throw new MyException("添加房间角色卡失败");
                }
                roomRoleCardDOList.add(roomRoleCardDO);
            }
        }
        return roomRoleCardDOList;
    }
}
