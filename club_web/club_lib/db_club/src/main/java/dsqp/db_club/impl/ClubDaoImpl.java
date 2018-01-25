package dsqp.db_club.impl;

import com.google.common.base.Strings;
import dsqp.db.db.DBHelper;
import dsqp.db.model.DBCommandType;
import dsqp.db.model.DataTable;
import dsqp.db.model.ParameterDirection;
import dsqp.db.util.DBUtils;
import dsqp.db.util.SqlUtils;
import dsqp.db_club.model.ClubModel;
import dsqp.util.CommonUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by ds on 2017/7/17.
 */
public class ClubDaoImpl implements dsqp.db.service.BaseDao<ClubModel> {

    private static final String CONNECTION = "club";

    public int updateShareCard(long clubId, long promoterId, long gameCard, int shareCard) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("TRAN_updateShareCard", DBCommandType.Procedure);
        db.addParameter("IN_clubId", clubId);
        db.addParameter("IN_promoterId", promoterId);
        db.addParameter("IN_gameCard", gameCard);
        db.addParameter("IN_shareCard", shareCard);
        return db.executeNonQuery();
    }

    public int add(ClubModel model) {
        return 0;
    }

    public int newClub(int gameId, long promoterId, long gameUserId, String clubName, boolean isEnable, Date createDate) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "INSERT INTO club (" +
                "  gameId," +
                "  promoterId," +
                "  gameUserId," +
                "  clubName," +
                "  isEnable," +
                "  createTime" +
                ") VALUES(?,?,?,?,?,?)";

        db.createCommand(sql);
        db.addParameter(gameId);
        db.addParameter(promoterId);
        db.addParameter(gameUserId);
        db.addParameter(clubName);
        db.addParameter(isEnable);
        db.addParameter(createDate);
        return db.executeNonQuery();
    }

    //    public DataTable getByGameIdAndUserId(int gameId, long userId) {
