package admin.service;

import dsqp.db.model.DataTable;

public interface WithdrawCheckService {

    DataTable listRequestByDate(String date, int gameId);

    //按时间段统计提现数据
    DataTable listRequestByDate(String startDate, String endDate, int gameId);
}
