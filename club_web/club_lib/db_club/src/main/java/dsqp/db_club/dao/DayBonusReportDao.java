package dsqp.db_club.dao;

import dsqp.db.model.DataTable;
import dsqp.db_club.impl.DayBonusReportDaoImpl;

import java.util.Date;

/**
 * Created by ds on 2017/7/21.
 */
public class DayBonusReportDao {

    private static final DayBonusReportDaoImpl impl = new DayBonusReportDaoImpl();

    //获取每日简表
    public static DataTable getDailyTable(long bonusId) {
        return impl.getDailyTable(bonusId);
    }

    //获取简表充值总额、提成总额
    public static DataTable getDailyTableTotal(long bonusId) {
        return impl.getDailyTableTotal(bonusId);
    }

    public static boolean isCheckedBonus(long bonusId) {
        return impl.isCheckedBonus(bonusId);
    }

    //确认单天提成
    public static int updateIsPass(long bonusId, Date payCreateDate, boolean isPass) {
        return impl.updateIsPass(bonusId, payCreateDate, isPass);
    }

    //用于财务对账 提成查询
    public static DataTable getDailyTable2(int gameId, long bonusId) {
        return impl.getDailyTable2(gameId, bonusId);
    }

}