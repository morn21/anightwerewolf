package online.morn.anightwerewolf.util.skillExtendInfoVO;

import java.util.List;

/**
 * 狼人角色扩展信息VO
 * @auther Horner 2017/12/3 11:49
 */
public class WerewolfVO {
    private List<Integer> teammateSeatNumList;
    private List<CheckCardVO> checkCardList;

    public List<CheckCardVO> getCheckCardList() {
        return checkCardList;
    }

    public void setCheckCardList(List<CheckCardVO> checkCardList) {
        this.checkCardList = checkCardList;
    }

    public List<Integer> getTeammateSeatNumList() {
        return teammateSeatNumList;
    }

    public void setTeammateSeatNumList(List<Integer> teammateSeatNumList) {
        this.teammateSeatNumList = teammateSeatNumList;
    }
}
