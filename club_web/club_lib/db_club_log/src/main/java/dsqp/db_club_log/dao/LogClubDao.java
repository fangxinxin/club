package dsqp.db_club_log.dao;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db_club_log.model.LogClubModel;

/**
 * Created by mj on 2017/9/7.
 */
public class LogClubDao {

    private static final LogClubDaoImpl impl = new LogClubDaoImpl();

    public static int add(LogClubModel logClubModel) {
        return impl.add(logClubModel);
    }

    public static LogClubModel getByCludIdAndType(long clubId, int clubType) {
        {
            return impl.getByCludIdAndType(clubId, clubType);
        }
    }

    public static DataTable listFail(int gameId) {
        return impl.listFail(gameId);
    }
}

class LogClubDaoImpl implements dsqp.db.service.BaseDao<LogClubModel> {

    private static final String CONNECTION = "club_log";

    public int add(LogClubModel logClubModel) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("INSERT INTO log_club " +
                "(gameId, gameUserId, gameNickName, clubId, peopleNum, peopleNumNew, pyjNum, pyjNumNew, clubType, expireTime, createTime, createDate ) " +
                " VALUES (?,?,?,?,?,?,?,?,?,?,?,?);");
        db.addParameter(logClubModel.getGameId());
        db.addParameter(logClubModel.getGameUserId());
        db.addParameter(logClubModel.getGameNickName());
        db.addParameter(logClubModel.getClubId());
        db.addParameter(logClubModel.getPeopleNum());
        db.addParameter(logClubModel.getPeopleNumNew());
        db.addParameter(logClubModel.getPyjNum());
        db.addParameter(logClubModel.getPyjNumNew());
        db.addParameter(logClubModel.getClubType());
        db.addParameter(logClubModel.getExpireTime());
        db.addParameter(logClubModel.getCreateTime());
        db.addParameter(logClubModel.getCreateDate());
        return db.executeNonQuery();
    }


    public int update(LogClubModel logClubModel) {
        return 0;
    }

    public LogClubModel getOne(long id) {
        return null;
    }

    public DataTable getList() {
        return null;
    }


    public LogClubModel getByCludIdAndType(long clubId, int clubType) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from log_club where clubId = ? and clubType = ?");
        db.addParameter(clubId);
        db.addParameter(clubType);

        DataTable dt = db.executeQuery();
        return dt.rows.length > 0 ? (DBUtils.convert2Model(LogClubModel.class, dt.rows[0])) : null;
    }

    public DataTable listFail(int gameId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from log_club where gameId = ? and clubType in (2,3) ORDER BY createTime DESC;");
        db.addParameter(gameId);

        return db.executeQuery();
    }

}
