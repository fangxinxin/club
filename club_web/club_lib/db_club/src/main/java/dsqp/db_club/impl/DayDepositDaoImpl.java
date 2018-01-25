package dsqp.db_club.impl;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db_club.model.ClubModel;

/**
 * Created by ds on 2017/7/17.
 */
public class DayDepositDaoImpl implements dsqp.db.service.BaseDao<ClubModel> {

    private static final String CONNECTION = "club";

    public int add(ClubModel clubModel) {
        return 0;
    }

    public int update(ClubModel clubModel) {
        return 0;
    }

    public ClubModel getOne(long id) {
        return null;
    }

    public DataTable getList() {
        return null;
    }

    public DataTable queryTotalByDay(String statDate, int gameId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT depositRemain AS remainTotal ,statDate AS createTime FROM day_deposit  WHERE gameId= ? and statDate = ?");
        db.addParameter(gameId);
        db.addParameter(statDate);
        return db.executeQuery();
    }
}
