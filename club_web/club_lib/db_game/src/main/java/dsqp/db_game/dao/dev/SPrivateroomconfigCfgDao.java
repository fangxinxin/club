package dsqp.db_game.dao.dev;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db.util.proxool.ProxoolUtil;
import dsqp.db_club_dict.model.DictGameDbModel;

/**
 * Created by Aris on 2017/8/2.
 */
class SPrivateroomconfigCfgDaoImpl extends dsqp.db_game.dao.BaseDao {

    public DataTable getByGameId(DictGameDbModel dictDb, int gameId) {
        String alias = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX_READ;
        String dbName = dictDb.getDbNamePrefix() + DBNAME_DEV_SUFFIX;

        boolean result = true;
        if (!ProxoolUtil.isAliasExist(alias)) {
            result = ProxoolUtil.registerProxool(alias, dbName, dictDb.getDbUrlRead(), dictDb.getDbUserRead(), dictDb.getDbPassRead(), this.getProperty());
        }
        if (result) {
            DBHelper db = new DBHelper(alias);
            db.createCommand("select * from s_privateroomconfig_cfg where gameId = ? ");
            db.addParameter(gameId);
            return db.executeQuery();
        } else {
            return new DataTable();
        }

    }
}

public class SPrivateroomconfigCfgDao {
    private static final SPrivateroomconfigCfgDaoImpl impl = new SPrivateroomconfigCfgDaoImpl();

    public static DataTable getByGameId(DictGameDbModel dictDb, int gameId) {
        return impl.getByGameId(dictDb, gameId);
    }
}
