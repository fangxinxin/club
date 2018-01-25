package admin.service.impl;

import admin.service.WithdrawCheckService;
import dsqp.db.model.DataRow;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db_club.dao.DayDepositDao;
import dsqp.db_club.dao.PromoterDao;
import dsqp.db_club.dao.WithdrawRequestDao;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by mj on 2017/9/6.
 */

@Service
public class WithdrawCheckServiceImpl implements WithdrawCheckService {


    //按天统计提现数据
    public DataTable listRequestByDate(String date, int gameId) {

        DataTable dt = WithdrawRequestDao.listRequestByDate(date, gameId);
        if (dt.rows.length > 0) {
            List promoterIds = DBUtils.convert2List(Long.class, "promoterId", dt);
            DataTable list = PromoterDao.getListByPromoter(promoterIds);
            DBUtils.addColumn(dt, list, "promoterId", "nickName");
            DBUtils.addColumn(dt, list, "promoterId", "realName");
            DBUtils.addColumn(dt, list, "promoterId", "cellPhone");
            DBUtils.addColumn(dt, list, "promoterId", "IDCard");
            DBUtils.addColumn(dt, list, "promoterId", "bankArea");
        }
        return dt;
    }

    //按时间段统计提现数据
    public DataTable listRequestByDate(String startDate, String endDate, int gameId) {

        DataTable dt = WithdrawRequestDao.listRequest(startDate, endDate, gameId);
        if (dt.rows.length > 0) {
            List<String> createTime = DBUtils.convert2List(String.class, "createTime", dt);
            DataTable remainList = new DataTable();
            for (String statDate : createTime) {
                DataRow[] rows = DayDepositDao.queryTotalByDay(statDate, gameId).rows;
                if (rows.length > 0) {
                    remainList.addRow(rows[0]);
                }
            }
            DBUtils.addColumn(dt, remainList, "createTime", "remainTotal");
            DataTable receiptNum = WithdrawRequestDao.queryReceiptNum(startDate, endDate, gameId);
            DBUtils.addColumn(dt, receiptNum, "createTime", "receiptNum");
            DataTable exceptionNum = WithdrawRequestDao.queryExceptionNum(startDate, endDate, gameId);
            DBUtils.addColumn(dt, exceptionNum, "createTime", "exceptionNum");
        }
        return dt;
    }
}
