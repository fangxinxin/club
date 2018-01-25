package dsqp.db_club_dict.dao;


import dsqp.db.model.DataTable;
import dsqp.db_club_dict.impl.DictGameDbDaoImpl;
import dsqp.db_club_dict.model.DictGameDbModel;

/**
 * Created by Aris on 2017/2/15.
 */
public class DictGameDbDao {

    private static final DictGameDbDaoImpl impl = new DictGameDbDaoImpl();

    public static DictGameDbModel getOne(long id) {
        return impl.getOne(id);
    }

    public static DataTable getListByIsEnable(boolean isEnable){
        return impl.getListByIsEnable(isEnable);
    }

    public static DictGameDbModel getByGameId(int gameId, boolean isEnable){
        return impl.getByGameId(gameId, isEnable);
    }


}
