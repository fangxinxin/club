package club.service.impl;

import club.service.SellService;
import club.vo.MsgVO;
import dsqp.common_const.club.GamecardSource;
import dsqp.common_const.game.GameApi;
import dsqp.db.model.DataTable;
import dsqp.db_club.dao.ClubUserDao;
import dsqp.db_club.dao.PromoterDao;
import dsqp.db_club.dao.PromoterSellDao;
import dsqp.db_club.model.ClubUserModel;
import dsqp.db_club.model.PromoterModel;
import dsqp.db_club.model.PromoterSellModel;
import dsqp.db_club_dict.dao.DictClubDao;
import dsqp.db_club_dict.dao.DictGameDbDao;
import dsqp.db_club_dict.model.DictClubModel;
import dsqp.db_club_dict.model.DictGameDbModel;
import dsqp.db_club_log.dao.LogGamecardDao;
import dsqp.db_club_log.model.LogGamecardModel;
import dsqp.db_game.dao.dev.UUserInfoDao;
import dsqp.db_game.dao.dev.UUserPointDao;
import dsqp.util.CommonUtils;
import dsqp.util.HttpUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Aris on 2017/7/25.
 */
@Service
public class SellServiceImpl implements SellService {

    public MsgVO sell(long clubId, long promoterId, int sellNum, long gameUserId) {
        MsgVO msg = new MsgVO();
        if (sellNum <= 0) {
            msg.setRemark("出售钻石数目必须大于0");
            return msg;
        }
        /**
         * 1.判断钻石是否足够
         * 2.扣除代理商钻石
         * 3.给玩家加钻石
         * 4.成功后更新订单状态
         */
        PromoterModel p = PromoterDao.getOne(promoterId);
        if (p == null) {
            msg.setRemark("查询不到用户");
            return msg;
        }

        ClubUserModel c = ClubUserDao.getClubUserInfo(clubId, gameUserId);
        if (c == null) {
            msg.setRemark("该玩家不属于您的俱乐部");
            return msg;
        }

        long sellAfter = p.getGameCard() - sellNum;
        if (sellAfter < 0) {
            msg.setRemark("代理商钻石不足");
            return msg;
        }

        DictGameDbModel db = DictGameDbDao.getByGameId(p.getGameId(), true);
        if (db == null) {
            msg.setRemark("游戏配置错误");
            return msg;
        }

        if (UUserPointDao.getPointByUserId(db, gameUserId).rows.length <= 0) {
            msg.setRemark("该玩家id有误，请确认后重试");
            return msg;
        }

        PromoterSellModel sell = new PromoterSellModel();
        sell.setGameId(p.getGameId());
        sell.setGameUserId(gameUserId);
        sell.setPromoterId(p.getId());
        sell.setSellNum(sellNum);
        sell.setSellAfter(CommonUtils.getIntValue(sellAfter));
        sell.setIsSuccess(false);
        sell.setGameNickName(ClubUserDao.queryByGameIdAndGameUserId(p.getGameId(), gameUserId).getGameNickName());
        sell.setCreateTime(new Date());
        sell.setCreateDate(new Date());

        PromoterSellDao.add(sell);
        if (sell.getId() > 0) {
            int result = UUserPointDao.updateGameCardByUserId(db, sellNum, gameUserId);
            if (result > 0) {
                //更新钻石到帐结果
                PromoterSellModel suc = new PromoterSellModel();
                suc.setId(sell.getId());
                suc.setIsSuccess(true);
                PromoterSellDao.update(suc);

                //设置提示信息
                msg.setSuccess(true);
                msg.setRemark("充值成功");

                //记录钻石日志
                LogGamecardModel log = new LogGamecardModel();
                log.setGameId(p.getGameId());
                log.setPromoterId(p.getId());
                log.setNickName(p.getNickName());
                log.setSource(GamecardSource.SELLCARD.getType());
                log.setChangeNum(-sellNum);
                log.setChangeBefore((int) p.getGameCard());
                log.setChangeAfter((int) (p.getGameCard() - sellNum));
                log.setCreateTime(new Date());
                log.setCreateDate(new Date());
                LogGamecardDao.add(log);
                //通知游戏刷新钻石
                String url = "http://" + db.getWebDomain() + ":" + db.getWebPort() + GameApi.ADD_POINT + "&id=" + gameUserId + "&sum=" + sellNum;
                try {
                    HttpUtils.get(url);
                } catch (Exception e) {
                }
            }
        } else {
            msg.setRemark("充值失败，请联系客服");
        }

        return msg;
    }

