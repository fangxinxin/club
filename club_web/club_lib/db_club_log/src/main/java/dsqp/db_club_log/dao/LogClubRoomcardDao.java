package dsqp.db_club_log.dao;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db_club_log.model.LogClubRoomcardModel;

import java.util.Date;

/**
 * Created by ds on 2017/12/25.
 */
public class LogClubRoomcardDao {

    private static LogClubRoomcardDaoImpl impl = new LogClubRoomcardDaoImpl();

    public static int add(LogClubRoomcardModel model) {
        return impl.add(model);
    }

    //存在记录
    public static boolean isExistLog(long roomId, Date createTime) {
        return impl.isExistLog(roomId, createTime);
    }

}

class LogClubRoomcardDaoImpl {

    private static final String CONNECTION = "club_log";

    public int add(LogClubRoomcardModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("INSERT INTO log_club_roomcard" +
                " (gameId, clubId, roomId, cardConsume, gameUserNum, roomRound, gameUserInfo, createTime, createDate )" +
                " VALUES(?,?,?,?,?,?,?,?,?);");
        db.addParameter(model.getGameId());
        db.addParameter(model.getClubId());
        db.addParameter(model.getRoomId());
        db.addParameter(model.getCardConsume());
        db.addParameter(model.getGameUserNum());
        db.addParameter(model.getRoomRound());
        db.addParameter(model.getGameUserInfo());
        db.addParameter(model.getCreateTime());
        db.addParameter(model.getCreateDate());

        return db.executeNonQuery();
    }

    public boolean isExistLog(long roomId, Date createTime) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select id from log_club_roomcard where roomId = ? and createTime = ?;");
        db.addParameter(roomId);
        db.addParameter(createTime);

        DataTable dt = db.executeQuery();

        return dt.rows.length>0 ? true : false;
    }

}
