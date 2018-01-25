package dsqp.db_club.dao;

import dsqp.db.model.DataTable;
import dsqp.db.model.SplitPage;
import dsqp.db_club.impl.DayBonusParentDaoImpl;

import java.util.Date;

/**
 * Created by Aris on 2017/7/21.
 */
public class DayBonusParentDao {

    private static final DayBonusParentDaoImpl impl = new DayBonusParentDaoImpl();

    //获取单笔明细
    public static DataTable getListByBonusId(long bonusId) {
        return impl.getListByBonusId(bonusId);
    }

    //摘要
    public static DataTable getDigest(long bonusId, Date payCreateDate) {
        return impl.getDigest(bonusId, payCreateDate);
    }

    //获取下级返利情况
    public static SplitPage getBonusByPage(long promoterId, Date startDate, Date endDate, int pageNum, int pageSize){
        return impl.getBonusByPage(promoterId, startDate, endDate, pageNum, pageSize);
    }
}