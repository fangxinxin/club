package dsqp.db_club_log.dao;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db_club_log.model.LogRebateGetModel;

import java.util.Date;

/**
 * Created by ds on 2017/11/22.
 */
public class LogRebateGetDao {

    private static final LogRebateGetDaoImpl impl = new LogRebateGetDaoImpl();

    public static int add(LogRebateGetModel model) {
        return impl.add(model);
    }

    public static DataTable list(long promoterId) {
        return impl.list(promoterId);
    }

    public static DataTable totalRebateGet(int gameId, Date startDate, Date endDate) {
        return impl.totalRebateGet(gameId, startDate, endDate);
    }

}


class LogRebateGetDaoImpl implements dsqp.db.service.BaseDao<LogRebateGetModel> {

    private static final String CONNECTION = "club_log";

    @Override
    public int add(LogRebateGetModel model) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("INSERT INTO log_rebate_get (gameId,promoterId,getDiamond,createTime,createDate) VALUES (?,?,?,?,?);");
        db.addParameter(model.getGameId());
        db.addParameter(model.getPromoterId());
        db.addParameter(model.getGetDiamond());
        db.addParameter(model.getCreateTime());
        db.addParameter(model.getCreateDate());
        return db.executeNonQuery();
    }

    public DataTable totalRebateGet(int gameId, Date startDate, Date endDate) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("SELECT SUM(getDiamond) rebateGet,createDate FROM log_rebate_get WHERE gameId= ? AND createTime BETWEEN ? AND ? GROUP BY createDate;");
        db.addParameter(gameId);
        db.addParameter(startDate);
        db.addParameter(endDate);
        return db.executeQuery();
    }

    @Override
    public int update(LogRebateGetModel logRebateGetModel) {
        return 0;
    }

    @Override
    public LogRebateGetModel getOne(long id) {
        return null;
    }

    @Override
    public DataTable getList() {
        return null;
    }


    public DataTable list(long promoterId) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("select * from log_rebate_get where promoterId = ? order by createTime desc;");
        db.addParameter(promoterId);
        return db.executeQuery();
    }
}