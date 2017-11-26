package online.morn.anightwerewolf.service.impl;

import online.morn.anightwerewolf.DO.RoleCardDO;
import online.morn.anightwerewolf.DO.RoomDO;
import online.morn.anightwerewolf.DO.RoomRoleCardDO;
import online.morn.anightwerewolf.mapper.RoomMapper;
import online.morn.anightwerewolf.service.RoomService;
import online.morn.anightwerewolf.util.IdUtil;
import online.morn.anightwerewolf.util.MyException;
import online.morn.anightwerewolf.util.SessionKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

/**
 * 房间服务
 * @auther Horner 2017/11/26 15:36
 */
@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomMapper roomMapper;

    @Override
    public RoomDO generateRoom(String password, Integer peopleCount) throws MyException {
        Integer nameValue = roomMapper.selectMaxNameValue();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMM-");
        String roomName = "";
        if(nameValue == null){
            roomName = df.format(System.currentTimeMillis()) + "1";
        } else {
            roomName = df.format(System.currentTimeMillis()) + (nameValue + 1);
        }
        RoomDO roomDO = new RoomDO();
        roomDO.setId(IdUtil.getId());
        roomDO.setName(roomName);
        roomDO.setPassword(password);
        roomDO.setPeopleCount(peopleCount);
        Integer rows = roomMapper.insert(roomDO);
        if(rows == null || rows == 0){
            throw new MyException("添加房间失败");
        }
        return roomDO;
    }

    @Override
    public RoomDO loginRoom(String name, String password) throws MyException {
        RoomDO roomDO = roomMapper.selectRoomByNameAndPassword(name, password);
        if(roomDO == null){
            throw new MyException("房间号或密码错误");
        }
        return roomDO;
    }

    @Override
    public RoomDO findRoomById(String id) throws MyException {
        return roomMapper.selectRoomById(id);
    }
}
