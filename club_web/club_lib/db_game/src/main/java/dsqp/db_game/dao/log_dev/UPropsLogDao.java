package dsqp.db_game.dao.log_dev;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db.util.proxool.ProxoolUtil;
import dsqp.db_club_dict.model.DictGameDbModel;
import dsqp.util.CommonUtils;

import java.util.Date;

/**
 * Created by Aris on 2017/9/13.
 */
class UPropsLogDaoImpl extends dsqp.db_game.dao.BaseDao {

    public long getFirstIdByDate(DictGameDbModel dictDb, Date date) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand("SELECT min(id) as id FROM u_props_log WHERE createTime >= ? ORDER BY createTime ASC LIMIT 1");
            db.addParameter(date);

            DataTable dt = db.executeQuery();

            return dt.rows.length > 0 ? CommonUtils.getIntValue(dt.rows[0].getColumnValue("id")) : 0;
        } else {
            return 0;
        }
    }

    public long getLastIdByDate(DictGameDbModel dictDb, Date date) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand("SELECT max(id) as id FROM u_props_log WHERE createTime < ? ORDER BY createTime DESC LIMIT 1");
            db.addParameter(date);

            DataTable dt = db.executeQuery();

            return dt.rows.length > 0 ? CommonUtils.getIntValue(dt.rows[0].getColumnValue("id")) : 0;
        } else {
            return 0;
        }
    }

    public DataTable getGameCardConsumeById(DictGameDbModel dictDb, long firstId, long lastId) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand("SELECT userId,IFNULL(SUM(propsNum), 0) AS gameCardConsume,DATE(createTime) AS createDate FROM u_props_log WHERE id BETWEEN ? AND ? AND propsId=10008 AND propsBefore > propsAfter GROUP BY userId");

            db.addParameter(firstId);
            db.addParameter(lastId);

            return db.executeQuery();
        } else {
            return new DataTable();
        }

    }

    //获取俱乐部成员加入俱乐部之后的钻石总消耗
    public DataTable getGameCardConsus(DictGameDbModel dictDb, long userId, Date joinClubTime) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand("SELECT IFNULL(SUM(propsNum), 0) AS diamondConsus FROM u_props_log WHERE userId= ? AND createTime> ? AND way = 100");

            db.addParameter(userId);
            db.addParameter(joinClubTime);

            return db.executeQuery();
        } else {
            return new DataTable();
        }
    }

    //获取俱乐部成员加入俱乐部之后的开局扣费记录
    public DataTable getGameCardDeduction(DictGameDbModel dictDb, int gameId, long userId, Date joinClubTime) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand("SELECT * FROM u_props_log WHERE gameId = ? AND userId = ? AND createTime > ? AND way = 100");

            db.addParameter(gameId);
            db.addParameter(userId);
            db.addParameter(joinClubTime);

            return db.executeQuery();
        } else {
            return new DataTable();
        }
    }

    //获取俱乐部成员加入俱乐部之后的分享获钻记录
    public DataTable getGameCardShareGain(DictGameDbModel dictDb, int gameId, long userId, Date joinClubTime) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand("SELECT * FROM u_props_log WHERE gameId = ? AND userId = ? AND createTime > ? AND way = 15");

            db.addParameter(gameId);
            db.addParameter(userId);
            db.addParameter(joinClubTime);

            return db.executeQuery();
        } else {
            return new DataTable();
        }
    }

    //获取钻石日志
    public DataTable getPropsLogByUserId(DictGameDbModel dictDb, long userId, boolean isAsc) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (result) {
            DBHelper db = new DBHelper(alias);
            if (isAsc == true) {
                db.createCommand("SELECT userId,nickName,propsBefore,propsAfter,propsNum,createTime FROM u_props_log WHERE userId = ? AND propsId = 10008 ORDER BY createTime LIMIT 10");
            } else {
                db.createCommand("SELECT userId,nickName,propsBefore,propsAfter,propsNum,createTime FROM u_props_log WHERE userId = ? AND propsId = 10008 ORDER BY createTime DESC LIMIT 10");
            }

            db.addParameter(userId);

            return db.executeQuery();
        } else {
            return new DataTable();
        }
    }

    public DataTable getPropsLogByNickName(DictGameDbModel dictDb, String nickName) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand("SELECT userId,nickName,propsBefore,propsAfter,propsNum,createTime FROM u_props_log WHERE nickName = ? AND propsId = 10008 ORDER BY createTime DESC LIMIT 10");

            db.addParameter(nickName);

            return db.executeQuery();
        } else {
            return new DataTable();
        }
    }


}

public class UPropsLogDao {
    private static final UPropsLogDaoImpl impl = new UPropsLogDaoImpl();

    public static long getFirstIdByDate(DictGameDbModel dictDb, Date date) {
        return impl.getFirstIdByDate(dictDb, date);
    }

    public static long getLastIdByDate(DictGameDbModel dictDb, Date date) {
        return impl.getLastIdByDate(dictDb, date);
    }

    public static DataTable getGameCardConsumeById(DictGameDbModel dictDb, long firstId, long lastId) {
        return impl.getGameCardConsumeById(dictDb, firstId, lastId);
    }

    public static DataTable getGameCardConsus(DictGameDbModel dictDb, long userId, Date joinClubTime) {
        return impl.getGameCardConsus(dictDb, userId, joinClubTime);
    }

    public static DataTable getGameCardDeduction(DictGameDbModel dictDb, int gameId, long userId, Date joinClubTime) {
        return impl.getGameCardDeduction(dictDb, gameId, userId, joinClubTime);
    }

    public static DataTable getGameCardShareGain(DictGameDbModel dictDb, int gameId, long userId, Date joinClubTime) {
        return impl.getGameCardShareGain(dictDb, gameId, userId, joinClubTime);
    }

    public static DataTable getPropsLogByUserId(DictGameDbModel dictDb, long userId, boolean isAsc) {
        return impl.getPropsLogByUserId(dictDb, userId, isAsc);
    }

    public static DataTable getPropsLogByNickName(DictGameDbModel dictDb, String nickName) {
        return impl.getPropsLogByNickName(dictDb, nickName);
    }
}
