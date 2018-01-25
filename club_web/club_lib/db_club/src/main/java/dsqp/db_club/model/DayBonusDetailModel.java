package dsqp.db_club.model;

import java.util.Date;

/**
 * Created by Aris on 2017/7/21.
 */
public class DayBonusDetailModel {
    private long id;
    private long bonusId;
    private long payId;
    private int gameId;
    private long promoterId;
    private int pLevel;
    private double pay;
    private long parentId;
    private int parentLevel;
    private double parentBonus;
    private long nonParentId;
    private int nonParentLevel;
    private double nonParentBonus;
    private Date payCreateTime;
    private Date payCreateDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBonusId() {
        return bonusId;
    }

    public void setBonusId(long bonusId) {
        this.bonusId = bonusId;
    }

    public long getPayId() {
        return payId;
    }

    public void setPayId(long payId) {
        this.payId = payId;
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

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public int getParentLevel() {
        return parentLevel;
    }

    public void setParentLevel(int parentLevel) {
        this.parentLevel = parentLevel;
    }

    public double getParentBonus() {
        return parentBonus;
    }

    public void setParentBonus(double parentBonus) {
        this.parentBonus = parentBonus;
    }

    public long getNonParentId() {
        return nonParentId;
    }

    public void setNonParentId(long nonParentId) {
        this.nonParentId = nonParentId;
    }

    public int getNonParentLevel() {
        return nonParentLevel;
    }

    public void setNonParentLevel(int nonParentLevel) {
        this.nonParentLevel = nonParentLevel;
    }

    public double getNonParentBonus() {
        return nonParentBonus;
    }

    public void setNonParentBonus(double nonParentBonus) {
        this.nonParentBonus = nonParentBonus;
    }

    public Date getPayCreateTime() {
        return payCreateTime;
    }

    public void setPayCreateTime(Date payCreateTime) {
        this.payCreateTime = payCreateTime;
    }

    public Date getPayCreateDate() {
        return payCreateDate;
    }

    public void setPayCreateDate(Date payCreateDate) {
        this.payCreateDate = payCreateDate;
    }
}
