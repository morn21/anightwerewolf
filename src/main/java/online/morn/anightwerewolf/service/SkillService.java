package online.morn.anightwerewolf.service;

import online.morn.anightwerewolf.util.MyException;

import java.util.List;

/**
 * 技能服务
 * @auther Horner 2017/12/9 2:30
 */
public interface SkillService {

    public void executeMySkill(String userId, String activityId, List<Integer> isSelectedSeatNumList) throws MyException;
}
