package admin.service.impl;

import admin.service.CusOperationLogService;
import dsqp.db.model.DataTable;
import dsqp.db_club_log.dao.LogRecordDao;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/8/21.
 */
@Service
public class CusOperationLogServiceImpl implements CusOperationLogService{

    @Override
    public DataTable getListByGameId(int gameId, String menuItem) {
        return LogRecordDao.getListByGameId(gameId, menuItem);
    }

    @Override
    public DataTable getListByGameIdAndDate(int gameId, String menuItem, String startdate, String endDate) {
        return LogRecordDao.getListByGameIdAndDate(gameId,menuItem,startdate,endDate);
    }

    @Override
    public DataTable getListByRecordTypeAndDate(int gameId, int recordType, String startdate, String endDate) {
        return LogRecordDao.getListByRecordTypeAndDate(gameId,recordType,startdate,endDate);
    }
}
