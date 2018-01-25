package club.task;

import club.util.GameApiUtil;
import com.google.common.collect.Maps;
import dsqp.common_const.club.*;
import dsqp.db.model.DataRow;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db_club.dao.*;
import dsqp.db_club.model.ClubUserModel;
import dsqp.db_club.model.PromoterLevelUpInfoModel;
import dsqp.db_club.model.PromoterModel;
import dsqp.db_club_dict.dao.DictFormalDao;
import dsqp.db_club_dict.dao.DictGameDao;
import dsqp.db_club_dict.dao.DictGameDbDao;
import dsqp.db_club_dict.model.DictFormalModel;
import dsqp.db_club_dict.model.DictGameDbModel;
import dsqp.db_club_log.dao.*;
import dsqp.db_club_log.model.LogClubModel;
import dsqp.db_club_log.model.LogFailFormalGamecardModel;
import dsqp.db_club_log.model.LogGamecardModel;
import dsqp.db_club_log.model.LogWeekPyjUserRecordModel;
import dsqp.db_game.dao.log_dev.UPyjUserRecordDao;
import dsqp.util.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ds on 2018/1/5.
 */
@Component
@EnableScheduling
public class TaskMin {

    /**
     * 代理转正（每分钟/次）
     * 条件：
     *     1.转正期限内（48小时）
     *     2.参与对局新玩家人数 大于等于 10
     *     3.代开房对局数:10 、非代开房局数：20（至少1名新玩家，两者完成一个即可）
     * by lyf 20180105
     */
    public void becomeFormalSuccess() {
        //游戏列表
        DataTable gameDt = DictGameDao.getList();
        if (gameDt.rows.length <= 0) {//游戏列表为空
            return;
        }

        int gameId;
        long promoterId     = 0L;
        long clubId         = 0L;
        long gameUserId     = 0L;
        String gameNickName = "";
        int peopleNum       = 0;
        int newPeopleNum    = 0;    //参与对局新玩家人数
        int pyjRoomNum      = 0;    //代开房局数
        int nonPyjRoomNum   = 0;    //非代开房局数

        DictGameDbModel dictDb;

        //审核条件
        int cfg_peopleNum;
        int cfg_newPeopleNum;
        int cfg_pyjRoomNum;
        int cfg_nonPyjRoomNum;
        int award;
        boolean isSuccess = false;

        //循环游戏
        for (DataRow gameRow: gameDt.rows) {
            gameId = CommonUtils.getIntValue(gameRow.getColumnValue("gameId"));
            dictDb = DictGameDbDao.getByGameId(gameId, true);
            if (dictDb == null) {//游戏库地址为空
                continue;
            }

            //推送通知 :: url
            String url = "http://" + dictDb.getWebDomain() + ":" + dictDb.getWebPort() + "/api";

            DictFormalModel f = DictFormalDao.getByGameId(gameId);
            cfg_peopleNum = f != null ? f.getPeopleNum() : CommonConfig.PEOPLE_NUM;
            cfg_newPeopleNum = f != null ? f.getNewPeopleNum() : CommonConfig.NEW_PEOPLE_NUM;
            cfg_pyjRoomNum = f != null ? f.getPyjRoomNum() : CommonConfig.PYJ_ROOM_NUM;
            cfg_nonPyjRoomNum = f != null ? f.getNonPyjRoomNum() : CommonConfig.NON_PYJ_ROOM_NUM;
            award = f != null ? f.getAward() : CommonConfig.AWARD;
//            int refreshDay = f != null ? f.getRefreshDay() : CommonConfig.REFRESH_DAY;


            //待转正: 代理信息列表
            DataTable preFormalDt = ClubDao.getPreFormal(gameId, ClubStatus.Init.getClubStatus());
            if (preFormalDt.rows.length > 0) {
                //循环待转正代理列表
                for (DataRow preFormalRow: preFormalDt.rows) {
                    clubId = CommonUtils.getLongValue(preFormalRow.getColumnValue("id"));                 //俱乐部ID
                    promoterId = CommonUtils.getLongValue(preFormalRow.getColumnValue("promoterId"));     //代理ID
                    gameUserId = CommonUtils.getLongValue(preFormalRow.getColumnValue("gameUserId"));     //玩家游戏ID
                    gameNickName = preFormalRow.getColumnValue("gameNickName");                          //俱乐部昵称
                    peopleNum = CommonUtils.getIntValue(preFormalRow.getColumnValue("peopleNum"));        //俱乐部人数
                    Date createTime = DateUtils.String2DateTime(preFormalRow.getColumnValue("createTime"));
                    Date expireTime = DateUtils.String2DateTime(preFormalRow.getColumnValue("ExpireTime"));

                    DataTable clubUserDt = ClubUserDao.getListByClubId(clubId);
                    if (clubUserDt.rows.length <= 0) {//俱乐部成员为0
                        continue;
                    }

                    List<Long> userIds = DBUtils.convert2List(Long.class, "gameUserId", clubUserDt);

                    /* 判断 :: 俱乐部人数条件 */
                    if (cfg_peopleNum != 0) {
                        isSuccess = userIds.size() >= cfg_peopleNum;    //条件 :: 俱乐部人数
                    }

                    /* 判断（新） :: 游戏对局条件 */
                    if (cfg_newPeopleNum != 0) {

//                        List<Long> oldUserIds = Lists.newArrayList(userIds);
//                        List<Long> newMemberUserIds = LogClubJoinDao.listNewMemberUserId(clubId, userIds);
//                        if (newMemberUserIds != null && newMemberUserIds.size() > 0) {
//                            newMemberUserIds.forEach(oldUserIds::remove);    //老玩家userId
//                        }
//
//                        //刷新身份
//                        DataTable dt = new DataTable();
//                        List<Long> list = Lists.newArrayList();    //待刷新玩家ID
//                        for (Long userId: oldUserIds) {
//                            dt = UUserInfoLogDao.getLastLoginOfLimit2(dictDb, userId);
//                            if (dt.rows.length == 2) {
//                                Date t1 = DateUtils.String2DateTime(dt.rows[0].getColumnValue("loginTime"));
//                                Date t2 = DateUtils.String2DateTime(dt.rows[1].getColumnValue("loginTime"));
//                                int day = DateUtils.daysOfTwoDate(t1, t2);
//                                if (day >= refreshDay) {
//                                    list.add(userId);
//                                }
//                            }
//                        }
//                        if (list!=null && list.size() > 0) {
//                            int r = LogClubJoinDao.removeForRefresh(gameId, list);
//                            System.out.println("状态：" + r + ",刷新身份userID" + list);
//                        }

                        List<Long> newMemberUserIds = LogClubJoinDao.listNewMemberUserId(clubId, userIds);    //新玩家UserId
                        if (newMemberUserIds == null) {//新玩家UserId为空
                            continue;
                        }


                        /*########
                            1.参与对局新玩家人数 大于等于 10（cfg_newPeopleNum）
                        ##########*/
                        newPeopleNum = UPyjUserRecordDao.getPeopleNumOfPyj(dictDb, newMemberUserIds, createTime, expireTime);
                        isSuccess = newPeopleNum >= cfg_newPeopleNum;
                        if (isSuccess) {
                            DataTable pyjRoomGameInfoDt = UPyjUserRecordDao.getUPyjRecordOfClubUser(dictDb, clubId, userIds, createTime, expireTime);    //代开房
                            DataTable nonPyjRoomGameInfoDt = UPyjUserRecordDao.getUPyjRecordOfClubUser(dictDb, 0, userIds, createTime, expireTime);      //非代开房

                            pyjRoomNum    = pyjRoomGameInfoDt.rows.length>0    ? CommonUtils.getIntValue(pyjRoomGameInfoDt.rows[0].getColumnValue("pyjRoomNum"))    : 0;
                            nonPyjRoomNum = nonPyjRoomGameInfoDt.rows.length>0 ? CommonUtils.getIntValue(nonPyjRoomGameInfoDt.rows[0].getColumnValue("pyjRoomNum")) : 0;

                            /*########
                                2.对局任务（两者完成一个即可）
                                    1)代开房对局数:10 、2）非代开房局数：20
                            ##########*/
                            isSuccess = pyjRoomNum >= cfg_pyjRoomNum || nonPyjRoomNum >= cfg_nonPyjRoomNum;
                        }

                    }//新条件


                    if (isSuccess) {

                        //转正成功 》更改 俱乐部状态为正常（clubStatus:1）
                        Date now = new Date();
                        int result = ClubDao.updateClubStatus(promoterId, ClubStatus.Normal.getClubStatus());
                        //转正成功后，特级代理生成记录（用于自动降级时间节点）
                        if (result > 0){

                            PromoterModel p = PromoterDao.getOne(promoterId);
                            if(p!=null){

                                //成功转正后，赠送100钻石给代理
                                boolean r = award > 0 && ClubDao.updateClubCard(promoterId,award) > 0;
                                if (r) {    //记录钻石日志
                                    LogGamecardModel log = new LogGamecardModel();
                                    log.setGameId(gameId);
                                    log.setPromoterId(promoterId);
                                    log.setNickName(p.getNickName());
                                    log.setSource(GamecardSource.FORMAL_AWARD.getType());
                                    log.setChangeNum(award);
                                    log.setChangeBefore(p.getGameCard());
                                    log.setChangeAfter(p.getGameCard() + award);
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
                            }


                            //TODO 推送通知 :: 代理商转正
                            try {
                                HttpUtils.get(url + "/club/?act=check&userId=" + gameUserId + "&clubId=" + clubId);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            int clubType = LogClubType.SUCCESS.getClubType();
                            //日志
                            LogClubModel log = new LogClubModel();
                            log.setClubId(clubId);
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

                    } else {
                        //转正未到达条件
                    }

                }//End循环: 待转正代理列表
            }

        }


    }


    /**
     * 转正失败处理（每分钟/次）
     *
     *   删除（代理账号、俱乐部 ）::  1)改变状态：代理状态、俱乐部状态 》 2)清除俱乐部玩家
     *   删除条件： clubStatus=0 AND expireTime < 当前
     * by lyf 20180105
     */
    public void becomeFormalFail() {
        //游戏列表
        DataTable gameDt = DictGameDao.getList();
        if (gameDt.rows.length <= 0) {
            return;//游戏列表为空
        }

        PromoterModel p;
        PromoterModel parentP;
        long clubId = 0L;
        long gameUserId;
        long gameCard;
        String gameNickName;
        int peopleNum;
        long parentCellPhone;

        LogFailFormalGamecardModel failSendLog;

        //循环游戏
        for (DataRow gameRow: gameDt.rows) {
            int gameId = CommonUtils.getIntValue(gameRow.getColumnValue("gameId"));
            DictGameDbModel dictDb = DictGameDbDao.getByGameId(gameId, true);
            if (dictDb == null) {
                continue;//游戏库地址为空
            }

            //推送通知 :: url
            String url = "http://" + dictDb.getWebDomain() + ":" + dictDb.getWebPort() + "/api";

            /** 代理到期
             * 1.删除club、club_user、club_share 中代理记录
             * 2.删除 promoter 代理信息
             * 3.记录log：club，promoOrder
             * 4.推送通知 :: 清理出俱乐部
             */
            DataTable clubList = ClubDao.getExpireList(gameId, ClubStatus.Init.getClubStatus());     //到期: 俱乐部信息列表
            if (clubList.rows.length <= 0) {
                continue;//查无，过期未转正俱乐部
            }

            List<Long> clubIds = DBUtils.convert2List(Long.class, "id", clubList);
            List<Long> promoterIds = DBUtils.convert2List(Long.class, "promoterId", clubList);

            DataTable clubUserList = ClubUserDao.listClubUserByClubId(clubIds);                      //俱乐部 :: 玩家信息
            DataTable promoterList = PromoterDao.listByPromoterIds(promoterIds);                     //俱乐部 :: 代理信息

            //根据未转正俱乐部状态、到期时间，删除俱乐部相关信息
            int result = ClubUserDao.removeByClubId(clubIds);        //过期处理
            if (result > 0) {

                /**
                 * 删除俱乐部相关
                 */
                int r_clubShare = ClubShareDao.removeByClubId(clubIds);
                int r_promoter = PromoterDao.removeByPromoterId(promoterIds);
                int r_club = ClubDao.removeByClubId(clubIds);



                //循环过期俱乐部clubList
                int clubType = LogClubType.FAILURE.getClubType();
                Date now = new Date();
                for (DataRow row: clubList.rows) {

                    clubId = CommonUtils.getLongValue(row.getColumnValue("id"));                 //俱乐部ID
                    gameUserId = CommonUtils.getLongValue(row.getColumnValue("gameUserId"));     //玩家游戏ID
                    gameNickName = row.getColumnValue("gameNickName");                          //俱乐部昵称
                    peopleNum = CommonUtils.getIntValue(row.getColumnValue("peopleNum"));        //俱乐部人数
                    Date expireTime = DateUtils.String2DateTime(row.getColumnValue("ExpireTime"));


                    //记录日志
                    LogClubModel log = new LogClubModel();
                    log.setGameId(gameId);
                    log.setClubId(clubId);
                    log.setGameUserId(gameUserId);
                    log.setGameNickName(gameNickName);
                    log.setPeopleNum(peopleNum);
                    log.setClubType(clubType);
                    log.setExpireTime(expireTime);
                    log.setCreateDate(now);
                    log.setCreateTime(now);
                    LogClubDao.add(log);

                    /* 工单 */
                    PromoterOrderDao.updatePromoterStatus(clubId, clubType);

                    /* promoter_del加一条记录 */
                    for (DataRow promoterRow: promoterList.rows) {
                        p               = DBUtils.convert2Model(PromoterModel.class, promoterRow);
                        parentP         = PromoterDao.getOne(p.getParentId());
                        parentCellPhone = parentP!=null ? p.getCellPhone() : 0L;
                        gameCard        = p.getGameCard();
                        Date start      = new Date();
                        Date end        = DateUtils.addDate("day", 30, start);

                        failSendLog     = new LogFailFormalGamecardModel();

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
                                String title = "转正失败通知";
                                String content =  "很抱歉，您的俱乐部因未在指定时间内完成转正任务，现已被系统解散。您所购买的钻石，现以邮件形式发回给您，请查收。如有疑问请联系客服。";

                                GameApiUtil.sendMails(gameId, userIds, title, content, gameCard+":1", start, end);
                            }
                        }

                        PromoterDelDao.add(p, parentCellPhone);
                    }



                    //TODO 推送通知 :: 代理转正失败    2017-10-20
                    try {
                        HttpUtils.get(url + "/club/?act=destroy&userId=" + gameUserId + "&clubId=" + clubId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }//循环clubList :: 结束


                //TODO 推送通知 :: 清理出俱乐部
                if (clubUserList.rows.length <= 0) {
                    continue;
                }
                List<Long> gameUserIds = DBUtils.convert2List(Long.class, "gameUserId", clubUserList);

                //释放新玩家
                List<Long> newMemberUserId = LogClubJoinDao.listNewMemberUserId(clubId, gameUserIds); //新玩家UserId
                if (newMemberUserId != null) {//新玩家UserId不为空
                    LogClubJoinDao.removeByClubIdAndUserIds(clubId, newMemberUserId);
                }


                //Start: 推送通知
                StringBuffer sb = new StringBuffer();
                for (Long value: gameUserIds) {
                    sb.append(value).append(",");
                }
                String userIds = sb.substring(0, sb.length() - 1);

                Map<String, String> maps = Maps.newHashMap();
                maps.put("userIds", userIds);
                maps.put("title", "通知");
                maps.put("content", "您所在的俱乐部已解散，请重新加入俱乐部。");
                maps.put("reward", "");
                maps.put("startTime", EncodingUtils.urlEncode(DateUtils.DateTime2String(new Date())));

                try {
                    HttpUtils.get(url + "/mail/?" + SignUtil.getParamsStrFromMap(maps) + "&act=newusermails");
                    HttpUtils.get(url + "/user/?act=RefreshUIStatus&userIds=" + maps.get("userIds"));
                    System.out.println(url + "/mail/?" + SignUtil.getParamsStrFromMap(maps) + "&act=newusermails");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //End: 推送通知

            }

        }


    }


    /**
     * 账户解封（每分钟/次）
     * 解封条件：
     *      解封时间 < 当前 ，解封
     * by lyf 20180105
     */
    public void unlockPromoter() {
        DataTable lockPromoterList = PromoterLockDao.getExpireList();
        if (lockPromoterList.rows.length <= 0) {
            return;
        }

        //1.解除账户封停状态
        List<Long> promoterIds = DBUtils.convert2List(Long.class, "promoterId", lockPromoterList);
        int r1 = PromoterDao.updateLoginStatus2More(promoterIds, LoginStatus.Normal.getLoginStatus());
        int r2 = ClubDao.updateClubStatus2More(promoterIds, ClubStatus.Normal.getClubStatus());

        //2.删除封停记录
        if (r1 > 0 && r2 > 0) {
            PromoterLockDao.deleteByPromoterIds(promoterIds);
        } else{
            System.out.println("task :: 解封失败："+promoterIds);
        }
    }


    /**
     * 统计本周玩家对局（15分钟/次）
     * by lyf 20180105
     */
    public void statWeekPyjRoomNum() {
        //游戏列表
        DataTable gameDt = DictGameDao.getList();

        Date now = new Date();
        Date start = DateUtils.String2Date(TimeUtil.getBegin(now));  //本周一
        Date end = DateUtils.String2Date(TimeUtil.getEnd(now));      //本周日

        int gameId;
        long clubId;
        long gameUserId;
        int pyjRoomNum;

        DictGameDbModel dictDb;
        LogWeekPyjUserRecordModel model;

        for (DataRow gameRow: gameDt.rows) {
            gameId = CommonUtils.getIntValue(gameRow.getColumnValue("gameId"));
            dictDb = DictGameDbDao.getByGameId(gameId, true);
            if (dictDb == null) {//游戏库地址为空
                continue;
            }

            //俱乐部列表
            DataTable clubDt = ClubDao.listByGameId(gameId);
            for (DataRow clubRow: clubDt.rows) {
                clubId = CommonUtils.getLongValue(clubRow.getColumnValue("id"));

                DataTable pyjDt = UPyjUserRecordDao.getClubUserPyjRoomNum(dictDb,clubId,start,end);
                for (DataRow pyjRow: pyjDt.rows) {
                    gameUserId = CommonUtils.getLongValue(pyjRow.getColumnValue("gameUserId"));
                    pyjRoomNum = CommonUtils.getIntValue(pyjRow.getColumnValue("pyjRoomNum"));

                    ClubUserModel user = ClubUserDao.getClubUserInfo(clubId,gameUserId);
                    if (user != null){
                        model = new LogWeekPyjUserRecordModel();
                        model.setGameId(gameId);
                        model.setClubId(clubId);
                        model.setPromoterId(user.getPromoterId());
                        model.setGameUserId(gameUserId);
                        model.setGameNickName(user.getGameNickName());
                        model.setPyjRoomNum(pyjRoomNum);
                        model.setYear(DateUtils.getYear());
                        model.setWeek(DateUtils.getWeekOfYear(now));
                        model.setCreateTime(new Date());

                        LogWeekPyjUserRecordDao.addOnDuplicate(model);
                    }

                }

            }

        }
    }

}
