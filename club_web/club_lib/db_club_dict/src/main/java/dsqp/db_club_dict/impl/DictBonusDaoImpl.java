package dsqp.db_club_dict.impl;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DBCommandType;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db_club_dict.model.DictBonusModel;

/**
 * Created by Aris on 2017/8/11.
 */
public class DictBonusDaoImpl implements dsqp.db.service.BaseDao<DictBonusModel> {
    private static final String CONNECTION = "club_dict";

    public int add(DictBonusModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "INSERT INTO dict_bonus " +
                "(gameId, directPercent, nonDirectPercent, isEnable, remark) " +
                "VALUES(?,?,?,?,?)";

        db.createCommand(sql, DBCommandType.Text, true);
        DBUtils.addSqlParameters(db, model);
        int result = db.executeNonQuery();
        model.setId(db.generatedValue);
        return result;
    }

    public int update(DictBonusModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sb = new StringBuilder(128);

        sb.append(",directPercent=?");
        db.addParameter(model.getDirectPercent());

        sb.append(",nonDirectPercent=?");
        db.addParameter(model.getNonDirectPercent());

        sb.append(",isEnable=?");
        db.addParameter(model.getIsEnable());

        if (sb.length() == 0){
            return 0;
        }else{
            db.createCommand("UPDATE dict_bonus set " + sb.substring(1) + " where id = ?");
            db.addParameter(model.getId());

            return db.executeNonQuery();
        }
    }

    public DictBonusModel getOne(long id) {
        return null;
    }

    public DictBonusModel getByGameId(int gameId, boolean isEnable) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from dict_bonus where gameId = ? and isEnable = ?");

        db.addParameter(gameId);
        db.addParameter(isEnable);

        DataTable dt = db.executeQuery();

        return dt.rows.length > 0 ? DBUtils.convert2Model(DictBonusModel.class, dt.rows[0]) : null;
    }


    public DataTable getList() {
        return null;
    }
}
