package dsqp.db_club.dao;

import dsqp.db.model.DataTable;
import dsqp.db_club.impl.MChargeDaoImpl;

import java.util.Date;

/**
 * Created by Aris on 2017/7/21.
 */
public class MChargeDao {

    private static final MChargeDaoImpl impl = new MChargeDaoImpl();

    /**
     * 公众号充值查询
     */
    public static DataTable getListByDate(int gameId, Date startDate, Date endDate) {
        return impl.getListByDate(gameId, startDate, endDate);
    }

    /**
     * 公众号充值查询单日明细
     */
    public static DataTable getDetailByDate(int gameId, Date startDate, Date endDate) {
        return impl.getDetailByDate(gameId, startDate, endDate);
    }
}
