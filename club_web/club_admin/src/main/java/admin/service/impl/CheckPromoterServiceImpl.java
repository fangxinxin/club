package admin.service.impl;

import admin.operation.OperationServiceLog;
import admin.service.CheckPromoterService;
import admin.util.GameApiUtil;
import com.google.common.collect.Maps;
import dsqp.common_const.club.*;
import dsqp.common_const.club_admin.MemberType;
import dsqp.db.model.DataRow;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db.util.DateUtils;
import dsqp.db_club.dao.*;
import dsqp.db_club.model.ClubModel;
import dsqp.db_club.model.PromoterLevelUpInfoModel;
import dsqp.db_club.model.PromoterModel;
import dsqp.db_club_dict.dao.DictFormalDao;
import dsqp.db_club_dict.dao.DictGameDbDao;
import dsqp.db_club_dict.model.DictFormalModel;
import dsqp.db_club_dict.model.DictGameDbModel;
import dsqp.db_club_log.dao.LogClubDao;
import dsqp.db_club_log.dao.LogClubJoinDao;
import dsqp.db_club_log.dao.LogFailFormalGamecardDao;
import dsqp.db_club_log.dao.LogGamecardDao;
import dsqp.db_club_log.model.LogClubModel;
import dsqp.db_club_log.model.LogFailFormalGamecardModel;
import dsqp.db_club_log.model.LogGamecardModel;
import dsqp.db_game.dao.log_dev.UPyjUserRecordDao;
import dsqp.util.CommonUtils;
import dsqp.util.EncodingUtils;
import dsqp.util.HttpUtils;
import dsqp.util.SignUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by jeremy on 2017/8/21.
 */
@Service
public class CheckPromoterServiceImpl implements CheckPromoterService {

    @OperationServiceLog(menuItem = "preSaleManage", menuName = "售前管理", recordType = 17, typeName = "延长转正")
    public int updateExpireTime(long promoterId, Date expireTime, int addTime) {
        return ClubDao.updateExpireTime(promoterId, expireTime);
    }

    @Override
    public int getTotalPlayGameNum(DataTable dt, DictGameDbModel dbModel) {
        int result = 0;
        if (dt.rows.length > 0) {
            for (DataRow row : dt.rows) {
                long userId = CommonUtils.getLongValue(row.getColumnValue("gameUserId"));
                Date joinClubTime = DateUtils.String2DateTime(row.getColumnValue("createTime"));
                result += UPyjUserRecordDao.getPyjCreateNums(dbModel, userId, joinClubTime);
            }
        }
        return result;
    }


    /**
     * 拼接： 成员参与局数、新成员人数、新成员局数、剩余时间（h:m）
     *
     * @param gameId
     * @return
     */
    @Override
    public DataTable listCheckPromoter(int gameId,Date startTime,Date endTime) {

        DictGameDbModel dictDb = DictGameDbDao.getByGameId(gameId, true);
        DataTable dt = ClubDao.listByGameIdAndExpireTime(gameId, startTime, endTime, ClubStatus.Init.getClubStatus());
        if (dt.rows.length > 0) {
            for (DataRow row : dt.rows) {
                long clubId = CommonUtils.getLongValue(row.getColumnValue("id"));
                long promoterId = CommonUtils.getLongValue(row.getColumnValue("promoterId"));
                Date expireTime = dsqp.util.DateUtils.String2DateTime(row.getColumnValue("expireTime"));

                //获取新成员人数
                List<Long> listUserId = ClubUserDao.listUserId(clubId);
                int newMemberNum = listUserId.size() > 0 ? LogClubJoinDao.getNewMemberNums(gameId, listUserId) : 0;//获取新成员人数

                //查询成员参与总局数
                DataTable clubUserInfoDt = ClubUserDao.getClubUserInfo(promoterId);
                int totalPlayGameNum = getTotalPlayGameNum(clubUserInfoDt, dictDb);

                //新成员参与局数
                DataTable newClueUser = LogClubJoinDao.getNewMember(gameId, listUserId);
                int totalNewPlayGameNum = getTotalPlayGameNum(newClueUser, dictDb);

                //剩余时间
                String restTime = dsqp.util.DateUtils.timeDiff(expireTime);

                row.addColumn("newMemberNum", newMemberNum);
                row.addColumn("totalPlayGameNum", totalPlayGameNum);
                row.addColumn("totalNewPlayGameNum", totalNewPlayGameNum);
                row.addColumn("restTime", restTime);

            }
        }

        return dt;
    }

