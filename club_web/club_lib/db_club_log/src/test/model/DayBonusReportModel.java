package dsqp.db_club.model;

import java.util.Date;

/**
 * Created by Aris on 2017/7/21.
 */
public class DayBonusReportModel {
    private long id;
    private long bonusId;
    private int promoterNum;
    private int bonusPeopleNum;
    private double payTotal;
    private double bonusPayTotal;
    private double directBonus;
    private double nonDirectBonus;
    private boolean isPass;
    private Date payCreateDate;
    private Date payCreateTime;

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

    public int getPromoterNum() {
        return promoterNum;
    }

    public void setPromoterNum(int promoterNum) {
        this.promoterNum = promoterNum;
    }

    public int getBonusPeopleNum() {
        return bonusPeopleNum;
    }

    public void setBonusPeopleNum(int bonusPeopleNum) {
        this.bonusPeopleNum = bonusPeopleNum;
    }

    public double getPayTotal() {
        return payTotal;
    }

    public void setPayTotal(double payTotal) {
        this.payTotal = payTotal;
    }

    public double getBonusPayTotal() {
        return bonusPayTotal;
    }

    public void setBonusPayTotal(double bonusPayTotal) {
        this.bonusPayTotal = bonusPayTotal;
    }

    public double getDirectBonus() {
        return directBonus;
    }

    public void setDirectBonus(double directBonus) {
        this.directBonus = directBonus;
    }

    public double getNonDirectBonus() {
        return nonDirectBonus;
    }

    public void setNonDirectBonus(double nonDirectBonus) {
        this.nonDirectBonus = nonDirectBonus;
    }

    public boolean getIsPass() {
        return isPass;
    }

    public void setIsPass(boolean isPass) {
        this.isPass = isPass;
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
