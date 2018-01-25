package dsqp.db_game.dao.log_dev;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db.util.SqlUtils;
import dsqp.db.util.proxool.ProxoolUtil;
import dsqp.db_club_dict.model.DictGameDbModel;
import dsqp.util.CommonUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Aris on 2017/9/8.
 */
class UPyjUserRecordDaoImpl extends dsqp.db_game.dao.BaseDao {

    //查询玩家所有对局明细
    public DataTable getUserDetail(DictGameDbModel dictDb, long userId, Date startDate, Date endDate) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;

        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }
        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand(" SELECT * FROM u_pyj_record_log WHERE gameStartTime>=SUBDATE(?,1) AND gameStartTime<? " +
                    "AND roomID IN " +
                    "( SELECT DISTINCT roomID FROM u_pyj_user_record WHERE userID= ? AND gameStartTime>= ? AND gameStartTime< ? );");

            db.addParameter(startDate);
            db.addParameter(endDate);
            db.addParameter(userId);
            db.addParameter(startDate);
            db.addParameter(endDate);

            return db.executeQuery();
        } else {
            return new DataTable();
        }

    }

    //查询玩家是大赢家的对局明细
    public DataTable getUserWinDetail(DictGameDbModel dictDb, long userId, Date startDate, Date endDate) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;

        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }
        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand(" SELECT * FROM u_pyj_record_log WHERE gameStartTime>= ? AND gameStartTime< ? AND winnerId= ? AND roundCount> 0 ;");

            db.addParameter(startDate);
            db.addParameter(endDate);
            db.addParameter(userId);

            return db.executeQuery();
        } else {
            return new DataTable();
        }

    }

    //查询玩家是房主的对局明细
    public DataTable getUserOwnerDetail(DictGameDbModel dictDb, long userId, Date startDate, Date endDate) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;

        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }
        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand(" SELECT * FROM u_pyj_record_log WHERE gameStartTime>= ? AND gameStartTime< ? AND ownerId= ? AND roundCount> 0 ;");

            db.addParameter(startDate);
            db.addParameter(endDate);
            db.addParameter(userId);

            return db.executeQuery();
        } else {
            return new DataTable();
        }

    }

    //查询参与局数
    public DataTable getPlayDetail(DictGameDbModel dictDb, long clubId, Date startDate, Date endDate) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;

        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }
        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand(" SELECT 'no', userId , nickName " +
                    " , (SELECT COUNT(DISTINCT roomID) FROM u_pyj_user_record WHERE userId=t.userId AND clubId= ? AND gameStartTime BETWEEN ? AND ?) times " +
                    " , FORMAT(SUM(gameCard/playerCount),2) price  " +
                    " FROM " +
                    " (SELECT * FROM u_pyj_user_record WHERE clubId= ? AND gameStartTime BETWEEN ? AND ? GROUP BY userId , roomId) t " +
                    " GROUP BY userId; ");

            db.addParameter(clubId);
            db.addParameter(startDate);
            db.addParameter(endDate);
            db.addParameter(clubId);
            db.addParameter(startDate);
            db.addParameter(endDate);

            return db.executeQuery();
        } else {
            return new DataTable();
        }

    }

    public long getFirstIdByDate(DictGameDbModel dictDb, Date date) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand("SELECT min(id) as id FROM u_pyj_user_record WHERE gameStartTime >= ? ORDER BY gameStartTime ASC LIMIT 1");
            db.addParameter(date);

            DataTable dt = db.executeQuery();

            return dt.rows.length > 0 ? CommonUtils.getIntValue(dt.rows[0].getColumnValue("id")) : 0;
        } else {
            return 0;
        }

    }

    public long getLastIdByDate(DictGameDbModel dictDb, Date date) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand("SELECT max(id) as id FROM u_pyj_user_record WHERE gameStartTime < ? ORDER BY gameStartTime DESC LIMIT 1");
            db.addParameter(date);

            DataTable dt = db.executeQuery();

            return dt.rows.length > 0 ? CommonUtils.getIntValue(dt.rows[0].getColumnValue("id")) : 0;
        } else {
            return 0;
        }
    }

    public DataTable getPyjCreatedNumById(DictGameDbModel dictDb, long firstId, long lastId) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand("SELECT createDate,userId,COUNT(0) AS pyjNum FROM (SELECT userId,DATE(gameStartTime) AS createDate FROM u_pyj_user_record WHERE id BETWEEN ? AND ?  GROUP BY userId,roomId)t GROUP BY userId");

            db.addParameter(firstId);
            db.addParameter(lastId);

            return db.executeQuery();
        } else {
            return new DataTable();
        }
    }

    //获取俱乐部成员在加入俱乐部之后的对局数
    public int getPyjCreateNums(DictGameDbModel dictDb, long userId, Date joinClubTime) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;

        int num = 0;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand("SELECT COUNT(0) AS pyjnum FROM (SELECT userId FROM u_pyj_user_record WHERE userId= ?  AND gameStartTime > ? GROUP BY userId,roomId)t");

            db.addParameter(userId);
            db.addParameter(joinClubTime);
            DataTable dt = db.executeQuery();

            return dt.rows.length > 0 ? CommonUtils.getIntValue(dt.rows[0].getColumnValue("pyjnum")) : 0;
        } else {
            return num;
        }
    }

    //获取俱乐部成员的战绩明细
    public DataTable getGameRecordDetail(DictGameDbModel dictDb, long clubId, Date startDate, Date endDate) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;

        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }
        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand(" SELECT id,roomId,isRead,ownerId,winnerId,winnerNum,userId1,userName1,userCashDiff1,userId2,userName2,userCashDiff2,userId3,userName3,userCashDiff3,userId4,userName4,userCashDiff4,gameStopTime " +
                    " FROM u_pyj_record_log " +
                    " WHERE gameStopTime between ? AND ? AND clubId = ? AND  roundCount >0 ORDER BY gameStopTime DESC;");

            db.addParameter(startDate);
            db.addParameter(endDate);
            db.addParameter(clubId);

            return db.executeQuery();
        } else {
            return new DataTable();
        }

    }

    //更新俱乐部已阅状态
    public int updateReadStatus(DictGameDbModel dictDb, long id) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;

        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrl(), dictDb.getDbUser(), dictDb.getDbPass(), this.getProperty());
        }
        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand(" UPDATE `u_pyj_record_log` SET isRead = 1 WHERE id = ?;");

            db.addParameter(id);

            return db.executeNonQuery();
        } else {
            return 0;
        }
    }


    //winnerId
    public boolean isExistColumn(DictGameDbModel dictDb, String columnName) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand("SELECT EXISTS (SELECT * FROM information_schema.columns  WHERE table_schema=? AND table_name='u_pyj_record_log' AND column_name=? LIMIT 1) AS isExist");
            db.addParameter(dbName);
            db.addParameter(columnName);

            DataTable dt = db.executeQuery();

            return dt.rows.length > 0 ? CommonUtils.getBooleanValue(dt.rows[0].getColumnValue("isExist")) : false;
        } else {
            return false;
        }
    }

    public DataTable getWinnerList(DictGameDbModel dictDb, long clubId, Date startDate, Date endDate) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;

        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (result) {
            DBHelper db = new DBHelper(alias);
            String sql = "SELECT 'no', winnerName,winnerId,COUNT(0) AS winnerTime,SUM(winnerNum) AS winnerNums,SUM(gameCard) AS gameCard " +
                    " FROM u_pyj_record_log  WHERE clubId = ? AND  roundCount >0 AND gameStopTime >= ? AND gameStopTime < ? GROUP BY winnerId;";

            db.createCommand(sql);
            db.addParameter(clubId);
            db.addParameter(startDate);
            db.addParameter(endDate);
            return db.executeQuery();
        } else {
            return new DataTable();
        }
    }

    public DataTable getOwnerList(DictGameDbModel dictDb, long clubId, Date startDate, Date endDate) {

        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;

        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (result) {
            DBHelper db = new DBHelper(alias);
            String sql = "SELECT 'no', ownerName,ownerId,COUNT(0) AS 'ownerNum',SUM(gameCard) AS gameCard FROM u_pyj_record_log WHERE clubId = ? AND  roundCount >0 AND gameStopTime >= ? AND gameStopTime < ?  and roundCount >0 GROUP BY ownerId;";
            db.createCommand(sql);
            db.addParameter(clubId);
            db.addParameter(startDate);
            db.addParameter(endDate);
            return db.executeQuery();
        } else {
            return new DataTable();
        }
    }


    public DataTable getHostDetail(DictGameDbModel dictDb, long clubId, long userId, Date startDate, Date endDate) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (result) {
            DBHelper db = new DBHelper(alias);
            String sql = "SELECT * FROM u_pyj_record_log WHERE clubId= ? AND  roundCount >0 AND ownerId= ? AND gameStopTime BETWEEN ? AND ? ORDER BY gameStopTime DESC";
            db.createCommand(sql);
            db.addParameter(clubId);
            db.addParameter(userId);
            db.addParameter(startDate);
            db.addParameter(endDate);
            return db.executeQuery();
        } else {
            return new DataTable();
        }
    }

    public DataTable getWinnerDetail(DictGameDbModel dictDb, long clubId, long userId, Date startDate, Date endDate) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (result) {
            DBHelper db = new DBHelper(alias);
            String sql = "SELECT * FROM u_pyj_record_log WHERE clubId= ? AND  roundCount >0 AND winnerId= ? AND gameStopTime BETWEEN ? AND ? ORDER BY gameStopTime DESC";
            db.createCommand(sql);
            db.addParameter(clubId);
            db.addParameter(userId);
            db.addParameter(startDate);
            db.addParameter(endDate);
            return db.executeQuery();
        } else {
            return new DataTable();
        }
    }


    public DataTable getPlayDetail(DictGameDbModel dictDb, long clubId, long userId, Date startDate, Date endDate) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (result) {
            DBHelper db = new DBHelper(alias);
            String sql = " SELECT * FROM u_pyj_record_log WHERE " +
                    " gameStartTime BETWEEN SUBDATE(?,1) AND ? AND clubId= ? AND (userId1= ? OR userId2= ? OR userId3= ? OR userId4= ?) " +
                    " AND roomId IN " +
                    " (SELECT DISTINCT roomId FROM u_pyj_user_record WHERE clubId = ? AND userID=? " +
                    " AND gameStartTime BETWEEN ? AND ?); ";
            db.createCommand(sql);
            db.addParameter(startDate);
            db.addParameter(endDate);
            db.addParameter(clubId);
            db.addParameter(userId);
            db.addParameter(userId);
            db.addParameter(userId);
            db.addParameter(userId);
            db.addParameter(clubId);
            db.addParameter(userId);
            db.addParameter(startDate);
            db.addParameter(endDate);
            return db.executeQuery();
        } else {
            return new DataTable();
        }
    }


    //更新所有 参与局数 记录为已阅状态
    public int updateAllReadStatus(DictGameDbModel dictDb, long clubId, long userId, Date startDate, Date endDate) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;

        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrl(), dictDb.getDbUser(), dictDb.getDbPass(), this.getProperty());
        }
        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand("UPDATE u_pyj_record_log SET isRead= 1 WHERE clubId= ? AND  roundCount >0 AND gameStopTime BETWEEN ? AND ? " +
                    "AND (userId1= ? OR userId2= ? OR userId3= ? OR userId4= ? );");

            db.addParameter(clubId);
            db.addParameter(startDate);
            db.addParameter(endDate);
            db.addParameter(userId);
            db.addParameter(userId);
            db.addParameter(userId);
            db.addParameter(userId);
            return db.executeNonQuery();
        } else {
            return 0;
        }
    }

    //更新所有 大赢家对局明细 为已阅状态
    public int updateAllWinnerReadStatus(DictGameDbModel dictDb, long clubId, long userId, Date startDate, Date endDate) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;

        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrl(), dictDb.getDbUser(), dictDb.getDbPass(), this.getProperty());
        }
        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand("update u_pyj_record_log set isRead = 1 WHERE clubId= ? AND  roundCount >0 AND winnerId= ? AND gameStopTime BETWEEN ? AND ? ;");

            db.addParameter(clubId);
            db.addParameter(userId);
            db.addParameter(startDate);
            db.addParameter(endDate);
            return db.executeNonQuery();
        } else {
            return 0;
        }
    }

    //更新所有 房主报表对局明细 为已阅状态
    public int updateAllHostReadStatus(DictGameDbModel dictDb, long clubId, long userId, Date startDate, Date endDate) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;

        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrl(), dictDb.getDbUser(), dictDb.getDbPass(), this.getProperty());
        }
        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand("update u_pyj_record_log set isRead = 1 WHERE clubId= ? AND  roundCount >0 AND ownerId= ? AND gameStopTime BETWEEN ? AND ? ;");

            db.addParameter(clubId);
            db.addParameter(userId);
            db.addParameter(startDate);
            db.addParameter(endDate);
            return db.executeNonQuery();
        } else {
            return 0;
        }
    }

    public DataTable getPlayDetailNoRead(DictGameDbModel dictDb, long clubId, long userId, Date startDate, Date endDate) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (result) {
            DBHelper db = new DBHelper(alias);
            String sql = "SELECT COUNT(0) nums FROM u_pyj_record_log WHERE clubId= ? AND  roundCount >0 AND gameStopTime BETWEEN ?  AND ? AND isRead=0 " +
                    " AND (userId1= ? OR userId2= ? OR userId3= ? OR userId4= ?); ";
            db.createCommand(sql);
            db.addParameter(clubId);
            db.addParameter(startDate);
            db.addParameter(endDate);
            db.addParameter(userId);
            db.addParameter(userId);
            db.addParameter(userId);
            db.addParameter(userId);
            return db.executeQuery();
        } else {
            return new DataTable();
        }
    }

    public DataTable getWinnerDetailNoRead(DictGameDbModel dictDb, long clubId, long userId, Date startDate, Date endDate) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (result) {
            DBHelper db = new DBHelper(alias);
            String sql = "SELECT COUNT(0) nums FROM u_pyj_record_log WHERE clubId= ? AND  roundCount >0 AND winnerId= ? AND gameStopTime BETWEEN  ?  AND ? AND isRead=0 ";
            db.createCommand(sql);
            db.addParameter(clubId);
            db.addParameter(userId);
            db.addParameter(startDate);
            db.addParameter(endDate);
            return db.executeQuery();
        } else {
            return new DataTable();
        }
    }

    public DataTable getHostDetailNoRead(DictGameDbModel dictDb, long clubId, long userId, Date startDate, Date endDate) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (result) {
            DBHelper db = new DBHelper(alias);
            String sql = "SELECT COUNT(0) nums FROM u_pyj_record_log WHERE clubId= ? AND  roundCount >0 AND ownerId= ? AND gameStopTime BETWEEN  ?  AND ? AND isRead=0 ";
            db.createCommand(sql);
            db.addParameter(clubId);
            db.addParameter(userId);
            db.addParameter(startDate);
            db.addParameter(endDate);
            return db.executeQuery();
        } else {
            return new DataTable();
        }
    }

    public DataTable getClubCreateNumAndGameCardConsume(DictGameDbModel dictDb, Date statDate) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;

        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (result == true) {
            DBHelper db = new DBHelper(alias);
            db.createCommand("SELECT clubId,COUNT(clubId) AS roomCreateNum,SUM(gameCard) AS gameCardConsume" +
                    " FROM u_pyj_record_log" +
                    " WHERE gameStopTime >= ? AND gameStopTime < ADDDATE(?, 1) AND clubId != 0 AND roundCount != 0" +
                    " GROUP BY clubId");
            db.addParameter(statDate);
            db.addParameter(statDate);

            return db.executeQuery();
        } else {
            return new DataTable();
        }
    }

    public DataTable getUPyjRecordOfClubUser(DictGameDbModel dictDb, long clubId, List<Long> userIds, Date startTime, Date endTime) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;

        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }
        if (result) {
            DBHelper db = new DBHelper(alias);
            StringBuilder sb = new StringBuilder(128);
            sb.append("SELECT COUNT(DISTINCT roomID) AS pyjRoomNum, COUNT(DISTINCT userID) AS newPeopleNum ")
                    .append("FROM ( ")
                    .append("SELECT roomID, userID ")
                    .append("FROM u_pyj_user_record WHERE userId in ").append(SqlUtils.buildInConditions(userIds.size()));

            userIds.forEach(db::addParameter);

            if (clubId > 0) {
                sb.append(" AND clubId = ? ");
                db.addParameter(clubId);
            } else {
                sb.append(" AND clubId = 0 ");
            }

            sb.append("AND gameStartTime >= ? ")
                    .append("AND gameStartTime <= ? ")
                    .append("GROUP BY roomID, userID) t");

            db.addParameter(startTime);
            db.addParameter(endTime);

            db.createCommand(sb.toString());

            return db.executeQuery();
        } else {
            return new DataTable();
        }
    }

    public int getClubUserPyjRoomNum(DictGameDbModel dictDb, long clubId, long userId, Date startTime, Date endTime) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;

        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }
        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand("SELECT COUNT(0) AS pyjRoomNum FROM (SELECT userID FROM u_pyj_user_record WHERE clubId = ? AND userID = ? AND gameStartTime >= ? AND gameStartTime < ADDDATE(?,1) GROUP BY userId,roomId)t;");

            db.addParameter(clubId);
            db.addParameter(userId);
            db.addParameter(startTime);
            db.addParameter(endTime);

            DataTable dt = db.executeQuery();

            return dt.rows.length > 0 ? CommonUtils.getIntValue(dt.rows[0].getColumnValue("pyjRoomNum")) : 0;
        } else {
            return 0;
        }
    }

    public DataTable getClubUserPyjRoomNum(DictGameDbModel dictDb, long clubId, Date startTime, Date endTime) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;

        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }
        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand("SELECT userID AS gameUserId, COUNT(0) AS pyjRoomNum FROM (SELECT userID FROM u_pyj_user_record WHERE clubId = ? AND gameStartTime >= ? AND gameStartTime < ADDDATE(?,1) GROUP BY userId,roomId )t GROUP BY userId");

            db.addParameter(clubId);
            db.addParameter(startTime);
            db.addParameter(endTime);

            return db.executeQuery();
        } else {
            return new DataTable();
        }
    }

    public int getPeopleNumOfPyj(DictGameDbModel dictDb, List<Long> userIds, Date startTime, Date endTime) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_LOG_SUFFIX;
        boolean result = true;

        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }
        if (result) {
            DBHelper db = new DBHelper(alias);
            StringBuilder sb = new StringBuilder(128);
            sb.append("SELECT COUNT(userID) AS newPeopleNum ")
                    .append("FROM ( ")
                    .append("SELECT userID ")
                    .append("FROM u_pyj_user_record WHERE userId in ").append(SqlUtils.buildInConditions(userIds.size()));

            userIds.forEach(db::addParameter);

            sb.append(" AND gameStartTime >= ? ")
                    .append("AND gameStartTime <= ? ")
                    .append("GROUP BY userID) t");

            db.addParameter(startTime);
            db.addParameter(endTime);

            db.createCommand(sb.toString());
            DataTable dt = db.executeQuery();

            return dt.rows.length > 0 ? CommonUtils.getIntValue(dt.rows[0].getColumnValue("newPeopleNum")) : 0;
        } else {
            return 0;
        }
    }

}

