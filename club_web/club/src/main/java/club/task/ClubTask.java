//package club.task;
//
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
//import dsqp.common_const.club.*;
//import dsqp.db.model.DataRow;
//import dsqp.db.model.DataTable;
//import dsqp.db.util.DBUtils;
//import dsqp.db_club.dao.*;
//import dsqp.db_club.model.PromoterLevelUpInfoModel;
//import dsqp.db_club.model.PromoterModel;
//import dsqp.db_club_dict.dao.DictFormalDao;
//import dsqp.db_club_dict.dao.DictGameDao;
//import dsqp.db_club_dict.dao.DictGameDbDao;
//import dsqp.db_club_dict.dao.DictLevelUpDao;
//import dsqp.db_club_dict.model.DictFormalModel;
//import dsqp.db_club_dict.model.DictGameDbModel;
//import dsqp.db_club_dict.model.DictLevelUpModel;
//import dsqp.db_club_log.dao.LogClubDao;
//import dsqp.db_club_log.dao.LogClubJoinDao;
//import dsqp.db_club_log.dao.LogGamecardDao;
//import dsqp.db_club_log.model.LogClubModel;
//import dsqp.db_club_log.model.LogGamecardModel;
//import dsqp.db_game.dao.log_dev.UPyjUserRecordDao;
//import dsqp.db_game.dao.log_dev.UUserInfoLogDao;
//import dsqp.util.*;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by Aris on 2017/6/22.
// */
//
//@Component
//@EnableScheduling
//public class ClubTask {
//
//    /**
//     * 账户解封
//     * 每1分钟，执行一次解封动作
//     *
//     * 解除账户封停状态 》删除封停记录
//     * 解封条件：
//     *      解封时间 < 当前 ，解封
//     */
//    @Scheduled(cron = "0 0/1 * * * ?")
//    public void unlockPromoter() {
//        DataTable lockPromoterList = PromoterLockDao.getExpireList();
//        if (lockPromoterList.rows.length <= 0) {
//            return;
//        }
//
//        //1.解除账户封停状态
//        List<Long> promoterIds = DBUtils.convert2List(Long.class, "promoterId", lockPromoterList);
//        int r1 = PromoterDao.updateLoginStatus2More(promoterIds, LoginStatus.Normal.getLoginStatus());
//        int r2 = ClubDao.updateClubStatus2More(promoterIds, ClubStatus.Normal.getClubStatus());
//
//        //2.删除封停记录
//        if (r1 > 0 && r2 > 0) {
//            PromoterLockDao.deleteByPromoterIds(promoterIds);
//        } else{
//            System.out.println("task :: 解封失败："+promoterIds);
//        }
//    }
//
//    /**
//     * 代理转正
//     * 每1分钟，执行一次
//     * 条件：
//     *     1.转正期限内（48小时）
//     *     2.参与对局新玩家人数 大于等于 10
//     *     3.代开房对局数:10 、非代开房局数：20（至少1名新玩家，两者完成一个即可）
//     *
//     * 一、未到期，判断 :: 转正任务是否完成
//     *   完成：
//     *     代理转正 :: 改变状态：代理状态、俱乐部状态：1（正常）
//     *   未完成：
//     *     do nothing
//     *
//     */
//    @Scheduled(fixedDelay = 1000 * 60, initialDelay = 1000 * 5)
//    public void checkPromoter() {
//        //游戏列表
//        DataTable gameDt = DictGameDao.getList();
//        if (gameDt.rows.length <= 0) {
//            return;//游戏列表为空
//        }
//
//        //循环游戏
//        for (DataRow gameRow: gameDt.rows) {
//            int gameId = CommonUtils.getIntValue(gameRow.getColumnValue("gameId"));
//            DictGameDbModel dictDb = DictGameDbDao.getByGameId(gameId, true);
//            if (dictDb == null) {
//                continue;//游戏库地址为空
//            }
//
//            //推送通知 :: url
//            DictGameDbModel m = DictGameDbDao.getByGameId(gameId, true);
//            String url = "http://" + m.getWebDomain() + ":" + m.getWebPort() + "/api";
//
//            long promoterId = 0L;
//            long clubId = 0L;
//            long gameUserId = 0L;
//            String gameNickName = "";
//            int peopleNum = 0;
//            //参与对局新玩家人数
//            int newPeopleNum = 0;
//            //代开房局数
//            int pyjRoomNum = 0;
//            //非代开房局数
//            int nonPyjRoomNum = 0;
//
//            /**
//             * 一、未到期，判断 :: 转正任务是否完成
//             */
//            //审核条件
//            DictFormalModel f = DictFormalDao.getByGameId(gameId);
//            int cfg_newPeopleNum = f != null ? f.getNewPeopleNum() : CommonConfig.NEW_PEOPLE_NUM;
//            int cfg_pyjRoomNum = f != null ? f.getPyjRoomNum() : CommonConfig.PYJ_ROOM_NUM;
//            int cfg_nonPyjRoomNum = f != null ? f.getNonPyjRoomNum() : CommonConfig.NON_PYJ_ROOM_NUM;
//            int award = f != null ? f.getAward() : CommonConfig.AWARD;
//            int refreshDay = f != null ? f.getRefreshDay() : CommonConfig.REFRESH_DAY;
//
//            DataTable preFormalDt = ClubDao.getPreFormal(gameId, ClubStatus.Init.getClubStatus());        //待转正: 代理信息列表
//            if (preFormalDt.rows.length > 0) {
//                //循环待转正代理列表
//                for (DataRow preFormalRow: preFormalDt.rows) {
//                    clubId = CommonUtils.getLongValue(preFormalRow.getColumnValue("id"));                 //俱乐部ID
//                    promoterId = CommonUtils.getLongValue(preFormalRow.getColumnValue("promoterId"));     //代理ID
//                    gameUserId = CommonUtils.getLongValue(preFormalRow.getColumnValue("gameUserId"));     //玩家游戏ID
//                    gameNickName = preFormalRow.getColumnValue("gameNickName");                   //俱乐部昵称
//                    peopleNum = CommonUtils.getIntValue(preFormalRow.getColumnValue("peopleNum"));        //俱乐部人数
//                    Date createTime = DateUtils.String2DateTime(preFormalRow.getColumnValue("createTime"));
//                    Date expireTime = DateUtils.String2DateTime(preFormalRow.getColumnValue("ExpireTime"));
//
//                    DataTable clubUserDt = ClubUserDao.getListByClubId(clubId);
//                    if (clubUserDt.rows.length <= 0) {
//                        continue;
//                    }
//
//                    List<Long> userIds = DBUtils.convert2List(Long.class, "gameUserId", clubUserDt);
//                    List<Long> oldUserIds = userIds;
//                    List<Long> newMemberUserIds = LogClubJoinDao.listNewMemberUserId(clubId, userIds);
//                    if (newMemberUserIds != null && newMemberUserIds.size() > 0) {
//                        int i1 = oldUserIds.size();
//                        System.out.println(i1+" "+"userId("+oldUserIds);
//                        newMemberUserIds.forEach(oldUserIds::remove);
//                        int i2 = oldUserIds.size();
//                        System.out.println(i2+" userId("+oldUserIds);
//                    }
//
//                    //刷新身份
//                    DataTable dt = new DataTable();
//                    List<Long> list = Lists.newArrayList();    //待刷新玩家ID
//                    for (Long userId: oldUserIds) {
//                        dt = UUserInfoLogDao.getLastLoginOfLimit2(dictDb, userId);
//                        if (dt.rows.length > 0) {
//                            Date t1 = DateUtils.String2DateTime(dt.rows[0].getColumnValue("loginTime"));
//                            Date t2 = DateUtils.String2DateTime(dt.rows[1].getColumnValue("loginTime"));
//                            int day = DateUtils.daysOfTwoDate(t1, t2);
//                            if (day >= refreshDay) {
//                                list.add(userId);
//                            }
//                        }
//                    }
//                    if (list!=null && list.size() > 0) {
//                        int r = LogClubJoinDao.removeForRefresh(gameId, list);
//                        System.out.println("状态：" + r + ",刷新身份userID" + list);
//                    }
//
//                    newMemberUserIds = LogClubJoinDao.listNewMemberUserId(clubId, userIds);    //新玩家UserId
//                    if (newMemberUserIds == null) {//新玩家UserId为空
//                        continue;
//                    }
//
//
//                    /* 判断是否完成任务 */
//                    //参与对局新玩家人数 大于等于 10（cfg_newPeopleNum）
//                    newPeopleNum = UPyjUserRecordDao.getClubUPyjRoomNumOfNew(dictDb, newMemberUserIds, createTime, expireTime);
//                    if (newPeopleNum >= cfg_newPeopleNum) {
//
//                        DataTable pyjRoomGameInfoDt = UPyjUserRecordDao.getClubUPyjRecordOfNew(dictDb, clubId, newMemberUserIds, createTime, expireTime);    //代开房
//                        DataTable nonPyjRoomGameInfoDt = UPyjUserRecordDao.getClubUPyjRecordOfNew(dictDb, 0, newMemberUserIds, createTime, expireTime);      //非代开房
//                        if (pyjRoomGameInfoDt.rows.length > 0) {
//                            pyjRoomNum = CommonUtils.getIntValue(pyjRoomGameInfoDt.rows[0].getColumnValue("pyjRoomNum"));
//                        }
//                        if (nonPyjRoomGameInfoDt.rows.length > 0) {
//                            nonPyjRoomNum = CommonUtils.getIntValue(pyjRoomGameInfoDt.rows[0].getColumnValue("pyjRoomNum"));
//                        }
//
//                        //代开房对局数:10 、非代开房局数：20（至少1名新玩家，两者完成一个即可）
//                        if (pyjRoomNum >= cfg_pyjRoomNum || nonPyjRoomNum >= cfg_nonPyjRoomNum) {
//                            //转正成功 》更改 俱乐部状态为正常（clubStatus:1）
//                            Date now = new Date();
//                            int result = ClubDao.updateClubStatus(promoterId, ClubStatus.Normal.getClubStatus());
//                            //转正成功后，特级代理生成记录（用于自动降级时间节点）
//                            if (result > 0){
//
//                                PromoterModel p = PromoterDao.getOne(promoterId);
//                                if(p!=null){
//
//                                    //成功转正后，赠送100钻石给代理
//                                    int r = PromoterDao.updateGameCardById(award, promoterId);
//                                    if (r > 0) {    //记录钻石日志
//                                        LogGamecardModel log = new LogGamecardModel();
//                                        log.setGameId(gameId);
//                                        log.setPromoterId(promoterId);
//                                        log.setNickName(p.getNickName());
//                                        log.setSource(GamecardSource.FORMAL_AWARD.getType());
//                                        log.setChangeNum(award);
//                                        log.setChangeBefore(p.getGameCard());
//                                        log.setChangeAfter(p.getGameCard() + award);
//                                        log.setCreateTime(now);
//                                        log.setCreateDate(now);
//                                        LogGamecardDao.add(log);
//                                    }
//
//
//                                    int pLevel = p.getpLevel();
//                                    if(pLevel==PromoterLevel.SUPER){//只记录特级转正时间节点
//                                        PromoterLevelUpInfoModel model = new PromoterLevelUpInfoModel();
//                                        model.setGameId(gameId);
//                                        model.setPromoterId(promoterId);
//                                        model.setpLevel(0);
//                                        model.setCreateTime(now);
//                                        model.setLevelUpType(LevelUp.OPEN2SUPER);
//                                        PromoterLevelUpInfoDao.add(model);
//                                    }
//                                }
//
//
//                                //TODO 推送通知 :: 代理商转正
//                                try {
//                                    HttpUtils.get(url + "/club/?act=check&userId=" + gameUserId + "&clubId=" + clubId);
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//
//                                int clubType = LogClubType.SUCCESS.getClubType();
//                                //日志
//                                LogClubModel log = new LogClubModel();
//                                log.setClubId(clubId);
//                                log.setGameId(gameId);
//                                log.setGameUserId(gameUserId);
//                                log.setGameNickName(gameNickName);
//                                log.setPeopleNum(peopleNum);
//                                log.setClubType(clubType);
//                                log.setExpireTime(expireTime);
//                                log.setCreateDate(now);
//                                log.setCreateTime(now);
//                                LogClubDao.add(log);
//                                //工单
//                                PromoterOrderDao.updatePromoterStatus(clubId, clubType);
//
//                            }
//                        }
//
//                    } else {
//                        //转正未到达条件
//                    }
//
//                }//End循环: 待转正代理列表
//            }
//
//        }
//
//
//    }
//
//
//    /**
//     * 代理转正 :: 过期处理
//     * 每5分钟，执行一次
//     *
//     * 二、到期处理
//     *   删除（代理账号、俱乐部 ）::  1)改变状态：代理状态、俱乐部状态 》 2)清除俱乐部玩家
//     *   删除条件： clubStatus=0 AND expireTime < 当前
//     */
//    @Scheduled(fixedDelay = 1000 * 60, initialDelay = 1000 * 10)
//    public void checkPromoterExpire() {
//        //游戏列表
//        DataTable gameDt = DictGameDao.getList();
//        if (gameDt.rows.length <= 0) {
//            return;//游戏列表为空
//        }
//
//        //循环游戏
//        for (DataRow gameRow: gameDt.rows) {
//            int gameId = CommonUtils.getIntValue(gameRow.getColumnValue("gameId"));
//            DictGameDbModel dictDb = DictGameDbDao.getByGameId(gameId, true);
//            if (dictDb == null) {
//                return;//游戏库地址为空
//            }
//
//            //推送通知 :: url
//            DictGameDbModel m = DictGameDbDao.getByGameId(gameId, true);
//            String url = "http://" + m.getWebDomain() + ":" + m.getWebPort() + "/api";
//
//            long clubId = 0L;
//            long gameUserId = 0L;
//            String gameNickName = "";
//            int peopleNum = 0;
//
//
//            /** 代理到期
//             * 1.删除club、club_user、club_share 中代理记录
//             * 2.删除 promoter 代理信息
//             * 3.记录log：club，promoOrder
//             * 4.推送通知 :: 清理出俱乐部
//             */
//            DataTable clubList = ClubDao.getExpireList(gameId, ClubStatus.Init.getClubStatus());      //到期: 俱乐部信息列表
//            if (clubList.rows.length <= 0) {
//                return;//查无，过期未转正俱乐部
//            }
//
//            /*List<Long> clubIds = DBUtils.convert2List(Long.class, "id", clubList);                //俱乐部信息*/
//            List<Long> promoterIds = DBUtils.convert2List(Long.class, "promoterId", clubList);       //俱乐部信息
//            List<PromoterModel> promoterModels = Lists.newArrayList();
//            PromoterModel p = new PromoterModel();
//            for (Long id: promoterIds) {
//                p = PromoterDao.getOne(id);
//                if (p != null) {
//                    promoterModels.add(p);
//                }
//            }
//            DataTable clubUserList = ClubUserDao.listClubUserByPromoterId(promoterIds);     //俱乐部 :: 玩家信息
//
//
//            //根据未转正俱乐部状态、到期时间，删除俱乐部相关信息
//            boolean result = ClubDao.removeClub(gameId);        //过期处理
//            if (result) {
//
//                //循环过期俱乐部clubList
//                int clubType = LogClubType.FAILURE.getClubType();
//                Date now = new Date();
//                for (DataRow row: clubList.rows) {
//
//                    clubId = CommonUtils.getLongValue(row.getColumnValue("id"));                 //俱乐部ID
//                    gameUserId = CommonUtils.getLongValue(row.getColumnValue("gameUserId"));     //玩家游戏ID
//                    gameNickName = row.getColumnValue("gameNickName");                   //俱乐部昵称
//                    peopleNum = CommonUtils.getIntValue(row.getColumnValue("peopleNum"));        //俱乐部人数
//                    Date expireTime = DateUtils.String2DateTime(row.getColumnValue("ExpireTime"));
//
//
//                    //记录日志
//                    LogClubModel log = new LogClubModel();
//                    log.setGameId(gameId);
//                    log.setClubId(clubId);
//                    log.setGameUserId(gameUserId);
//                    log.setGameNickName(gameNickName);
//                    log.setPeopleNum(peopleNum);
//                    log.setClubType(clubType);
//                    log.setExpireTime(expireTime);
//                    log.setCreateDate(now);
//                    log.setCreateTime(now);
//                    LogClubDao.add(log);
//
//                    /* 工单 */
//                    PromoterOrderDao.updatePromoterStatus(clubId, clubType);
//
//                    /* promoter_del加一条记录 */
//                    for (PromoterModel model: promoterModels) {
//                        p = PromoterDao.getOne(model.getParentId());
//                        long parentCellPhone = p!=null ? p.getCellPhone() : 0L;             //父级电话
//
//                        PromoterDelDao.add(model, parentCellPhone);
//                    }
//
//                    //TODO 推送通知 :: 代理转正失败    2017-10-20
//                    try {
//                        HttpUtils.get(url + "/club/?act=destroy&userId=" + gameUserId + "&clubId=" + clubId);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }//循环clubList :: 结束
//
//
//                if (clubUserList.rows.length <= 0) {
//                    return;
//                }
//                List<Long> gameUserIds = DBUtils.convert2List(Long.class, "gameUserId", clubUserList);
//
//                //释放新玩家
//                List<Long> listUserId = LogClubJoinDao.listNewMemberUserId(clubId, gameUserIds); //新玩家UserId
//                if (listUserId != null) {//新玩家UserId不为空
//                    LogClubJoinDao.removeByClubIdAndUserIds(clubId, listUserId);
//                }
//
//
//                //Start: 推送通知
//                StringBuffer sb = new StringBuffer();
//                for (Long value: gameUserIds) {
//                    sb.append(value).append(",");
//                }
//                String userIds = sb.substring(0, sb.length() - 1);
//
//                Map<String, String> maps = Maps.newHashMap();
//                maps.put("userIds", userIds);
//                maps.put("title", "通知");
//                maps.put("content", "您所在的俱乐部已解散，请重新加入俱乐部。");
//                maps.put("reward", "");
//                maps.put("startTime", EncodingUtils.urlEncode(DateUtils.DateTime2String(new Date())));
////                maps.put("endTime", "");
//
//                //TODO 推送通知 :: 清理出俱乐部
//                try {
//                    HttpUtils.get(url + "/mail/?" + SignUtil.getParamsStrFromMap(maps) + "&act=newusermails");
//                    HttpUtils.get(url + "/user/?act=RefreshUIStatus&userIds=" + maps.get("userIds"));
//                    System.out.println(url + "/mail/?" + SignUtil.getParamsStrFromMap(maps) + "&act=newusermails");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                //End: 推送通知
//            }
//
//        }
//
//
//    }
//
//    /**
//     * 特级代理降级
//     * 每个月第一天执行一次
//     *
//     * 降级条件：
//     *      在一个月内，特级代理及旗下代理商充值总额未满要求则自动降级
//     *      降级考核为成为特级代理商之后的自然月算起，当月不算做考核的时间节点
//     *
//     */
////    @Scheduled(cron = "0 0 2 1 1/1 ?")
////    @Scheduled(cron = "0 0/2 * * * ?") //测试
//    public void degrade() {
//
//        //1.获取所有的特级代理商
//        //2.判断是否符合降级的条件
//            //2.1 获取他最近一次升特级的时间
//            //2.2 拿当前的时间和最近一次升特级时间的下个月的最后一天作比较，不在考核的时间节点内金额条件不满足则降级
//        List<Long> superPromoterIdList = PromoterDao.getSuperPromoterIdList();
//        if(superPromoterIdList != null){
//            for (long promoterId : superPromoterIdList){
//                PromoterModel model = PromoterDao.getOne(promoterId);
//                int gameId = model.getGameId();
//
//                //上个月代理商自己购买总额
//                DataTable totalPayPrveMonth = PromoterPayDao.getPayOfPrveMonthById(promoterId);
//                double totalPayPre = 0;
//                if (totalPayPrveMonth.rows[0].getColumnValue("totalPrice") != "") {
//                    totalPayPre = Double.parseDouble(totalPayPrveMonth.rows[0].getColumnValue("totalPrice"));
//                }
//                //上个月直属代理商购买总额
//                DataTable directTotalPayPrveMonth = PromoterPayDao.getDirectPayOfPrveMonthById(promoterId);
//                double totalPayDirectPre = 0;
//                if (directTotalPayPrveMonth.rows[0].getColumnValue("totalPrice")
//
//                        != "") {
//                    totalPayDirectPre = Double.parseDouble(directTotalPayPrveMonth.rows[0].getColumnValue("totalPrice"));
//                }
//
//                DictLevelUpModel levelUpModel = DictLevelUpDao.getConditionByGameIdAndLevelUpType(gameId, LevelUp.SUPER2FIRST);
//                double totalPayCondition = 0;
//                if(levelUpModel!=null){
//                    totalPayCondition = levelUpModel.getTotalPay();
//                }else{// 没有配置降级条件，则使用默认配置的降级条件
//                    totalPayCondition = CommonConfig.SUPER2FIRST_TOTALPAY;
//                }
//
//                //上个月代理商及直属代理商购买总额
//                    double total = totalPayPre + totalPayDirectPre;
//
//                    //特级降级
//                    DataTable dtLevelUpTime = PromoterLevelUpInfoDao.getLatestLevelUpTimeByPromoterIdAndType(promoterId, LevelUp.FIRST2SUPER);  //最近一次升级为特级代理的时间
//                    if(dtLevelUpTime.rows.length>0){
//                        String levelUpTime = dtLevelUpTime.rows[0].getColumnValue("createTime");
//                        Date levelUpDate = DateUtils.String2DateTime(levelUpTime); //最近一次升级为特级代理的时间
//
//                        //升级为特级代理的下个月的第一天
//                        Calendar calendar = Calendar.getInstance();
//                        calendar.setTime(levelUpDate);
//                        calendar.set(Calendar.DAY_OF_MONTH,1);
//                        calendar.add(Calendar.MONTH,1);
//                        Date firstDateOfNextMonth = calendar.getTime();//升级为特级代理的下个月的第一天
//                        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//                        Date lastDateOfNextMonth = calendar.getTime();//升级为特级代理的下个月的最后一天
//
//                        if(new Date().getTime()>lastDateOfNextMonth.getTime()){ //降级考核为成为特级代理商之后的自然月算起，当月不算做考核的时间节点
//                            //如果不满足保持特级代理的条件，则降为一级代理
//                            if(total<totalPayCondition){
//                                int m = PromoterDao.updateLevelById(promoterId, 1);
//
//                                PromoterLevelUpInfoModel mode1 = new PromoterLevelUpInfoModel();
//                                mode1.setGameId(gameId);
//                                mode1.setPromoterId(promoterId);
//                                mode1.setpLevel(PromoterLevel.SUPER);
//                                mode1.setLevelUpType(LevelUp.SUPER2FIRST);
//                                mode1.setCreateTime(new Date());
//                                PromoterLevelUpInfoDao.add(mode1); //新增降级记录
//                            }
//                        }
//
//                    }else {// 没有最近一次升为特级的时间记录
//
//                    }
//
//            }
//
//        }
//
//    }
//
//
//}
