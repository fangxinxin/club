package dsqp.db_club.model;

import java.util.Date;

/**
 * Created by mj on 2017/8/14.
 */
public class DayDepositModel {

    private long id;
    private int gameId;
    private long promoter;
    private double depositRemain;
    private Date statDate;
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

    public long getPromoter() {
        return promoter;
    }

    public void setPromoter(long promoter) {
        this.promoter = promoter;
    }

    public double getDepositRemain() {
        return depositRemain;
    }

    public void setDepositRemain(double depositRemain) {
        this.depositRemain = depositRemain;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
