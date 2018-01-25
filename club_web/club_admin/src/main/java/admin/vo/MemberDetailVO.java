package admin.vo;

import java.util.Date;

/**
 * Created by jeremy on 2017/9/20.
 */
public class MemberDetailVO {
    private long userId;
    private String userNickName;
    private int partiGameNums;
    private int consumeDiamNums;
    private int giveDiamNums;
    private int remainDiaNums;
    private Date createTime;
    private Date joinClubTime;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public int getPartiGameNums() {
        return partiGameNums;
    }

    public void setPartiGameNums(int partiGameNums) {
        this.partiGameNums = partiGameNums;
    }

    public int getConsumeDiamNums() {
        return consumeDiamNums;
    }

    public void setConsumeDiamNums(int consumeDiamNums) {
        this.consumeDiamNums = consumeDiamNums;
    }

    public int getGiveDiamNums() {
        return giveDiamNums;
    }

    public void setGiveDiamNums(int giveDiamNums) {
        this.giveDiamNums = giveDiamNums;
    }

    public int getRemainDiaNums() {
        return remainDiaNums;
    }

    public void setRemainDiaNums(int remainDiaNums) {
        this.remainDiaNums = remainDiaNums;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getJoinClubTime() {
        return joinClubTime;
    }

    public void setJoinClubTime(Date joinClubTime) {
        this.joinClubTime = joinClubTime;
    }

}
