package dsqp.db_club_dict.dao;

import dsqp.db.model.DataTable;
import dsqp.db_club_dict.impl.DictFormalDaoImpl;
import dsqp.db_club_dict.model.DictFormalModel;


/**
 * Created by jeremy on 2017/7/31.
 */
public class DictFormalDao {
    private static final DictFormalDaoImpl impl = new DictFormalDaoImpl();

    public static int add(DictFormalModel model) {
         return impl.add(model);
    }

    public static int update(DictFormalModel model) {
        return impl.update(model);
    }

    public static DictFormalModel getOne(int id) {
        return impl.getOne(id);
    }

    public static DataTable getList() {
        return impl.getList();
    }

    public static DictFormalModel  getByGameId(int gameId){
        return impl.getByGameId(gameId);
    }

}
