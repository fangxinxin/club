package dsqp.db_club_dict.model;

/**
 * Created by Aris on 2017/7/14.
 */
public class DictGameDbModel {
    private int id;
    private int gameId;
    private String dbNamePrefix;
    private String dbUrl;
    private String dbUser;
    private String dbPass;
    private String dbUrlRead;
    private String dbUserRead;
    private String dbPassRead;
    private String webDomain;
    private int webPort;
    private int itemId;
    private boolean isEnable;

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

    public String getDbNamePrefix() {
        return dbNamePrefix;
    }

    public void setDbNamePrefix(String dbNamePrefix) {
        this.dbNamePrefix = dbNamePrefix;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPass() {
        return dbPass;
    }

    public void setDbPass(String dbPass) {
        this.dbPass = dbPass;
    }

    public String getDbUrlRead() {
        return dbUrlRead;
    }

    public void setDbUrlRead(String dbUrlRead) {
        this.dbUrlRead = dbUrlRead;
    }

    public String getDbUserRead() {
        return dbUserRead;
    }

    public void setDbUserRead(String dbUserRead) {
        this.dbUserRead = dbUserRead;
    }

    public String getDbPassRead() {
        return dbPassRead;
    }

    public void setDbPassRead(String dbPassRead) {
        this.dbPassRead = dbPassRead;
    }

    public String getWebDomain() {
        return webDomain;
    }

    public void setWebDomain(String webDomain) {
        this.webDomain = webDomain;
    }

    public int getWebPort() {
        return webPort;
    }

    public void setWebPort(int webPort) {
        this.webPort = webPort;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public boolean getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }
}
