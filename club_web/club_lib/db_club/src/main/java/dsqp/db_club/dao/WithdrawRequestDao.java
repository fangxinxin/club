package dsqp.db_club.dao;


import dsqp.db.model.DataTable;
import dsqp.db.model.SplitPage;
import dsqp.db_club.impl.WithdrawRequestDaoImpl;
import dsqp.db_club.model.WithdrawRequestModel;

import java.util.Date;

/**
 * Created by ds on 2017/6/21.
 */
public class WithdrawRequestDao {

    private static final WithdrawRequestDaoImpl impl = new WithdrawRequestDaoImpl();

    public static DataTable getList() {
        return impl.getList();
    }

    public static DataTable listRequestByDate(String date, int gameId) {
        return impl.listRequestByDate(date, gameId);
    }

    public static DataTable listRequest(String startDate, String endDate, int gameId) {
        return impl.listRequest(startDate, endDate, gameId);
    }

    public static int addRequest(int gameId, long promoterId, int pLevel, String serialNo, String receiptNo, double withdrawBefore, double withdrawAfter, double withdrawAmount, long bankAccound, String remark) {
        return impl.addRequest(gameId, promoterId, pLevel, serialNo, receiptNo, withdrawBefore, withdrawAfter, withdrawAmount, bankAccound, remark);
    }

    public static SplitPage getListByCreateDate(long promoterId, Date startDate, Date endDate, int pageNum, int pageSize) {
        return impl.getPageByDate(promoterId, startDate, endDate, pageNum, pageSize);
    }

    public static int queryRequestTimes(long promoterId) {
        return impl.queryRequestTimes(promoterId);
    }

    public static String queryTotalByPromoterId(long promoterId, int withdrawStatus) {
        return impl.queryTotalByPromoterId(promoterId, withdrawStatus);
    }

    public static DataTable queryByCreateTime(long promoterId, Date startDate, Date endDate) {
        return impl.queryByCreateTime(promoterId, startDate, endDate);
    }

    //算每周提成金额总数
    public static DataTable queryTotalByWeek(String startDate, String endDate, long promoterId) {
        return impl.queryTotalByWeek(startDate, endDate, promoterId);
    }

    //截止今日提成金额总数
    public static DataTable queryTotalByEndDate(String endDate, long promoterId) {
        return impl.queryTotalByEndDate(endDate, promoterId);
    }

    //更新提款状态
    public static int updateStatus(int withdrawStatus, int gameId, String date) {
        return impl.updateStatus(withdrawStatus, gameId, date);
    }

    //更新回执号以及备注
    public static int updateRemark(WithdrawRequestModel model) {
        return impl.updateRemark(model);
    }

    //按时间段查询有回执的数量
    public static DataTable queryReceiptNum(String start, String end, int gameId) {
        return impl.queryReceiptNum(start, end, gameId);
    }

    //按时间段查询 每日的异常提现笔数
    public static DataTable queryExceptionNum(String start, String end, int gameId) {
        return impl.queryExceptionNum(start, end, gameId);
    }
}