//        DBHelper db = new DBHelper(CONNECTION);
//
//        db.createCommand("SELECT * FROM club WHERE gameId = ? and userId = ?;");
//        db.addParameter(gameId);
//        db.addParameter(userId);
//
//        return db.executeQuery();
//    }
    public DataTable listPeopleNum(List<Long> promoterIds) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sql = new StringBuilder(128);

        sql.append("SELECT promoterId, peopleNum FROM club ")
                .append("WHERE promoterId in ").append(SqlUtils.buildInConditions(promoterIds.size()))
                .append(" group by promoterId;");

        db.createCommand(sql.toString());

        for (Long id : promoterIds) {
            db.addParameter(id);
        }

        return db.executeQuery();
    }

    public int update(ClubModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sb = new StringBuilder(128);

        if (model.getPromoterId() != 0) {
            sb.append(",promoterId=?");
            db.addParameter(model.getPromoterId());
        }

        if (model.getGameUserId() != 0) {
            sb.append(",gameUserId=?");
            db.addParameter(model.getGameUserId());
        }

        if (Strings.isNullOrEmpty(model.getClubName())) {
            sb.append(",clubName=?");
            db.addParameter(model.getClubName());
        }

        if (Strings.isNullOrEmpty(model.getClubURL())) {
            sb.append(",clubURL=?");
            db.addParameter(model.getClubURL());
        }

        if (model.getPeopleNum() != 0) {
            sb.append(",peopleNum=?");
            db.addParameter(model.getPeopleNum());
        }

        if (model.getClubStatus() != 0) {
            sb.append(",clubStatus=?");
            db.addParameter(model.getClubStatus());
        }

        if (model.getExpireTime() != null) {
            sb.append(",expireTime=?");
            db.addParameter(model.getExpireTime());
        }

        if (sb.length() == 0) {
            return 0;
        } else {
            db.createCommand("UPDATE club set " + sb.substring(1) + " where id=?");
            db.addParameter(model.getId());

            return db.executeNonQuery();
        }
    }

    public ClubModel getOne(long id) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM club WHERE id = ?;");
        db.addParameter(id);
        DataTable dt = db.executeQuery();
        if (dt.rows.length > 0) {
            return DBUtils.convert2Model(ClubModel.class, dt.rows[0]);
        } else {
            return null;
        }
    }

    public DataTable getOne2(long id) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("SELECT * FROM club WHERE id = ?;");
        db.addParameter(id);

        return db.executeQuery();
    }

    public DataTable getList() {
        return null;
    }

    public DataTable getListByGameIdDateAndClubStatus(int gameId, Date date, int clubStatus) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("SELECT * FROM club WHERE gameId=? AND createTime < ? AND clubStatus=?");
        db.addParameter(gameId);
        db.addParameter(date);
        db.addParameter(clubStatus);

        return db.executeQuery();
    }

    public DataTable queryByPromoterId(long promoterId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM club WHERE promoterId =?;");
        db.addParameter(promoterId);
        return db.executeQuery();
    }

    public ClubModel getByPromoterId(long promoterId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM club WHERE promoterId =?;");
        db.addParameter(promoterId);

        DataTable dt = db.executeQuery();
        return dt.rows.length > 0 ? DBUtils.convert2Model(ClubModel.class, dt.rows[0]) : null;
    }

    public ClubModel queryByClubNameAndGameId(int gameId, String clubName) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM club WHERE clubName = ? and gameId= ? ;");
        db.addParameter(clubName);
        db.addParameter(gameId);
        DataTable dt = db.executeQuery();
        if (dt.rows.length > 0) {
            return DBUtils.convert2Model(ClubModel.class, dt.rows[0]);
        } else {
            return null;
        }
    }

    public ClubModel queryByIdAndGamId(long id, int gameId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM club WHERE id = ? and gameId= ? ;");
        db.addParameter(id);
        db.addParameter(gameId);
        DataTable dt = db.executeQuery();
        if (dt.rows.length > 0) {
            return DBUtils.convert2Model(ClubModel.class, dt.rows[0]);
        } else {
            return null;
        }
    }

    public ClubModel queryByGameUserIdAndGamId(long gameUserId, int gameId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM club WHERE gameUserId = ? and gameId= ? ;");
        db.addParameter(gameUserId);
        db.addParameter(gameId);
        DataTable dt = db.executeQuery();
        if (dt.rows.length > 0) {
            return DBUtils.convert2Model(ClubModel.class, dt.rows[0]);
        } else {
            return null;
        }
    }

    public ClubModel queryByIdAndStatus(long id, int gameId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM club WHERE id = ? and gameId= ?;");
        db.addParameter(id);
        db.addParameter(gameId);
        DataTable dt = db.executeQuery();
        if (dt.rows.length > 0) {
            return DBUtils.convert2Model(ClubModel.class, dt.rows[0]);
        } else {
            return null;
        }
    }

    public int updatePeopleNum(long id, int peopleNum) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("update club set peopleNum= ? where id= ? ;");
        db.addParameter(peopleNum);
        db.addParameter(id);
        return db.executeNonQuery();
    }

    public DataTable getClubPeopleNumByClubId(long clubId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT peopleNum FROM club WHERE id =?;");
        db.addParameter(clubId);
        return db.executeQuery();
    }


    public int updateClubStatus(long promoterId, int clubStatus) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("update club set clubStatus = ? where promoterId = ? ;");
        db.addParameter(clubStatus);
        db.addParameter(promoterId);
        return db.executeNonQuery();
    }

    public int updateClubStatus2More(List<Long> promoterIds, int clubStatus) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuffer sb = new StringBuffer(128);
        sb.append("update club set clubStatus = ? where promoterId in ")
                .append(SqlUtils.buildInConditions(promoterIds.size()));

        db.addParameter(clubStatus);
        for (long id : promoterIds) {
            db.addParameter(id);
        }
        db.createCommand(sb.toString());
        return db.executeNonQuery();
    }

    public int updateClubURL(String clubURL, long id) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("update club set clubURL = ? where id = ? ");
        db.addParameter(clubURL);
        db.addParameter(id);
        return db.executeNonQuery();
    }

    public DataTable listByGameId(int gameId) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sql = new StringBuilder(128);
        sql.append("select * from club");

        if (gameId != 0) {
            sql.append(" WHERE gameId = ?");
            db.addParameter(gameId);
        }
        sql.append(" ORDER BY createTime DESC");

        db.createCommand(sql.toString());
        return db.executeQuery();
    }

    public DataTable listByGameId(int gameId, int clubStatus) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sql = new StringBuilder(128);
        sql.append("select * from club where clubStatus = ? ");
        db.addParameter(clubStatus);

        if (gameId != 0) {
            sql.append("and gameId = ? ");
            db.addParameter(gameId);
        }
        sql.append("ORDER BY createTime DESC");

        db.createCommand(sql.toString());
        return db.executeQuery();
    }

    public DataTable listByGameIdAndExpireTime(int gameId, Date startTime, Date endTime, int clubStatus) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sql = new StringBuilder(128);
        sql.append("select * from club where expireTime between ? and ? and clubStatus = ? ");
        db.addParameter(startTime);
        db.addParameter(endTime);
        db.addParameter(clubStatus);

        if (gameId != 0) {
            sql.append("and gameId = ? ");
            db.addParameter(gameId);
        }
        sql.append("ORDER BY createTime DESC");

        db.createCommand(sql.toString());
        return db.executeQuery();
    }


    /**
     * 代理审核
     * (延长代理时间，获取预开代理，获取到期俱乐部)
     */
    public int updateExpireTime(long promoterId, Date expireTime) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("update club set expireTime = ? where promoterId = ? ;");
        db.addParameter(expireTime);
        db.addParameter(promoterId);
        return db.executeNonQuery();
    }

    public ClubModel getPreClub(long promoterId, int clubStatus) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("SELECT * FROM club WHERE promoterId = ? AND clubStatus = ?;");
        db.addParameter(promoterId);
        db.addParameter(clubStatus);
        DataTable dt = db.executeQuery();

        return dt.rows.length > 0 ? DBUtils.convert2Model(ClubModel.class, dt.rows[0]) : null;
    }

    public DataTable getPreFormal(int gameId, int clubStatus) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sql = new StringBuilder(128);
        sql.append("select * from club where clubStatus = ? ");
        db.addParameter(clubStatus);

        if (gameId != 0) {
            sql.append("and gameId = ?");
            db.addParameter(gameId);
        }

        db.createCommand(sql.toString());
        return db.executeQuery();
    }

    public DataTable getExpireList(int gameId, int clubStatus) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sql = new StringBuilder(128);
        sql.append("select * from club where clubStatus = ? and expireTime <= ? ");
        db.addParameter(clubStatus);
        db.addParameter(new Date());

        if (gameId != 0) {
            sql.append("and gameId = ? ");
            db.addParameter(gameId);
        }
        sql.append("ORDER BY createTime DESC");

        db.createCommand(sql.toString());
        return db.executeQuery();
    }

    public int updateClubsPeopleNum(List<Long> promoterIds, int peopleNum) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuffer sb = new StringBuffer(128);
        sb.append("update club set peopleNum = ? where promoterId in ")
                .append(SqlUtils.buildInConditions(promoterIds.size()));

        db.addParameter(peopleNum);
        for (long id : promoterIds) {
            db.addParameter(id);
        }
        db.createCommand(sb.toString());
        return db.executeNonQuery();
    }

    public int removeByClubId(List<Long> clubIds) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sb = new StringBuilder(128);
        sb.append("DELETE FROM club WHERE id IN ").append(SqlUtils.buildInConditions(clubIds.size()));

        for (Long clubId: clubIds) {
            db.addParameter(clubId);
        }

        db.createCommand(sb.toString());

        return db.executeNonQuery();
    }

    public boolean dissolveClubByClubId(long clubId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("TRAN_dissolveClub", DBCommandType.Procedure);
        db.addParameter("IN_clubId", clubId);
        db.addParameter("OUT_result", ParameterDirection.Output);

        db.executeNonQuery();
        return CommonUtils.getIntValue(db.getParamValue("OUT_result")) == 1 ? true : false;
    }

    public boolean refuseClub(long clubId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("TRAN_refuseClub", DBCommandType.Procedure);
        db.addParameter("IN_clubId", clubId);
        db.addParameter("OUT_result", ParameterDirection.Output);

        db.executeNonQuery();
        return CommonUtils.getIntValue(db.getParamValue("OUT_result")) == 1 ? true : false;
    }

    public DataTable listClubInfo(List<Long> promoterIds) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sql = new StringBuilder(128);

        sql.append("SELECT *,id AS clubId FROM club ")
                .append("WHERE promoterId in ").append(SqlUtils.buildInConditions(promoterIds.size()));

        db.createCommand(sql.toString());

        for (Long id : promoterIds) {
            db.addParameter(id);
        }

        return db.executeQuery();
    }

    public DataTable listClubInfoById(List<Long> clubIds) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sql = new StringBuilder(128);

        sql.append("SELECT *,id AS clubId FROM club ")
                .append("WHERE id in ").append(SqlUtils.buildInConditions(clubIds.size()));

        db.createCommand(sql.toString());

        for (Long id : clubIds) {
            db.addParameter(id);
        }

        return db.executeQuery();
    }

    public int decreaseClubNum(long clubId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("update club set peopleNum = peopleNum - 1 where id= ? ;");
        db.addParameter(clubId);
        return db.executeNonQuery();
    }

    public int increaseClubNum(long clubId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("update club set peopleNum = peopleNum + 1 where id= ? ;");
        db.addParameter(clubId);
        return db.executeNonQuery();
    }

    //修改俱乐部昵称
    public int updateClubName(String newClubName, long clubId) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("update club set clubName = ? where id = ? ");
        db.addParameter(newClubName);
        db.addParameter(clubId);
        return db.executeNonQuery();
    }


    public int updateClubCard(long promoterId, long gameCard) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("update club set clubCard = clubCard + ? where promoterId= ?;");
        db.addParameter(gameCard);
        db.addParameter(promoterId);
        return db.executeNonQuery();
    }
}
