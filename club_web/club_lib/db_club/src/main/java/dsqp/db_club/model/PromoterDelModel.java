package dsqp.db_club.model;

import java.util.Date;

/**
 * Created by mj on 2017/7/20.
 */
public class PromoterDelModel {
    private long id;
    private int gameId;
    private long cellPhone;
    private long parentCellPhone;
    private String loginPassword;
    private String realName;
    private String nickName;
    private String IDCard;
    private String bankArea;
    private long bankAccount;
    private int pLevel;
    private long gameCard;
    private long gameCardTotal;
    private long gameCardSellTotal;
    private double deposit;
    private double depositTotal;
    private double totalPay;
    private long gameUserId;
    private String editAdmin;
    private long editAdminId;
    private Date promoterTime;
    private Date promoterDate;
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

    public long getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(long cellPhone) {
        this.cellPhone = cellPhone;
    }

    public long getParentCellPhone() {
        return parentCellPhone;
    }

    public void setParentCellPhone(long parentCellPhone) {
        this.parentCellPhone = parentCellPhone;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public String getBankArea() {
        return bankArea;
    }

    public void setBankArea(String bankArea) {
        this.bankArea = bankArea;
    }

    public long getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(long bankAccount) {
        this.bankAccount = bankAccount;
    }

    public int getpLevel() {
        return pLevel;
    }

    public void setpLevel(int pLevel) {
        this.pLevel = pLevel;
    }

    public long getGameCard() {
        return gameCard;
    }

    public void setGameCard(long gameCard) {
        this.gameCard = gameCard;
    }

    public long getGameCardTotal() {
        return gameCardTotal;
    }

    public void setGameCardTotal(long gameCardTotal) {
        this.gameCardTotal = gameCardTotal;
    }

    public long getGameCardSellTotal() {
        return gameCardSellTotal;
    }

    public void setGameCardSellTotal(long gameCardSellTotal) {
        this.gameCardSellTotal = gameCardSellTotal;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public double getDepositTotal() {
        return depositTotal;
    }

    public void setDepositTotal(double depositTotal) {
        this.depositTotal = depositTotal;
    }

    public double getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(double totalPay) {
        this.totalPay = totalPay;
    }

    public long getGameUserId() {
        return gameUserId;
    }

    public void setGameUserId(long gameUserId) {
        this.gameUserId = gameUserId;
    }

    public String getEditAdmin() {
        return editAdmin;
    }

    public void setEditAdmin(String editAdmin) {
        this.editAdmin = editAdmin;
    }

    public long getEditAdminId() {
        return editAdminId;
    }

    public void setEditAdminId(long editAdminId) {
        this.editAdminId = editAdminId;
    }

    public Date getPromoterTime() {
        return promoterTime;
    }

    public void setPromoterTime(Date promoterTime) {
        this.promoterTime = promoterTime;
    }

    public Date getPromoterDate() {
        return promoterDate;
    }

    public void setPromoterDate(Date promoterDate) {
        this.promoterDate = promoterDate;
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