    @Override
    public DataTable listNewMember(long clubId, int memberType) {
        DataTable clubUserDt = ClubUserDao.getListByClubId(clubId);

        if (clubUserDt.rows.length > 0) {
            List<Long> userIds = DBUtils.convert2List(Long.class, "gameUserId", clubUserDt);
            List<Long> newMembers = LogClubJoinDao.listNewMemberUserId(clubId,userIds);

            List<Long> oldUserIds = userIds;
            if (newMembers != null) {
                for (Long userId: oldUserIds) {
                    for (Long newMember: newMembers){
                        if (userId == newMember) {
                            oldUserIds.remove(userId);
                        }
                    }
                }
            }

            DataTable newMemberDt = LogClubJoinDao.listMemberDetail(clubId, userIds);    //新玩家

            switch (memberType) {
                case MemberType.ALL:
                    DBUtils.addColumn(clubUserDt, newMemberDt, "gameUserId", "isNewMember");
                    return clubUserDt;    //全部
                case MemberType.NEW_MEMBER:
                    return newMemberDt;
                case MemberType.OLD_MEMBER:
                    DataTable oldUsers = ClubUserDao.listMembers(clubId, oldUserIds);
                    for (DataRow row: oldUsers.rows) {
                        row.addColumn("isNewMember", 0);
                    }
                    return oldUsers;
            }



        }

        return clubUserDt;
    }

