package club.task;

import dsqp.common_const.club.ClubStatus;
import dsqp.db.model.DataRow;
import dsqp.db.model.DataTable;
import dsqp.db_club_dict.dao.DictGameDbDao;
import dsqp.db_club_dict.model.DictGameDbModel;
import dsqp.db_club_log.model.LogPromoterReportModel;
import dsqp.util.CommonUtils;
import dsqp.util.DateUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by ds on 2018/1/5.
 */
@Component
@EnableScheduling
public class TaskDay {

    /**
     * 日报
     */
    public void countReport(){
        Date statDate = DateUtils.String2Date(DateUtils.Date2String(DateUtils.addDay(-1, new Date())));
        DataTable gameList = DictGameDbDao.getListByIsEnable(true);
        for (DataRow row : gameList.rows){
            int gameId = CommonUtils.getIntValue(row.getColumnValue("gameId"));
            DictGameDbModel db = DictGameDbDao.getByGameId(gameId, true);
            if (db == null) {
                continue;
            }

            //先查出来该gameid下的所有转正俱乐部
            DataTable clubList = dsqp.db_club.dao.ClubDao.getListByGameIdDateAndClubStatus(gameId, DateUtils.addDay(1, statDate), ClubStatus.Normal.getClubStatus());
            for (DataRow clubRow : clubList.rows){
                //后期update
                LogPromoterReportModel report = new LogPromoterReportModel();
                report.setGameId(gameId);
                report.setClubId(CommonUtils.getLongValue(clubRow.getColumnValue("id")));
                report.setRoomCreateNum(0);
                report.setGameCardConsume(0);
                report.setGameCardSell(0);
                report.setStatDate(statDate);
                report.setCreateDate(new Date());
                dsqp.db_club_log.dao.LogPromoterReportDao.add(report);
            }

            long clubId           = 0;
            int  roomCreateNum    = 0;
            int  gameCardConsume  = 0;
            int  gameCardSell     = 0;
            //先更新对局数和钻石消耗
            DataTable createNumAndGameCardConsume = dsqp.db_game.dao.log_dev.UPyjUserRecordDao.getClubCreateNumAndGameCardConsume(db, statDate);
            for (DataRow cg : createNumAndGameCardConsume.rows) {
                roomCreateNum   = CommonUtils.getIntValue(cg.getColumnValue("roomCreateNum"));
                gameCardConsume = CommonUtils.getIntValue(cg.getColumnValue("gameCardConsume"));
                clubId          = CommonUtils.getLongValue(cg.getColumnValue("clubId"));
                dsqp.db_club_log.dao.LogPromoterReportDao.updateRoomCreateNumAndgameCardConsume(roomCreateNum, gameCardConsume, clubId, statDate);
            }
            //更新钻石售卖
            DataTable gameCardSellData = dsqp.db_club.dao.PromoterSellDao.getGameCardSellReportByDate(statDate);
            for (DataRow gc : gameCardSellData.rows) {
                clubId       = CommonUtils.getLongValue(gc.getColumnValue("clubId"));
                gameCardSell = CommonUtils.getIntValue(gc.getColumnValue("gameCardSell"));
                dsqp.db_club_log.dao.LogPromoterReportDao.updateGameCardSell(gameCardSell, clubId, statDate);
            }

        }
    }

}
