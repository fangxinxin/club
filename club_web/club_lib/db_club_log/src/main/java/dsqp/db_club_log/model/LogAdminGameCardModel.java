package dsqp.db_club_log.model;

import java.util.Date;

/**
 * Created by Aris on 2017/7/26.
 */
public class LogAdminGameCardModel {
    private long id;
    private long adminId;
    private String adminName;
    private int gameId;
    private long promoterId;
    private int changeNum;
    private long changeBefore;
    private long changeAfter;
    private Date createTime;
    private Date createDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAdminId() {
        return adminId;
    }

    public void setAdminId(long adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public long getPromoterId() {
        return promoterId;
    }

    public void setPromoterId(long promoterId) {
        this.promoterId = promoterId;
    }

    public int getChangeNum() {
        return changeNum;
    }

    public void setChangeNum(int changeNum) {
        this.changeNum = changeNum;
    }

    public long getChangeBefore() {
        return changeBefore;
    }

    public void setChangeBefore(long changeBefore) {
        this.changeBefore = changeBefore;
    }

    public long getChangeAfter() {
        return changeAfter;
    }

    public void setChangeAfter(long changeAfter) {
        this.changeAfter = changeAfter;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
