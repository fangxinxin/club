package dsqp.db_club_dict.impl;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db_club_dict.model.DictMpModel;

/**
 * Created by Aris on 2017/8/1.
 */
public class DictMpDaoImpl implements dsqp.db.service.BaseDao<DictMpModel> {
    private static final String CONNECTION = "club_dict";

    public int add(DictMpModel dictMpModel) {
        return 0;
    }

    public int update(DictMpModel dictMpModel) {
        return 0;
    }

    public DictMpModel getOne(long id) {
        return null;
    }

    public DataTable getList() {
        return null;
    }

    public DictMpModel getByClassName(String className){
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from dict_mp where className = ? ");

        db.addParameter(className);

        DataTable dt = db.executeQuery();

        return dt.rows.length > 0 ? DBUtils.convert2Model(DictMpModel.class, dt.rows[0]) : null;
    }

    public DictMpModel getByGameId(int gameId){
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from dict_mp where gameId = ? ");
        db.addParameter(gameId);
        DataTable dt = db.executeQuery();
        return dt.rows.length > 0 ? DBUtils.convert2Model(DictMpModel.class, dt.rows[0]) : null;
    }
}