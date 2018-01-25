package dsqp.db_club_dict.impl;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db_club_dict.model.DictApiKeyModel;


/**
 * Created by Aris on 2017/8/1.
 */
public class DictApiKeyDaoImpl implements dsqp.db.service.BaseDao<DictApiKeyModel> {
    private static final String CONNECTION = "club_dict";

    public int add(DictApiKeyModel dictMpModel) {
        return 0;
    }

    public int update(DictApiKeyModel dictMpModel) {
        return 0;
    }

    public DictApiKeyModel getOne(long id) {
        return null;
    }

    public DataTable getList() {
        return null;
    }

    public DictApiKeyModel getByGameName(String gameName){
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from dict_api_key where gameName = ? ");

        db.addParameter(gameName);

        DataTable dt = db.executeQuery();

        return dt.rows.length > 0 ? DBUtils.convert2Model(DictApiKeyModel.class, dt.rows[0]) : null;
    }

    public DictApiKeyModel getByGameId(int gameId){
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from dict_api_key where gameId = ? ");
        db.addParameter(gameId);
        DataTable dt = db.executeQuery();
        return dt.rows.length > 0 ? DBUtils.convert2Model(DictApiKeyModel.class, dt.rows[0]) : null;
    }
}