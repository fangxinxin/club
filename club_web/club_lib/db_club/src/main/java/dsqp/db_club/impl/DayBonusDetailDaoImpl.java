package dsqp.db_club.impl;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DBCommandType;
import dsqp.db.model.DataTable;
import dsqp.db.model.ParameterDirection;
import dsqp.db.util.DBUtils;
import dsqp.db_club.model.DayBonusDetailModel;
import dsqp.util.CommonUtils;

/**
 * Created by Aris on 2017/7/21.
 */
public class DayBonusDetailDaoImpl implements dsqp.db.service.BaseDao<DayBonusDetailModel> {

    private static final String CONNECTION = "club";

    public int add(DayBonusDetailModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "INSERT INTO day_bonus_detail " +
                "(bonusId, payId, gameId, promoterId, pLevel, pay, parentId, parentLevel, parentBonus, nonParentId, nonParentLevel, nonParentBonus, payCreateTime, payCreateDate) " +
                " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        db.createCommand(sql, DBCommandType.Text, true);
        DBUtils.addSqlParameters(db, model);

        int result = db.executeNonQuery();
        model.setId(db.generatedValue);
        return result;
    }

    public int update(DayBonusDetailModel model) {
        return 0;
    }

    public DayBonusDetailModel getOne(long id) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from bonus_detail where id = ?");

        db.addParameter(id);
        DataTable dt = db.executeQuery();

        return dt.rows.length > 0 ? (DBUtils.convert2Model(DayBonusDetailModel.class, dt.rows[0])) : null;
    }

    public DataTable getList() {
        return null;
    }

    public DataTable getListByBonusId(long bonusId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from day_bonus_detail where bonusId = ? order by payCreateTime desc;");

        db.addParameter(bonusId);

        return db.executeQuery();
    }

    public DataTable getBonusByBonusId(long bonusId, int gameId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("CP_getBonusByBonusId", DBCommandType.Procedure);

        db.addParameter("IN_bonusId", bonusId);
        db.addParameter("IN_gameId", gameId);

        return db.executeQuery();
    }

    public DataTable getPayByBonusId(long bonusId, int gameId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("CP_getPayByBonusId", DBCommandType.Procedure);

        db.addParameter("IN_bonusId", bonusId);
        db.addParameter("IN_gameId", gameId);

        return db.executeQuery();
    }

    public boolean givingBonusByBonusId(long bonusId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("TRAN_giveBonus", DBCommandType.Procedure);

        db.addParameter("IN_bonusId", bonusId);
        db.addParameter("OUT_result", ParameterDirection.Output);

        db.executeNonQuery();

        return CommonUtils.getIntValue(db.getParamValue("OUT_result")) == 1 ? true : false;
    }


    //查截止今日直属与非直属提成情况
    public DataTable getBonusTotalByEndDate(String endDate, long promoterId) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "(SELECT promoterId, SUM(pay) payTotal,SUM(parentBonus) bonusTotal FROM day_bonus_detail " +
                "WHERE parentId = ? AND payCreateTime < DATE_ADD( ? ,INTERVAL 1 DAY) " +
                "GROUP BY promoterId ORDER BY payTotal DESC)" +
                "UNION ALL" +
                "(SELECT promoterId, SUM(pay) payTotal,SUM(nonParentBonus) bonusTotal FROM day_bonus_detail " +
                "WHERE nonParentId= ? AND payCreateTime < DATE_ADD( ? ,INTERVAL 1 DAY) " +
                "GROUP BY promoterId ORDER BY payTotal DESC)";
        db.createCommand(sql);
        db.addParameter(promoterId);
        db.addParameter(endDate);
        db.addParameter(promoterId);
        db.addParameter(endDate);
        return db.executeQuery();

    }

    //查截止今日直属与非直属提成总额
    public DataTable getBonusTotalToday(String endDate, long promoterId) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "SELECT IFNULL(t1.total,0)+IFNULL(t2.nonTotal,0) bonusTotal ,? AS endDate FROM " +
                "(SELECT SUM(parentBonus) total FROM day_bonus_detail WHERE parentId = ? AND payCreateTime < DATE_ADD(? ,INTERVAL 1 DAY) )t1," +
                "(SELECT SUM(nonParentBonus) nonTotal FROM day_bonus_detail WHERE nonParentId= ? AND payCreateTime < DATE_ADD(? ,INTERVAL 1 DAY) )t2";
        db.createCommand(sql);
        db.addParameter(endDate);
        db.addParameter(promoterId);
        db.addParameter(endDate);
        db.addParameter(promoterId);
        db.addParameter(endDate);
        return db.executeQuery();
    }

    //查截止今日直属提成情况
    public DataTable getDirectBonusTotalByEndDate(String endDate, long promoterId) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "(SELECT promoterId, SUM(pay) payTotal,SUM(parentBonus) bonusTotal ,? AS endDate FROM day_bonus_detail " +
                "WHERE payCreateTime < DATE_ADD( ? ,INTERVAL 1 DAY)" +
                "AND parentId = ? GROUP BY promoterId ORDER BY payTotal DESC)";
        db.createCommand(sql);
        db.addParameter(endDate);
        db.addParameter(endDate);
        db.addParameter(promoterId);
        return db.executeQuery();
    }

    //查每周直属提成情况
    public DataTable getDirectBonusWeek(String startDate, String endDate, long promoterId) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "SELECT ? AS startDate ,?  AS endDate ,SUM(parentBonus) weekTotal FROM day_bonus_detail " +
                "WHERE payCreateTime BETWEEN ? AND DATE_ADD( ? ,INTERVAL 1 DAY) AND parentId = ? ";
        db.createCommand(sql);
        db.addParameter(startDate);
        db.addParameter(endDate);
        db.addParameter(startDate);
        db.addParameter(endDate);
        db.addParameter(promoterId);
        return db.executeQuery();
    }


    //查每周直属以及非直属提成情况
    public DataTable getBonusTotalWeek(String startDate, String endDate, long promoterId) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "SELECT ? AS startDate,? AS endDate , IFNULL(t1.total,0)+IFNULL(t2.nonTotal,0) weekTotal FROM (SELECT SUM(parentBonus) total FROM day_bonus_detail " +
                "WHERE payCreateTime BETWEEN ? AND DATE_ADD(? ,INTERVAL 1 DAY) AND parentId = ? )t1," +
                "(SELECT SUM(nonParentBonus) nonTotal FROM day_bonus_detail " +
                "WHERE payCreateTime BETWEEN ? AND DATE_ADD( ? ,INTERVAL 1 DAY) AND nonParentId= ? )t2";

        db.createCommand(sql);
        db.addParameter(startDate);
        db.addParameter(endDate);
        db.addParameter(startDate);
        db.addParameter(endDate);
        db.addParameter(promoterId);
        db.addParameter(startDate);
        db.addParameter(endDate);
        db.addParameter(promoterId);
        return db.executeQuery();
    }
}
