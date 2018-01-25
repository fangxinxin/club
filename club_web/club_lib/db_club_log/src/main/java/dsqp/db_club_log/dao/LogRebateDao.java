package dsqp.db_club_log.dao;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db_club_log.model.LogRebateModel;

import java.util.Date;

/**
 * Created by Aris on 2017/11/23.
 */
public class LogRebateDao {
    private LogRebateDao() {
    }

    private static final LogRebateDaoImpl impl = new LogRebateDaoImpl();

    public static int add(LogRebateModel model) {
        return impl.add(model);
    }

    public static DataTable getPayListByDate(long promoterId, Date startDate, Date endDate) {
        return impl.getPayListByDate(promoterId, startDate, endDate);
    }

}

class LogRebateDaoImpl extends BaseDaoImpl implements dsqp.db.service.BaseDao<LogRebateModel> {

    @Override
    public int add(LogRebateModel model) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("INSERT INTO log_rebate (gameId,payId,promoterId,pLevel,diamond,rebateDiamond,createTime,createDate) VALUES(?,?,?,?,?,?,?,?)");
        db.addParameter(model.getGameId());
        db.addParameter(model.getPayId());
        db.addParameter(model.getPromoterId());
        db.addParameter(model.getpLevel());
        db.addParameter(model.getDiamond());
        db.addParameter(model.getRebateDiamond());
        db.addParameter(model.getCreateTime());
        db.addParameter(model.getCreateDate());
        return db.executeNonQuery();
    }


    public DataTable getPayListByDate(long promoterId, Date startDate, Date endDate) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from log_rebate where createTime between ? and ? and promoterId = ? order by createTime desc");
        db.addParameter(startDate);
        db.addParameter(endDate);
        db.addParameter(promoterId);
        return db.executeQuery();
    }

    @Override
    public int update(LogRebateModel logRebateModel) {
        return 0;
    }

    @Override
    public LogRebateModel getOne(long id) {
        return null;
    }

    @Override
    public DataTable getList() {
        return null;
    }
}
