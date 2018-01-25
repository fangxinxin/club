package dsqp.db_club.model;

import java.util.Date;

/**
 * Created by Aris on 2017/7/21.
 */
public class BonusModel {
    private long id;
    private int gameId;
    private Date startDate;
    private Date endDate;
    private boolean isPass;
    private long editAdminId;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean getIsPass() {
        return isPass;
    }

    public void setIsPass(boolean isPass) {
        this.isPass = isPass;
    }

    public long getEditAdminId() {
        return editAdminId;
    }

    public void setEditAdminId(long editAdminId) {
        this.editAdminId = editAdminId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
