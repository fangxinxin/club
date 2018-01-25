package dsqp.db_club_dict.impl;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DBCommandType;
import dsqp.db.model.DataTable;
import dsqp.db.service.BaseDao;
import dsqp.db.util.DBUtils;
import dsqp.db_club_dict.model.DictLevelUpModel;

/**
 * Created by jeremy on 2017/7/26.
 */
public class DictLevelUpDaoImpl implements BaseDao<DictLevelUpModel> {

    private static final String CONNECTION = "club_dict";

    public int add(DictLevelUpModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "INSERT INTO dict_levelup " +
                "(gameId, levelUpType, totalPay, totalPromoter, isEnable ) " +
                "VALUES(?,?,?,?,?)";

        db.createCommand(sql, DBCommandType.Text, true);
        DBUtils.addSqlParameters(db, model);

        int result = db.executeNonQuery();
        model.setId(db.generatedValue);
        return result;
    }

    public int update(DictLevelUpModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sb = new StringBuilder(128);

        if (model.getLevelUpType() != 0){
            sb.append(",levelUpType=?");
            db.addParameter(model.getLevelUpType());
        }

        sb.append(",totalPay=?");
        db.addParameter(model.getTotalPay());

        sb.append(",totalPromoter=?");
        db.addParameter(model.getTotalPromoter());

        sb.append(",isEnable=?");
        db.addParameter(model.getIsEnable());

        if (sb.length() == 0){
            return 0;
        }else{
            db.createCommand("UPDATE dict_levelup set " + sb.substring(1) + " where id = ?");
            db.addParameter(model.getId());

            return db.executeNonQuery();
        }
    }

    public DictLevelUpModel getOne(long id) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from dict_levelup where id = ?");

        db.addParameter(id);
        DataTable dt = db.executeQuery();

        return dt.rows.length>0 ? (DBUtils.convert2Model(DictLevelUpModel.class, dt.rows[0])) : null;
    }

    public DataTable getList() {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from dict_levelup");
        return db.executeQuery();
    }

    public DictLevelUpModel getConditionByGameIdAndLevelUpType(int gameId,int levelUpType) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from dict_levelup where gameId = ? and levelUpType = ?");

        db.addParameter(gameId);

        db.addParameter(levelUpType);

        DataTable dt = db.executeQuery();

        if(dt.rows.length>0) {
           return DBUtils.convert2Model(DictLevelUpModel.class, dt.rows[0]);
       }
        return null;
    }

}
