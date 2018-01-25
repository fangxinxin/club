package dsqp.db_club.model;

import java.util.Date;

/**
 * Created by mj on 2017/7/20.
 */
public class PromoterModel {
    private long id;
    private long clubId;
    private int gameId;
    private long parentId;
    private long cellPhone;
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
    private long rebate;
    private long rebateTotal;
    private double totalPay;
    private long gameUserId;
    private int loginStatus;
    private boolean isEnable;
    private String editAdmin;
    private long editAdminId;
    private String staffWechat;
    private String remark;
    private Date createTime;
    private Date createDate;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getClubId() {
        return clubId;
    }

    public void setClubId(long clubId) {
        this.clubId = clubId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public long getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(long cellPhone) {
        this.cellPhone = cellPhone;
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

    public long getRebate() {
        return rebate;
    }

    public void setRebate(long rebate) {
        this.rebate = rebate;
    }

    public long getRebateTotal() {
        return rebateTotal;
    }

    public void setRebateTotal(long rebateTotal) {
        this.rebateTotal = rebateTotal;
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

    public int getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }

    public boolean getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
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

    public String getStaffWechat() {
        return staffWechat;
    }

    public void setStaffWechat(String staffWechat) {
        this.staffWechat = staffWechat;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
