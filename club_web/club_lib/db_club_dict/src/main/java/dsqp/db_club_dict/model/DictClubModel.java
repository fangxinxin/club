package dsqp.db_club_dict.model;

/**
 * Created by jeremy on 2017/9/27.
 */
public class DictClubModel {
    private int id;
    private int gameId;
    private int joinMax;
    private boolean isAllowSell;
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

    public int getJoinMax() {
        return joinMax;
    }

    public void setJoinMax(int joinMax) {
        this.joinMax = joinMax;
    }

    public boolean isAllowSell() {
        return isAllowSell;
    }

    public void setAllowSell(boolean allowSell) {
        isAllowSell = allowSell;
    }

    public boolean getIsAllowSell() {
        return isAllowSell;
    }

    public void setIsAllowSell(boolean isAllowSell) {
        this.isAllowSell = isAllowSell;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