    /**
     * 立即转正
     * by ds
     * 20180122
     */
    @Override
    @OperationServiceLog(menuItem = "preSaleManage", menuName = "售前管理", recordType = 37, typeName = "立即转正")
    public boolean becomeFormal(long promoterId) {
        boolean result = false;

        PromoterModel p = PromoterDao.getOne(promoterId);
        ClubModel c     = ClubDao.getByPromoterId(promoterId);
        if (p == null || c == null) {
            return result;
        }

        Date now = new Date();
        int gameId             = p.getGameId();
        long gameUserId        = p.getGameUserId();
        long clubId            = p.getClubId();
        int peopleNum          = ClubUserDao.getUserNum(promoterId);
        long gameCard          = p.getGameCard();
        String gameNickName    = p.getNickName();
        Date expireTime        = c.getExpireTime();

        DictFormalModel f      = DictFormalDao.getByGameId(gameId);
        int award              = f != null ? f.getAward() : CommonConfig.AWARD;                             //转正奖励

        DictGameDbModel dictDb = DictGameDbDao.getByGameId(gameId, true);
        if (dictDb == null) {//游戏库地址为空
            return result;
        }

        int r1 = ClubDao.updateClubStatus(promoterId, ClubStatus.Normal.getClubStatus());
        if (r1 > 0){
            result = true;

            //成功转正后，赠送100钻石给代理
            boolean r2 = award > 0 && ClubDao.updateClubCard(promoterId,award) > 0;
            if (r2) {                                                                     //记录钻石日志
                LogGamecardModel log = new LogGamecardModel();
                log.setGameId(gameId);
                log.setPromoterId(promoterId);
                log.setNickName(gameNickName);
                log.setSource(GamecardSource.FORMAL_AWARD.getType());
                log.setChangeNum(award);
                log.setChangeBefore(gameCard);
                log.setChangeAfter(gameCard + award);
                log.setCreateTime(now);
                log.setCreateDate(now);
                LogGamecardDao.add(log);
            }


            int pLevel = p.getpLevel();
            if(pLevel== PromoterLevel.SUPER){//只记录特级转正时间节点
                PromoterLevelUpInfoModel model = new PromoterLevelUpInfoModel();
                model.setGameId(gameId);
                model.setPromoterId(promoterId);
                model.setpLevel(0);
                model.setCreateTime(now);
                model.setLevelUpType(LevelUp.OPEN2SUPER);
                PromoterLevelUpInfoDao.add(model);
            }


            //TODO 推送通知 :: 代理商转正
            String url = "http://" + dictDb.getWebDomain() + ":" + dictDb.getWebPort() + "/api";  //推送通知 :: url
            try {
                HttpUtils.get(url + "/club/?act=check&userId=" + gameUserId + "&clubId=" + clubId);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int clubType = LogClubType.SUCCESS.getClubType();

            LogClubModel log = new LogClubModel();
            log.setClubId(p.getClubId());
            log.setGameId(gameId);
            log.setGameUserId(gameUserId);
            log.setGameNickName(gameNickName);
            log.setPeopleNum(peopleNum);
            log.setClubType(clubType);
            log.setExpireTime(expireTime);
            log.setCreateDate(now);
            log.setCreateTime(now);
            LogClubDao.add(log);
            //工单
            PromoterOrderDao.updatePromoterStatus(clubId, clubType);

        }

        return result;
    }


    /**
     * 取消审核
     * 1.删除club、club_user、club_share 中代理记录
     * 2.删除 promoter 代理信息
     * 3.记录log：club，promoOrder
     * 4.推送通知 :: 清理出俱乐部
     * by ds
     */
    @OperationServiceLog(menuItem = "preSaleManage", menuName = "售前管理", recordType = 19, typeName = "取消转正")
    public boolean refuseCheckPromoter(int gameId, long clubId) {

        ClubModel c = ClubDao.getOne(clubId);
        DictGameDbModel dictDb = DictGameDbDao.getByGameId(gameId, true);
        //获取新成员人数
        List<Long> listUserId = ClubUserDao.listUserId(clubId);
        int peopleNumNew = listUserId.size() > 0 ? LogClubJoinDao.getNewMemberNums(gameId, listUserId) : 0;//获取新成员人数

        //查询成员参与总局数
        DataTable clubUserDt = ClubUserDao.listByClubId(clubId);
        int pyjNum = getTotalPlayGameNum(clubUserDt, dictDb);

        //新成员参与局数
        DataTable newClueUser = LogClubJoinDao.getNewMember(gameId, listUserId);
        int pyjNumNew = getTotalPlayGameNum(newClueUser, dictDb);

        //删除未转正俱乐部及俱乐部相关信息
        boolean result = ClubDao.refuseClub(clubId);
        if (result) {

            //释放俱乐部玩家
            LogClubJoinDao.removeByClubIdAndUserIds(clubId, listUserId);

            int clubType = LogClubType.REFUSE.getClubType();
            Date now = new Date();

            //俱乐部日志
            LogClubModel log = new LogClubModel();
            log.setClubId(c.getId());
            log.setGameId(gameId);
            log.setGameUserId(c.getGameUserId());
            log.setGameNickName(c.getGameNickName());
            log.setPeopleNum(c.getPeopleNum());
            log.setPeopleNumNew(peopleNumNew);
            log.setPyjNum(pyjNum);
            log.setPyjNumNew(pyjNumNew);
            log.setClubType(clubType);
            log.setExpireTime(c.getExpireTime());
            log.setCreateDate(now);
            log.setCreateTime(now);
            LogClubDao.add(log);
            //工单
            PromoterOrderDao.updatePromoterStatus(c.getId(), clubType);

            //Start: 推送通知
            String url = "http://" + dictDb.getWebDomain() + ":" + dictDb.getWebPort() + "/api";

            try {
                HttpUtils.get(url + "/club/?act=destroy&clubId=" + clubId + "&userId=" + c.getGameUserId() + "&gameId=" + gameId);
            } catch (IOException e) {
                e.printStackTrace();
            }

            PromoterModel p        = PromoterDao.getByClubId(clubId);
            PromoterModel parentP  = PromoterDao.getOne(p.getParentId());
            long parentCellPhone   = parentP != null ? p.getCellPhone() : 0L;
            long gameCard          = p.getGameCard();
            Date start             = new Date();
            Date end               = dsqp.util.DateUtils.addDate("day", 30, start);

            /* promoter_del加一条记录 */
            PromoterDelDao.add(p, parentCellPhone);

            LogFailFormalGamecardModel failSendLog     = new LogFailFormalGamecardModel();
            failSendLog.setGameId(gameId);
            failSendLog.setCellphone(p.getCellPhone());
            failSendLog.setGameUserId(p.getGameUserId());
            failSendLog.setGameNickName(p.getNickName());
            failSendLog.setGameCard(gameCard);
            failSendLog.setCreateTime(start);
            failSendLog.setCreateDate(start);

            /* log_fail_formal_gamecard加一条记录 */
            int r_fail = LogFailFormalGamecardDao.add(failSendLog);
            if (r_fail > 0) {
                if (gameCard > 0) {
                    String userIds = CommonUtils.getStringValue(p.getGameUserId());
                    String title = "取消审核通知";
                    String content =  "您的俱乐部已被系统解散，您所购买的钻石，现以邮件形式发回给您，请查收。如有疑问请联系客服。";

                    GameApiUtil.sendMails(gameId, userIds, title, content, gameCard+":1", start, end);
                }
            }


            if (clubUserDt.rows.length > 0) {
                List<Long> gameUserIds = DBUtils.convert2List(Long.class, "gameUserId", clubUserDt);
                StringBuffer sb = new StringBuffer();
                for (Long value : gameUserIds) {
                    sb.append(value).append(",");
                }
                String userIds = sb.substring(0, sb.length() - 1);

                Map<String, String> maps = Maps.newHashMap();
                maps.put("userIds", userIds);
                maps.put("title", "通知");
                maps.put("content", "您所在的俱乐部已解散，请重新加入俱乐部。");
                maps.put("reward", "");
                maps.put("startTime", EncodingUtils.urlEncode(dsqp.util.DateUtils.DateTime2String(new Date())));

                //TODO 推送通知 :: 清理出俱乐部
                try {
                    HttpUtils.get(url + "/mail/?" + SignUtil.getParamsStrFromMap(maps) + "&act=newusermails");
                    HttpUtils.get(url + "/user/?act=RefreshUIStatus&userIds=" + maps.get("userIds"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //End: 推送通知
        }

        return result;
    }


}
