package dsqp.db_club_dict.model;

import java.util.Date;

/**
 * Created by jeremy on 2017/7/31.
 */
public class DictFormalModel {
    private int id;
    private int gameId;
    private int peopleNum; //新玩家人数
    private int newPeopleNum; //新玩家人数
    private double expireDay; //过期时间
    private int pyjRoomNum; //代开房局数
    private int nonPyjRoomNum; //非代开房局数
    private int award; //转正后赠送钻石
    private int refreshDay; //刷新时间
    private boolean isEnable;
    private Date createDate;

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

    public int getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(int peopleNum) {
        this.peopleNum = peopleNum;
    }

    public int getNewPeopleNum() {
        return newPeopleNum;
    }

    public void setNewPeopleNum(int newPeopleNum) {
        this.newPeopleNum = newPeopleNum;
    }

    public double getExpireDay() {
        return expireDay;
    }

    public void setExpireDay(double expireDay) {
        this.expireDay = expireDay;
    }

    public int getPyjRoomNum() {
        return pyjRoomNum;
    }

    public void setPyjRoomNum(int pyjRoomNum) {
        this.pyjRoomNum = pyjRoomNum;
    }

    public int getNonPyjRoomNum() {
        return nonPyjRoomNum;
    }

    public void setNonPyjRoomNum(int nonPyjRoomNum) {
        this.nonPyjRoomNum = nonPyjRoomNum;
    }

    public int getAward() {
        return award;
    }

    public void setAward(int award) {
        this.award = award;
    }

    public int getRefreshDay() {
        return refreshDay;
    }

    public void setRefreshDay(int refreshDay) {
        this.refreshDay = refreshDay;
    }

    public boolean getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
