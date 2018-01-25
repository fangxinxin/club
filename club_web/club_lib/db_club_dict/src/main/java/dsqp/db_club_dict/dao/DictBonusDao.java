package dsqp.db_club_dict.dao;

import dsqp.db_club_dict.impl.DictBonusDaoImpl;
import dsqp.db_club_dict.model.DictBonusModel;

/**
 * Created by Aris on 2017/8/11.
 */
public class DictBonusDao {
    private static final DictBonusDaoImpl impl = new DictBonusDaoImpl();

    public static int add(DictBonusModel model) {
        return impl.add(model);
    }

    public  static int update(DictBonusModel model){
        return impl.update(model);
    }


    public static DictBonusModel getByGameId(int gameId) {
        return impl.getByGameId(gameId, true);
    }

}
