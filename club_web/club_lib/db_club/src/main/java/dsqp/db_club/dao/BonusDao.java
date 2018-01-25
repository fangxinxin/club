package dsqp.db_club.dao;

import dsqp.db.model.DataTable;
import dsqp.db_club.impl.BonusDaoImpl;
import dsqp.db_club.model.BonusModel;

import java.util.Date;

/**
 * Created by Aris on 2017/7/21.
 */
public class BonusDao {

    private static final BonusDaoImpl impl = new BonusDaoImpl();

    public static int add(BonusModel model) {
        return impl.add(model);
    }

    public static int update(BonusModel model) {
        return impl.update(model);
    }

    public static BonusModel getOne(long id) {
        return impl.getOne(id);
    }

    /** 根据上周时间查询 :: 时间菜单 **/
    public static BonusModel getByDate(int gameId, Date startDate, Date endDate) {
        return impl.getByDate(gameId, startDate, endDate);
    }

    /** 根据游戏ID查询 :: 时间菜单列表 **/
    public static DataTable listByGameId(int gameId) {
        return impl.listByGameId(gameId);
    }
    public static DataTable listByGameIdAndIsPass(int gameId, boolean isPass) {
        return impl.listByGameIdAndIsPass(gameId, isPass);
    }

}
