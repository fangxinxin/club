package dsqp.db_club_log.model;

import java.util.Date;

/**
 * Created by ds on 2017/12/25.
 */
public class LogClubRoomcardModel {

    private long id;
    private int gameId;
    private long clubId;
    private long roomId;
    private int cardConsume;
    private int gameUserNum;
    private int roomRound;
    private String gameUserInfo;
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

    public long getClubId() {
        return clubId;
    }

    public void setClubId(long clubId) {
        this.clubId = clubId;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public int getCardConsume() {
        return cardConsume;
    }

    public void setCardConsume(int cardConsume) {
        this.cardConsume = cardConsume;
    }

    public int getGameUserNum() {
        return gameUserNum;
    }

    public void setGameUserNum(int gameUserNum) {
        this.gameUserNum = gameUserNum;
    }

    public int getRoomRound() {
        return roomRound;
    }

    public void setRoomRound(int roomRound) {
        this.roomRound = roomRound;
    }

    public String getGameUserInfo() {
        return gameUserInfo;
    }

    public void setGameUserInfo(String gameUserInfo) {
        this.gameUserInfo = gameUserInfo;
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
