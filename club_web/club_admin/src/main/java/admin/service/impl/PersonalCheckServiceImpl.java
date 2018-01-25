package admin.service.impl;

import admin.service.PersonalCheckService;
import dsqp.db.model.DataRow;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db_club.dao.*;
import dsqp.db_club.model.PromoterModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonalCheckServiceImpl implements PersonalCheckService {


    public DataTable getTotalByWeek(long promoterId, int gameId) {

        DataTable bonusList = BonusDao.listByGameId(gameId);
        DataTable list = null;
        DataTable list2 = null;

        for (DataRow row : bonusList.rows) {
            DataTable dt = WithdrawRequestDao.queryTotalByWeek(row.getColumnValue("startDate"), row.getColumnValue("endDate"), promoterId);
            for (DataRow row1 : dt.rows) {
                list.addRow(row1);
            }

            DataTable dt2 = WithdrawRequestDao.queryTotalByEndDate(row.getColumnValue("endDate"), promoterId);
            for (DataRow row2 : dt2.rows) {
                list2.addRow(row2);
            }
        }

        DBUtils.addColumn(list, list2, "endDate", "total");
        return list;
    }

    //根据手下代理商 查截止今日提成统计情况
    public DataTable getBonusTotalByEndDate(String endDate, long promoterId, boolean isSuper) {
        DataTable listByPromoter = null;
        DataTable bonusTotalByEndDate = null;
        if (isSuper) {
            bonusTotalByEndDate = DayBonusDetailDao.getBonusTotalByEndDate(endDate, promoterId);
            listByPromoter = PromoterDao.getAllUnder(promoterId);
        } else {
            bonusTotalByEndDate = DayBonusDetailDao.getDirectBonusTotalByEndDate(endDate, promoterId);
            listByPromoter = PromoterDao.getDirectUnder(promoterId);
        }
        if (listByPromoter.rows.length > 0) {
            DBUtils.addColumn(listByPromoter, bonusTotalByEndDate, "promoterId", "payTotal");
            DBUtils.addColumn(listByPromoter, bonusTotalByEndDate, "promoterId", "bonusTotal");
        }
        return listByPromoter;
    }

    //按周查询提成结算情况
    public DataTable getBonusTotaWeek(String startDate, String endDate, long promoterId, boolean isSuper) {
        DataTable bonusTotalWeek = null;
        DataTable bonusTotalByEndDate = null;
        if (isSuper) {
            bonusTotalWeek = DayBonusDetailDao.getBonusTotalWeek(startDate, endDate, promoterId);
            bonusTotalByEndDate = DayBonusDetailDao.getBonusTotalToday(endDate, promoterId);
            DBUtils.addColumn(bonusTotalWeek, bonusTotalByEndDate, "endDate", "bonusTotal");
        } else {
            bonusTotalWeek = DayBonusDetailDao.getDirectBonusWeek(startDate, endDate, promoterId);
            bonusTotalByEndDate = DayBonusDetailDao.getDirectBonusTotalByEndDate(endDate, promoterId);
            DBUtils.addColumn(bonusTotalWeek, bonusTotalByEndDate, "endDate", "bonusTotal");
        }
        return bonusTotalWeek;
    }


    //查询下线明细
    public DataTable getUnderDT(PromoterModel promoter) {
        DataTable under = null;
        if (promoter.getpLevel() == -1) {
            under = PromoterDao.getAllUnder(promoter.getId());
        } else if (promoter.getpLevel() == 1) {
            under = PromoterDao.getDirectUnder(promoter.getId());
        }
        if (under != null && under.rows.length > 0) {
            List promoters = DBUtils.convert2List(Long.class, "promoterId", under);
            DataTable clubList = ClubDao.listClubInfo(promoters);
            if (clubList.rows.length > 0) {
                DBUtils.addColumn(under, clubList, "promoterId", "clubId");
                DBUtils.addColumn(under, clubList, "promoterId", "clubName");
                DBUtils.addColumn(under, clubList, "promoterId", "peopleNum");
            }
        }
        return under;
    }
}
