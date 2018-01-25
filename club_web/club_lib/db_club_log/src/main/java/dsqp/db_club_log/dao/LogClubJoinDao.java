package dsqp.db_club_log.dao;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db.util.SqlUtils;
import dsqp.db_club_log.model.LogClubJoinModel;
import dsqp.util.CommonUtils;

import java.util.List;

/**
 * Created by jeremy on 2017/9/19.
 */
public class LogClubJoinDao {
    private static final LogClubJoinDaoImpl impl = new LogClubJoinDaoImpl();

    public static int getNewMemberNums(int gameId, List<Long> listUserId) {
        return impl.getNewMemberNums(gameId, listUserId);
    }

    public static DataTable getNewMember(int gameId, List<Long> listUserId) {
        return impl.getNewMember(gameId, listUserId);
    }

    public static List<Long> listNewMemberUserId(long clubId, List<Long> listUserId) {
        return impl.listNewMemberUserId(clubId, listUserId);
    }

    public static DataTable listMemberDetail(long clubId, List<Long> listUserId) {
        return impl.listMemberDetail(clubId, listUserId);
    }

    public static int add(LogClubJoinModel model) {
        return impl.add(model);
    }

    public static int removeByClubIdAndGameUserId(long clubId, long gameUserId){
        return impl.removeByClubIdAndGameUserId(clubId, gameUserId);
    }

    public static int removeByClubIdAndUserIds(long clubId, List<Long> listUserId){
        return impl.removeByClubIdAndUserIds(clubId, listUserId);
    }

    public static int removeForRefresh(int gameId, List<Long> listUserId){
        return impl.removeForRefresh(gameId, listUserId);
    }
}

class LogClubJoinDaoImpl implements dsqp.db.service.BaseDao<LogClubJoinModel> {

    private static final String CONNECTION = "club_log";

    public int add(LogClubJoinModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("INSERT INTO log_club_join " +
                "(gameId, clubId, gameUserId, gameNickName, joinWay, createTime, createDate ) " +
                "VALUES(?,?,?,?,?,?,?);");
        db.addParameter(model.getGameId());
        db.addParameter(model.getClubId());
        db.addParameter(model.getGameUserId());
        db.addParameter(model.getGameNickName());
        db.addParameter(model.getJoinWay());
        db.addParameter(model.getCreateTime());
        db.addParameter(model.getCreateDate());
        return db.executeNonQuery();
    }

    public int update(LogClubJoinModel logClubJoinModel) {
        return 0;
    }

    public LogClubJoinModel getOne(long id) {
        return null;
    }

    public DataTable getList() {
        return null;
    }

    //获取新成员人数
    public int getNewMemberNums(int gameId, List<Long> listUserId) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuffer sb = new StringBuffer(128);
        sb.append("SELECT COUNT(gameUserId) as nums FROM (SELECT gameUserId,COUNT(gameUserId) FROM log_club_join WHERE gameId= ? AND gameUserId IN  ")
                .append(SqlUtils.buildInConditions(listUserId.size())).append(" GROUP BY gameUserId HAVING COUNT(gameUserId) = 1)t");

        db.addParameter(gameId);
        for (long id : listUserId) {
            db.addParameter(id);
        }

        db.createCommand(sb.toString());
        DataTable dt = db.executeQuery();

        return dt.rows.length > 0 ? CommonUtils.getIntValue(dt.rows[0].getColumnValue("nums")) : 0;

    }

    //获取新成员userId
    public DataTable getNewMember(int gameId, List<Long> listUserId) {
        if (listUserId.size() == 0) {
            return new DataTable();
        } else {
            DBHelper db = new DBHelper(CONNECTION);

            StringBuffer sb = new StringBuffer(128);
            sb.append("SELECT * FROM (SELECT * FROM log_club_join WHERE gameId= ? AND gameUserId IN  ")
                    .append(SqlUtils.buildInConditions(listUserId.size())).append(" GROUP BY gameUserId HAVING COUNT(gameUserId) = 1)t");

            db.addParameter(gameId);
            for (long id : listUserId) {
                db.addParameter(id);
            }

            db.createCommand(sb.toString());

            return db.executeQuery();
        }

    }


    //获取新成员userId ds
    public List<Long> listNewMemberUserId(long clubId, List<Long> userIds) {
        if (userIds.size() == 0) {
            return null;
        } else {
            DBHelper db = new DBHelper(CONNECTION);

            StringBuffer sb = new StringBuffer(128);
            sb.append("SELECT gameUserId FROM (SELECT clubId, gameUserId FROM log_club_join WHERE gameUserId IN ")
                    .append(SqlUtils.buildInConditions(userIds.size()))
                    .append(" GROUP BY gameUserId)t ")
                    .append("WHERE clubId = ?");

            userIds.forEach(db::addParameter);
            db.addParameter(clubId);

            db.createCommand(sb.toString());
            DataTable dt = db.executeQuery();

            return dt.rows.length > 0 ? DBUtils.convert2List(Long.class, "gameUserId", dt) : null;
        }

    }

    //获取俱乐部成员信息 By ds
    public DataTable listMemberDetail(long clubId, List<Long> userIds) {
        if (userIds.size() == 0) {
            return null;
        } else {
            DBHelper db = new DBHelper(CONNECTION);

            StringBuffer sb = new StringBuffer(128);
            sb.append("SELECT clubId, gameUserId, gameNickName, 1 AS isNewMember FROM (SELECT clubId, gameUserId, gameNickName FROM log_club_join WHERE gameUserId IN ")
                    .append(SqlUtils.buildInConditions(userIds.size()))
                    .append(" GROUP BY gameUserId)t ")
                    .append("WHERE clubId = ?");

            userIds.forEach(db::addParameter);
            db.addParameter(clubId);

            db.createCommand(sb.toString());
            DataTable dt = db.executeQuery();

            return dt;
        }

    }

    //删除记录
    public int removeByClubIdAndGameUserId(long clubId,long gameUserId){
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("delete from log_club_join where clubId = ? and gameUserId = ?");
        db.addParameter(clubId);
        db.addParameter(gameUserId);

        return db.executeNonQuery();
    }

    //释放玩家 ds
    public int removeByClubIdAndUserIds(long clubId, List<Long> userIds) {
        if (userIds == null || userIds.size() == 0) {
            return 0;
        } else {
            DBHelper db = new DBHelper(CONNECTION);

            StringBuffer sb = new StringBuffer(128);
            sb.append("delete from log_club_join where gameUserId IN ")
                    .append(SqlUtils.buildInConditions(userIds.size()))
                    .append(" and clubId = ?;");

            userIds.forEach(db::addParameter);
            db.addParameter(clubId);

            db.createCommand(sb.toString());

            return db.executeNonQuery();
        }
    }

    //刷新玩家身份 ds
    public int removeForRefresh(int gameId, List<Long> userIds) {
        if (userIds == null || userIds.size() == 0) {
            return 0;
        } else {
            DBHelper db = new DBHelper(CONNECTION);

            StringBuffer sb = new StringBuffer(128);
            sb.append("delete from log_club_join where gameUserId IN ")
                    .append(SqlUtils.buildInConditions(userIds.size()))
                    .append(" and gameId = ? and createTime < DATE_SUB(NOW(),INTERVAL 1 DAY);");

            userIds.forEach(db::addParameter);
            db.addParameter(gameId);

            db.createCommand(sb.toString());

            return db.executeNonQuery();
        }
    }

}
