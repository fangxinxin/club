package dsqp.db_club.impl;

import dsqp.db.db.DBHelper;
import dsqp.db.model.*;
import dsqp.db.service.BaseDao;
import dsqp.db_club.model.WithdrawRequestModel;
import dsqp.util.CommonUtils;

import java.util.Date;

public class WithdrawRequestDaoImpl implements BaseDao<WithdrawRequestModel> {

    private static final String CONNECTION = "club";

    public int add(WithdrawRequestModel withdrawRequestModel) {
        return 0;
    }

    public int update(WithdrawRequestModel withdrawRequestModel) {
        return 0;
    }

    public WithdrawRequestModel getOne(long id) {
        return null;
    }

    public DataTable getList() {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from withdraw_request ");
        return db.executeQuery();
    }

    public DataTable listRequest(String startDate, String endDate, int gameId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT COUNT(0) requestNum, COUNT(DISTINCT promoterId,0) peopleNum, SUM(withdrawAmount) withdrawTotal, createDate as createTime, withdrawStatus " +
                " FROM  withdraw_request WHERE gameId = ? and createDate BETWEEN ? AND ? GROUP BY createDate ORDER BY createTime DESC;");
        db.addParameter(gameId);
        db.addParameter(startDate);
        db.addParameter(endDate);
        return db.executeQuery();
    }

    public DataTable listRequestByDate(String date, int gameId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from withdraw_request where gameId= ? and createDate = ? order by createTime desc;");
        db.addParameter(gameId);
        db.addParameter(date);
        return db.executeQuery();
    }

    public int addRequest(int gameId, long promoterId, int pLevel, String serialNo, String receiptNo, double withdrawBefore, double withdrawAfter, double withdrawAmount, long bankAccound, String remark) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("INSERT INTO withdraw_request " +
                "(gameId, promoterId, pLevel, serialNo, receiptNo, withdrawBefore, withdrawAfter, withdrawAmount, bankAccount, withdrawStatus, editAdmin, editTime, remark, createDate, createTime ) " +
                " VALUE (?,?,?,?,?,?,?,?,?,0,0,NOW(),?,NOW(),NOW())");
        db.addParameter(gameId);
        db.addParameter(promoterId);
        db.addParameter(pLevel);
        db.addParameter(serialNo);
        db.addParameter(receiptNo);
        db.addParameter(withdrawBefore);
        db.addParameter(withdrawAfter);
        db.addParameter(withdrawAmount);
        db.addParameter(bankAccound);
        db.addParameter(remark);
        return db.executeNonQuery();
    }

    //分页
    public SplitPage getPageByDate(long promoterId, Date startDate, Date endDate, int pageNum, int pageSize) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("CP_withdraw_request_getPage", DBCommandType.Procedure);
        db.addParameter("IN_promoterId", promoterId);
        db.addParameter("IN_pageNum", pageNum);
        db.addParameter("IN_pageSize", pageSize);
        db.addParameter("IN_startDate", startDate);
        db.addParameter("IN_endDate", endDate);
        db.addParameter("OUT_total", ParameterDirection.Output);
        DataTable dt = db.executeQuery();
        //获取记录数
        int totalNum = Integer.parseInt(db.getParamValue("OUT_total"));
        return new SplitPage(pageNum, pageSize, totalNum, dt);
    }

    public int queryRequestTimes(long promoterId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select count(0) sums from withdraw_request where promoterid= ? and createtime between SUBDATE(CURDATE(), WEEKDAY(CURDATE())) and now()");
        db.addParameter(promoterId);
        DataTable dt = db.executeQuery();

        return dt.rows.length > 0 ? CommonUtils.getIntValue(dt.rows[0].getColumnValue("sums")) : 0;
    }

    public String queryTotalByPromoterId(long promoterId, int withdrawStatus) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand(" SELECT SUM(withdrawAmount) sums FROM withdraw_request WHERE promoterId= ? and withdrawStatus = ? ");
        db.addParameter(promoterId);
        db.addParameter(withdrawStatus);
        DataTable dt = db.executeQuery();

        return dt.rows.length > 0 ? dt.rows[0].getColumnValue("sums") : "0";
    }

    public DataTable queryByCreateTime(long promoterId, Date startDate, Date endDate) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM withdraw_request WHERE promoterId = ? and createDate between ? and ? order by createTime desc");
        db.addParameter(promoterId);
        db.addParameter(startDate);
        db.addParameter(endDate);
        return db.executeQuery();
    }

    //算每周提现金额总数
    public DataTable queryTotalByWeek(String startDate, String endDate, long promoterId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT SUM(withdrawAmount) weekTotal , ? AS startDate, ? AS endDate  FROM withdraw_request WHERE promoterId = ?  AND createTime BETWEEN ? AND DATE_ADD( ? ,INTERVAL 1 DAY)");
        db.addParameter(startDate);
        db.addParameter(endDate);
        db.addParameter(promoterId);
        db.addParameter(startDate);
        db.addParameter(endDate);
        return db.executeQuery();
    }

    //截止今日提现金额总数
    public DataTable queryTotalByEndDate(String endDate, long promoterId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT SUM(withdrawAmount) total,? AS endDate FROM withdraw_request WHERE promoterId= ? AND createTime < DATE_ADD( ? ,INTERVAL 1 DAY)");
        db.addParameter(endDate);
        db.addParameter(promoterId);
        db.addParameter(endDate);
        return db.executeQuery();
    }

    //更新提款状态
    public int updateStatus(int withdrawStatus, int gameId, String date) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("update withdraw_request set withdrawStatus = ?  where gameId= ? and createDate= ? ");
        db.addParameter(withdrawStatus);
        db.addParameter(gameId);
        db.addParameter(date);
        return db.executeNonQuery();
    }

    //更新回执号以及备注
    public int updateRemark(WithdrawRequestModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("update withdraw_request set receiptNo = ? , remark= ? , withdrawStatus=? where id= ?");
        db.addParameter(model.getReceiptNo());
        db.addParameter(model.getRemark());
        db.addParameter(model.getWithdrawStatus());
        db.addParameter(model.getId());
        return db.executeNonQuery();
    }

    //按时间段查询有回执的数量
    public DataTable queryReceiptNum(String start, String end, int gameId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT createDate AS createTime , COUNT(0) AS receiptNum  FROM withdraw_request " +
                "WHERE receiptNo != '' AND gameId= ? AND createDate BETWEEN ? AND ? GROUP BY createDate;");
        db.addParameter(gameId);
        db.addParameter(start);
        db.addParameter(end);
        return db.executeQuery();
    }

    //按时间段查询 每日的异常提现笔数
    public DataTable queryExceptionNum(String start, String end, int gameId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT createDate AS createTime , COUNT(0) AS exceptionNum  FROM withdraw_request " +
                "WHERE withdrawStatus=2 AND gameId= ? AND createDate BETWEEN ? AND ? GROUP BY createDate;");
        db.addParameter(gameId);
        db.addParameter(start);
        db.addParameter(end);
        return db.executeQuery();
    }
}
