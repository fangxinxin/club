package dsqp.db_club_log.model;

import java.util.Date;

/**
 * Created by mj on 2017/9/7.
 */
public class LogClubModel {

    private long id;
    private int gameId;
    private long gameUserId;
    private String gameNickName;
    private long clubId;
    private int peopleNum;
    private int peopleNumNew;
    private int pyjNum;
    private int pyjNumNew;
    private int clubType;
    private Date expireTime;
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

    public long getClubId() {
        return clubId;
    }

    public void setClubId(long clubId) {
        this.clubId = clubId;
    }

    public int getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(int peopleNum) {
        this.peopleNum = peopleNum;
    }

    public int getPeopleNumNew() {
        return peopleNumNew;
    }

    public void setPeopleNumNew(int peopleNumNew) {
        this.peopleNumNew = peopleNumNew;
    }

    public int getPyjNum() {
        return pyjNum;
    }

    public void setPyjNum(int pyjNum) {
        this.pyjNum = pyjNum;
    }

    public int getPyjNumNew() {
        return pyjNumNew;
    }

    public void setPyjNumNew(int pyjNumNew) {
        this.pyjNumNew = pyjNumNew;
    }

    public int getClubType() {
        return clubType;
    }

    public void setClubType(int clubType) {
        this.clubType = clubType;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