public class UPyjUserRecordDao {
    private static final UPyjUserRecordDaoImpl impl = new UPyjUserRecordDaoImpl();

    //查询玩家所有对局明细
    public static DataTable getUserDetail(DictGameDbModel dictDb, long userId, Date startDate, Date endDate) {
        return impl.getUserDetail(dictDb, userId, startDate, endDate);
    }

    //查询玩家是大赢家的对局明细
    public static DataTable getUserWinDetail(DictGameDbModel dictDb, long userId, Date startDate, Date endDate) {
        return impl.getUserWinDetail(dictDb, userId, startDate, endDate);
    }


    //查询玩家是房主的对局明细
    public static DataTable getUserOwnerDetail(DictGameDbModel dictDb, long userId, Date startDate, Date endDate) {
        return impl.getUserOwnerDetail(dictDb, userId, startDate, endDate);
    }

    public static long getFirstIdByDate(DictGameDbModel dictDb, Date date) {
        return impl.getFirstIdByDate(dictDb, date);
    }

    public static long getLastIdByDate(DictGameDbModel dictDb, Date date) {
        return impl.getLastIdByDate(dictDb, date);
    }

    public static DataTable getPyjCreatedNumById(DictGameDbModel dictDb, long firstId, long lastId) {
        return impl.getPyjCreatedNumById(dictDb, firstId, lastId);
    }

