package dsqp.db_club.model;

import java.util.Date;

/**
 * Created by fx on 2017/9/23.
 */
public class PromoterOrderModel {

    private long id;
    private int gameId;
    private String orderNo;
    private long clubId;
    private long promoterId;
    private String gameNickName;
    private long gameUserId;
    private int promoterStatus;
    private long editAdminId;
    private String editAdmin;
    private Date changeTime;
    private Date changeDate;
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public String getGameNickName() {
        return gameNickName;
    }

    public void setGameNickName(String gameNickName) {
        this.gameNickName = gameNickName;
    }

    public long getGameUserId() {
        return gameUserId;
    }

    public void setGameUserId(long gameUserId) {
        this.gameUserId = gameUserId;
    }

    public int getPromoterStatus() {
        return promoterStatus;
    }

    public void setPromoterStatus(int promoterStatus) {
        this.promoterStatus = promoterStatus;
    }

    public long getEditAdminId() {
        return editAdminId;
    }

    public void setEditAdminId(long editAdminId) {
        this.editAdminId = editAdminId;
    }

    public String getEditAdmin() {
        return editAdmin;
    }

    public void setEditAdmin(String editAdmin) {
        this.editAdmin = editAdmin;
    }

    public Date getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
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
