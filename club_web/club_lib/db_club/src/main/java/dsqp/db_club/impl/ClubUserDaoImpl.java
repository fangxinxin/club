package dsqp.db_club.impl;

import com.google.common.base.Strings;
import dsqp.db.db.DBHelper;
import dsqp.db.model.DBCommandType;
import dsqp.db.model.DataTable;
import dsqp.db.model.ParameterDirection;
import dsqp.db.model.SplitPage;
import dsqp.db.service.BaseDao;
import dsqp.db.util.DBUtils;
import dsqp.db.util.SqlUtils;
import dsqp.db_club.model.ClubUserModel;
import dsqp.util.CommonUtils;

import java.util.List;

/**
 * Created by ds on 2017/7/17.
 */
public class ClubUserDaoImpl implements BaseDao<ClubUserModel> {

    private static final String CONNECTION = "club";

    public int removeByClubIdAndGameUserId(long clubId, long gameUserId) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("delete from club_user where clubId= ? and gameUserId = ? ");
        db.addParameter(clubId);
        db.addParameter(gameUserId);

        return db.executeNonQuery();
    }

    //清空俱乐部成员
    public int removeByClubId(long clubId) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("delete from club_user where clubId= ? AND promoterId !=0");
        db.addParameter(clubId);

        return db.executeNonQuery();
    }

    //俱乐部审核到期 :: 清空俱乐部 、 获取俱乐部玩家
    public int removeByClubId(List<Long> clubIds) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuffer sb = new StringBuffer(128);
        sb.append("DELETE FROM club_user WHERE clubId in ")
                .append(SqlUtils.buildInConditions(clubIds.size()));

        for (long id : clubIds) {
            db.addParameter(id);
        }
        db.createCommand(sb.toString());
        return db.executeNonQuery();
    }

    public DataTable listClubUserByClubId(List<Long> clubIds) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuffer sb = new StringBuffer(128);
        sb.append("SELECT * FROM club_user WHERE clubId in ")
                .append(SqlUtils.buildInConditions(clubIds.size()));

        for (long id : clubIds) {
            db.addParameter(id);
        }
        db.createCommand(sb.toString());
        return db.executeQuery();
    }

    public DataTable listClubUserByPromoterId(List<Long> promoterIds) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuffer sb = new StringBuffer(128);
        sb.append("SELECT * FROM club_user WHERE promoterId in ")
                .append(SqlUtils.buildInConditions(promoterIds.size()));

        for (long id : promoterIds) {
            db.addParameter(id);
        }
        db.createCommand(sb.toString());
        return db.executeQuery();
    }


    public int add(ClubUserModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "INSERT INTO club_user " +
                "(gameId, clubId, promoterId, promoterGameUserId, gameUserId, gameNickName, createTime )  " +
                " VALUES( ?,?,?,?,?,?,?)";

        db.createCommand(sql, DBCommandType.Text, true);
        DBUtils.addSqlParameters(db, model);

        int result = db.executeNonQuery();
        model.setId(db.generatedValue);
        return result;
    }

    public int update(ClubUserModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sb = new StringBuilder(128);

        if (model.getClubId() != 0) {
            sb.append(",clubId=?");
            db.addParameter(model.getClubId());
        }

        if (model.getPromoterId() != 0) {
            sb.append(",promoterId=?");
            db.addParameter(model.getPromoterId());
        }

        if (model.getGameUserId() != 0) {
            sb.append(",gameUserId=?");
            db.addParameter(model.getGameUserId());
        }

        if (Strings.isNullOrEmpty(model.getGameNickName())) {
            sb.append(",gameNickName=?");
            db.addParameter(model.getGameNickName());
        }

        if (sb.length() == 0) {
            return 0;
        } else {
            db.createCommand("UPDATE club_user set " + sb.substring(1) + " where id=?");
            db.addParameter(model.getId());

            return db.executeNonQuery();
        }
    }

    public ClubUserModel getOne(long id) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM club_user WHERE id = ?;");
        db.addParameter(id);
        DataTable dt = db.executeQuery();

        return dt.rows.length > 0 ? (DBUtils.convert2Model(ClubUserModel.class, dt.rows[0])) : null;
    }

    public ClubUserModel getByGameIdAndGameUserId(int gameId, long gameUserId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM club_user WHERE gameId = ? and gameUserId = ?");
        db.addParameter(gameId);
        db.addParameter(gameUserId);

        DataTable dt = db.executeQuery();
        return dt.rows.length > 0 ? DBUtils.convert2Model(ClubUserModel.class, dt.rows[0]) : null;
    }

    public ClubUserModel getByGameIdGameUserIdAndClubId(int gameId, long gameUserId, long clubId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM club_user WHERE gameId = ? and gameUserId = ? and clubId = ?");
        db.addParameter(gameId);
        db.addParameter(gameUserId);
        db.addParameter(clubId);

        DataTable dt = db.executeQuery();
        return dt.rows.length > 0 ? DBUtils.convert2Model(ClubUserModel.class, dt.rows[0]) : null;
    }

    public DataTable getDtByGameIdAndGameUserId(int gameId, long gameUserId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM club_user WHERE gameId = ? and gameUserId = ?");
        db.addParameter(gameId);
        db.addParameter(gameUserId);

        return db.executeQuery();
    }

    /**
     * 获取俱乐部成员信息
     */
    public DataTable getByClubIdAndUserId(long clubId, long userId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM club_user WHERE clubId = ? and gameUserId = ?;");
        db.addParameter(clubId);
        db.addParameter(userId);

        return db.executeQuery();
    }

    public DataTable listByNickname(int gameId, String nickname) {
        DBHelper db = new DBHelper(CONNECTION);
        String sql = "SELECT * FROM club_user WHERE gameId = ? and gameNickName like ?;";
        db.createCommand(sql);
        db.addParameter(gameId);
        db.addParameter("%" + nickname + "%");

        return db.executeQuery();
    }

    public List<Long> listUserId(long clubId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT gameUserId FROM club_user WHERE clubId = ?;");
        db.addParameter(clubId);

        return DBUtils.convert2List(Long.class, "gameUserId", db.executeQuery());
    }

    public List<Long> listClubId(int gameId, long gameUserId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT clubId FROM club_user WHERE gameId = ? and gameUserId = ?;");
        db.addParameter(gameId);
        db.addParameter(gameUserId);

        return DBUtils.convert2List(Long.class, "clubId", db.executeQuery());
    }

    public DataTable listByClubId(long clubId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM club_user WHERE clubId = ?;");
        db.addParameter(clubId);

        return db.executeQuery();
    }

    public DataTable getList() {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM club_user;");
        return db.executeQuery();
    }

    public int getUserNum(long promoterId) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("SELECT COUNT(0) num FROM club_user WHERE promoterId = ?;");
        db.addParameter(promoterId);

        DataTable dt = db.executeQuery();
        int num = 0;
        if (dt.rows.length > 0) {
            num = CommonUtils.getIntValue(dt.rows[0].getColumnValue("num"));
        }
        return num;
    }

    public SplitPage getPage(long clubId, int pageNum, int pageSize) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("CP_club_user_getPage", DBCommandType.Procedure);

        db.addParameter("IN_clubId", clubId);
        db.addParameter("IN_pageNum", pageNum);
        db.addParameter("IN_pageSize", pageSize);
        db.addParameter("OUT_total", ParameterDirection.Output);

        DataTable dt = db.executeQuery();
        //获取记录数
        int totalNum = Integer.parseInt(db.getParamValue("OUT_total"));
        return new SplitPage(pageNum, pageSize, totalNum, dt);
    }

    //不适用
    public int unBundleGameUser(long gameId, long gameUserId) {

        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("delete from club_user where gameId = ? and gameUserId = ?");

        db.addParameter(gameId);
        db.addParameter(gameUserId);

        return db.executeNonQuery();
    }

    //一个玩家对应多个俱乐部
    public int unBundleGameUserByClubIdAndGameUserId(long clubId, long gameUserId) {

        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("delete from club_user where clubId = ? and gameUserId = ?");

        db.addParameter(clubId);
        db.addParameter(gameUserId);

        return db.executeNonQuery();
    }


    public ClubUserModel queryByGameIdAndGameUserId(int gameId, long gameUserId) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("select * from club_user where gameId = ? and gameUserId = ?");
        db.addParameter(gameId);
        db.addParameter(gameUserId);
        DataTable dt = db.executeQuery();
        if (dt.rows.length > 0) {
            return DBUtils.convert2Model(ClubUserModel.class, dt.rows[0]);
        } else {
            return null;
        }
    }

    public ClubUserModel queryByClubIdAndGameUserId(long clubId, long gameUserId) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("select * from club_user where clubId = ? and gameUserId = ?");
        db.addParameter(clubId);
        db.addParameter(gameUserId);
        DataTable dt = db.executeQuery();
        if (dt.rows.length > 0) {
            return DBUtils.convert2Model(ClubUserModel.class, dt.rows[0]);
        } else {
            return null;
        }
    }

    public DataTable getListByClubId(long clubId) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("select * from club_user where clubId = ? and promoterId != 0 ");
        db.addParameter(clubId);
        return db.executeQuery();
    }

    public DataTable getListByClubIdIncludePromoter(long clubId) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("select * from club_user where clubId = ?;");
        db.addParameter(clubId);
        return db.executeQuery();
    }


    //获取俱乐部成员玩家信息
    public DataTable getClubUserInfo(long promoterId) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("select * from club_user where promoterId = ? ");
        db.addParameter(promoterId);

        return db.executeQuery();
    }


    public ClubUserModel getClubUserInfo(long clubId, long gameUserId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM club_user WHERE clubId = ? and gameUserId = ?");
        db.addParameter(clubId);
        db.addParameter(gameUserId);

        DataTable dt = db.executeQuery();
        return dt.rows.length > 0 ? DBUtils.convert2Model(ClubUserModel.class, dt.rows[0]) : null;
    }

    //获取玩家已加入的俱乐部数量
    public int getJoinClubNums(int gameId,long gameUserId){
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT COUNT(*) AS joinClubNums FROM club_user WHERE gameId = ? and gameUserId = ?");
        db.addParameter(gameId);
        db.addParameter(gameUserId);

        DataTable dt = db.executeQuery();
        return dt.rows.length > 0 ? CommonUtils.getIntValue(dt.rows[0].getColumnValue("joinClubNums")) : 0;
    }


    public DataTable listMembers(long clubId, List<Long> userIds) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuffer sb = new StringBuffer(128);
        sb.append("SELECT * FROM club_user WHERE clubId = ? AND gameUserId IN ")
                .append(SqlUtils.buildInConditions(userIds.size()));

        db.addParameter(clubId);
        userIds.forEach(db::addParameter);

        db.createCommand(sb.toString());

        return db.executeQuery();
    }
}