    public static int getPyjCreateNums(DictGameDbModel dictDb, long userId, Date joinClubTime) {
        return impl.getPyjCreateNums(dictDb, userId, joinClubTime);
    }

    public static DataTable getGameRecordDetail(DictGameDbModel dictDb, long clubId, Date startDate, Date endDate) {
        return impl.getGameRecordDetail(dictDb, clubId, startDate, endDate);
    }

    public static int updateReadStatus(DictGameDbModel dictDb, long id) {
        return impl.updateReadStatus(dictDb, id);
    }

    public static boolean isExistColumn(DictGameDbModel dictDb, String columnName) {
        return impl.isExistColumn(dictDb, columnName);
    }

    /**
     * 查询大赢家报表
     */
    public static DataTable getWinnerList(DictGameDbModel dictDb, long clubId, Date startDate, Date endDate) {
        return impl.getWinnerList(dictDb, clubId, startDate, endDate);
    }

    /**
     * 查询房主报表
     */
    public static DataTable getOwnerList(DictGameDbModel dictDb, long clubId, Date startDate, Date endDate) {
        return impl.getOwnerList(dictDb, clubId, startDate, endDate);
    }

    /**
     * 查询房主报表对局明细
     */
    public static DataTable getHostDetail(DictGameDbModel dictDb, long clubId, long userId, Date startDate, Date endDate) {
        return impl.getHostDetail(dictDb, clubId, userId, startDate, endDate);
    }

