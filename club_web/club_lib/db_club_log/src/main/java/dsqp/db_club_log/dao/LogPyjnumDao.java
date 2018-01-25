package dsqp.db_club_log.dao;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DBCommandType;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db.util.SqlUtils;
import dsqp.db_club_log.model.LogPyjnumModel;
import dsqp.util.CommonUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Aris on 2017/9/8.
 */
public class LogPyjnumDao {
    private static final  LogPyjnumDaoImpl impl = new LogPyjnumDaoImpl();

    public static int add(LogPyjnumModel model) {
        return impl.add(model);
    }

    public static int add(int gameId, long gameUserId, int pyjNum, Date statDate) {
        return impl.add(gameId, gameUserId, pyjNum, statDate);
    }

    public static long getPyjNumByGameIdGameUserIdsAndDate(int gameId, List<Long> gameUserIds, Date date){
        return impl.getPyjNumByGameIdGameUserIdsAndDate(gameId, gameUserIds, date);
    }

}

class LogPyjnumDaoImpl extends BaseDaoImpl implements dsqp.db.service.BaseDao<LogPyjnumModel> {

    public int add(LogPyjnumModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("INSERT INTO log_pyjnum " +
                "(gameId, gameUserId, pyjNum, statDate, createDate) " +
                " VALUES(?,?,?,?,?)", DBCommandType.Text, true);
        DBUtils.addSqlParameters(db, model);

        int result = db.executeNonQuery();
        model.setId(db.generatedValue);

        return result;
    }

    public int add(int gameId, long gameUserId, int pyjNum, Date statDate) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("INSERT INTO log_pyjnum " +
                "(gameId, gameUserId, pyjNum, statDate, createDate) " +
                " VALUES( ?,?,?,?,curdate())", DBCommandType.Text, true);

        db.addParameter(gameId);
        db.addParameter(gameUserId);
        db.addParameter(pyjNum);
        db.addParameter(statDate);

        return db.executeNonQuery();
    }

    public int update(LogPyjnumModel model) {
        return 0;
    }

    public LogPyjnumModel getOne(long id) {
        return null;
    }

    public DataTable getList() {
        return null;
    }

    public long getPyjNumByGameIdGameUserIdsAndDate(int gameId, List<Long> gameUserIds, Date date){
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sql = new StringBuilder(128);

        sql.append("select ifnull(sum(pyjNum), 0) as pyjNum from log_pyjnum where gameId=? and gameUserId in ").append(SqlUtils.buildInConditions(gameUserIds.size())).append(" and statDate = ?");

        db.createCommand(sql.toString());
        db.addParameter(gameId);
        for (Long id : gameUserIds) {
            db.addParameter(id);
        }
        db.addParameter(date);

        DataTable dt = db.executeQuery();

        return dt.rows.length > 0 ? CommonUtils.getLongValue(dt.rows[0].getColumnValue("pyjNum")) : 0;
    }
}
