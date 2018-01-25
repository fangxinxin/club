package dsqp.db_club_dict.impl;

import com.google.common.base.Strings;
import dsqp.db.db.DBHelper;
import dsqp.db.model.DBCommandType;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db_club_dict.model.DictClubModel;

/**
 * Created by jeremy on 2017/9/27.
 */
public class DictClubDaoImpl implements dsqp.db.service.BaseDao<DictClubModel>{
    private static final String CONNECTION = "club_dict";

    public int add(DictClubModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "INSERT INTO dict_club " +
                "(gameId, joinMax, isAllowSell, remark) " +
                "VALUES(?,?,?,?)";

        db.createCommand(sql, DBCommandType.Text, true);
        DBUtils.addSqlParameters(db, model);

        int result = db.executeNonQuery();
        model.setId(db.generatedValue);
        return result;
    }

    public int update(DictClubModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sb = new StringBuilder(128);

        if (model.getJoinMax() != 0){
            sb.append(",joinMax=?");
            db.addParameter(model.getJoinMax());
        }

        if (Strings.isNullOrEmpty(model.getRemark())){
            sb.append(",remark=?");
            db.addParameter(model.getRemark());
        }

        sb.append(",isAllowSell=?");
        db.addParameter(model.getIsAllowSell());

        if (sb.length() == 0){
            return 0;
        }else{
            db.createCommand("UPDATE dict_club set " + sb.substring(1) + " where id = ?");
            db.addParameter(model.getId());

            return db.executeNonQuery();
        }
    }

    public DictClubModel getOne(long id) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from dict_club where id = ?");

        db.addParameter(id);

        DataTable dt = db.executeQuery();

        if(dt.rows.length>0){
            return DBUtils.convert2Model(DictClubModel.class, dt.rows[0]);
        }else{
            return null;
        }
    }

    public DataTable getList() {
        return null;
    }

    public DictClubModel getByGameId(int gameId){
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from dict_club where gameId = ? ");

        db.addParameter(gameId);

        DataTable dt = db.executeQuery();
        return dt.rows.length > 0 ? DBUtils.convert2Model(DictClubModel.class, dt.rows[0]) : null;
    }

}
