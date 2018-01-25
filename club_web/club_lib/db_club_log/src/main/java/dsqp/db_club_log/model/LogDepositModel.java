package dsqp.db_club_log.model;

import java.util.Date;

/**
 * Created by Aris on 2017/7/28.
 */
public class LogDepositModel {
    private long id;
    private int gameId;
    private long promoterId;
    private String nickName;
    private int source;
    private double changeNum;
    private double changeBefore;
    private double changeAfter;
    private Date createTime;
    private Date createDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public double getChangeNum() {
        return changeNum;
    }

    public void setChangeNum(double changeNum) {
        this.changeNum = changeNum;
    }

    public double getChangeBefore() {
        return changeBefore;
    }

    public void setChangeBefore(double changeBefore) {
        this.changeBefore = changeBefore;
    }

    public double getChangeAfter() {
        return changeAfter;
    }

    public void setChangeAfter(double changeAfter) {
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
