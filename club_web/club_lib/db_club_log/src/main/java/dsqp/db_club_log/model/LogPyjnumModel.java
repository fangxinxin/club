package dsqp.db_club_log.model;

import java.util.Date;

/**
 * Created by Aris on 2017/9/8.
 */
public class LogPyjnumModel {
    private long id;
    private int  gameId;
    private long gameUserId;
    private int  pyjNum;
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

    public int getPyjNum() {
        return pyjNum;
    }

    public void setPyjNum(int pyjNum) {
        this.pyjNum = pyjNum;
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
