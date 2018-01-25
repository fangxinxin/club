package dsqp.db_club_log.dao;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db_club_log.model.LogClubClearModel;

/**
 * Created by fx on 2017/9/30.
 */
public class LogClubClearDao {

    private static final LogClubClearDaoImpl impl = new LogClubClearDaoImpl();

    public static int add(LogClubClearModel model) {
        return impl.add(model);
    }

}

class LogClubClearDaoImpl implements dsqp.db.service.BaseDao<LogClubClearModel> {

    private static final String CONNECTION = "club_log";


    public DataTable listFail(int gameId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from log_club where gameId = ? and clubType in (2,3);");
        db.addParameter(gameId);

        return db.executeQuery();
    }

    public int add(LogClubClearModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("INSERT INTO log_club_clear " +
                "(clubId,clubName,peopleNum,adminId,adminName,createTime,createDate ) " +
                " VALUES(?,?,?,?,?,?,?);");
        db.addParameter(model.getClubId());
        db.addParameter(model.getClubName());
        db.addParameter(model.getPeopleNum());
        db.addParameter(model.getAdminId());
        db.addParameter(model.getAdminName());
        db.addParameter(model.getCreateTime());
        db.addParameter(model.getCreateDate());
        return db.executeNonQuery();
    }

    public int update(LogClubClearModel logClubClearModel) {
        return 0;
    }

    public LogClubClearModel getOne(long id) {
        return null;
    }

    public DataTable getList() {
        return null;
    }
}
