package dsqp.db_club_log.dao;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DBCommandType;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db.util.SqlUtils;
import dsqp.db_club_log.model.LogGamecardConsumeModel;
import dsqp.util.CommonUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Aris on 2017/9/13.
 */
public class LogGamecardConsumeDao {
    private static final LogGamecardConsumeDaoImpl impl = new LogGamecardConsumeDaoImpl();

    public static int add(LogGamecardConsumeModel model) {
        return impl.add(model);
    }

    public static int add(int gameId, long gameUserId, int gameCardConsume, Date statDate) {
        return impl.add(gameId, gameUserId, gameCardConsume, statDate);
    }

    public static long getGamecardConsumeByGameIdGameUserIdsAndDate(int gameId, List<Long> gameUserIds, Date date) {
        return impl.getGamecardConsumeByGameIdGameUserIdsAndDate(gameId, gameUserIds, date);
    }

}

class LogGamecardConsumeDaoImpl extends BaseDaoImpl implements dsqp.db.service.BaseDao<LogGamecardConsumeModel> {

    public int add(LogGamecardConsumeModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("INSERT INTO log_gamecard_consume " +
                "(gameId, gameUserId, gameCardConsume, statDate, createDate) " +
                " VALUES(?,?,?,?,?)", DBCommandType.Text, true);
        DBUtils.addSqlParameters(db, model);

        int result = db.executeNonQuery();
        model.setId(db.generatedValue);

        return result;
    }

    public int add(int gameId, long gameUserId, int gameCardConsume, Date statDate) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("INSERT INTO log_gamecard_consume " +
                "(gameId, gameUserId, gameCardConsume, statDate, createDate)  " +
                "VALUES(?,?,?,?,curdate())", DBCommandType.Text, true);

        db.addParameter(gameId);
        db.addParameter(gameUserId);
        db.addParameter(gameCardConsume);
        db.addParameter(statDate);

        return db.executeNonQuery();
    }

    public int update(LogGamecardConsumeModel model) {
        return 0;
    }

    public LogGamecardConsumeModel getOne(long id) {
        return null;
    }

    public DataTable getList() {
        return null;
    }

    public long getGamecardConsumeByGameIdGameUserIdsAndDate(int gameId, List<Long> gameUserIds, Date date) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sql = new StringBuilder(128);

        sql.append("select ifnull(sum(gameCardConsume), 0) as gameCardConsume from log_gamecard_consume where gameId=? and gameUserId in ").append(SqlUtils.buildInConditions(gameUserIds.size())).append(" and statDate = ?");

        db.createCommand(sql.toString());
        db.addParameter(gameId);
        for (Long id : gameUserIds) {
            db.addParameter(id);
        }
        db.addParameter(date);

        DataTable dt = db.executeQuery();

        return dt.rows.length > 0 ? CommonUtils.getLongValue(dt.rows[0].getColumnValue("gameCardConsume")) : 0;
    }
}