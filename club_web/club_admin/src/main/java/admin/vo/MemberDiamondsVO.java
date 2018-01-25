package admin.vo;

import javax.xml.crypto.Data;
import java.util.Date;

/**
 * Created by jeremy on 2017/9/22.
 */
public class MemberDiamondsVO {
    private long userId;
    private String userNickName;
    private String diamondChangeNums;
    private String changeReason;
    private int remainDiamondNums;
    private Date createTime;

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

    public String getDiamondChangeNums() {
        return diamondChangeNums;
    }

    public void setDiamondChangeNums(String diamondChangeNums) {
        this.diamondChangeNums = diamondChangeNums;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public int getRemainDiamondNums() {
        return remainDiamondNums;
    }

    public void setRemainDiamondNums(int remainDiamondNums) {
        this.remainDiamondNums = remainDiamondNums;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
