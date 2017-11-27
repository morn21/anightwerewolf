package online.morn.anightwerewolf.service.impl;

import online.morn.anightwerewolf.DO.UserDO;
import online.morn.anightwerewolf.mapper.UserMapper;
import online.morn.anightwerewolf.service.UserService;
import online.morn.anightwerewolf.util.IdUtil;
import online.morn.anightwerewolf.util.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户服务
 * @auther Horner 2017/11/26 15:25
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDO generateUser() throws MyException {
        Integer nameValue = userMapper.selectMaxNameValue();
        String userName = "";
        if(nameValue == null){
            userName = "u-1";
        } else {
            userName = "u-" + (nameValue + 1);
        }
        UserDO userDO = new UserDO();
        userDO.setId(IdUtil.getId());
        userDO.setName(userName);
        Integer rows = userMapper.insert(userDO);
        if(rows == null || rows == 0){
            throw new MyException("添加用户失败");
        }
        return userDO;
    }

    @Override
    public UserDO findUserById(String id) throws MyException {
        return userMapper.selectUserById(id);
    }
}
