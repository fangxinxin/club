package dsqp.db_club_dict.model;

/**
 * Created by jeremy on 2017/7/26.
 */
public class DictLevelUpModel {
    private long id;
    private int gameId;
    private int levelUpType;
    private double totalPay;
    private int totalPromoter;
    private boolean isEnable;


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

    public int getLevelUpType() {
        return levelUpType;
    }

    public void setLevelUpType(int levelUpType) {
        this.levelUpType = levelUpType;
    }

    public double getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(double totalPay) {
        this.totalPay = totalPay;
    }

    public int getTotalPromoter() {
        return totalPromoter;
    }

    public void setTotalPromoter(int totalPromoter) {
        this.totalPromoter = totalPromoter;
    }

    public boolean getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }
}

