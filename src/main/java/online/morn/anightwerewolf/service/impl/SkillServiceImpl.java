package online.morn.anightwerewolf.service.impl;

import com.alibaba.fastjson.JSONObject;
import online.morn.anightwerewolf.DO.ActivityDO;
import online.morn.anightwerewolf.DO.ActivityDetailDO;
import online.morn.anightwerewolf.DO.RoleCardDO;
import online.morn.anightwerewolf.DO.RoleDO;
import online.morn.anightwerewolf.service.*;
import online.morn.anightwerewolf.util.MapMaker;
import online.morn.anightwerewolf.util.MyException;
import online.morn.anightwerewolf.util.enumeration.ActivityStatus;
import online.morn.anightwerewolf.util.enumeration.RoleId;
import online.morn.anightwerewolf.util.skillExtendInfoVO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SkillServiceImpl implements SkillService {

    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleCardService roleCardService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityDetailService activityDetailService;

    @Override
    public void executeMySkill(String userId, String activityId, List<Integer> isSelectedSeatNumList) throws MyException {
        /**场次信息*/
        ActivityDO activityDO = activityService.findActivityById(activityId);
        if(!ActivityStatus.NOT_SKILL.equals(activityDO.getStatus())){
            throw new MyException("场次状态错误");
        }
        /**场次明细信息*/
        ActivityDetailDO myActivityDetailDO = null;
        List<ActivityDetailDO> activityDetailDOList = activityDetailService.findActivityDetailListByActivityId(activityId);
        for(ActivityDetailDO detailDO : activityDetailDOList){
            if(userId.equals(detailDO.getUserId())){
                myActivityDetailDO = detailDO;
            }
        }
        if(myActivityDetailDO == null){
            throw new MyException("您不存在于本场次中");
        }
        /**本房间下 角色以及角色牌*/
        List<RoleDO> roleDOList = roleService.findRoleByRoomId(activityDO.getRoomId());//获本房间全部角色
        List<RoleCardDO> roleCardDOList = roleCardService.findRoleCardByRoomId(activityDO.getRoomId());//获本房间全部角色牌
        Map<String,RoleDO> roleDOMap = MapMaker.makeRoleMap(roleDOList);
        Map<String,RoleCardDO> roleCardDOMap = MapMaker.makeRoleCardMap(roleCardDOList);
        /**枚举每一种角色*/
        //String mySkillExtendInfoJsonStr = myActivityDetailDO.getSkillExtendInfo();//我的技能扩展信息json字符串
        if(RoleId.DOPPELGANGER.equals(activityDO.getSkillRoleId())){//化身幽灵
            ////////////////////////////////////////////////////////////////////////////
        } else if(RoleId.WEREWOLF.equals(activityDO.getSkillRoleId())){//狼人
            this.processWerewolf(myActivityDetailDO,activityDetailDOList,roleDOMap,roleCardDOMap,isSelectedSeatNumList);//【私有方法】处理狼人技能
        } else if(RoleId.MYSTIC_WOLF.equals(activityDO.getSkillRoleId())){//狼先知
            ////////////////////////////////////////////////////////////////////////////
        } else if(RoleId.MINION.equals(activityDO.getSkillRoleId())){//爪牙
            this.processMinion(myActivityDetailDO,activityDetailDOList,roleDOMap,roleCardDOMap);//【私有方法】处理爪牙技能
        } else if(RoleId.DREAM_WOLF.equals(activityDO.getSkillRoleId())){//贪睡狼
            ////////////////////////////////////////////////////////////////////////////
        } else if(RoleId.TANNER.equals(activityDO.getSkillRoleId())){//皮匠
            this.processTanner(myActivityDetailDO,activityDetailDOList,roleDOMap,roleCardDOMap);//【私有方法】处理皮匠技能
        } else if(RoleId.SEER.equals(activityDO.getSkillRoleId())){//预言家
            this.processSeer(myActivityDetailDO,activityDetailDOList,roleDOMap,roleCardDOMap,isSelectedSeatNumList);//【私有方法】处理预言家技能
        } else if(RoleId.APPRENTICE_SEER.equals(activityDO.getSkillRoleId())){//见习预言家
            ////////////////////////////////////////////////////////////////////////////
        } else if(RoleId.ROBBER.equals(activityDO.getSkillRoleId())){//强盗
            this.processRobber(myActivityDetailDO,activityDetailDOList,roleDOMap,roleCardDOMap,isSelectedSeatNumList);//【私有方法】处理强盗技能
        } else if(RoleId.WITCH.equals(activityDO.getSkillRoleId())){//女巫
            ////////////////////////////////////////////////////////////////////////////
        } else if(RoleId.TROUBLEMAKER.equals(activityDO.getSkillRoleId())){//捣蛋鬼
            this.processTroubleMaker(myActivityDetailDO,activityDetailDOList,roleDOMap,roleCardDOMap,isSelectedSeatNumList);//【私有方法】处理捣蛋鬼技能
        } else if(RoleId.DRUNK.equals(activityDO.getSkillRoleId())){//酒鬼
            this.processDrunk(myActivityDetailDO,activityDetailDOList,roleDOMap,roleCardDOMap,isSelectedSeatNumList);//【私有方法】处理酒鬼技能
        } else if(RoleId.INSOMNIAC.equals(activityDO.getSkillRoleId())){//失眠者
            this.processInsomniac(myActivityDetailDO,activityDetailDOList,roleDOMap,roleCardDOMap);//【私有方法】处理酒鬼技能
        } else if(RoleId.HUNTER.equals(activityDO.getSkillRoleId())){//猎人
            this.processHunter(myActivityDetailDO,activityDetailDOList,roleDOMap,roleCardDOMap);//【私有方法】处理猎人技能
        } else if(RoleId.MASON.equals(activityDO.getSkillRoleId())){//守夜人
            this.processMason(myActivityDetailDO,activityDetailDOList,roleDOMap,roleCardDOMap);//【私有方法】处理守夜人技能
        } else if(RoleId.VILLAGER.equals(activityDO.getSkillRoleId())){//村民
            this.processVillager(myActivityDetailDO,activityDetailDOList,roleDOMap,roleCardDOMap);//【私有方法】处理村民技能
        }
        activityDetailService.changeById(myActivityDetailDO);//修改场次 根据ID
        activityService.changeActivityStatus(activityId);//更新场次状态 根据场次ID
    }

    /**
     * 【私有方法】处理狼人技能
     * @auther Horner 2017/12/9 3:00
     * @param myDetailDO
     * @param activityDetailDOList
     * @param roleDOMap
     * @param roleCardDOMap
     * @param isSelectedSeatNumList
     */
    private void processWerewolf(ActivityDetailDO myDetailDO, List<ActivityDetailDO> activityDetailDOList, Map<String,RoleDO> roleDOMap, Map<String,RoleCardDO> roleCardDOMap, List<Integer> isSelectedSeatNumList){
        String mySkillExtendInfoJsonStr = myDetailDO.getSkillExtendInfo();
        WerewolfVO werewolfVO = jsonStrToJavaObject(mySkillExtendInfoJsonStr,WerewolfVO.class);
        if(werewolfVO.getTeammateSeatNumList() == null){//不知道有几只狼的时候
            List<Integer> teammateSeatNumList = new ArrayList<>();
            for(ActivityDetailDO detailDO : activityDetailDOList){
                if(detailDO.getSeatNum() < 0 || detailDO.getId().equals(myDetailDO.getId())){//不计算底牌 和 自己
                    continue;
                }
                RoleCardDO roleCardDO = roleCardDOMap.get(detailDO.getInitialRoleCardId());
                String roleId = roleCardDO.getRoleId();
                if(roleId.equals(RoleId.WEREWOLF) || roleId.equals(RoleId.MYSTIC_WOLF) || roleId.equals(RoleId.DREAM_WOLF)){//狼人 狼先知 贪睡狼
                    teammateSeatNumList.add(detailDO.getSeatNum());
                }
                ///////////////////这里还没处理化身幽灵 化身狼的情况
            }
            if(teammateSeatNumList.size() > 0){//存在狼队友的时候
                myDetailDO.setSkillStatus(1);//标记技能已执行
            }
            werewolfVO.setTeammateSeatNumList(teammateSeatNumList);//设置狼队友座号列表
            mySkillExtendInfoJsonStr = JSONObject.toJSONString(werewolfVO);//我的技能扩展信息json字符串
        } else if(werewolfVO.getTeammateSeatNumList().size() == 0){//不存在狼队友的时候
            List<CheckCardVO> checkCardList = werewolfVO.getCheckCardList();//查牌列表
            if(checkCardList == null){
                checkCardList = new ArrayList<>();
            }
            CheckCardVO checkCard =  this.executeCheckCard(activityDetailDOList,roleDOMap,roleCardDOMap,isSelectedSeatNumList.get(0));//执行查牌
            checkCardList.add(checkCard);
            String roleId = checkCard.getRoleId();
            if((!roleId.equals(RoleId.WEREWOLF) && !roleId.equals(RoleId.MYSTIC_WOLF) && !roleId.equals(RoleId.DREAM_WOLF)) || checkCardList.size() == 2){//查到的不是【狼人、狼先知、贪睡狼】 或 查了两张牌了
                myDetailDO.setSkillStatus(1);//标记技能已执行
            }
            werewolfVO.setCheckCardList(checkCardList);
            mySkillExtendInfoJsonStr = JSONObject.toJSONString(werewolfVO);//我的技能扩展信息json字符串
        }
        myDetailDO.setSkillExtendInfo(mySkillExtendInfoJsonStr);//设置技能扩展信息
    }

    /**
     * 【私有方法】处理爪牙技能
     * @auther Horner 2017/12/9 3:56
     * @param myDetailDO
     * @param activityDetailDOList
     * @param roleDOMap
     * @param roleCardDOMap
     */
    private void processMinion(ActivityDetailDO myDetailDO, List<ActivityDetailDO> activityDetailDOList, Map<String,RoleDO> roleDOMap, Map<String,RoleCardDO> roleCardDOMap){
        String mySkillExtendInfoJsonStr = myDetailDO.getSkillExtendInfo();
        MinionVO minionVO = jsonStrToJavaObject(mySkillExtendInfoJsonStr,MinionVO.class);
        List<CheckCardVO> checkCardList = minionVO.getCheckCardList();//查牌列表
        if(checkCardList == null){
            checkCardList = new ArrayList<>();
        }
        for(ActivityDetailDO detailDO : activityDetailDOList){
            if(detailDO.getSeatNum() < 0){//不计算底牌
                continue;
            }
            RoleCardDO roleCardDO = roleCardDOMap.get(detailDO.getInitialRoleCardId());
            if(roleCardDO.getRoleId().equals(RoleId.WEREWOLF)){//狼人
                CheckCardVO checkCard =  this.executeCheckCard(activityDetailDOList,roleDOMap,roleCardDOMap,detailDO.getSeatNum());//执行查牌
                checkCardList.add(checkCard);
            }
            ///////////////////这里还没处理化身幽灵 化身狼的情况
        }
        myDetailDO.setSkillStatus(1);//标记技能已执行
        minionVO.setCheckCardList(checkCardList);
        mySkillExtendInfoJsonStr = JSONObject.toJSONString(minionVO);//我的技能扩展信息json字符串
        myDetailDO.setSkillExtendInfo(mySkillExtendInfoJsonStr);//设置技能扩展信息
    }

    /**
     * 【私有方法】处理皮匠技能
     * @auther Horner 2017/12/9 3:23
     * @param myDetailDO
     * @param activityDetailDOList
     * @param roleDOMap
     * @param roleCardDOMap
     */
    private void processTanner(ActivityDetailDO myDetailDO, List<ActivityDetailDO> activityDetailDOList, Map<String,RoleDO> roleDOMap, Map<String,RoleCardDO> roleCardDOMap){
        String mySkillExtendInfoJsonStr = myDetailDO.getSkillExtendInfo();
        TannerVO tannerVO = jsonStrToJavaObject(mySkillExtendInfoJsonStr,TannerVO.class);
        CheckCardVO checkCard =  this.executeCheckCard(activityDetailDOList,roleDOMap,roleCardDOMap,myDetailDO.getSeatNum());//执行查牌
        myDetailDO.setSkillStatus(1);//标记技能已执行
        tannerVO.setCheckCard(checkCard);
        mySkillExtendInfoJsonStr = JSONObject.toJSONString(tannerVO);//我的技能扩展信息json字符串
        myDetailDO.setSkillExtendInfo(mySkillExtendInfoJsonStr);//设置技能扩展信息
    }

    /**
     * 【私有方法】处理预言家技能
     * @auther Horner 2017/12/9 3:02
     * @param myDetailDO
     * @param activityDetailDOList
     * @param roleDOMap
     * @param roleCardDOMap
     * @param isSelectedSeatNumList
     */
    private void processSeer(ActivityDetailDO myDetailDO, List<ActivityDetailDO> activityDetailDOList, Map<String,RoleDO> roleDOMap, Map<String,RoleCardDO> roleCardDOMap, List<Integer> isSelectedSeatNumList){
        String mySkillExtendInfoJsonStr = myDetailDO.getSkillExtendInfo();
        SeerVO seerVO = jsonStrToJavaObject(mySkillExtendInfoJsonStr,SeerVO.class);
        List<CheckCardVO> checkCardList = seerVO.getCheckCardList();//查牌列表
        if(checkCardList == null){
            checkCardList = new ArrayList<>();
        }
        CheckCardVO checkCard =  this.executeCheckCard(activityDetailDOList,roleDOMap,roleCardDOMap,isSelectedSeatNumList.get(0));//执行查牌
        checkCardList.add(checkCard);
        if(checkCard.getSeatNum() > 0 || checkCardList.size() == 2){
            myDetailDO.setSkillStatus(1);//标记技能已执行
        }
        seerVO.setCheckCardList(checkCardList);
        mySkillExtendInfoJsonStr = JSONObject.toJSONString(seerVO);//我的技能扩展信息json字符串
        myDetailDO.setSkillExtendInfo(mySkillExtendInfoJsonStr);//设置技能扩展信息
    }

    /**
     * 【私有方法】处理强盗技能
     * @auther Horner 2017/12/9 3:07
     * @param myDetailDO
     * @param activityDetailDOList
     * @param roleDOMap
     * @param roleCardDOMap
     * @param isSelectedSeatNumList
     * @throws MyException
     */
    private void processRobber(ActivityDetailDO myDetailDO, List<ActivityDetailDO> activityDetailDOList, Map<String,RoleDO> roleDOMap, Map<String,RoleCardDO> roleCardDOMap, List<Integer> isSelectedSeatNumList) throws MyException {
        String mySkillExtendInfoJsonStr = myDetailDO.getSkillExtendInfo();
        RobberVO robberVO = jsonStrToJavaObject(mySkillExtendInfoJsonStr,RobberVO.class);
        CheckCardVO checkCard =  this.executeCheckCard(activityDetailDOList,roleDOMap,roleCardDOMap,isSelectedSeatNumList.get(0));//执行查牌
        for(ActivityDetailDO detailDO : activityDetailDOList){
            if(detailDO.getSeatNum() == checkCard.getSeatNum()){//找到对应的牌 将自己的牌与对方交换
                this.executeSwapCard(myDetailDO,detailDO);//执行换牌
                break;
            }
        }
        myDetailDO.setSkillStatus(1);//标记技能已执行
        robberVO.setCheckCard(checkCard);
        mySkillExtendInfoJsonStr = JSONObject.toJSONString(robberVO);//我的技能扩展信息json字符串
        myDetailDO.setSkillExtendInfo(mySkillExtendInfoJsonStr);//设置技能扩展信息
    }

    /**
     * 【私有方法】处理捣蛋鬼技能
     * @auther Horner 2017/12/9 3:09
     * @param myDetailDO
     * @param activityDetailDOList
     * @param roleDOMap
     * @param roleCardDOMap
     * @param isSelectedSeatNumList
     * @throws MyException
     */
    private void processTroubleMaker(ActivityDetailDO myDetailDO, List<ActivityDetailDO> activityDetailDOList, Map<String,RoleDO> roleDOMap, Map<String,RoleCardDO> roleCardDOMap, List<Integer> isSelectedSeatNumList) throws MyException {
        String mySkillExtendInfoJsonStr = myDetailDO.getSkillExtendInfo();
        TroubleMakerVO troubleMakerVO = jsonStrToJavaObject(mySkillExtendInfoJsonStr,TroubleMakerVO.class);
        List<CheckCardVO> checkCardList = new ArrayList<>();//查牌列表
        CheckCardVO checkCard1 =  this.executeCheckCard(activityDetailDOList,roleDOMap,roleCardDOMap,isSelectedSeatNumList.get(0));//执行查牌
        CheckCardVO checkCard2 =  this.executeCheckCard(activityDetailDOList,roleDOMap,roleCardDOMap,isSelectedSeatNumList.get(1));//执行查牌
        checkCardList.add(checkCard1);
        checkCardList.add(checkCard2);
        /**执行交换*/
        ActivityDetailDO detailDO1 = null;
        ActivityDetailDO detailDO2 = null;
        for(ActivityDetailDO detailDO : activityDetailDOList){
            if(detailDO.getSeatNum() == checkCard1.getSeatNum()){
                detailDO1 = detailDO;
            } else if(detailDO.getSeatNum() == checkCard2.getSeatNum()){
                detailDO2 = detailDO;
            }
        }
        this.executeSwapCard(detailDO1,detailDO2);//执行换牌
        myDetailDO.setSkillStatus(1);//标记技能已执行
        troubleMakerVO.setCheckCardList(checkCardList);
        mySkillExtendInfoJsonStr = JSONObject.toJSONString(troubleMakerVO);//我的技能扩展信息json字符串
        myDetailDO.setSkillExtendInfo(mySkillExtendInfoJsonStr);//设置技能扩展信息
    }

    /**
     * 【私有方法】处理酒鬼技能
     * @auther Horner 2017/12/9 3:10
     * @param myDetailDO
     * @param activityDetailDOList
     * @param roleDOMap
     * @param roleCardDOMap
     * @param isSelectedSeatNumList
     * @throws MyException
     */
    private void processDrunk(ActivityDetailDO myDetailDO, List<ActivityDetailDO> activityDetailDOList, Map<String,RoleDO> roleDOMap, Map<String,RoleCardDO> roleCardDOMap, List<Integer> isSelectedSeatNumList) throws MyException {
        String mySkillExtendInfoJsonStr = myDetailDO.getSkillExtendInfo();
        DrunkVO drunkVO = jsonStrToJavaObject(mySkillExtendInfoJsonStr,DrunkVO.class);
        CheckCardVO checkCard =  this.executeCheckCard(activityDetailDOList,roleDOMap,roleCardDOMap,isSelectedSeatNumList.get(0));//执行查牌
        for(ActivityDetailDO detailDO : activityDetailDOList){
            if(detailDO.getSeatNum() == checkCard.getSeatNum()){//找到对应的牌 将自己的牌与对方交换
                this.executeSwapCard(myDetailDO,detailDO);//执行换牌
                break;
            }
        }
        myDetailDO.setSkillStatus(1);//标记技能已执行
        drunkVO.setCheckCard(checkCard);
        mySkillExtendInfoJsonStr = JSONObject.toJSONString(drunkVO);//我的技能扩展信息json字符串
        myDetailDO.setSkillExtendInfo(mySkillExtendInfoJsonStr);//设置技能扩展信息
    }

    /**
     * 【私有方法】处理失眠者技能
     * @auther Horner 2017/12/9 3:12
     * @param myDetailDO
     * @param activityDetailDOList
     * @param roleDOMap
     * @param roleCardDOMap
     */
    private void processInsomniac(ActivityDetailDO myDetailDO, List<ActivityDetailDO> activityDetailDOList, Map<String,RoleDO> roleDOMap, Map<String,RoleCardDO> roleCardDOMap){
        String mySkillExtendInfoJsonStr = myDetailDO.getSkillExtendInfo();
        InsomniacVO insomniacVO = jsonStrToJavaObject(mySkillExtendInfoJsonStr,InsomniacVO.class);
        CheckCardVO checkCard =  this.executeCheckCard(activityDetailDOList,roleDOMap,roleCardDOMap,myDetailDO.getSeatNum());//执行查牌
        myDetailDO.setSkillStatus(1);//标记技能已执行
        insomniacVO.setCheckCard(checkCard);
        mySkillExtendInfoJsonStr = JSONObject.toJSONString(insomniacVO);//我的技能扩展信息json字符串
        myDetailDO.setSkillExtendInfo(mySkillExtendInfoJsonStr);//设置技能扩展信息
    }

    /**
     * 【私有方法】处理猎人技能
     * @auther Horner 2017/12/9 3:50
     * @param myDetailDO
     * @param activityDetailDOList
     * @param roleDOMap
     * @param roleCardDOMap
     */
    private void processHunter(ActivityDetailDO myDetailDO, List<ActivityDetailDO> activityDetailDOList, Map<String,RoleDO> roleDOMap, Map<String,RoleCardDO> roleCardDOMap){
        String mySkillExtendInfoJsonStr = myDetailDO.getSkillExtendInfo();
        HunterVO hunterVO = jsonStrToJavaObject(mySkillExtendInfoJsonStr,HunterVO.class);
        CheckCardVO checkCard =  this.executeCheckCard(activityDetailDOList,roleDOMap,roleCardDOMap,myDetailDO.getSeatNum());//执行查牌
        myDetailDO.setSkillStatus(1);//标记技能已执行
        hunterVO.setCheckCard(checkCard);
        mySkillExtendInfoJsonStr = JSONObject.toJSONString(hunterVO);//我的技能扩展信息json字符串
        myDetailDO.setSkillExtendInfo(mySkillExtendInfoJsonStr);//设置技能扩展信息
    }


    /**
     * 【私有方法】处理守夜人技能
     * @auther Horner 2017/12/9 3:13
     * @param myDetailDO
     * @param activityDetailDOList
     * @param roleDOMap
     * @param roleCardDOMap
     */
    private void processMason(ActivityDetailDO myDetailDO, List<ActivityDetailDO> activityDetailDOList, Map<String,RoleDO> roleDOMap, Map<String,RoleCardDO> roleCardDOMap){
        String mySkillExtendInfoJsonStr = myDetailDO.getSkillExtendInfo();
        MasonVO masonVO = jsonStrToJavaObject(mySkillExtendInfoJsonStr,MasonVO.class);
        for(ActivityDetailDO detailDO : activityDetailDOList){
            RoleCardDO roleCardDO = roleCardDOMap.get(detailDO.getInitialRoleCardId());
            if(RoleId.MASON.equals(roleCardDO.getRoleId()) && detailDO.getSeatNum() != myDetailDO.getSeatNum()){
                CheckCardVO checkCard =  this.executeCheckCard(activityDetailDOList,roleDOMap,roleCardDOMap,detailDO.getSeatNum());//执行查牌
                myDetailDO.setSkillStatus(1);//标记技能已执行
                masonVO.setCheckCard(checkCard);
                mySkillExtendInfoJsonStr = JSONObject.toJSONString(masonVO);//我的技能扩展信息json字符串
                break;
            }
        }
        myDetailDO.setSkillExtendInfo(mySkillExtendInfoJsonStr);//设置技能扩展信息
    }

    /**
     * 【私有方法】处理村民技能
     * @auther Horner 2017/12/9 3:18
     * @param myDetailDO
     * @param activityDetailDOList
     * @param roleDOMap
     * @param roleCardDOMap
     */
    private void processVillager(ActivityDetailDO myDetailDO, List<ActivityDetailDO> activityDetailDOList, Map<String,RoleDO> roleDOMap, Map<String,RoleCardDO> roleCardDOMap){
        String mySkillExtendInfoJsonStr = myDetailDO.getSkillExtendInfo();
        VillagerVO villagerVO = jsonStrToJavaObject(mySkillExtendInfoJsonStr,VillagerVO.class);
        CheckCardVO checkCard =  this.executeCheckCard(activityDetailDOList,roleDOMap,roleCardDOMap,myDetailDO.getSeatNum());//执行查牌
        myDetailDO.setSkillStatus(1);//标记技能已执行
        villagerVO.setCheckCard(checkCard);
        mySkillExtendInfoJsonStr = JSONObject.toJSONString(villagerVO);//我的技能扩展信息json字符串
        myDetailDO.setSkillExtendInfo(mySkillExtendInfoJsonStr);//设置技能扩展信息
    }

    /**
     * 【私有方法】执行换牌
     * @auther Horner 2017/12/3 17:37
     * @param detailDO1
     * @param detailDO2
     */
    private void executeSwapCard(ActivityDetailDO detailDO1, ActivityDetailDO detailDO2) throws MyException {
        String roleCardId = detailDO1.getFinalRoleCardId();
        detailDO1.setFinalRoleCardId(detailDO2.getFinalRoleCardId());
        detailDO2.setFinalRoleCardId(roleCardId);
        activityDetailService.changeById(detailDO1);
        activityDetailService.changeById(detailDO2);
    }

    /**
     * 【私有方法】执行查牌
     * @auther Horner 2017/12/3 17:27
     * @param activityDetailDOList
     * @param roleDOMap
     * @param roleCardDOMap
     * @param isSelectedSeatNum 选中要查的座号
     * @return
     */
    private CheckCardVO executeCheckCard(List<ActivityDetailDO> activityDetailDOList, Map<String,RoleDO> roleDOMap, Map<String,RoleCardDO> roleCardDOMap, Integer isSelectedSeatNum){
        CheckCardVO checkCard = new CheckCardVO();
        for(ActivityDetailDO detailDO : activityDetailDOList){
            if(detailDO.getSeatNum() == isSelectedSeatNum){
                RoleCardDO roleCardDO = roleCardDOMap.get(detailDO.getFinalRoleCardId());//查当下角色牌
                RoleDO roleDO = roleDOMap.get(roleCardDO.getRoleId());
                checkCard.setSeatNum(detailDO.getSeatNum());//座牌号
                checkCard.setRoleId(roleDO.getId());//角色ID
                checkCard.setRoleNmae(roleDO.getName());//角色名称
                checkCard.setRoleCardId(roleCardDO.getId());//角色牌ID
                checkCard.setRoleCardName(roleCardDO.getName());//角色牌名称
                break;
            }
        }
        return checkCard;
    }

    /**
     * 【私有方法】json字符串转Java对象
     * @auther Horner 2017/12/3 13:19
     * @param jsonStr
     * @param tClass
     * @param <T>
     * @return
     */
    private <T> T jsonStrToJavaObject(String jsonStr, Class<T> tClass){
        return (T) JSONObject.toJavaObject(JSONObject.parseObject(jsonStr),tClass);
    }
}
