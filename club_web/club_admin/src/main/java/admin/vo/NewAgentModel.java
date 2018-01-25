package admin.vo;

/**
 * Created by Administrator on 2017/12/12.
 */
public class NewAgentModel {

    private int gameId;
    private String realName;
    private long cellphone;
    private String loginPassword;
    private long parentId;
    private String nickName;
    private int pLevel;
    private long gameUserId;
    private int loginStatus;
    private long clubId;
    private String clubName;
    private double expireDay;
    private String className;
    private String editAdmin;
    private int clubStatus;
    private long editAdminId;


    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public long getCellphone() {
        return cellphone;
    }

    public void setCellphone(long cellphone) {
        this.cellphone = cellphone;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getpLevel() {
        return pLevel;
    }

    public void setpLevel(int pLevel) {
        this.pLevel = pLevel;
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

    public long getClubId() {
        return clubId;
    }

    public void setClubId(long clubId) {
        this.clubId = clubId;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public double getExpireDay() {
        return expireDay;
    }

    public void setExpireDay(double expireDay) {
        this.expireDay = expireDay;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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

    public int getClubStatus() {
        return clubStatus;
    }

    public void setClubStatus(int clubStatus) {
        this.clubStatus = clubStatus;
    }

    public void setEditAdminId(long editAdminId) {
        this.editAdminId = editAdminId;
    }
}