    /**
     * 查询大赢家对局明细
     */
    public static DataTable getWinnerDetail(DictGameDbModel dictDb, long clubId, long userId, Date startDate, Date endDate) {
        return impl.getWinnerDetail(dictDb, clubId, userId, startDate, endDate);
    }

    /**
     * 查询参与对局数
     */
    public static DataTable getPlayDetail(DictGameDbModel dictDb, long clubId, Date startDate, Date endDate) {
        return impl.getPlayDetail(dictDb, clubId, startDate, endDate);
    }

    /**
     * 查询参与对局数 :对局明细
     */
    public static DataTable getPlayDetail(DictGameDbModel dictDb, long clubId, long userId, Date startDate, Date endDate) {
        return impl.getPlayDetail(dictDb, clubId, userId, startDate, endDate);
    }

    /**
     * 更新所有 参与局数 记录为已阅状态
     */
    public static int updateAllReadStatus(DictGameDbModel dictDb, long clubId, long userId, Date startDate, Date endDate) {
        return impl.updateAllReadStatus(dictDb, clubId, userId, startDate, endDate);
    }


    /**
     * 查看 参与局数 记录为未阅状态的总数量
     */
    public static DataTable getPlayDetailNoRead(DictGameDbModel dictDb, long clubId, long userId, Date startDate, Date endDate) {
        return impl.getPlayDetailNoRead(dictDb, clubId, userId, startDate, endDate);
    }

