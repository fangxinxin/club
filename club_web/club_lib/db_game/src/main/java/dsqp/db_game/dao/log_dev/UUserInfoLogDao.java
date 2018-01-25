package dsqp.db_game.dao.log_dev;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db.util.SqlUtils;
import dsqp.db.util.proxool.ProxoolUtil;
import dsqp.db_club_dict.model.DictGameDbModel;

import java.util.Date;
import java.util.List;

/**
 * Created by Aris on 2017/7/17.
 */
class UUserInfoLogDaoImpl extends dsqp.db_game.dao.BaseDao {

    public DataTable statUserLogin(DictGameDbModel dictDb, int gameId, Date statDate) {
        String alias1 = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX_READ;
        String dbName1 = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX;

        String alias2 = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName2 = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;
        boolean result2 = true;

        if (!ProxoolUtil.isAliasExist(alias1)) {
            result = ProxoolUtil.registerProxool(alias1, dbName1, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (!ProxoolUtil.isAliasExist(alias2)) {
            result2 = ProxoolUtil.registerProxool(alias2, dbName2, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (result && result2) {
            DBHelper db = new DBHelper(alias2);

            StringBuilder sb = new StringBuilder(128);

            sb.append("select ? as gameId ")
                    .append(",t2.os")
                    .append(",t2.spId")
                    .append(",count(distinct t1.userId) as loginNum")
                    .append(",count(t1.id) as loginTimes")
                    .append(",CAST(SUM((UNIX_TIMESTAMP(IFNULL(logoutTime,DATE_ADD(loginTime,INTERVAL 50 MINUTE))) - UNIX_TIMESTAMP(loginTime))/60) AS UNSIGNED) as onlineTime")
                    .append(",? as createDate")
                    .append(" from u_user_info_log t1 join")
                    .append(dbName1)
                    .append(".u_user_info t2")
                    .append(" on t1.userId = t2.userId")
                    .append(" where loginTime >= ? and loginTime < DATE_ADD(?, interval 1 DAY) ");

            db.createCommand(sb.toString());

            db.addParameter(gameId);
            db.addParameter(statDate);
            db.addParameter(statDate);
            db.addParameter(statDate);

            return db.executeQuery();
        } else {
            return new DataTable();
        }
    }

    public DataTable getLastLogin(DictGameDbModel dictDb, long userId) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }
        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand("SELECT * FROM u_user_info_log WHERE userId= ? and logoutTime is not null ORDER BY logoutTime DESC LIMIT 1;");

            db.addParameter(userId);

            return db.executeQuery();
        } else {
            return new DataTable();
        }
    }

    public DataTable getLastLoginList(DictGameDbModel dictDb, List<Long> userId) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (result) {
            StringBuilder sb = new StringBuilder(128);
            sb.append("SELECT userId AS gameUserId ,MAX(loginTime) AS loginTime,MAX(logoutTime) AS logoutTime,loginIP,registerTime FROM u_user_info_log WHERE userId in ").append(SqlUtils.buildInConditions(userId.size())).append("AND logoutTime IS NOT NULL GROUP BY userId");

            DBHelper db = new DBHelper(alias);
            db.createCommand(sb.toString());

            for (long uid : userId) {
                db.addParameter(uid);
            }

            return db.executeQuery();
        } else {
            return new DataTable();
        }
    }

    public DataTable getLastLoginOfLimit2(DictGameDbModel dictDb, long gameUserId) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand("SELECT userId AS gameUserId , loginTime FROM u_user_info_log WHERE userId = ? AND logoutTime IS NOT NULL ORDER BY loginTime LIMIT 2");
            db.addParameter(gameUserId);

            return db.executeQuery();
        } else {
            return new DataTable();
        }
    }
}

public class UUserInfoLogDao {

    private static final UUserInfoLogDaoImpl impl = new UUserInfoLogDaoImpl();

    public static DataTable statUserLogin(DictGameDbModel dictDb, int gameId, Date statDate) {
        return impl.statUserLogin(dictDb, gameId, statDate);
    }

    public static DataTable getLastLogin(DictGameDbModel dictDb, long userId) {
        return impl.getLastLogin(dictDb, userId);
    }

    public static DataTable getLastLoginList(DictGameDbModel dictDb, List<Long> userId) {
        return impl.getLastLoginList(dictDb, userId);
    }

    public static DataTable getLastLoginOfLimit2(DictGameDbModel dictDb, long gameUserId) {
        return impl.getLastLoginOfLimit2(dictDb, gameUserId);
    }
}
