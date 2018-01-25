package dsqp.db_club_dict.dao;

import dsqp.db_club_dict.impl.DictApiKeyDaoImpl;
import dsqp.db_club_dict.model.DictApiKeyModel;

/**
 * Created by ds on 2017/8/1.
 */
public class DictApiKeyDao {
    private static final DictApiKeyDaoImpl impl = new DictApiKeyDaoImpl();

    public static DictApiKeyModel getByClassName(String gameName){
        return impl.getByGameName(gameName);
    }

    public static DictApiKeyModel getByGameId(int gameId){
        return impl.getByGameId(gameId);
    }
}
