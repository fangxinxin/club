package dsqp.db_club_dict.impl;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DBCommandType;
import dsqp.db.model.DataTable;
import dsqp.db.service.BaseDao;
import dsqp.db.util.DBUtils;
import dsqp.db_club_dict.model.DictFormalModel;

/**
 * Created by jeremy on 2017/7/31.
 */
public class DictFormalDaoImpl implements BaseDao<DictFormalModel> {
    private static final String CONNECTION = "club_dict";
    public int add(DictFormalModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "INSERT INTO dict_formal " +
                "(gameId, peopleNum, newPeopleNum, expireDay, pyjRoomNum, nonPyjRoomNum, award, refreshDay, isEnable, createDate) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?)";

        db.createCommand(sql, DBCommandType.Text, true);
        DBUtils.addSqlParameters(db, model);

        int result = db.executeNonQuery();
        model.setId(db.generatedValue);
        return result;
    }

    public int update(DictFormalModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sb = new StringBuilder(128);

        sb.append(",peopleNum=?");
        db.addParameter(model.getPeopleNum());

        sb.append(",newPeopleNum=?");
        db.addParameter(model.getNewPeopleNum());

        if (model.getExpireDay() != 0){
            sb.append(",expireDay=?");
            db.addParameter(model.getExpireDay());
        }

        sb.append(",pyjRoomNum=?");
        db.addParameter(model.getPyjRoomNum());

        sb.append(",nonPyjRoomNum=?");
        db.addParameter(model.getNonPyjRoomNum());

        sb.append(",award=?");
        db.addParameter(model.getAward());

        if (model.getRefreshDay() != 0){
            sb.append(",refreshDay=?");
            db.addParameter(model.getRefreshDay());
        }

        sb.append(",isEnable=?");
        db.addParameter(model.getIsEnable());

        if (sb.length() == 0){
            return 0;
        }else{
            db.createCommand("UPDATE dict_formal set " + sb.substring(1) + " where id = ?");
            db.addParameter(model.getId());

            return db.executeNonQuery();
        }
    }

    public DictFormalModel getOne(long id) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from dict_formal where id = ?");

        db.addParameter(id);

        DataTable dt = db.executeQuery();

        if(dt.rows.length>0){
            return DBUtils.convert2Model(DictFormalModel.class, dt.rows[0]);
        }else{
            return null;
        }
    }

    public DataTable getList() {
        return null;
    }

    public DictFormalModel getByGameId(int gameId){
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from dict_formal where gameId = ? and isEnable = ?");

        db.addParameter(gameId);
        db.addParameter(true);

        DataTable dt = db.executeQuery();
        return dt.rows.length > 0 ? DBUtils.convert2Model(DictFormalModel.class, dt.rows[0]) : null;
    }

}
