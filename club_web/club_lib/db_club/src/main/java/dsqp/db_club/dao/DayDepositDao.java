package dsqp.db_club.dao;


import dsqp.db.model.DataTable;
import dsqp.db_club.impl.DayDepositDaoImpl;

/**
 * Created by ds on 2017/6/21.
 */
public class DayDepositDao {


    private static final DayDepositDaoImpl impl = new DayDepositDaoImpl();

    //统计每天的可提现余额
    public static DataTable queryTotalByDay(String statDate, int gameId) {
        return impl.queryTotalByDay(statDate, gameId);
    }
}
