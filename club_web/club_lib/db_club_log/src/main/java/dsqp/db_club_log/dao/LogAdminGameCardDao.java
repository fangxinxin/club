package dsqp.db_club_log.dao;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DBCommandType;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db_club_log.model.LogAdminGameCardModel;
import dsqp.db_club_log.model.LogGamecardModel;

/**
 * Created by Aris on 2017/7/21.
 */
class LogAdminGameCardDaoImpl implements dsqp.db.service.BaseDao<LogAdminGameCardModel> {

    private static final String CONNECTION = "club_log";

    public int update(LogAdminGameCardModel logAdminGameCardModel) {
        return 0;
    }

    public int add(LogAdminGameCardModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "INSERT INTO log_admin_gamecard (adminId,adminName,gameId,promoterId,changeNum,changeBefore,changeAfter,createTime,createDate) values(?,?,?,?,?,?,?,?,?)";

        db.createCommand(sql, DBCommandType.Text, true);
        DBUtils.addSqlParameters(db, model);

        int result = db.executeNonQuery();
        model.setId(db.generatedValue);
        return result;
    }

    public LogAdminGameCardModel getOne(long id) {
        return null;
    }

    public DataTable getList() {
        return null;
    }

}

public class LogAdminGameCardDao {
    private static final LogAdminGameCardDaoImpl impl = new LogAdminGameCardDaoImpl();

    public static int add(LogAdminGameCardModel model) {
        return impl.add(model);
    }

}