    /**
     * 更新所有 大赢家对局明细 为已阅状态
     */
    public static int updateAllWinnerReadStatus(DictGameDbModel dictDb, long clubId, long userId, Date startDate, Date endDate) {
        return impl.updateAllWinnerReadStatus(dictDb, clubId, userId, startDate, endDate);
    }

    /**
     * 更新所有 房主报表对局明细 为已阅状态
     */
    public static int updateAllHostReadStatus(DictGameDbModel dictDb, long clubId, long userId, Date startDate, Date endDate) {
        return impl.updateAllHostReadStatus(dictDb, clubId, userId, startDate, endDate);
    }

    /**
     * 查看 大赢家表 记录为未阅状态的总数量
     */
    public static DataTable getWinnerDetailNoRead(DictGameDbModel dictDb, long clubId, long userId, Date startDate, Date endDate) {
        return impl.getWinnerDetailNoRead(dictDb, clubId, userId, startDate, endDate);
    }

    /**
     * 查看 房主报表 记录为未阅状态的总数量
     */
    public static DataTable getHostDetailNoRead(DictGameDbModel dictDb, long clubId, long userId, Date startDate, Date endDate) {
        return impl.getHostDetailNoRead(dictDb, clubId, userId, startDate, endDate);
    }

    /**
     * 拉取代开俱乐部信息
     */
    public static DataTable getClubCreateNumAndGameCardConsume(DictGameDbModel dictDb, Date statDate) {
        return impl.getClubCreateNumAndGameCardConsume(dictDb, statDate);
    }

    /**
     * 俱乐部对战局数
     * by ds
     */
    public static DataTable getUPyjRecordOfClubUser(DictGameDbModel dictDb, long clubId, List<Long> userIds, Date startTime, Date endTime) {
        return impl.getUPyjRecordOfClubUser(dictDb, clubId, userIds, startTime, endTime);
    }

    /**
     * 俱乐部成员对战局数
     */
    public static DataTable getClubUserPyjRoomNum(DictGameDbModel dictDb, long clubId, Date startTime, Date endTime) {
        return impl.getClubUserPyjRoomNum(dictDb, clubId, startTime, endTime);
    }

    public static int getClubUserPyjRoomNum(DictGameDbModel dictDb, long clubId, long userId, Date startTime, Date endTime) {
        return impl.getClubUserPyjRoomNum(dictDb, clubId, userId, startTime, endTime);
    }

    /**
     * 参与对局人数
     * by ds
     */
    public static int getPeopleNumOfPyj(DictGameDbModel dictDb, List<Long> userIds, Date startTime, Date endTime) {
        return impl.getPeopleNumOfPyj(dictDb, userIds, startTime, endTime);
    }

}

