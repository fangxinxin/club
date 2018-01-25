package dsqp.db_club_log.dao;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db_club_log.model.LogClubQuitModel;

/**
 * Created by jeremy on 2017/9/7.
 */
public class LogClubQuitDao {
    private static final LogClubQuitDaoImpl impl = new LogClubQuitDaoImpl();

    public static int add(LogClubQuitModel model) {
        return impl.add(model);
    }

}

class LogClubQuitDaoImpl implements dsqp.db.service.BaseDao<LogClubQuitModel> {

    private static final String CONNECTION = "club_log";

    public int add(LogClubQuitModel logClubQuitModel) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("insert into log_club_quit " +
                "(gameId, clubId, gameUserId, gameNickName, quitWay, createTime, createDate ) " +
                " values (?,?,?,?,?,?,?)");
        db.addParameter(logClubQuitModel.getGameId());
        db.addParameter(logClubQuitModel.getClubId());
        db.addParameter(logClubQuitModel.getGameUserId());
        db.addParameter(logClubQuitModel.getGameNickName());
        db.addParameter(logClubQuitModel.getQuitWay());
        db.addParameter(logClubQuitModel.getCreateTime());
        db.addParameter(logClubQuitModel.getCreateDate());
        return db.executeNonQuery();
    }

    public int update(LogClubQuitModel logClubQuitModel) {
        return 0;
    }

    public LogClubQuitModel getOne(long id) {
        return null;
    }

    public DataTable getList() {
        return null;
    }
}
