package dsqp.db_club.model;

import java.util.Date;

/**
 * Created by ds on 2017/7/20.
 */
public class PromoterLockModel {
    private long id;
    private long promoterId;
    private String nickName;
    private int peopleNum;
    private int pLevel;
    private int gameId;
    private int lockDay;
    private String remark;
    private String editAdmin;
    private Date expireTime;
    private Date createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPromoterId() {
        return promoterId;
    }

    public void setPromoterId(long promoterId) {
        this.promoterId = promoterId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(int peopleNum) {
        this.peopleNum = peopleNum;
    }

    public int getpLevel() {
        return pLevel;
    }

    public void setpLevel(int pLevel) {
        this.pLevel = pLevel;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getLockDay() {
        return lockDay;
    }

    public void setLockDay(int lockDay) {
        this.lockDay = lockDay;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEditAdmin() {
        return editAdmin;
    }

    public void setEditAdmin(String editAdmin) {
        this.editAdmin = editAdmin;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
