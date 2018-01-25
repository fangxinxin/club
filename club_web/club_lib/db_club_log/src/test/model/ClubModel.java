package dsqp.db_club.model;

import java.util.Date;

/**
 * Created by ds on 2017/7/20.
 */
public class ClubModel {
    private long   id;
    private int    gameId;
    private long   promoterId;
    private String gameNickName;
    private long   gameUserId;
    private String clubName;
    private int    peopleNum;
    private String clubURL;
    private int    clubStatus;
    private Date   expireTime;
    private Date   createTime;

    public ClubModel(){}

    public ClubModel(int gameId, long promoterId,String gameNickName, long gameUserId, String clubName, int peopleNum, int clubStatus, String clubURL, Date createTime) {
        this.gameId     = gameId;
        this.promoterId = promoterId;
        this.gameNickName = gameNickName;
        this.gameUserId = gameUserId;
        this.clubName   = clubName;
        this.peopleNum = peopleNum;
        this.clubURL = clubURL;
        this.clubStatus = clubStatus;
        this.createTime = createTime;
    }

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

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public int getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(int peopleNum) {
        this.peopleNum = peopleNum;
    }

    public String getClubURL() {
        return clubURL;
    }

    public void setClubURL(String clubURL) {
        this.clubURL = clubURL;
    }

    public int getClubStatus() {
        return clubStatus;
    }

    public void setClubStatus(int clubStatus) {
        this.clubStatus = clubStatus;
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
