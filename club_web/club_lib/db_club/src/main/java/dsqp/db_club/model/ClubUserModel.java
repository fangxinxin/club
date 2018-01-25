package dsqp.db_club.model;

import java.util.Date;

/**
 * Created by ds on 2017/7/20.
 */
public class ClubUserModel {
    private long id;
    private int gameId;
    private long clubId;
    private long promoterId;
    private long promoterGameUserId;
    private long gameUserId;
    private String gameNickName;
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

    public long getPromoterGameUserId() {
        return promoterGameUserId;
    }

    public void setPromoterGameUserId(long promoterGameUserId) {
        this.promoterGameUserId = promoterGameUserId;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
