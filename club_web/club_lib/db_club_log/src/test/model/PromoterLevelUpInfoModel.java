package dsqp.db_club.model;

import java.util.Date;

/**
 * Created by jeremy on 2017/7/27.
 */
public class PromoterLevelUpInfoModel {
    private int id;
    private int gameId;
    private long promoterId;
    private int pLevel;
    private int levelUpType;
    private Date createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getpLevel() {
        return pLevel;
    }

    public void setpLevel(int pLevel) {
        this.pLevel = pLevel;
    }

    public int getLevelUpType() {
        return levelUpType;
    }

    public void setLevelUpType(int levelUpType) {
        this.levelUpType = levelUpType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