    public MsgVO sellAll(long promoterId, int sellNum, long gameUserId) {
        MsgVO msg = new MsgVO();
        if (sellNum <= 0) {
            msg.setRemark("出售钻石数目必须大于0");
            return msg;
        }
        /**
         * 1.判断钻石是否足够
         * 2.扣除代理商钻石
         * 3.给玩家加钻石
         * 4.成功后更新订单状态
         */
        PromoterModel p = PromoterDao.getOne(promoterId);
        if (p == null) {
            msg.setRemark("查询不到用户");
            return msg;
        }

        long sellAfter = p.getGameCard() - sellNum;
        if (sellAfter < 0) {
            msg.setRemark("代理商钻石不足");
            return msg;
        }

        int gameId = p.getGameId();
        DictClubModel dictClubModel = DictClubDao.getByGameId(gameId);
        boolean isAllowSell = false;
        if (dictClubModel != null) {
            isAllowSell = dictClubModel.getIsAllowSell();
        }

        if (isAllowSell == false) {
            msg.setRemark("游戏未开放该功能");
            return msg;
        }

        DictGameDbModel db = DictGameDbDao.getByGameId(gameId, true);
        if (db == null) {
            msg.setRemark("游戏配置错误");
            return msg;
        }
        DataTable userInfo = UUserInfoDao.getByUserId(db, gameUserId);
        if (userInfo.rows.length <= 0) {
            msg.setRemark("查询不到该玩家");
            return msg;
        }

        PromoterSellModel sell = new PromoterSellModel();
        sell.setGameId(gameId);
        sell.setGameUserId(gameUserId);
        sell.setPromoterId(p.getId());
        sell.setSellNum(sellNum);
        sell.setSellAfter(CommonUtils.getIntValue(sellAfter));
        sell.setIsSuccess(false);
        sell.setGameNickName(userInfo.rows[0].getColumnValue("nickName"));
        sell.setCreateTime(new Date());
        sell.setCreateDate(new Date());

        PromoterSellDao.add(sell);
        if (sell.getId() > 0) {

            int result = UUserPointDao.updateGameCardByUserId(db, sellNum, gameUserId);
            if (result > 0) {
                //更新钻石到帐结果
                PromoterSellModel suc = new PromoterSellModel();
                suc.setId(sell.getId());
                suc.setIsSuccess(true);
                PromoterSellDao.update(suc);

                //设置提示信息
                msg.setSuccess(true);
                msg.setRemark("充值成功");

                //记录钻石日志
                LogGamecardModel log = new LogGamecardModel();
                log.setGameId(p.getGameId());
                log.setPromoterId(p.getId());
                log.setNickName(p.getNickName());
                log.setSource(GamecardSource.SELLCARD.getType());
                log.setChangeNum(-sellNum);
                log.setChangeBefore((int) p.getGameCard());
                log.setChangeAfter((int) (p.getGameCard() - sellNum));
                log.setCreateTime(new Date());
                log.setCreateDate(new Date());
                LogGamecardDao.add(log);
                //通知游戏刷新钻石
                String url = "http://" + db.getWebDomain() + ":" + db.getWebPort() + GameApi.ADD_POINT + "&id=" + gameUserId + "&sum=" + sellNum;
                try {
                    HttpUtils.get(url);
                } catch (Exception e) {
                }
            }

        } else {
            msg.setRemark("充值失败，请联系客服");
        }

        return msg;
    }

}
