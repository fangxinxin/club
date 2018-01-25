package dsqp.db_game.dao.dev;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db.util.SqlUtils;
import dsqp.db.util.proxool.ProxoolUtil;
import dsqp.db_club_dict.model.DictGameDbModel;
import dsqp.util.CommonUtils;

import java.util.List;

/**
 * Created by ds on 2017/7/17.
 */
class UUserPointDaoImpl extends dsqp.db_game.dao.BaseDao {

    public DataTable getPointByUserId(DictGameDbModel dictDb, long userId) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX;

        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }
        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand("select privateRoomDiamond,userId from u_user_point where userId = ? ");
            db.addParameter(userId);
            return db.executeQuery();
        } else {
            return new DataTable();
        }
    }

    public DataTable getPointAndPaperByUserId(DictGameDbModel dictDb, long userId) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX;
        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }
        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand("select privateRoomDiamond,paper,userId from u_user_point where userId = ? ");
            db.addParameter(userId);
            return db.executeQuery();
        } else {
            return new DataTable();
        }
    }

    public int getGameCardByUserId(DictGameDbModel dictDb, long userId) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX;

        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }
        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand("select privateRoomDiamond from u_user_point where userId = ? ");
            db.addParameter(userId);
            DataTable dt = db.executeQuery();
            int num = dt.rows.length > 0 ? CommonUtils.getIntValue(dt.rows[0].getColumnValue("privateRoomDiamond")) : 0;
            return num;
        } else {
            return 0;
        }
    }

    public DataTable getDTGameCardByNickName(DictGameDbModel dictDb, String nickName) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX;

        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }
        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand("select privateRoomDiamond,userId from u_user_point WHERE userId IN\n" +
                    "(SELECT userId FROM  u_user_info  WHERE  nickName = ?) ORDER BY userId");
            db.addParameter(nickName);
            DataTable dt = db.executeQuery();

            return dt;
        } else {
            return new DataTable();
        }
    }

    public DataTable getDiamondAndPaperByNickName(DictGameDbModel dictDb, String nickName) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX;
        boolean result = true;

        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }
        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand("select privateRoomDiamond,paper,userId from u_user_point WHERE userId IN\n" +
                    "(SELECT userId FROM  u_user_info  WHERE  nickName = ?) ORDER BY userId");
            db.addParameter(nickName);
            DataTable dt = db.executeQuery();

            return dt;
        } else {
            return new DataTable();
        }
    }


    public int getGameCardByNickName(DictGameDbModel dictDb, String nickName) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX;

        int num = 0;
        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }
        if (result) {
            DBHelper db = new DBHelper(alias);

            db.createCommand("select privateRoomDiamond from u_user_point WHERE userId IN\n" +
                    "(SELECT userId FROM  u_user_info  WHERE  nickName = ?) ORDER BY userId");
            db.addParameter(nickName);
            DataTable dt = db.executeQuery();
            num = dt.rows.length > 0 ? CommonUtils.getIntValue(dt.rows[0].getColumnValue("privateRoomDiamond")) : 0;
        }
        return num;
    }

    public DataTable getDTPaperByUserId(DictGameDbModel dictDb, long userId) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX;
        boolean result = true;

        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }
        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand("select paper,userId from u_user_point where userId = ? ");
            db.addParameter(userId);
            DataTable dt = db.executeQuery();

            return dt;
        } else {
            return new DataTable();
        }
    }

    public int getPaperByUserId(DictGameDbModel dictDb, long userId) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX;

        int num = 0;
        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (result) {
            DBHelper db = new DBHelper(alias);

            db.createCommand("select paper from u_user_point where userId = ? ");
            db.addParameter(userId);
            DataTable dt = db.executeQuery();
            num = dt.rows.length > 0 ? CommonUtils.getIntValue(dt.rows[0].getColumnValue("paper")) : 0;
        }

        return num;
    }

    public DataTable getDTPaperByNickName(DictGameDbModel dictDb, String nickName) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX;
        boolean result = true;

        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (result) {
            DBHelper db = new DBHelper(alias);

            db.createCommand("select paper,userId from u_user_point WHERE userId IN\n" +
                    "(SELECT userId FROM  u_user_info  WHERE  nickName = ?) ORDER BY userId");
            db.addParameter(nickName);
            DataTable dt = db.executeQuery();

            return dt;
        } else {
            return new DataTable();
        }
    }

    public int getPaperByNickName(DictGameDbModel dictDb, String nickName) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX;

        int num = 0;
        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (result) {
            DBHelper db = new DBHelper(alias);

            db.createCommand("select paper from u_user_point WHERE userId IN\n" +
                    "(SELECT userId FROM  u_user_info  WHERE  nickName = ?) ORDER BY userId");
            db.addParameter(nickName);
            DataTable dt = db.executeQuery();
            num = dt.rows.length > 0 ? CommonUtils.getIntValue(dt.rows[0].getColumnValue("paper")) : 0;
        }

        return num;
    }

    public DataTable listPoint(DictGameDbModel dictDb, List<Long> userIds) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX;

        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (result == true) {
            DBHelper db = new DBHelper(alias);

            StringBuilder sql = new StringBuilder(128);

            sql.append("select userId as gameUserId, privateRoomDiamond from u_user_point ")
                    .append("where userId in ").append(SqlUtils.buildInConditions(userIds.size()));

            db.createCommand(sql.toString());

            for (Long id : userIds) {
                db.addParameter(id);
            }

            return db.executeQuery();
        } else {
            return new DataTable();
        }
    }

    public int updateGameCardByUserId(DictGameDbModel dictDb, int gameCard, long userId) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX;
        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrl(), dictDb.getDbUser(), dictDb.getDbPass(), this.getProperty());
        }

        if (result == true) {
            DBHelper db = new DBHelper(alias);

            db.createCommand("update u_user_point set privateRoomDiamond = privateRoomDiamond + ? where userId = ? ");

            db.addParameter(gameCard);
            db.addParameter(userId);

            return db.executeNonQuery();
        } else {
            return 0;
        }

    }
}

