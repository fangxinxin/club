package dsqp.db_club_dict.model;

/**
 * Created by Aris on 2017/8/11.
 */
public class DictBonusModel {
    private int id;
    private int gameId;
    private double directPercent;
    private double nonDirectPercent;
    private boolean isEnable;
    private String remark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public double getDirectPercent() {
        return directPercent;
    }

    public void setDirectPercent(double directPercent) {
        this.directPercent = directPercent;
    }

    public double getNonDirectPercent() {
        return nonDirectPercent;
    }

    public void setNonDirectPercent(double nonDirectPercent) {
        this.nonDirectPercent = nonDirectPercent;
    }

    public boolean getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
