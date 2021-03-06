package online.morn.anightwerewolf.DO;

import java.sql.Date;

/**
 * 场次明细表
 * @auther Horner 2017/11/26 11:50
 */
public class ActivityDetailDO {
    private String id;
    private Date createTime;
    private Date updateTime;
    private String userId;
    private String activityId;
    private Integer seatNum;
    private String initialRoleCardId;
    private String finalRoleCardId;
    private Integer skillStatus;
    private String skillExtendInfo;
    private String skillDescription;
    private Integer voteNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(Integer seatNum) {
        this.seatNum = seatNum;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getInitialRoleCardId() {
        return initialRoleCardId;
    }

    public void setInitialRoleCardId(String initialRoleCardId) {
        this.initialRoleCardId = initialRoleCardId;
    }

    public String getFinalRoleCardId() {
        return finalRoleCardId;
    }

    public void setFinalRoleCardId(String finalRoleCardId) {
        this.finalRoleCardId = finalRoleCardId;
    }

    public String getSkillDescription() {
        return skillDescription;
    }

    public void setSkillDescription(String skillDescription) {
        this.skillDescription = skillDescription;
    }

    public Integer getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(Integer voteNum) {
        this.voteNum = voteNum;
    }

    public Integer getSkillStatus() {
        return skillStatus;
    }

    public void setSkillStatus(Integer skillStatus) {
        this.skillStatus = skillStatus;
    }

    public String getSkillExtendInfo() {
        return skillExtendInfo;
    }

    public void setSkillExtendInfo(String skillExtendInfo) {
        this.skillExtendInfo = skillExtendInfo;
    }
}
