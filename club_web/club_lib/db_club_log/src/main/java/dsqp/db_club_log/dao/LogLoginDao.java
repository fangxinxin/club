package dsqp.db_club_log.dao;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db_club_log.model.LogLoginModel;

/**
 * Created by mj on 2017/8/8.
 */
public class LogLoginDao {

    private static final LogLoginDaoImpl impl = new LogLoginDaoImpl();

    public static int add(LogLoginModel model) {
        return impl.add(model);
    }

    public static int add(long promoterId, String loginIp) {
        return impl.add(promoterId, loginIp);
    }

    public static int update(LogLoginModel logLoginModel) {
        return impl.update(logLoginModel);
    }

    public static DataTable queryByPromoterId(long promoterId) {
        return impl.queryByPromoterId(promoterId);
    }
}

class LogLoginDaoImpl implements dsqp.db.service.BaseDao<LogLoginModel> {
    private static final String CONNECTION = "club_log";

    public int add(LogLoginModel model) {

        return 0;
    }

    public int add(long promoterId, String loginIp) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("insert into log_login " +
                "(promoterId, loginIp, createTime, createDate) " +
                "values(?,?,now(),now())");
        db.addParameter(promoterId);
        db.addParameter(loginIp);

        return db.executeNonQuery();
    }

    public DataTable queryByPromoterId(long promoterId) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("SELECT * FROM log_login WHERE promoterid= ? ORDER BY createtime DESC limit 1;");
        db.addParameter(promoterId);

        return db.executeQuery();
    }

    public int update(LogLoginModel logLoginModel) {
        return 0;
    }

    public LogLoginModel getOne(long id) {
        return null;
    }

    public DataTable getList() {
        return null;
    }
}