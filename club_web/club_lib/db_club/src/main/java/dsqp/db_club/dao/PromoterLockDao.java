package dsqp.db_club.dao;

import dsqp.db.model.DataTable;
import dsqp.db_club.impl.PromoterLockDaoImpl;
import dsqp.db_club.model.PromoterLockModel;

import java.util.List;

/**
 * Created by ds on 2017/7/30.
 */
public class PromoterLockDao {
    private static final PromoterLockDaoImpl impl = new PromoterLockDaoImpl();

    public static int add(PromoterLockModel model) {
        return impl.add(model);
    }

    public static PromoterLockModel getOne(long id) {
        return impl.getOne(id);
    }

    public static int deleteByPromoterId(long promoterId) {
        return impl.deleteByPromoterId(promoterId);
    }

    public static PromoterLockModel getByPromoterId(long promoterId) {
        return impl.getByPromoterId(promoterId);
    }

    public static DataTable listByGameId(int gameId) {
        return impl.listByGameId(gameId);
    }

    public static DataTable getList() {
        return impl.getList();
    }


    /** 代理解封    task
     *(获取可解封账户，删除解封记录)
     */
    public static DataTable getExpireList() {
        return impl.getExpireList();
    }
    public static int deleteByPromoterIds(List<Long> promoterIds) {
        return impl.deleteByPromoterIds(promoterIds);
    }

}
