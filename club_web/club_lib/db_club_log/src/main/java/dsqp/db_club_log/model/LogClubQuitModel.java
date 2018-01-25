package dsqp.db_club_log.model;

import java.util.Date;

/**
 * Created by jeremy on 2017/9/7.
 */
public class LogClubQuitModel {
    private long id;
    private int gameId;
    private long clubId;
    private long gameUserId;
    private String gameNickName;
    private int quitWay; //1、群主踢出 2、客服解绑
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

    public int getQuitWay() {
        return quitWay;
    }

    public void setQuitWay(int quitWay) {
        this.quitWay = quitWay;
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
