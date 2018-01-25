package dsqp.db_game.dao.stat;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db.util.proxool.ProxoolUtil;
import dsqp.db_club_dict.model.DictGameDbModel;

import java.util.Date;

/**
 * Created by Aris on 2017/7/19.
 */
class StatDaoImpl extends dsqp.db_game.dao.BaseDao {

    public DataTable getUserLogin(DictGameDbModel dictDb, int gameId, Date statDate){
        String alias1  = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX_READ;
        String dbName1 = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX;

        String alias2  = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName2 = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        if (!ProxoolUtil.isAliasExist(alias1)){
            if (ProxoolUtil.registerProxool(alias1, dbName1, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty())){
                return new DataTable();
            }
        }

        if (!ProxoolUtil.isAliasExist(alias2)){
            if (ProxoolUtil.registerProxool(alias2, dbName2, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty())){
                return new DataTable();
            }
        }

        DBHelper db = new DBHelper(alias2);

        String sql = "SELECT  ? AS gameId " +
                " ,os" +
                " ,spId" +
                " ,COUNT(DISTINCT t1.userId) AS loginNum" +
                " ,COUNT(t1.id) AS loginTimes" +
                " ,CAST(SUM((UNIX_TIMESTAMP(IFNULL(logoutTime,DATE_ADD(loginTime,INTERVAL 50 MINUTE))) - UNIX_TIMESTAMP(loginTime))/60) AS SIGNED) AS onlineTime" +
                " ,? AS createDate" +
                " FROM u_user_info_log t1" +
                " JOIN " + dbName1 +  ".u_user_info t2" +
                " ON t1.userId = t2.userId" +
                " WHERE loginTime >= ? AND loginTime < DATE_ADD(?, INTERVAL 1 DAY)" +
                " GROUP BY os,spId;";

        db.createCommand(sql);

        db.addParameter(gameId);
        db.addParameter(statDate);
        db.addParameter(statDate);
        db.addParameter(statDate);

        return db.executeQuery();
    }

    public DataTable getUserPyj(DictGameDbModel dictDb, int gameId, Date statDate){
        String alias  = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        if (!ProxoolUtil.isAliasExist(alias)){
            if (ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty())){
                return new DataTable();
            }
        }

        DBHelper db = new DBHelper(alias);

        String sql = "SELECT ? as gameId,t2.userNum,t4.totalTime,? AS createDate" +
                " FROM (" +
                " SELECT COUNT(0) AS userNum" +
                " FROM (" +
                " SELECT userId" +
                " FROM u_pyj_user_record" +
                " WHERE gameStartTime >= ? AND gameStartTime < DATE_ADD(?,INTERVAL 1 DAY)" +
                " GROUP BY userId,DATE(gameStartTime)" +
                " ) t1" +
                " )t2," +
                " (" +
                " SELECT SUM(totalTime) totalTime" +
                " FROM(" +
                " SELECT (UNIX_TIMESTAMP(gameStopTime) - UNIX_TIMESTAMP(gameStartTime))/60 totalTime" +
                " FROM u_pyj_record_log" +
                " WHERE gameStartTime >= ? AND gameStartTime < DATE_ADD(?,INTERVAL 1 DAY)" +
                " )t3" +
                " )t4";

        db.createCommand(sql);

        db.addParameter(gameId);
        db.addParameter(statDate);
        db.addParameter(statDate);
        db.addParameter(statDate);
        db.addParameter(statDate);
        db.addParameter(statDate);

        return db.executeQuery();
    }

}
public class StatDao {
    private static final StatDaoImpl impl = new StatDaoImpl();

    public static DataTable getUserLogin(DictGameDbModel dictDb, int gameId, Date statDate){
        return impl.getUserLogin(dictDb, gameId, statDate);
    }

    public static DataTable getUserPyj(DictGameDbModel dictDb, int gameId, Date statDate){
        return impl.getUserPyj(dictDb, gameId, statDate);
    }

}
