package online.morn.anightwerewolf.util.skillExtendInfoVO;

/**
 * 查牌模型VO
 * @auther Horner 2017/12/3 12:57
 */
public class CheckCardVO {
    private Integer seatNum;
    private String roleId;
    private String roleNmae;
    private String roleCardId;
    private String roleCardName;

    public Integer getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(Integer seatNum) {
        this.seatNum = seatNum;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleCardId() {
        return roleCardId;
    }

    public void setRoleCardId(String roleCardId) {
        this.roleCardId = roleCardId;
    }

    public String getRoleNmae() {
        return roleNmae;
    }

    public void setRoleNmae(String roleNmae) {
        this.roleNmae = roleNmae;
    }

    public String getRoleCardName() {
        return roleCardName;
    }

    public void setRoleCardName(String roleCardName) {
        this.roleCardName = roleCardName;
    }
}
