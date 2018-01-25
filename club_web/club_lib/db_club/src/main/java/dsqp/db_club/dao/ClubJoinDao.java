package dsqp.db_club.dao;


import dsqp.db.model.DataTable;
import dsqp.db_club.impl.ClubJoinDaoImpl;
import dsqp.db_club.model.ClubJoinModel;

/**
 * Created by jeremy on 2017/9/28.
 */
public class ClubJoinDao {
    private static final ClubJoinDaoImpl impl = new ClubJoinDaoImpl();

    public static int add(ClubJoinModel model) {
        return impl.add(model);
    }

    public static int update(ClubJoinModel model) {
        return impl.update(model);
    }

    public static ClubJoinModel getOne(long id) {
        return impl.getOne(id);
    }

    public static DataTable getRequestListByGameIdPromoterIdAndPassStatus(int gameId, long promoterId, int passStatus) {
        return impl.getRequestListByGameIdPromoterIdAndPassStatus(gameId, promoterId, passStatus);
    }

    /**
     * 判断是否  第一次被此俱乐部拒绝
     */
    public static int getRefuseNum(long clubId, long gameUserId, int passStatus) {
        return impl.getRefuseNum(clubId, gameUserId, passStatus);
    }

    /**
     * 更新入会申请状态
     */
    public static int updateStatusById(ClubJoinModel model) {
        return impl.updateStatusById(model);
    }

}
