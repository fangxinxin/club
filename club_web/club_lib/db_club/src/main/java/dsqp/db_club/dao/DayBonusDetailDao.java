package dsqp.db_club.dao;

import dsqp.db.model.DataTable;
import dsqp.db_club.impl.DayBonusDetailDaoImpl;
import dsqp.db_club.model.DayBonusDetailModel;

/**
 * Created by Aris on 2017/7/21.
 */
public class DayBonusDetailDao {

    private static final DayBonusDetailDaoImpl impl = new DayBonusDetailDaoImpl();

    public static int add(DayBonusDetailModel model) {
        return impl.add(model);
    }
    //获取单笔明细
    public static DataTable getListByBonusId(long bonusId) {
        return impl.getListByBonusId(bonusId);
    }

    //获取提成审核
    public static DataTable getBonusByBonusId(long bonusId, int gameId) {
        return impl.getBonusByBonusId(bonusId, gameId);
    }
    //获取充值报表
    public static DataTable getPayByBonusId(long bonusId, int gameId) {
        return impl.getPayByBonusId(bonusId, gameId);
    }

    //确认通过提成审核
    public static boolean givingBonusByBonusId(long bonusId) {
        return impl.givingBonusByBonusId(bonusId);
    }

    //查直属与非直属提成情况
    public static DataTable getBonusTotalByEndDate(String endDate, long promoterId) {
        return impl.getBonusTotalByEndDate(endDate, promoterId);
    }

    //查直属提成情况
    public static DataTable getDirectBonusTotalByEndDate(String endDate, long promoterId) {
        return impl.getDirectBonusTotalByEndDate(endDate, promoterId);
    }

    //查每周直属提成情况
    public static DataTable getDirectBonusWeek(String startDate, String endDate, long promoterId) {
        return impl.getDirectBonusWeek(startDate, endDate, promoterId);
    }

    //查每周直属以及非直属提成情况
    public static DataTable getBonusTotalWeek(String startDate, String endDate, long promoterId) {
        return impl.getBonusTotalWeek(startDate, endDate, promoterId);
    }

    //查截止今日直属与非直属提成总额
    public static DataTable getBonusTotalToday(String endDate, long promoterId) {
        return impl.getBonusTotalToday(endDate, promoterId);
    }
}