package dsqp.db_club.dao;

import dsqp.db.model.DataTable;
import dsqp.db_club.impl.ClubQuitDaoImpl;
import dsqp.db_club.model.ClubQuitModel;

/**
 * Created by jeremy on 2017/9/28.
 */
public class ClubQuitDao {
    private static final ClubQuitDaoImpl impl = new ClubQuitDaoImpl();

    public static int add(ClubQuitModel model) {
        return impl.add(model);
    }

    public static int update(ClubQuitModel model) {
        return impl.update(model);
    }

    public static ClubQuitModel getOne(long id) {
        return impl.getOne(id);
    }

    public static DataTable getRequestListByGameIdPromoterIdAndQuitStatus(int gameId, long promoterId, int quitStatus) {
        return impl.getRequestListByGameIdPromoterIdAndQuitStatus(gameId, promoterId, quitStatus);
    }
}
