package dsqp.db_club.dao;

import dsqp.db.model.DataTable;
import dsqp.db_club.impl.PromoterLevelUpInfoDaoImpl;
import dsqp.db_club.model.PromoterLevelUpInfoModel;

/**
 * Created by jeremy on 2017/7/27.
 */
public class PromoterLevelUpInfoDao {

    private static final PromoterLevelUpInfoDaoImpl impl = new PromoterLevelUpInfoDaoImpl();

    public static int add(PromoterLevelUpInfoModel model) {
        return impl.add(model);
    }

    public static int update(PromoterLevelUpInfoModel model) {
        return impl.update(model);
    }

    public static PromoterLevelUpInfoModel getOne(long id) {
        return impl.getOne(id);
    }

    public static DataTable getList() {
        return impl.getList();
    }

    public static DataTable getLatestLevelUpTimeByPromoterIdAndType(long promoterId,int levelUpType){
        return impl.getLatestLevelUpTimeByPromoterIdAndType(promoterId,levelUpType);
    }


}
