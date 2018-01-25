package dsqp.db_club.dao;

import dsqp.db.model.DataTable;
import dsqp.db_club.impl.PromoterOrderDaoImpl;
import dsqp.db_club.model.PromoterOrderModel;

import java.util.Date;

/**
 * Created by fx on 2017/9/23.
 */
public class PromoterOrderDao {

    private static final PromoterOrderDaoImpl impl = new PromoterOrderDaoImpl();

    /**
     * 新增工单
     */
    public static int add(PromoterOrderModel model) {
        return impl.add(model);
    }


    /**
     * 按日期统计查询工单
     */
    public static DataTable queryListByDate(int gameId, Date startDate, Date endDate) {
        return impl.queryListByDate(gameId, startDate, endDate);
    }

    /**
     * 按日期统计查询工单
     */
    public static DataTable getListByDate(int gameId, Date date) {
        return impl.getListByDate(gameId, date);
    }

    /**
     * 查询所有员工的工单
     */
    public static DataTable queryAll(int gameId) {
        return impl.queryAll(gameId);
    }

    /**
     * 查询指定员工的工单
     */
    public static DataTable queryByEditAdmin(int gameId, String editAdmin) {
        return impl.queryByEditAdmin(gameId, editAdmin);
    }

    /**
     * 工单查询 所有员工
     */
    public static DataTable queryAllEditAdmin(int gameId) {
        return impl.queryAllEditAdmin(gameId);
    }

    /**
     * 工单查询 查指定员工
     */
    public static DataTable queryAllByEditAdmin(int gameId, String editAdmin) {
        return impl.queryAllByEditAdmin(gameId, editAdmin);
    }

    /**
     * 工单明细 查指定员工
     */
    public static DataTable getOneDetail(int gameId, String editAdmin) {
        return impl.getOneDetail(gameId, editAdmin);
    }


    /**
     * 修改工单 :: 转正状态和时间
     */
    public static int updatePromoterStatus(long clubId, int promoterStatus){
        return impl.updatePromoterStatus(clubId, promoterStatus);
    }

}
