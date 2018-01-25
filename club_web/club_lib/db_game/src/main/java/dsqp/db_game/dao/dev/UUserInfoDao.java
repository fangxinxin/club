package dsqp.db_game.dao.dev;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db.util.proxool.ProxoolUtil;
import dsqp.db_club_dict.model.DictGameDbModel;

/**
 * Created by Aris on 2017/7/17.
 */
class UUserInfoDaoImpl extends dsqp.db_game.dao.BaseDao {

    public DataTable getByUserId(DictGameDbModel dictDb, long userId) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX;

        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }

        if (result) {
            DBHelper db = new DBHelper(alias);

            db.createCommand("select * from u_user_info where userId = ? ");
            db.addParameter(userId);

            return db.executeQuery();
        } else {
            return new DataTable();
        }

    }

    public DataTable getByNickName(DictGameDbModel dictDb, String nickName) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX;

        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }
        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand("select * from u_user_info where nickName = ? ORDER BY userId");
            db.addParameter(nickName);
            return db.executeQuery();
        } else {
            return new DataTable();
        }
    }

    public DataTable getByWxUnionID(DictGameDbModel dictDb, String wxUnionID) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX;

        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }
        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand("select * from u_user_info where wxUnionID = ? ");
            db.addParameter(wxUnionID);
            return db.executeQuery();
        } else {
            return new DataTable();
        }
    }
}

public class UUserInfoDao {

    private static final UUserInfoDaoImpl impl = new UUserInfoDaoImpl();

    public static DataTable getByUserId(DictGameDbModel dictDb, long userId) {
        return impl.getByUserId(dictDb, userId);
    }

    public static DataTable getByNickName(DictGameDbModel dictDb, String nickName) {
        return impl.getByNickName(dictDb, nickName);
    }

    public static DataTable getByWxUnionID(DictGameDbModel dictDb, String wxUnionID) {
        return impl.getByWxUnionID(dictDb, wxUnionID);
    }

}
