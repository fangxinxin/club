package dsqp.db_club.impl;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db_club.model.BonusDetailModel;
import dsqp.util.CommonUtils;

import java.util.Date;

/**
 * Created by Aris on 2017/11/28.
 */
public class BonusDetailDaoImpl implements dsqp.db.service.BaseDao<BonusDetailModel> {
    private static final String CONNECTION = "club";

    @Override
    public int add(BonusDetailModel model) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("INSERT INTO bonus_detail (" +
                "  gameId," +
                "  payId," +
                "  pLevel," +
                "  promoterId," +
                "  fromPromoterId," +
                "  diamond," +
                "  rebateDiamond," +
                "  rebatePercent," +
                "  createTime," +
                "  createDate" +
                ") " +
                "VALUES (?,?,?,?,?,?,?,?,now(),curdate())");
        db.addParameter(model.getGameId());
        db.addParameter(model.getPayId());
        db.addParameter(model.getpLevel());
        db.addParameter(model.getPromoterId());
        db.addParameter(model.getFromPromoterId());
        db.addParameter(model.getDiamond());
        db.addParameter(model.getRebateDiamond());
        db.addParameter(model.getRebatePercent());
        return db.executeNonQuery();
    }

    @Override
    public int update(BonusDetailModel model) {
        return 0;
    }

    @Override
    public BonusDetailModel getOne(long id) {
        return null;
    }

    @Override
    public DataTable getList() {
        return null;
    }

    public int getTotalRebateByPromoterId(long promoterId){
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("SELECT IFNULL(SUM(rebateDiamond),0) AS totalRebate FROM bonus_detail WHERE promoterId = ?");

        db.addParameter(promoterId);
        DataTable dt = db.executeQuery();
        return dt.rows.length > 0 ? CommonUtils.getIntValue(dt.rows[0].getColumnValue("totalRebate")) : 0;
    }

    public DataTable getRebateDetail(long promoterId, Date startDate, Date endDate){
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("SELECT fromPromoterId,diamond,rebateDiamond,createTime FROM bonus_detail WHERE promoterId = ? AND pLevel != 2 AND rebateDiamond != 0 AND createDate BETWEEN ? AND ? ORDER BY createTime DESC");

        db.addParameter(promoterId);
        db.addParameter(startDate);
        db.addParameter(endDate);
        DataTable dt = db.executeQuery();
        return dt;
    }


}
