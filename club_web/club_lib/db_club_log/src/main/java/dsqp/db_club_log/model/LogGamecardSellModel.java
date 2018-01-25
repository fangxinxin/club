package dsqp.db_club_log.model;

import java.util.Date;

/**
 * Created by Aris on 2017/9/13.
 */
public class LogGamecardSellModel {
    private long id;
    private int  gameId;
    private long gameUserId;
    private int  gameCardSell;
    private Date statDate;
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

    public long getGameUserId() {
        return gameUserId;
    }

    public void setGameUserId(long gameUserId) {
        this.gameUserId = gameUserId;
    }

    public int getGameCardSell() {
        return gameCardSell;
    }

    public void setGameCardSell(int gameCardSell) {
        this.gameCardSell = gameCardSell;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
