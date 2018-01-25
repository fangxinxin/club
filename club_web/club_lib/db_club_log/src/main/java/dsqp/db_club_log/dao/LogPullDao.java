package dsqp.db_club_log.dao;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DBCommandType;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db_club_log.model.LogPullModel;

import java.util.Date;

/**
 * Created by Aris on 2017/9/13.
 */
public class LogPullDao {
    private static final LogPullDaoImpl impl = new LogPullDaoImpl();

    public static int add(LogPullModel model) {
        return impl.add(model);
    }

    public static int updateIsFinishById(boolean isFinished, long id) {
        return impl.updateIsFinishById(isFinished, id);
    }

    public static LogPullModel getOne(long id) {
        return impl.getOne(id);
    }

    public static DataTable getListBystatDate(Date statDate, boolean isFinish) {
        return impl.getListBystatDate(statDate, isFinish);
    }

}
class LogPullDaoImpl extends BaseDaoImpl implements dsqp.db.service.BaseDao<LogPullModel> {

    public int add(LogPullModel model) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("insert into log_pull " +
                "(gameId, sourceTable, firstId, lastId, isFinish, statDate, createDate) " +
                " values(?,?,?,?,?,?,?)", DBCommandType.Text, true);

        DBUtils.addSqlParameters(db, model);

        int result = db.executeNonQuery();
        model.setId(db.generatedValue);
        return result;
    }

    public int update(LogPullModel model) {
        return 0;
    }

    public int updateIsFinishById(boolean isFinished, long id) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("update log_pull set isFinish = ? where id = ?");

        db.addParameter(isFinished);
        db.addParameter(id);

        return db.executeNonQuery();
    }

    public LogPullModel getOne(long id) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("select * from log_pull where id = ?");

        db.addParameter(id);

        DataTable dt = db.executeQuery();
        return dt.rows.length > 0 ? DBUtils.convert2Model(LogPullModel.class, dt.rows[0]) : null;
    }

    public DataTable getList() {
        return null;
    }

    public DataTable getListBystatDate(Date statDate, boolean isFinish) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("select * from log_pull where statDate = ? and isFinish = ?");

        db.addParameter(statDate);
        db.addParameter(isFinish);

        return db.executeQuery();
    }

}