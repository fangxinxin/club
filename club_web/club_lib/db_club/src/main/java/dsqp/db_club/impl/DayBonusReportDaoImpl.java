package dsqp.db_club.impl;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.util.CommonUtils;

import java.util.Date;

/**
 * Created by ds on 2017/9/5.
 */
public class DayBonusReportDaoImpl {

    private static final String CONNECTION = "club";


    public DataTable getDailyTable(long bonusId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM day_bonus_report WHERE bonusId = ? ORDER BY payCreateTime DESC;");

        db.addParameter(bonusId);

        return db.executeQuery();
    }


    public DataTable getDailyTableTotal(long bonusId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT SUM(payTotal) AS payTotal, SUM(directBonus + nonDirectBonus) AS bonusTotal FROM day_bonus_report WHERE bonusId = ?;");

        db.addParameter(bonusId);

        return db.executeQuery();
    }

    public boolean isCheckedBonus(long bonusId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT COUNT(0) AS isPass FROM day_bonus_report WHERE bonusId = ? AND isPass = FALSE;");
        db.addParameter(bonusId);

        boolean result = false;
        DataTable dt = db.executeQuery();
        if (dt.rows.length > 0) {
            result = CommonUtils.getIntValue(dt.rows[0].getColumnValue("isPass")) == 0;
        }

        return result;
    }

    public int updateIsPass(long bonusId, Date payCreateDate, boolean isPass) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("UPDATE day_bonus_report SET isPass = ? WHERE bonusId = ? AND payCreateDate = ?;");
        db.addParameter(isPass);
        db.addParameter(bonusId);
        db.addParameter(payCreateDate);

        return db.executeNonQuery();
    }


    //用于财务对账 提成查询
    public DataTable getDailyTable2(int gameId, long bonusId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT t.*," +
                "(SELECT IFNULL(SUM(price),0) " +
                " FROM promoter_pay " +
                " WHERE gameId=? AND isSuccess=1 AND createDate <= t.payCreateDate) allPayTotal, " +
                " (SELECT IFNULL(SUM(price),0) " +
                " FROM promoter_pay " +
                " WHERE gameId=? AND isSuccess=1 AND createDate <= t.payCreateDate AND payType=1 ) bonusBuyTotal, " +
                " IFNULL((SELECT depositRemain " +
                " FROM day_deposit " +
                " WHERE gameId=? AND statDate=t.payCreateDate ),0) depositRemain " +
                " FROM " +
                " (SELECT * FROM day_bonus_report WHERE bonusId = ? ORDER BY payCreateDate DESC) t;");
        db.addParameter(gameId);
        db.addParameter(gameId);
        db.addParameter(gameId);
        db.addParameter(bonusId);
        return db.executeQuery();
    }
}
