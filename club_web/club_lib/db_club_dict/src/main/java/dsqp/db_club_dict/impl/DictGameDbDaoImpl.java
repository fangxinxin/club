package dsqp.db_club_dict.impl;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db.service.BaseDao;
import dsqp.db.util.DBUtils;
import dsqp.db_club_dict.model.DictGameDbModel;

/**
 * Created by Aris on 2017/2/15.
 */
public class DictGameDbDaoImpl implements BaseDao<DictGameDbModel> {

    private static final String CONNECTION = "club_dict";

    public int add(DictGameDbModel model) {
//        DBHelper db = new DBHelper(CONNECTION);
//
//        db.createCommand("CP_dict_game_db_add", DBCommandType.Procedure);
//        DBUtils.addSpParameters(db, model);
//        //int参数在方法里面添加,out参数在外面添加
//        db.addParameter("OUT_lastId", ParameterDirection.Output);
//
//        int num = db.executeNonQuery();
//        model.setId(Integer.parseInt(db.getParamValue("OUT_lastId")));
//        return num;
        return 0;
    }

    public int update(DictGameDbModel model) {
//        DBHelper db = new DBHelper(CONNECTION);
//
//        db.createCommand("CP_dict_game_db_update", DBCommandType.Procedure);
//        //id要填进去就设置为true
//        DBUtils.addSpParameters(db, model, true);
//
//        return db.executeNonQuery();
        return 0;
    }

    public DictGameDbModel getOne(long id) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from dict_game_db where id = ?");

        db.addParameter(id);
        DataTable dt = db.executeQuery();

        return dt.rows.length>0 ? (DBUtils.convert2Model(DictGameDbModel.class, dt.rows[0])) : null;
    }

    public DataTable getList() {
        return null;
    }

    public DataTable getListByIsEnable(boolean isEnable) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from dict_game_db where isEnable = ?");

        db.addParameter(isEnable);

        return db.executeQuery();
    }

    public DictGameDbModel getByGameId(int gameId, boolean isEnable) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from dict_game_db where gameId = ? and isEnable = ?");

        db.addParameter(gameId);
        db.addParameter(isEnable);
        DataTable dt = db.executeQuery();

        return dt.rows.length > 0 ? DBUtils.convert2Model(DictGameDbModel.class, dt.rows[0]) : null;
    }

}
