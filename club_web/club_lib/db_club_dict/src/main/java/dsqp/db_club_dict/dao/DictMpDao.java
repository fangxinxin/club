package dsqp.db_club_dict.dao;

import dsqp.db_club_dict.impl.DictMpDaoImpl;
import dsqp.db_club_dict.model.DictMpModel;

/**
 * Created by Aris on 2017/8/1.
 */
public class DictMpDao {
    private static final DictMpDaoImpl impl = new DictMpDaoImpl();

    public static DictMpModel getByClassName(String className){
        return impl.getByClassName(className);
    }

    public static DictMpModel getByGameId(int gameId){
        return impl.getByGameId(gameId);
    }
}
