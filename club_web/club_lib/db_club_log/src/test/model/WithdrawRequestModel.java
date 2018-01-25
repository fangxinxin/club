package dsqp.db_club.model;

import java.util.Date;

/**
 * Created by mj on 2017/7/25.
 */
public class WithdrawRequestModel {

    private long id;
    private int gameId;
    private long promoterId;
    private int pLevel;
    private String serialNo;
    private String receiptNo;
    private double withdrawBefore;
    private double withdrawAfter;
    private double withdrawAmount;
    private long bankAccount;
    private int withdrawStatus;
    private long editAdmin;
    private Date editTime;
    private String remark;
    private Date createDate;
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

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public double getWithdrawBefore() {
        return withdrawBefore;
    }

    public void setWithdrawBefore(double withdrawBefore) {
        this.withdrawBefore = withdrawBefore;
    }

    public double getWithdrawAfter() {
        return withdrawAfter;
    }

    public void setWithdrawAfter(double withdrawAfter) {
        this.withdrawAfter = withdrawAfter;
    }

    public double getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(double withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public long getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(long bankAccount) {
        this.bankAccount = bankAccount;
    }

    public int getWithdrawStatus() {
        return withdrawStatus;
    }

    public void setWithdrawStatus(int withdrawStatus) {
        this.withdrawStatus = withdrawStatus;
    }

    public long getEditAdmin() {
        return editAdmin;
    }

    public void setEditAdmin(long editAdmin) {
        this.editAdmin = editAdmin;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

