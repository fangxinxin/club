package dsqp.db_club_dict.dao;

import dsqp.db.model.DataTable;
import dsqp.db_club_dict.impl.DictLevelUpDaoImpl;
import dsqp.db_club_dict.model.DictLevelUpModel;

/**
 * Created by jeremy on 2017/7/26.
 */
public class DictLevelUpDao {
    private static final DictLevelUpDaoImpl impl = new DictLevelUpDaoImpl();

    public static int add(DictLevelUpModel model) {
        return impl.add(model);
    }

    public static int update(DictLevelUpModel model) {
        return impl.update(model);
    }

    public static DictLevelUpModel getOne(int id) {
        return impl.getOne(id);
    }

    public static DataTable getList() {
        return impl.getList();
    }

    public static DictLevelUpModel getConditionByGameIdAndLevelUpType(int gameId, int levelUpType) {
        return impl.getConditionByGameIdAndLevelUpType(gameId, levelUpType);
    }

}
