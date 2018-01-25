package admin.service;


import dsqp.db.model.DataTable;
import dsqp.db_club.model.PromoterModel;

public interface PersonalCheckService {

    DataTable getTotalByWeek(long promoterId, int gameId);

    DataTable getBonusTotalByEndDate(String endDate, long promoterId, boolean isSuper);

    //按周查询提成结算情况
    DataTable getBonusTotaWeek(String startDate, String endDate, long promoterId, boolean isSuper);

    //查询下线明细
    DataTable getUnderDT(PromoterModel promoter);
}
