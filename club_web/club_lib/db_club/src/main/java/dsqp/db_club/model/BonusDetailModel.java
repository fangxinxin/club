package dsqp.db_club.model;

import java.util.Date;

/**
 * Created by Aris on 2017/11/28.
 */
public class BonusDetailModel {
    private long   id;
    private int    gameId;
    private long   payId;
    private int    pLevel;
    private long   promoterId;
    private long   fromPromoterId;
    private int    diamond;
    private int    rebateDiamond;
    private double rebatePercent;
    private Date   createTime;
    private Date   createDate;

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

    public long getPayId() {
        return payId;
    }

    public void setPayId(long payId) {
        this.payId = payId;
    }

    public int getpLevel() {
        return pLevel;
    }

    public void setpLevel(int pLevel) {
        this.pLevel = pLevel;
    }

    public long getPromoterId() {
        return promoterId;
    }

    public void setPromoterId(long promoterId) {
        this.promoterId = promoterId;
    }

    public long getFromPromoterId() {
        return fromPromoterId;
    }

    public void setFromPromoterId(long fromPromoterId) {
        this.fromPromoterId = fromPromoterId;
    }

    public int getDiamond() {
        return diamond;
    }

    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }

    public int getRebateDiamond() {
        return rebateDiamond;
    }

    public void setRebateDiamond(int rebateDiamond) {
        this.rebateDiamond = rebateDiamond;
    }

    public double getRebatePercent() {
        return rebatePercent;
    }

    public void setRebatePercent(double rebatePercent) {
        this.rebatePercent = rebatePercent;
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
