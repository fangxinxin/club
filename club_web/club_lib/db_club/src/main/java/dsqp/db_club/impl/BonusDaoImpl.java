package dsqp.db_club.impl;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DBCommandType;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db_club.model.BonusModel;

import java.util.Date;

/**
 * Created by Aris on 2017/7/21.
 */
public class BonusDaoImpl implements dsqp.db.service.BaseDao<BonusModel> {

    private static final String CONNECTION = "club";

    public int add(BonusModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "INSERT INTO bonus (gameId, startDate, endDate, isPass, editAdminId, createTime) VALUES(?,?,?,?,?,?)";

        db.createCommand(sql, DBCommandType.Text, true);
        DBUtils.addSqlParameters(db, model);

        int result = db.executeNonQuery();
        model.setId(db.generatedValue);
        return result;
    }

    public int update(BonusModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sb = new StringBuilder(128);

        if (model.getStartDate() != null) {
            sb.append(",startDate=?");
            db.addParameter(model.getStartDate());
        }

        if (model.getEndDate() != null) {
            sb.append(",endDate=?");
            db.addParameter(model.getEndDate());
        }

        if (sb.length() == 0) {
            return 0;
        } else {
            db.createCommand("UPDATE bonus set " + sb.substring(1) + " where id=?");
            db.addParameter(model.getId());

            return db.executeNonQuery();
        }
    }

    public BonusModel getOne(long id) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from bonus where id = ?");

        db.addParameter(id);
        DataTable dt = db.executeQuery();

        return dt.rows.length > 0 ?  (DBUtils.convert2Model(BonusModel.class, dt.rows[0])) : null;
    }

    public DataTable getList() {
        return null;
    }

    public BonusModel getByDate(int gameId, Date startDate, Date endDate) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from bonus where gameId = ? and startDate = ? and endDate = ?;");
        db.addParameter(gameId);
        db.addParameter(startDate);
        db.addParameter(endDate);
        DataTable dt = db.executeQuery();

        return dt.rows.length > 0 ? DBUtils.convert2Model(BonusModel.class, dt.rows[0]) : null;
    }

    public DataTable listByGameId(int gameId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from bonus where gameId = ? ORDER BY endDate DESC;");
        db.addParameter(gameId);

        return db.executeQuery();
    }

    public DataTable listByGameIdAndIsPass(int gameId, boolean isPass) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from bonus where gameId = ? and isPass = ?;");
        db.addParameter(gameId);
        db.addParameter(isPass);

        return db.executeQuery();
    }
}
