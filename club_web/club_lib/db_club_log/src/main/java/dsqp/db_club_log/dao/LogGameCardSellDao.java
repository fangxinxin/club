package dsqp.db_club_log.dao;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DBCommandType;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db_club_log.model.LogGamecardSellModel;
import dsqp.util.CommonUtils;

import java.util.Date;

/**
 * Created by Aris on 2017/9/13.
 */
public class LogGameCardSellDao {
    private static final LogGameCardSellDaoImpl impl = new LogGameCardSellDaoImpl();

    public static int add(LogGamecardSellModel model) {
        return impl.add(model);
    }

    public static int add(int gameId, long promoterId, int gameCardSell, Date statDate) {
        return impl.add(gameId, promoterId, gameCardSell, statDate);
    }

    public static long getGamecardSellByGameIdPromoterIdAndDate(long promoterId, Date date) {
        return impl.getGamecardSellByGameIdPromoterIdAndDate(promoterId, date);
    }


}
class LogGameCardSellDaoImpl extends BaseDaoImpl implements dsqp.db.service.BaseDao<LogGamecardSellModel> {

    public int add(LogGamecardSellModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("INSERT INTO log_gamecard_sell " +
                "(gameId, promoterId, gameCardSell, statDate, createDate)  " +
                " VALUES( ?,?,?,?,?)", DBCommandType.Text, true);
        DBUtils.addSqlParameters(db, model);

        int result = db.executeNonQuery();
        model.setId(db.generatedValue);

        return result;
    }

    public int add(int gameId, long promoterId, int gameCardSell, Date statDate) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("INSERT INTO log_gamecard_sell  " +
                "(gameId, promoterId, gameCardSell, statDate, createDate)  " +
                "VALUES( ?,?,?,?,curdate())", DBCommandType.Text, true);

        db.addParameter(gameId);
        db.addParameter(promoterId);
        db.addParameter(gameCardSell);
        db.addParameter(statDate);

        return db.executeNonQuery();
    }

    public int update(LogGamecardSellModel model) {
        return 0;
    }

    public LogGamecardSellModel getOne(long id) {
        return null;
    }

    public DataTable getList() {
        return null;
    }

    public long getGamecardSellByGameIdPromoterIdAndDate(long promoterId, Date date) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("select gameCardSell from log_gamecard_sell where promoterId=? and statDate = ?");

        db.addParameter(promoterId);
        db.addParameter(date);

        DataTable dt = db.executeQuery();

        return dt.rows.length > 0 ? CommonUtils.getLongValue(dt.rows[0].getColumnValue("gameCardSell")) : 0;
    }
}
