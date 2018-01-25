package dsqp.db_club_log.model;

import java.util.Date;

/**
 * Created by ds on 2017/11/22.
 */
public class LogRebateGetModel {
    private long id;
    private int gameId;
    private long promoterId;
    private int getDiamond;
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

    public int getGetDiamond() {
        return getDiamond;
    }

    public void setGetDiamond(int getDiamond) {
        this.getDiamond = getDiamond;
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
