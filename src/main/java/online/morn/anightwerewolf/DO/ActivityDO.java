package online.morn.anightwerewolf.DO;

import java.sql.Date;

/**
 * 场次表
 * @auther Horner 2017/11/26 11:58
 */
public class ActivityDO {
    private String id;
    private Date createTime;
    private Date updateTime;
    private String roomId;
    private String status;
    private String skillRoleId;
    private Integer speakNum;
    private Integer winStatus;

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

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Integer getWinStatus() {
        return winStatus;
    }

    public void setWinStatus(Integer winStatus) {
        this.winStatus = winStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSpeakNum() {
        return speakNum;
    }

    public void setSpeakNum(Integer speakNum) {
        this.speakNum = speakNum;
    }

    public String getSkillRoleId() {
        return skillRoleId;
    }

    public void setSkillRoleId(String skillRoleId) {
        this.skillRoleId = skillRoleId;
    }
}