public class UUserPointDao {

    private static final UUserPointDaoImpl impl = new UUserPointDaoImpl();

    public static DataTable getPointByUserId(DictGameDbModel dictDb, long userId) {
        return impl.getPointByUserId(dictDb, userId);
    }

    public static DataTable getPointAndPaperByUserId(DictGameDbModel dictDb, long userId) {
        return impl.getPointAndPaperByUserId(dictDb, userId);
    }

    public static int getGameCardByUserId(DictGameDbModel dictDb, long userId) {
        return impl.getGameCardByUserId(dictDb, userId);
    }

    public static DataTable getDTGameCardByNickName(DictGameDbModel dictDb, String nickName) {
        return impl.getDTGameCardByNickName(dictDb, nickName);
    }

    public static DataTable getDiamondAndPaperByNickName(DictGameDbModel dictDb, String nickName) {
        return impl.getDiamondAndPaperByNickName(dictDb, nickName);
    }

    public static int getGameCardByNickName(DictGameDbModel dictDb, String nickName) {
        return impl.getGameCardByNickName(dictDb, nickName);
    }

    public static DataTable getDTPaperByUserId(DictGameDbModel dictDb, long userId) {
        return impl.getDTPaperByUserId(dictDb, userId);
    }

    public static int getPaperByUserId(DictGameDbModel dictDb, long userId) {
        return impl.getPaperByUserId(dictDb, userId);
    }

    public static DataTable getDTPaperByNickName(DictGameDbModel dictDb, String nickName) {
        return impl.getDTPaperByNickName(dictDb, nickName);
    }

    public static int getPaperByNickName(DictGameDbModel dictDb, String nickName) {
        return impl.getPaperByNickName(dictDb, nickName);
    }

    public static DataTable listPoint(DictGameDbModel dictDb, List<Long> userIds) {
        return impl.listPoint(dictDb, userIds);
    }

    public static int updateGameCardByUserId(DictGameDbModel dictDb, int gameCard, long userId) {
        return impl.updateGameCardByUserId(dictDb, gameCard, userId);
    }
}
