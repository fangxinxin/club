package dsqp.db_club.dao;

import dsqp.db.model.DataTable;
import dsqp.db_club.impl.BonusDetailDaoImpl;
import dsqp.db_club.model.BonusDetailModel;

import java.util.Date;

/**
 * Created by Aris on 2017/11/28.
 */
public class BonusDetailDao {
    private static final BonusDetailDaoImpl impl = new BonusDetailDaoImpl();

    public static int add(BonusDetailModel model) {
        return impl.add(model);
    }

    public static int getTotalRebateByPromoterId(long promoterId){
        return impl.getTotalRebateByPromoterId(promoterId);
    }

    public static DataTable getRebateDetail(long promoterId, Date startDate,Date endDate){
        return impl.getRebateDetail(promoterId,startDate,endDate);
    }
}
