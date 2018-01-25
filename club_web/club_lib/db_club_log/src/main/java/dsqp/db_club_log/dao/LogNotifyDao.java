package dsqp.db_club_log.dao;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DBCommandType;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db_club_log.model.LogNotifyModel;

/**
 * Created by Aris on 2017/7/21.
 */

public class LogNotifyDao {
    private static final LogNotifyDaoImpl impl = new LogNotifyDaoImpl();

    public static int add(LogNotifyModel model) {
        return impl.add(model);
    }

    public static int update(long id, String responseParam) {
        return impl.update(id, responseParam);
    }
}

class LogNotifyDaoImpl implements dsqp.db.service.BaseDao<LogNotifyModel> {

    private static final String CONNECTION = "club_log";

    public int add(LogNotifyModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "INSERT INTO log_notify " +
                " (requestUrl,requestParam,requestMethod,contentType,responseParam,createTime,createDate) " +
                " VALUES (?,?,?,?,?,?,?)";

        db.createCommand(sql, DBCommandType.Text, true);
        DBUtils.addSqlParameters(db, model);

        int result = db.executeNonQuery();
        model.setId(db.generatedValue);
        return result;
    }

    public int update(LogNotifyModel logNotifyModel) {
        return 0;
    }

    public int update(long id, String responseParam) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "update log_notify set responseParam = ? where id = ?";

        db.createCommand(sql);
        db.addParameter(responseParam);
        db.addParameter(id);

        return db.executeNonQuery();
    }

    public LogNotifyModel getOne(long id) {
        return null;
    }

    public DataTable getList() {
        return null;
    }

}

