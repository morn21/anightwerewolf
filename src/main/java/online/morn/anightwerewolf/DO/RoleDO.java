package online.morn.anightwerewolf.DO;

/**
 * 角色表
 * @auther Horner 2017/11/29 19:25
 */
public class RoleDO {
    private String id;
    private String name;
    private Integer orderNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }
}
