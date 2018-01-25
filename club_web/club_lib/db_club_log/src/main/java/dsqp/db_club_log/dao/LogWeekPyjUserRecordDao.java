package dsqp.db_club_log.dao;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DBCommandType;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db_club_log.model.LogWeekPyjUserRecordModel;
import dsqp.util.CommonUtils;

/**
 * Created by ds on 2018/1/5.
 */
public class LogWeekPyjUserRecordDao {
    private static final LogWeekPyjUserRecordDaoImpl impl = new LogWeekPyjUserRecordDaoImpl();

    public static int addOnDuplicate(LogWeekPyjUserRecordModel model) {
        return impl.addOnDuplicate(model);
    }

    public static int getPyjRoomNum(long clubId, long gameUserId, int year, int week) {
        return impl.getPyjRoomNum(clubId, gameUserId, year, week);
    }

}

class LogWeekPyjUserRecordDaoImpl {
    private static final String CONNECTION = "club_log";

    public int addOnDuplicate(LogWeekPyjUserRecordModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "INSERT INTO log_week_pyj_user_record (gameId,clubId,promoterId,gameUserId,gameNickName,pyjRoomNum,year,week,createTime) VALUES(?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE pyjRoomNum=?,createTime=now()";

        db.createCommand(sql, DBCommandType.Text, true);
        DBUtils.addSqlParameters(db, model);
        db.addParameter(model.getPyjRoomNum());

        return db.executeNonQuery();
    }


    public int getPyjRoomNum(long clubId, long gameUserId, int year, int week) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT pyjRoomNum FROM log_week_pyj_user_record WHERE clubId = ? AND gameUserId = ? AND year = ? AND week = ?;");
        db.addParameter(clubId);
        db.addParameter(gameUserId);
        db.addParameter(year);
        db.addParameter(week);

        DataTable dt = db.executeQuery();

        return dt.rows.length>0 ? CommonUtils.getIntValue(dt.rows[0].getColumnValue("pyjRoomNum")) : 0;
    }
}
