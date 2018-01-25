package club.task;

import dsqp.common_const.club.LevelUp;
import dsqp.common_const.club.PromoterLevel;
import dsqp.db.model.DataRow;
import dsqp.db.model.DataTable;
import dsqp.db_club.dao.*;
import dsqp.db_club.model.NoticeDetailModel;
import dsqp.db_club.model.NoticeModel;
import dsqp.db_club.model.PromoterLevelUpInfoModel;
import dsqp.db_club.model.PromoterModel;
import dsqp.db_club_dict.dao.DictGameDao;
import dsqp.db_club_dict.dao.DictGameDbDao;
import dsqp.db_club_dict.dao.DictLevelUpDao;
import dsqp.db_club_dict.model.DictBonusModel;
import dsqp.db_club_dict.model.DictGameDbModel;
import dsqp.db_club_dict.model.DictLevelUpModel;
import dsqp.util.CommonUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/1/23.
 */
@Component
@EnableScheduling
public class TaskSecond {

    /**
     * 代理商自动升级
     * by lyf 20180122
     */
    public void agentUp() {
        //游戏列表
        DataTable gameDt = DictGameDao.getList();
        if (gameDt.rows.length <= 0) {//游戏列表为空
            return;
        }

        int                      pLevel;
        int                      upLevel   = 0;
        int                      directNum = 0;
        NoticeModel notice;
        String                   content = "";    //代理消息内容
        NoticeDetailModel noticeDetail;
        DictLevelUpModel dictLevelUp;
        PromoterLevelUpInfoModel levelUpInfo;
        DictBonusModel dictBonus;

        //循环游戏
        for (DataRow gameRow: gameDt.rows) {
            int gameId = CommonUtils.getIntValue(gameRow.getColumnValue("gameId"));
            DictGameDbModel dictDb = DictGameDbDao.getByGameId(gameId, true);
            if (dictDb == null) {
                continue;//游戏库地址为空
            }


            List<Long> promoterIds = PromoterDao.listPromoterIdByGameId(gameId);
            for (Long promoterId: promoterIds) {
                PromoterModel p = PromoterDao.getOne(promoterId);               //代理信息
                if (p == null) {
                    continue;
                }
                pLevel = p.getpLevel();

                DataTable dt = PromoterPayDao.queryPayInfo(promoterId, true);    //代理充值
                boolean canLevelUp = false;
                if (dt.rows.length > 0) {

                    //获取最近一个月的购买总额
                    double lateTotalPrice = CommonUtils.getDoubleValue(dt.rows[0].getColumnValue("lateTotalPrice"));
                    //当月直属代理商购买总额
                    double dCurrentTotalPrice = CommonUtils.getDoubleValue(dt.rows[0].getColumnValue("dCurrentTotalPrice"));

                    DataTable directNumDt = PromoterDao.getDirectNumsByParentId(promoterId);//名下代理商人数
                    if (directNumDt.rows.length > 0) {
                        directNum = CommonUtils.getIntValue(directNumDt.rows[0].getColumnValue("nums"));
                    }

                    switch(pLevel) {
                        case (PromoterLevel.FIRST): {
                            content     = "恭喜您的代理等级由一级升至特级！";
                            upLevel     = PromoterLevel.SUPER;
                            dictLevelUp = DictLevelUpDao.getConditionByGameIdAndLevelUpType(gameId, LevelUp.FIRST2SUPER);

                            //一级升特级 :: 近一个月，直属下属充值金额、直属人数 >= 升级条件
                            if (dictLevelUp != null) {
                                canLevelUp = dictLevelUp.getTotalPay() <= dCurrentTotalPrice && dictLevelUp.getTotalPromoter() <= directNum;
                            }

                            break;
                        }
                        case (PromoterLevel.SECOND): {
                            content     = "恭喜您的代理等级由二级升至一级！";
                            upLevel     = PromoterLevel.FIRST;
                            dictLevelUp = DictLevelUpDao.getConditionByGameIdAndLevelUpType(gameId, LevelUp.SECOND2FIRST);

                            //二级升一级 :: 近一个月代理商充值金额 >= 升级条件
                            if (dictLevelUp != null) {
                                canLevelUp = dictLevelUp.getTotalPay() <= lateTotalPrice && dictLevelUp.getTotalPromoter() <= directNum;
                            }

                            break;
                        }
                    }

                    //代理升级
                    if (canLevelUp) {
                        int r = PromoterDao.updateLevelById(promoterId, upLevel);
                        if (r > 0) {
                            /* 升级信息记录 */
                            levelUpInfo = new PromoterLevelUpInfoModel();
                            levelUpInfo.setGameId(gameId);
                            levelUpInfo.setPromoterId(promoterId);
                            levelUpInfo.setpLevel(pLevel);
                            levelUpInfo.setCreateTime(new Date());
                            levelUpInfo.setLevelUpType(pLevel);
                            PromoterLevelUpInfoDao.add(levelUpInfo);

                            /* 发送代理消息 */
                            notice       = new NoticeModel();
                            noticeDetail = new NoticeDetailModel();

                            notice.setGameId(gameId);
                            notice.setTitle("升级通知");
                            notice.setContent(content);
                            notice.setNoticeType(0);
                            notice.setCreatTime(new Date());
                            NoticeDao.add(notice);

                            noticeDetail.setNoticeId(notice.getId());
                            noticeDetail.setGameId(gameId);
                            noticeDetail.setPromoterId(promoterId);
                            noticeDetail.setCreateTime(new Date());
                            noticeDetail.setIsRead(false);
                            NoticeDetailDao.add(noticeDetail);
                        }
                    }//代理升级 :: END


                }

            }


        }//循环游戏 :: END


    }

}
