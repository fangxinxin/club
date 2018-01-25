package dsqp.db_club.impl;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db_club.model.MChargeModel;

import java.util.Date;

/**
 * Created by Aris on 2017/7/21.
 */
public class MChargeDaoImpl implements dsqp.db.service.BaseDao<MChargeModel> {

    private static final String CONNECTION = "shdsqp";

    /**
     * 公众号充值查询
     */
    public DataTable getListByDate(int gameId, Date startDate, Date endDate) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT IFNULL(COUNT(DISTINCT userId),0) nums, IFNULL(COUNT(0),0) times , IFNULL(SUM(price),0) price ,DATE_FORMAT(createTime,'%Y-%m-%d') createDate from m_charge_mp WHERE gameId = ? AND payStatus=2 AND createTime BETWEEN ? AND ? GROUP BY createDate ORDER BY createDate DESC;");

        db.addParameter(gameId);
        db.addParameter(startDate);
        db.addParameter(endDate);
        return db.executeQuery();
    }

    /**
     * 公众号充值查询单日明细
     */
    public DataTable getDetailByDate(int gameId, Date startDate, Date endDate) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM m_charge_mp WHERE gameId= ? AND payStatus=2 AND createTime BETWEEN ? AND ? ORDER BY createTime DESC;");

        db.addParameter(gameId);
        db.addParameter(startDate);
        db.addParameter(endDate);
        return db.executeQuery();
    }

    public int add(MChargeModel mChargeModel) {
        return 0;
    }

    public int update(MChargeModel mChargeModel) {
        return 0;
    }

    public MChargeModel getOne(long id) {
        return null;
    }

    public DataTable getList() {
        return null;
    }
}
