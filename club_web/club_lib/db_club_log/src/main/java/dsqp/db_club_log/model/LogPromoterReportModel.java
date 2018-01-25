package dsqp.db_club_log.model;

import java.util.Date;

/**
 * Created by mj on 2017/9/7.
 */
public class LogPromoterReportModel {
    private long id;
    private int gameId;
    private long clubId;
    private int roomCreateNum;
    private int gameCardConsume;
    private int gameCardSell;
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

    public long getClubId() {
        return clubId;
    }

    public void setClubId(long clubId) {
        this.clubId = clubId;
    }

    public int getRoomCreateNum() {
        return roomCreateNum;
    }

    public void setRoomCreateNum(int roomCreateNum) {
        this.roomCreateNum = roomCreateNum;
    }

    public int getGameCardConsume() {
        return gameCardConsume;
    }

    public void setGameCardConsume(int gameCardConsume) {
        this.gameCardConsume = gameCardConsume;
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
