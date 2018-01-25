package dsqp.db_club_log.dao;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DBCommandType;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db_club_log.model.LogGamecardModel;

import java.util.Date;

/**
 * Created by Aris on 2017/7/21.
 */
class LogGamecardDaoImpl implements dsqp.db.service.BaseDao<LogGamecardModel> {

    private static final String CONNECTION = "club_log";

    public int add(LogGamecardModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "INSERT INTO log_gamecard  " +
                "(gameId, promoterId, nickName, source, changeNum, changeBefore, changeAfter, createTime, createDate) " +
                " values(?,?,?,?,?,?,?,?,?)";

        db.createCommand(sql, DBCommandType.Text, true);
        DBUtils.addSqlParameters(db, model);

        int result = db.executeNonQuery();
        model.setId(db.generatedValue);
        return result;
    }

    public DataTable queryChangeNumByDate(int gameId, Date startDate, Date endDate) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "SELECT IFNULL(SUM(changeNum),0) changeNum,createDate " +
                " FROM log_gamecard " +
                " WHERE gameId= ? AND  createDate BETWEEN ? AND ? AND source=3 OR source=4 " +
                " GROUP BY createDate ORDER BY createDate DESC;";
        db.createCommand(sql);
        db.addParameter(gameId);
        db.addParameter(startDate);
        db.addParameter(endDate);
        return db.executeQuery();
    }

    public int update(LogGamecardModel model) {
        return 0;
    }

    public LogGamecardModel getOne(long id) {
        return null;
    }

    public DataTable getList() {
        return null;
    }

    public LogGamecardModel getLateOpreTime(long promoterId){
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("SELECT * FROM log_gamecard WHERE promoterId = ? AND source NOT IN (3,4) ORDER BY createTime DESC LIMIT ?;");
        db.addParameter(promoterId);
        db.addParameter(1);

        DataTable dt = db.executeQuery();
        return dt.rows.length > 0 ? (DBUtils.convert2Model(LogGamecardModel.class, dt.rows[0])):null;
    }

}

public class LogGamecardDao {
    private static final LogGamecardDaoImpl impl = new LogGamecardDaoImpl();

    public static int add(LogGamecardModel model) {
        return impl.add(model);
    }

    public static LogGamecardModel getLateOpreTime(long promoterId){
        return impl.getLateOpreTime(promoterId);
    }

    public static DataTable queryChangeNumByDate(int gameId, Date startDate, Date endDate) {
        return impl.queryChangeNumByDate(gameId, startDate, endDate);
    }

}
