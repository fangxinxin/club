package dsqp.db_club_log.model;

import java.util.Date;

/**
 * Created by Aris on 2017/7/26.
 */
public class LogGamecardModel {
    private long id;
    private int gameId;
    private long promoterId;
    private String nickName;
    private int source;
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
