package admin.service;

import dsqp.db.model.DataTable;

/**
 * Created by Administrator on 2017/8/21.
 */
public interface CusOperationLogService {
    DataTable getListByGameId(int gameId, String menuItem);

    DataTable getListByGameIdAndDate(int gameId, String menuItem, String startdate, String endDate);

    DataTable getListByRecordTypeAndDate(int gameId, int recordType, String startdate, String endDate);
}
