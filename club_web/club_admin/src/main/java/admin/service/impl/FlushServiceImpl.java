package admin.service.impl;

import admin.operation.OperationServiceLog;
import admin.service.FlushService;
import admin.util.GameApiUtil;
import db_admin.model.SysUserModel;
import dsqp.db_club.dao.ClubDao;
import dsqp.db_club.dao.PromoterDao;
import dsqp.db_club.dao.PromoterDelDao;
import dsqp.db_club.model.ClubModel;
import dsqp.db_club.model.PromoterModel;
import dsqp.db_club_log.dao.LogClubClearDao;
import dsqp.db_club_log.dao.LogFailFormalGamecardDao;
import dsqp.db_club_log.model.LogClubClearModel;
import dsqp.db_club_log.model.LogFailFormalGamecardModel;
import dsqp.util.CommonUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by jeremy on 2017/8/22.
 */
@Service
public class FlushServiceImpl implements FlushService {

//    @OperationServiceLog(menuItem = "afterSaleManage",menuName = "售后管理",recordType =18,typeName = "清空俱乐部")
//    public int removeByClubId(long clubId, ClubModel model) {
//        return ClubUserDao.removeByClubId(clubId);
//    }

    @OperationServiceLog(menuItem = "afterSaleManage",menuName = "售后管理",recordType =18,typeName = "解散俱乐部")
    public boolean dissolveClubByClubId(ClubModel club, PromoterModel promoter, SysUserModel admin) {
        Date date = new Date();
        long clubId = club.getId();
        boolean result = ClubDao.dissolveClubByClubId(clubId);

        if(result) {
            LogClubClearModel model = new LogClubClearModel();
            model.setClubId(clubId);
            model.setClubName(club.getClubName());
            model.setPeopleNum(club.getPeopleNum());
            model.setAdminId(admin.getId());
            model.setAdminName(admin.getRealName());
            model.setCreateTime(date);
            model.setCreateDate(date);

            /* log_club_clear加一条记录 */
            LogClubClearDao.add(model);


            PromoterModel p        = PromoterDao.getByClubId(clubId);
            PromoterModel parentP  = PromoterDao.getOne(p.getParentId());
            long parentCellPhone   = parentP != null ? p.getCellPhone() : 0L;
            int gameId             = p.getGameId();
            long gameCard          = p.getGameCard();
            Date startTime         = new Date();
            Date endTime           = dsqp.util.DateUtils.addDate("day", 30, startTime);

            /* promoter_del加一条记录 */
            PromoterDelDao.add(p, parentCellPhone);

            LogFailFormalGamecardModel failSendLog     = new LogFailFormalGamecardModel();
            failSendLog.setGameId(gameId);
            failSendLog.setCellphone(p.getCellPhone());
            failSendLog.setGameUserId(p.getGameUserId());
            failSendLog.setGameNickName(p.getNickName());
            failSendLog.setGameCard(gameCard);
            failSendLog.setCreateTime(startTime);
            failSendLog.setCreateDate(startTime);

            /* log_fail_formal_gamecard加一条记录 */
            int r_fail = LogFailFormalGamecardDao.add(failSendLog);
            if (r_fail > 0) {
                if (gameCard > 0) {
                    String userIds = CommonUtils.getStringValue(p.getGameUserId());
                    String title = "俱乐部解散通知";
                    String content =  "您的俱乐部已被系统解散，您所购买的钻石，现以邮件形式发回给您，请查收。如有疑问请联系客服。";

                    GameApiUtil.sendMails(gameId,userIds,title,content,gameCard+":1",startTime,endTime);
                }
            }
        }

        return result;
    }
}
