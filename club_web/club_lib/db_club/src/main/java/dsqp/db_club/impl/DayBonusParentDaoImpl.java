package dsqp.db_club.impl;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DBCommandType;
import dsqp.db.model.DataTable;
import dsqp.db.model.ParameterDirection;
import dsqp.db.model.SplitPage;
import dsqp.util.CommonUtils;

import java.util.Date;

/**
 * Created by Aris on 2017/7/21.
 */
public class DayBonusParentDaoImpl {

    private static final String CONNECTION = "club";


    public DataTable getListByBonusId(long bonusId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT fromPromoterId " +
                " ,pay " +
                " ,IFNULL(SUM(CASE bonusType WHEN 1 THEN promoterId END), 0) AS parentId " +
                " ,IFNULL(SUM(CASE bonusType WHEN 1 THEN bonus END), 0) AS parentBonus " +
                " ,IFNULL(SUM(CASE bonusType WHEN 2 THEN promoterId END), 0) AS nonParentId " +
                " ,IFNULL(SUM(CASE bonusType WHEN 2 THEN bonus END), 0) AS nonParentBonus " +
                " ,payCreateTime " +
                "FROM day_bonus_parent " +
                "WHERE bonusId = ? " +
                "GROUP BY payId " +
                "ORDER BY payCreateTime DESC");

        db.addParameter(bonusId);

        return db.executeQuery();
    }

    public DataTable getDigest(long bonusId, Date payCreateDate) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT " +
                "promoterId," +
                "(SELECT COUNT(0) FROM day_bonus_parent WHERE bonusId = t.bonusId AND payCreateDate = t.payCreateDate AND promoterId = t.promoterId AND bonusType = 1) parentBonusTime, " +
                "(SELECT SUM(bonus) FROM day_bonus_parent WHERE bonusId = t.bonusId AND payCreateDate = t.payCreateDate AND promoterId = t.promoterId AND bonusType = 1) parentBonus, " +
                "(SELECT COUNT(0) FROM day_bonus_parent WHERE bonusId = t.bonusId AND payCreateDate = t.payCreateDate AND promoterId = t.promoterId AND bonusType = 2) nonParentBonusTime, " +
                "(SELECT SUM(bonus) FROM day_bonus_parent WHERE bonusId = t.bonusId AND payCreateDate = t.payCreateDate AND promoterId = t.promoterId AND bonusType = 2) nonParentBonus, " +
                "(SELECT SUM(bonus) FROM day_bonus_parent WHERE bonusId = t.bonusId AND payCreateDate = t.payCreateDate AND promoterId = t.promoterId) totalBonus " +
                "FROM " +
                "day_bonus_parent t " +
                "WHERE " +
                "bonusId = ? AND payCreateDate = ? " +
                "GROUP BY promoterId;");
        db.addParameter(bonusId);
        db.addParameter(payCreateDate);

        return db.executeQuery();
    }

    public SplitPage getBonusByPage(long promoterId, Date startDate, Date endDate, int pageNum, int pageSize) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "SELECT * FROM bonus_detail WHERE promoterId= ? AND createDate BETWEEN ? AND ? ORDER BY createTime DESC limit ?,? ;";
        db.createCommand(sql);
        db.addParameter(promoterId);
        db.addParameter(startDate);
        db.addParameter(endDate);
        db.addParameter((pageNum - 1) * pageSize);
        db.addParameter(pageSize);
        DataTable dt = db.executeQuery();

        DBHelper db2 = new DBHelper(CONNECTION);
        String sql2 = "SELECT count(0) num FROM bonus_detail WHERE promoterId= ? AND createDate BETWEEN ? AND ? ;";
        db2.createCommand(sql2);
        db2.addParameter(promoterId);
        db2.addParameter(startDate);
        db2.addParameter(endDate);
        DataTable dt2 = db2.executeQuery();
        //获取记录数
        int totalNum = dt2.rows.length > 0 ? CommonUtils.getIntValue(dt2.rows[0].getColumnValue("num")) : 0;
        return new SplitPage(pageNum, pageSize, totalNum, dt);
    }

}
