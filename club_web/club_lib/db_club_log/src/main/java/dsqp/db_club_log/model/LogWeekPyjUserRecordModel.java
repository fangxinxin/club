package dsqp.db_club_log.model;

import java.util.Date;

/**
 * Created by ds on 2018/1/5.
 */
public class LogWeekPyjUserRecordModel {
    private long id;
    private int gameId;
    private long clubId;
    private long promoterId;
    private long gameUserId;
    private String gameNickName;
    private long pyjRoomNum;
    private int year;
    private int week;
    private Date createTime;

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

    public long getPromoterId() {
        return promoterId;
    }

    public void setPromoterId(long promoterId) {
        this.promoterId = promoterId;
    }

    public long getGameUserId() {
        return gameUserId;
    }

    public void setGameUserId(long gameUserId) {
        this.gameUserId = gameUserId;
    }

    public String getGameNickName() {
        return gameNickName;
    }

    public void setGameNickName(String gameNickName) {
        this.gameNickName = gameNickName;
    }

    public long getPyjRoomNum() {
        return pyjRoomNum;
    }

    public void setPyjRoomNum(long pyjRoomNum) {
        this.pyjRoomNum = pyjRoomNum;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
