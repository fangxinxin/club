package dsqp.db_club.dao;


import dsqp.db.model.DataTable;
import dsqp.db.model.SplitPage;
import dsqp.db_club.impl.PromoterSellDaoImpl;
import dsqp.db_club.model.PromoterSellModel;

import java.util.Date;

/**
 * Created by ds on 2017/6/21.
 */
public class PromoterSellDao {

    private static final PromoterSellDaoImpl impl = new PromoterSellDaoImpl();

    public static int add(PromoterSellModel model) {
        return impl.add(model);
    }

    public static void update(PromoterSellModel model) {
        impl.update(model);
    }

    public static PromoterSellModel getOne(long id) {
        return impl.getOne(id);
    }

    public static DataTable getList() {
        return impl.getList();
    }

    public static DataTable getListByCreateTime(long promoterId, Date startTime, Date endTime) {
        return impl.getListByCreateTime(promoterId, startTime, endTime);
    }

    public static int getTotalSell(long promoterId, long gameUserId, boolean isSuccess) {
        return impl.getTotalSell(promoterId, gameUserId, isSuccess);
    }

    public static SplitPage getPageByDate(long promoterId, Date startDate, Date endDate, int pageNum, int pageSize) {
        return impl.getPageByDate(promoterId, startDate, endDate, pageNum, pageSize);
    }

    public static DataTable queryToralByPromoterAndGameId(int gameId, long promoterId) {
        return impl.queryToralByPromoterAndGameId(gameId, promoterId);
    }

    public static DataTable getAllListByCreateTime(long promoterId, Date startTime, Date endTime) {
        return impl.getAllListByCreateTime(promoterId, startTime, endTime);
    }

    public static DataTable getGameCardSellReportByDate(Date date) {
        return impl.getGameCardSellReportByDate(date);
    }

    public static DataTable getGameDiamondSellByUserId(long promoterId, long userId) {
        return impl.getGameDiamondSellByUserId(promoterId, userId);
    }

    public static DataTable getGameDiamondSellInfo(long promoterId, long userId, Date joinClubTime) {
        return impl.getGameDiamondSellInfo(promoterId, userId, joinClubTime);
    }

    public static DataTable getListByCreateDateAndGameId(int gameId, Date startTime, Date endTime) {
        return impl.getListByCreateDateAndGameId(gameId, startTime, endTime);
    }

    //查询玩家所加入的俱乐部明细
    public static DataTable getDetailByUserIdAndGameId(int gameId, long gameUserId) {
        return impl.getDetailByUserIdAndGameId(gameId, gameUserId);
    }
}
