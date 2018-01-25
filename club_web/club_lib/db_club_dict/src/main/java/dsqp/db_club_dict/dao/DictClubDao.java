package dsqp.db_club_dict.dao;

import dsqp.db_club_dict.impl.DictClubDaoImpl;
import dsqp.db_club_dict.model.DictClubModel;

/**
 * Created by jeremy on 2017/9/27.
 */
public class DictClubDao {
    private static final DictClubDaoImpl impl = new DictClubDaoImpl();

    public static int add(DictClubModel model) {
        return impl.add(model);
    }

    public static int update(DictClubModel model) {
        return impl.update(model);
    }

    public static DictClubModel getOne(long id) {
        return impl.getOne(id);
    }

    public static DictClubModel getByGameId(int gameId){
        return impl.getByGameId(gameId);
    }

}
