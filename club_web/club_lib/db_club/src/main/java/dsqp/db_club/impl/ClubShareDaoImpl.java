package dsqp.db_club.impl;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db.service.BaseDao;
import dsqp.db.util.DBUtils;
import dsqp.db.util.SqlUtils;
import dsqp.db_club.model.ClubShareModel;

import java.util.List;

/**
 * Created by ds on 2017/7/17.
 */
public class ClubShareDaoImpl implements BaseDao<ClubShareModel> {

    private static final String CONNECTION = "club";

    public int add(ClubShareModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("INSERT INTO club_share " +
                "(mpClassName, gameId, gameUserId, unionid, promoterId, clubId, clubURL, headImg, joinStatus, createTime, createDate ) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?)");
        DBUtils.addSqlParameters(db, model);

        return db.executeNonQuery();
    }

    public int update(ClubShareModel model) {
        return 0;
    }

    public int updateByJoinStatus(int joinStatus, long id) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("update club_share set joinStatus = ? where id = ?");
        db.addParameter(joinStatus);
        db.addParameter(id);

        return db.executeNonQuery();
    }

    public ClubShareModel getOne(long id) {
        return null;
    }

    public ClubShareModel getByUnionid(String unionId, int gameId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM club_share WHERE unionid = ? and gameId = ?");
        db.addParameter(unionId);
        db.addParameter(gameId);

        DataTable dt = db.executeQuery();
        return dt.rows.length > 0 ? DBUtils.convert2Model(ClubShareModel.class, dt.rows[0]) : null;
    }

    public ClubShareModel getByUnionidAndClubId(String unionId, int gameId, long clubId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM club_share WHERE unionid = ? and gameId = ? and clubId = ?");
        db.addParameter(unionId);
        db.addParameter(gameId);
        db.addParameter(clubId);

        DataTable dt = db.executeQuery();
        return dt.rows.length > 0 ? DBUtils.convert2Model(ClubShareModel.class, dt.rows[0]) : null;
    }

    public ClubShareModel getByUnionidAndStatus(String unionId, int gameId, long clubId, int joinStatus) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM club_share WHERE unionid = ? and gameId = ? and clubId = ? and joinStatus = ?");
        db.addParameter(unionId);
        db.addParameter(gameId);
        db.addParameter(clubId);
        db.addParameter(joinStatus);

        DataTable dt = db.executeQuery();
        return dt.rows.length > 0 ? DBUtils.convert2Model(ClubShareModel.class, dt.rows[0]) : null;
    }

    public DataTable getList() {
        return null;
    }

    public int removeByClubId(long clubId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("delete from club_share where clubId = ?");
        db.addParameter(clubId);

        return db.executeNonQuery();
    }

    public int removeByClubIdAndGameUserId(long clubId, long gameUserId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("delete from club_share where clubId = ? and gameUserId = ?;");
        db.addParameter(clubId);
        db.addParameter(gameUserId);

        return db.executeNonQuery();
    }

    public int removeByClubId(List<Long> clubIds) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuffer sb = new StringBuffer(128);
        sb.append("DELETE FROM club_share WHERE clubId in ")
                .append(SqlUtils.buildInConditions(clubIds.size()));

        for (long id : clubIds) {
            db.addParameter(id);
        }
        db.createCommand(sb.toString());
        return db.executeNonQuery();
    }
}
