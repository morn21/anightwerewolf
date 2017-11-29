package online.morn.anightwerewolf.service.impl;

import online.morn.anightwerewolf.DO.ActivityDO;
import online.morn.anightwerewolf.DO.ActivityDetailDO;
import online.morn.anightwerewolf.DO.RoleCardDO;
import online.morn.anightwerewolf.DO.RoomDO;
import online.morn.anightwerewolf.mapper.ActivityDetailMapper;
import online.morn.anightwerewolf.service.ActivityDetailService;
import online.morn.anightwerewolf.service.ActivityService;
import online.morn.anightwerewolf.service.RoleCardService;
import online.morn.anightwerewolf.service.RoomService;
import online.morn.anightwerewolf.util.ActivityStatus;
import online.morn.anightwerewolf.util.IdUtil;
import online.morn.anightwerewolf.util.MyException;
import online.morn.anightwerewolf.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 场次明细服务
 * @auther Horner 2017/11/26 15:39
 */
@Service
public class ActivityDetailServiceImpl implements ActivityDetailService {

    @Autowired
    private ActivityDetailMapper activityDetailMapper;
    @Autowired
    private RoomService roomService;
    @Autowired
    private RoleCardService roleCardService;
    @Autowired
    private ActivityService activityService;

    @Override
    public ActivityDetailDO addActivityDetail(String userId, String activityId, Integer seatNum) throws MyException {
        /**判定座号已被占*/
        ActivityDetailDO activityDetailDO = activityDetailMapper.selectActivityDetailByActivityIdAndSeatNum(activityId,seatNum);
        if(activityDetailDO != null){
            throw new MyException("傻逼了吧，座号让人家抢了，再选一个吧！");
        }
        /**创建一条场次明细（标识占座号）*/
        this.executeAddActivityDetail(userId,activityId,seatNum);
        /**判断是否符合开场条件*/
        ActivityDO activityDO = activityService.findActivityById(activityId);
        RoomDO roomDO = roomService.findRoomById(activityDO.getRoomId());
        List<ActivityDetailDO> activityDetailDOList = activityDetailMapper.selectActivityDetailListByActivityId(activityId);
        if(roomDO.getPeopleCount() == activityDetailDOList.size()){
            executeAddThreeNobody(activityId,activityDetailDOList);//执行添加三张底牌 并填充给已有列表
            List<RoleCardDO> roleCardDOList = roleCardService.findRoleCardByRoomId(roomDO.getId());
            randomFillRoleCard(activityDetailDOList,roleCardDOList);//随机填充角色牌
            /**执行更新本场次*/
            this.changeByList(activityDetailDOList);
            activityDO.setStatus(ActivityStatus.NOT_SKILL);//未执行技能
            activityService.changeById(activityDO);
        }
        return activityDetailDO;
    }

    /**
     * 随机填充角色牌
     * @auther Horner 2017/11/27 0:30
     * @param activityDetailDOList
     * @param roleCardDOList
     * @return
     * @throws MyException
     */
    private List<ActivityDetailDO> randomFillRoleCard(List<ActivityDetailDO> activityDetailDOList, List<RoleCardDO> roleCardDOList) throws MyException {
        /**角色牌ID列表*/
        List<String> roleCardIdList = new ArrayList<>();
        for(RoleCardDO roleCardDO : roleCardDOList){
            roleCardIdList.add(roleCardDO.getId());
        }
        if(activityDetailDOList.size() != roleCardIdList.size()){
            throw new MyException("随机填充角色牌出现数据异常，请联系管理员");
        }
        /**获得两个随机列表 并给场次明细随机填充角色牌ID*/
        int listSize = activityDetailDOList.size();
        List<Integer> activityDetailIndexList = RandomUtil.generateRandomList(listSize);//场次明细随机下标列表
        List<Integer> roleCardIdIndexList = RandomUtil.generateRandomList(roleCardIdList.size());//角色牌随机下标列表
        for(int i = 0 ; i < listSize ; i++){
            Integer activityDetailIndex = activityDetailIndexList.get(i);//场次明细下标
            Integer roleCardIdIndex = roleCardIdIndexList.get(i);//角色牌随机下标下标
            ActivityDetailDO activityDetailDO = activityDetailDOList.get(activityDetailIndex);
            String roleCardId = roleCardIdList.get(roleCardIdIndex);
            activityDetailDO.setInitialRoleCardId(roleCardId);//初始角色牌ID
            activityDetailDO.setFinalRoleCardId(roleCardId);//最终角色牌ID
        }
        return activityDetailDOList;
    }

    /**
     * (私有方法)执行添加三张底牌 并填充给已有列表
     * @auther Horner 2017/11/26 22:51
     * @param activityId
     * @param activityDetailDOList
     * @return
     * @throws MyException
     */
    private List<ActivityDetailDO> executeAddThreeNobody(String activityId, List<ActivityDetailDO> activityDetailDOList) throws MyException {
        for(int i = -1 ; i > -4 ; i--){
            ActivityDetailDO activityDetailDO = this.executeAddActivityDetail(null,activityId,i);
            activityDetailDOList.add(activityDetailDO);
        }
        return activityDetailDOList;
    }

    /**
     * (私有方法)执行添加场次明细
     * @auther Horner 2017/11/26 22:49
     * @param userId
     * @param activityId
     * @param seatNum
     * @return
     * @throws MyException
     */
    private ActivityDetailDO executeAddActivityDetail(String userId, String activityId, Integer seatNum) throws MyException {
        ActivityDetailDO activityDetailDO = new ActivityDetailDO();
        activityDetailDO.setId(IdUtil.getId());
        activityDetailDO.setUserId(userId);
        activityDetailDO.setActivityId(activityId);
        activityDetailDO.setSeatNum(seatNum);
        Integer rows = activityDetailMapper.insert(activityDetailDO);
        if(rows == null || rows == 0){
            throw new MyException("添加场次明细失败");
        }
        return activityDetailDO;
    }

    @Override
    public Integer changeByList(List<ActivityDetailDO> activityDetailDOList) throws MyException {
        Integer rowCount = 0;
        for(ActivityDetailDO activityDetailDO : activityDetailDOList){
            Integer rows = activityDetailMapper.updateById(activityDetailDO);
            if(rows == null || rows == 0){
                throw new MyException("修改场次明细失败");
            }
            rowCount += rows;
        }
        if(rowCount != activityDetailDOList.size()){
            throw new MyException("修改场次明细执行不一致");
        }
        return rowCount;
    }

    @Override
    public List<ActivityDetailDO> findActivityDetailListByActivityId(String activityId) throws MyException {
        return activityDetailMapper.selectActivityDetailListByActivityId(activityId);
    }
}
