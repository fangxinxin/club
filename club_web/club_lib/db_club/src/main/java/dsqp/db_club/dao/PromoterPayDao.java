package dsqp.db_club.dao;


import dsqp.db.model.DataTable;
import dsqp.db.model.SplitPage;
import dsqp.db_club.impl.PromoterPayDaoImpl;
import dsqp.db_club.model.PromoterPayModel;

import java.util.Date;

/**
 * Created by ds on 2017/6/21.
 */
public class PromoterPayDao {

    private static final PromoterPayDaoImpl impl = new PromoterPayDaoImpl();

    public static int add(PromoterPayModel model) {
        return impl.add(model);
    }

    public static int payByDeposit(PromoterPayModel model) {
        return impl.payByDeposit(model);
    }

    public static int update(PromoterPayModel model) {
        return impl.update(model);
    }

    public static PromoterPayModel getOne(long id) {
        return impl.getOne(id);
    }

    public static DataTable getList() {
        return impl.getList();
    }

    public static DataTable getPayOfPrveMonthById(long promoterId) {
        return impl.getPayOfPrveMonthById(promoterId);
    }

    public static DataTable getPayOfLateMonthById(long promoterId) {
        return impl.getPayOfLateMonthById(promoterId);
    }

    public static DataTable getPayOfCurrentMonthById(long promoterId) {
        return impl.getPayOfCurrentMonthById(promoterId);
    }

    public static DataTable getDirectPayOfLateMonthById(long promoterId) {
        return impl.getDirectPayOfLateMonthById(promoterId);
    }

    public static DataTable getDirectPayOfPrveMonthById(long promoterId) {
        return impl.getDirectPayOfPrveMonthById(promoterId);
    }

    public static DataTable getDirectPayOfCurrentMonthById(long promoterId) {
        return impl.getDirectPayOfCurrentMonthById(promoterId);
    }


    public static SplitPage getPageByDate(long promoterId, Date startTime, Date endTime, int pageNum, int pageSize) {
        return impl.getPageByDate(promoterId, true, startTime, endTime, pageNum, pageSize);
    }

//    public static SplitPage getSuperTotalPayByDatePage(long promoterId, double directPercent, double nonDirectPercent, Date startDate, Date endDate, int pageNum, int pageSize) {
//        return impl.getSuperTotalPayByDatePage(promoterId, true, directPercent, nonDirectPercent, startDate, endDate, pageNum, pageSize);
//    }

    public static double getTotalPayByPromoterId(long promoterId, boolean isSuccess) {
        return impl.getTotalPayByPromoterId(promoterId, isSuccess);
    }

//    public static SplitPage getDirectTotalPayByDate(long promoterId, double directPercent, Date startDate, Date endDate, int pageNum, int pageSize) {
//        return impl.getDirectTotalPayByDate(promoterId, true, directPercent, startDate, endDate, pageNum, pageSize);
//    }

    public static PromoterPayModel getByOrderId(String orderId) {
        return impl.getByOrderId(orderId);
    }

    public static int setOrderSuccess(long id, String payOrderId) {
        return impl.setOrderSuccess(id, payOrderId);
    }

    public static String queryTotalByPromoterIdAndPayType(long promoterId, int payType) {
        return impl.queryTotalByPromoterIdAndPayType(promoterId, payType);
    }

    //个人对账 查购买明细
    public static DataTable getPayListByDate(long promoterId, Date startDate, Date endDate) {
        return impl.getPayListByDate(promoterId, startDate, endDate);
    }

    /**
     * 查询代理升级相关充值信息
     **/
    public static DataTable queryPayInfo(long promoterId, boolean isSuccess) {
        return impl.queryPayInfo(promoterId, isSuccess);
    }

    /**
     * 根据游戏ID :: 获取充值人数
     **/
    public static DataTable listPayNum(int gameId, int payType, Date startDate, Date endDate) {
        return impl.listPayNum(gameId, payType, startDate, endDate);
    }

    /**
     * 按时间段统计 提成金购买房卡情况
     **/
    public static DataTable listByDate(int gameId, int payType, Date startDate, Date endDate) {
        return impl.listByDate(gameId, payType, startDate, endDate);
    }

    /**
     * 按日期查询 提成金购买房卡情况
     **/
    public static DataTable listByDate(int gameId, int payType, Date date) {
        return impl.listByDate(gameId, payType, date);
    }

    /**
     * 查询充值总额，购买房卡总额
     **/
    public static DataTable listPayTotal(int gameId, Date startDate, Date endDate) {
        return impl.listPayTotal(gameId, startDate, endDate);
    }

    /**
     * 按游戏  日期 充值方式 统计所有代理商充值
     */
    public static DataTable listByDateAndGameId(int gameId, int payType, Date startDate, Date endDate) {
        return impl.listByDateAndGameId(gameId, payType, startDate, endDate);
    }

    /**
     * 按游戏  日期  统计所有代理商充值
     */
    public static DataTable listByDateAndGameId(int gameId, Date startDate, Date endDate) {
        return impl.listByDateAndGameId(gameId, startDate, endDate);
    }

    /**
     * 查询单日充值明细 根据游戏id 支付方式
     */
    public static DataTable getListByPayType(int gameId, int payType, Date createDate) {
        return impl.getListByPayType(gameId, payType, createDate);
    }

    /**
     * 查询单日充值明细 根据游戏id
     */
    public static DataTable getListByPayType(int gameId, Date createDate) {
        return impl.getListByGameId(gameId, createDate);
    }
}