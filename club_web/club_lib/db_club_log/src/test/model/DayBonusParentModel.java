package dsqp.db_club.model;

import java.util.Date;

/**
 * Created by Aris on 2017/7/21.
 */
public class DayBonusParentModel {
    private long id;
    private long payId;
    private long bonusId;
    private int gameId;
    private long promoterId;
    private int pLevel;
    private double pay;
    private double bonus;
    private int bonusType;
    private long fromPromoterId;
    private int fromPromoterLevel;
    private Date payCreateDate;
    private Date payCreateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPayId() {
        return payId;
    }

    public void setPayId(long payId) {
        this.payId = payId;
    }

    public long getBonusId() {
        return bonusId;
    }

    public void setBonusId(long bonusId) {
        this.bonusId = bonusId;
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

    public double getPay() {
        return pay;
    }

    public void setPay(double pay) {
        this.pay = pay;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public int getBonusType() {
        return bonusType;
    }

    public void setBonusType(int bonusType) {
        this.bonusType = bonusType;
    }

    public long getFromPromoterId() {
        return fromPromoterId;
    }

    public void setFromPromoterId(long fromPromoterId) {
        this.fromPromoterId = fromPromoterId;
    }

    public int getFromPromoterLevel() {
        return fromPromoterLevel;
    }

    public void setFromPromoterLevel(int fromPromoterLevel) {
        this.fromPromoterLevel = fromPromoterLevel;
    }

    public Date getPayCreateDate() {
        return payCreateDate;
    }

    public void setPayCreateDate(Date payCreateDate) {
        this.payCreateDate = payCreateDate;
    }

    public Date getPayCreateTime() {
        return payCreateTime;
    }

    public void setPayCreateTime(Date payCreateTime) {
        this.payCreateTime = payCreateTime;
    }
}
