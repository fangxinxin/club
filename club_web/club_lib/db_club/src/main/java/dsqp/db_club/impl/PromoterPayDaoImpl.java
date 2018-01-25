package dsqp.db_club.impl;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DBCommandType;
import dsqp.db.model.DataTable;
import dsqp.db.model.ParameterDirection;
import dsqp.db.model.SplitPage;
import dsqp.db.service.BaseDao;
import dsqp.db.util.DBUtils;
import dsqp.db_club.model.PromoterPayModel;

import java.util.Date;

public class PromoterPayDaoImpl implements BaseDao<PromoterPayModel> {

    private static final String CONNECTION = "club";

    public int add(PromoterPayModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "INSERT INTO promoter_pay " +
                "(gameId, promoterId, pLevel, nickName, parentId, orderId, payType, payOrderId, price, goodNum, goodGivingNum, isSuccess, createTime, createDate ) " +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        db.createCommand(sql, DBCommandType.Text, true);
        DBUtils.addSqlParameters(db, model);

        int result = db.executeNonQuery();
        model.setId(db.generatedValue);
        return result;
    }

    public int payByDeposit(PromoterPayModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("TRAN_setOrderSuccessByCash", DBCommandType.Procedure);
        DBUtils.addSpParameters(db, model);
        db.addParameter("OUT_lastId", ParameterDirection.Output);

        db.executeNonQuery();
        model.setId(Integer.parseInt(db.getParamValue("OUT_lastId")));
        return (int) model.getId();
    }

    public int update(PromoterPayModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("CP_promoter_pay_update", DBCommandType.Procedure);
        //id要填进去就设置为true
        DBUtils.addSpParameters(db, model, true);

        return db.executeNonQuery();
    }

    public PromoterPayModel getOne(long id) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from promoter_pay where id = ?");

        db.addParameter(id);
        DataTable dt = db.executeQuery();

