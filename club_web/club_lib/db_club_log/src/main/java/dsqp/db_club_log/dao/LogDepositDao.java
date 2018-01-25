package dsqp.db_club_log.dao;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db_club_log.model.LogDepositModel;
import dsqp.db_club_log.model.LogLoginModel;

/**
 * Created by mj on 2017/8/10.
 */
public class LogDepositDao {

    private static final LogDepositDaoImpl impl = new LogDepositDaoImpl();

    public static int add(LogDepositModel model) {
        return impl.add(model);
    }

    public static int add(int gameId, long promoterId, String nickName, int source, double changeNum, double changeBefore, double changeAfter) {
        return impl.add(gameId, promoterId, nickName, source, changeNum, changeBefore, changeAfter);
    }

    public static int update(LogDepositModel logLoginModel) {
        return impl.update(logLoginModel);
    }
}

class LogDepositDaoImpl implements dsqp.db.service.BaseDao<LogDepositModel> {
    private static final String CONNECTION = "club_log";

    public int add(int gameId, long promoterId, String nickName, int source, double changeNum, double changeBefore, double changeAfter) {

        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("insert into log_deposit " +
                "(gameId, promoterId, nickName, source, changeNum, changeBefore, changeAfter, createTime, createDate) " +
                " values(?,?,?,?,?,?,?,NOW(),NOW())");
        db.addParameter(gameId);
        db.addParameter(promoterId);
        db.addParameter(nickName);
        db.addParameter(source);
        db.addParameter(changeNum);
        db.addParameter(changeBefore);
        db.addParameter(changeAfter);

        return db.executeNonQuery();
    }

    public int add(LogDepositModel logDepositModel) {
        return 0;
    }

    public int update(LogDepositModel logDepositModel) {
        return 0;
    }

    public LogDepositModel getOne(long id) {
        return null;
    }

    public DataTable getList() {
        return null;
    }
}