        return dt.rows.length > 0 ? (DBUtils.convert2Model(PromoterPayModel.class, dt.rows[0])) : null;
    }

    public DataTable getList() {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from promoter_pay");
        return db.executeQuery();
    }

    public SplitPage getPageByDate(long promoterId, boolean isSuccess, Date startDate, Date endDate, int pageNum, int pageSize) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("CP_promoter_pay_getPage", DBCommandType.Procedure);

        db.addParameter("IN_promoterId", promoterId);
        db.addParameter("IN_isSuccess", isSuccess);
        db.addParameter("IN_startDate", startDate);
        db.addParameter("IN_endDate", endDate);
        db.addParameter("IN_pageNum", pageNum);
        db.addParameter("IN_pageSize", pageSize);
        db.addParameter("OUT_total", ParameterDirection.Output);

        DataTable dt = db.executeQuery();
        //获取记录数
        int totalNum = Integer.parseInt(db.getParamValue("OUT_total"));
        return new SplitPage(pageNum, pageSize, totalNum, dt);
    }

    public DataTable getPayListByDate(long promoterId, Date startDate, Date endDate) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select *,id AS payId from promoter_pay where createTime between ? and ? and promoterId = ? order by createTime desc");
        db.addParameter(startDate);
        db.addParameter(endDate);
        db.addParameter(promoterId);
        return db.executeQuery();
    }

    //获取上个月的购买总额
    public DataTable getPayOfPrveMonthById(long promoterId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT SUM(price) AS totalPrice FROM promoter_pay WHERE createDate > LAST_DAY(DATE_SUB(CURDATE(), INTERVAL 2 MONTH)) AND createDate <= LAST_DAY(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) AND promoterId = ? AND isSuccess = ? ");

        db.addParameter(promoterId);
        db.addParameter(1);

        return db.executeQuery();
    }

    //获取最近一个月的购买总额
    public DataTable getPayOfLateMonthById(long promoterId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT SUM(price) AS totalPrice FROM promoter_pay WHERE promoterId = ? AND createDate >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH) AND isSuccess = ? ");

        db.addParameter(promoterId);
        db.addParameter(1);

        return db.executeQuery();
    }

    //获取当月的购买总额
    public DataTable getPayOfCurrentMonthById(long promoterId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT SUM(price) AS totalPrice FROM promoter_pay WHERE promoterId = ? AND createDate > LAST_DAY(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) AND createDate <= LAST_DAY(CURDATE()) AND isSuccess = ? ");

        db.addParameter(promoterId);
        db.addParameter(1);

        return db.executeQuery();
    }

    //获取名下直属最近一个月购买总额
    public DataTable getDirectPayOfLateMonthById(long parentId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT SUM(price) AS totalPrice FROM promoter_pay WHERE promoterId IN (SELECT id FROM promoter WHERE parentId = ?) AND createDate >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH) AND isSuccess = ? ");

        db.addParameter(parentId);
        db.addParameter(1);

        return db.executeQuery();
    }

    //获取名下直属上个月购买总额
    public DataTable getDirectPayOfPrveMonthById(long parentId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT SUM(price) AS totalPrice FROM promoter_pay WHERE promoterId IN (SELECT id FROM promoter WHERE parentId = ? ) AND createDate > LAST_DAY(DATE_SUB(CURDATE(), INTERVAL 2 MONTH)) AND createDate <= LAST_DAY(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) AND isSuccess = ? ");

        db.addParameter(parentId);
        db.addParameter(1);

        return db.executeQuery();
    }

    //获取名下直属当月的购买总额
    public DataTable getDirectPayOfCurrentMonthById(long parentId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT SUM(price) AS totalPrice FROM promoter_pay WHERE promoterId IN (SELECT id FROM promoter WHERE parentId = ? ) AND createDate > LAST_DAY(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) AND createDate <= LAST_DAY(CURDATE()) AND isSuccess = ? ");

        db.addParameter(parentId);
        db.addParameter(1);

        return db.executeQuery();
    }

    public double getTotalPayByPromoterId(long promoterId, boolean isSuccess) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT SUM(price) totalPay FROM promoter_pay WHERE promoterId = ? AND isSuccess = ?;");

        db.addParameter(promoterId);
        db.addParameter(isSuccess);
        DataTable dt = db.executeQuery();

        double totalPay = 0;
        if (dt.rows.length > 0) {
            totalPay = Double.parseDouble(dt.rows[0].getColumnValue("totalPay"));
        }
        return totalPay;
    }

    public PromoterPayModel getByOrderId(String orderId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from promoter_pay where orderId = ?");

        db.addParameter(orderId);
        DataTable dt = db.executeQuery();

        return dt.rows.length > 0 ? (DBUtils.convert2Model(PromoterPayModel.class, dt.rows[0])) : null;
    }

    public int setOrderSuccess(long id, String payOrderId) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("UPDATE promoter_pay SET payOrderId = ?, isSuccess = TRUE WHERE id = ? AND isSuccess = FALSE");
        db.addParameter(payOrderId);
        db.addParameter(id);
        return db.executeNonQuery();
    }

    public String queryTotalByPromoterIdAndPayType(long promoterId, int payType) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT SUM(price) price FROM promoter_pay WHERE promoterid= ? AND payType= ?");

        db.addParameter(promoterId);
        db.addParameter(payType);
        DataTable dt = db.executeQuery();
        return dt.rows[0].getColumnValue("price");
    }

    public DataTable listPayNum(int gameId, int payType, Date startDate, Date endDate) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT COUNT(0), createDate " +
                "FROM promoter_pay " +
                "WHERE gameId = ? AND payType= ? AND createDate BETWEEN ? AND ? " +
                "GROUP BY createDate");

        db.addParameter(gameId);
        db.addParameter(payType);
        db.addParameter(startDate);
        db.addParameter(endDate);
        return db.executeQuery();
    }

    //提成金购买统计
    public DataTable listByDate(int gameId, int payType, Date startDate, Date endDate) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT IFNULL((SELECT COUNT(0) FROM promoter_pay WHERE gameId=t.gameId AND createDate <= t.createDate),0) promoterNum ,t.* FROM " +
                " (SELECT COUNT(DISTINCT 0) payNum,COUNT(0) times,SUM(price) prices,gameId,createDate " +
                " FROM promoter_pay WHERE gameId= ? AND isSuccess = true AND payType=? AND createDate BETWEEN ? AND ? " +
                " GROUP BY createDate ORDER BY createDate DESC) t;");

        db.addParameter(gameId);
        db.addParameter(payType);
        db.addParameter(startDate);
        db.addParameter(endDate);
        return db.executeQuery();
    }


    //查询充值总额，购买房卡总额
    public DataTable listPayTotal(int gameId, Date startDate, Date endDate) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT t.*,(SELECT IFNULL(COUNT(0),0) FROM promoter WHERE gameId=t.gameId AND createDate<=t.createDate) promoterNum " +
                " FROM " +
                " (SELECT createDate,IFNULL(SUM(price),0) allPrices ,IFNULL(SUM(goodNum),0) goodNums,IFNULL(SUM(goodGivingNum),0) goodGivingNums,gameId" +
                " FROM promoter_pay " +
                " WHERE gameId= ? AND isSuccess = true AND createDate BETWEEN ? AND ? " +
                " GROUP BY createDate ORDER BY createDate DESC) t");

        db.addParameter(gameId);
        db.addParameter(startDate);
        db.addParameter(endDate);
        return db.executeQuery();
    }

    //按日期查询 提成金购买情况
    public DataTable listByDate(int gameId, int payType, Date date) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM promoter_pay WHERE gameId=? AND payType= ? AND createDate= ? AND isSuccess = true");
        db.addParameter(gameId);
        db.addParameter(payType);
        db.addParameter(date);
        return db.executeQuery();
    }

    //按游戏  日期 充值方式 统计所有代理商充值
    public DataTable listByDateAndGameId(int gameId, int payType, Date startDate, Date endDate) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT COUNT(DISTINCT promoterId) nums, " +
                " COUNT(0) times, " +
                " SUM(price) price, " +
                " createDate " +
                " FROM " +
                " promoter_pay " +
                " WHERE " +
                " isSuccess=TRUE " +
                " AND gameId= ?  " +
                " AND payType= ? " +
                " AND createDate BETWEEN ? AND ? " +
                "GROUP BY " +
                " createDate " +
                " ORDER BY " +
                " createDate DESC");
        db.addParameter(gameId);
        db.addParameter(payType);
        db.addParameter(startDate);
        db.addParameter(endDate);
        return db.executeQuery();
    }

    //按游戏  日期  统计所有代理商充值
    public DataTable listByDateAndGameId(int gameId, Date startDate, Date endDate) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT COUNT(DISTINCT promoterId) nums, " +
                " COUNT(0) times, " +
                " SUM(price) price, " +
                " createDate " +
                " FROM " +
                " promoter_pay " +
                " WHERE " +
                " isSuccess = TRUE " +
                " AND gameId = ?  " +
                " AND createDate BETWEEN ? AND ? " +
                "GROUP BY " +
                " createDate " +
                " ORDER BY " +
                " createDate DESC");
        db.addParameter(gameId);
        db.addParameter(startDate);
        db.addParameter(endDate);
        return db.executeQuery();
    }

    //查询单日明细 根据游戏id 支付方式
    public DataTable getListByPayType(int gameId, int payType, Date createDate) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM promoter_pay WHERE gameId= ?  AND createDate= ? AND payType= ? AND isSuccess=TRUE");
        db.addParameter(gameId);
        db.addParameter(createDate);
        db.addParameter(payType);
        return db.executeQuery();
    }

    //查询单日明细 根据游戏id
    public DataTable getListByGameId(int gameId, Date createDate) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM promoter_pay WHERE gameId= ?  AND createDate= ? AND isSuccess=TRUE");
        db.addParameter(gameId);
        db.addParameter(createDate);
        return db.executeQuery();
    }

    public DataTable queryPayInfo(long promoterId, boolean isSuccess) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand(
                //获取最近一个月的购买总额(往前退一个月--现在)
                "SELECT " +
                        "(SELECT SUM(price) FROM promoter_pay WHERE createDate >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH) AND promoterId = ?  AND isSuccess = ?) AS lateTotalPrice, " +
                        //近一个月代理商下线购买钻石总金额
                        "(SELECT SUM(price) FROM promoter_pay WHERE promoterId IN (SELECT id FROM promoter WHERE parentId = ?) AND createDate >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH) AND isSuccess = ?) AS dLateTotalPrice, " +
                        //当月代理商自己购买总额（本月）
                        "(SELECT SUM(price) FROM promoter_pay WHERE promoterId = ? AND createDate > LAST_DAY(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) AND createDate <= LAST_DAY(CURDATE()) AND isSuccess = ?) AS currentTotalPrice, " +
                        //当月直属代理商购买总额
                        "(SELECT SUM(price) FROM promoter_pay WHERE promoterId IN (SELECT id FROM promoter WHERE parentId = ? ) AND createDate > LAST_DAY(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) AND createDate <= LAST_DAY(CURDATE()) AND isSuccess = ?) AS dCurrentTotalPrice "
        );

        db.addParameter(promoterId);
        db.addParameter(isSuccess);
        db.addParameter(promoterId);
        db.addParameter(isSuccess);
        db.addParameter(promoterId);
        db.addParameter(isSuccess);
        db.addParameter(promoterId);
        db.addParameter(isSuccess);
        return db.executeQuery();
    }


}
