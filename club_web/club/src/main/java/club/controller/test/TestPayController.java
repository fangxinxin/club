package club.controller.test;

import club.service.BonusService;
import club.service.PayService;
import com.google.common.base.Strings;
import dsqp.common_const.club.GamecardSource;
import dsqp.common_const.club.PayType;
import dsqp.common_const.club.PromoterLevel;
import dsqp.db_club.dao.PromoterDao;
import dsqp.db_club.dao.PromoterPayDao;
import dsqp.db_club.model.PromoterModel;
import dsqp.db_club.model.PromoterPayModel;
import dsqp.db_club_dict.dao.DictBonusDao;
import dsqp.db_club_dict.model.DictBonusModel;
import dsqp.db_club_log.dao.LogGamecardDao;
import dsqp.db_club_log.dao.LogRebateDao;
import dsqp.db_club_log.model.LogGamecardModel;
import dsqp.db_club_log.model.LogRebateModel;
import dsqp.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * Created by Aris on 2017/8/15.
 */
@Controller
@RequestMapping("test")
public class TestPayController {
    private static final String PATH = "test/";

    @Autowired
    private PayService payService;

    @Autowired
    private BonusService bonusService;

    @RequestMapping(value = "pay", method = RequestMethod.GET)
    public String pay(){


        return PATH + "pay";
    }

    @RequestMapping(value = "pay", method = RequestMethod.POST)
    public void pay(@RequestParam(value = "promoterId" , defaultValue = "0") long promoterId
            ,@RequestParam(value = "createTime", defaultValue = "")String createTime
            ,@RequestParam(value = "price", defaultValue = "0")double price
            ,@RequestParam(value = "goodNum", defaultValue = "0")int goodNum){

        if (promoterId == 0)    return;
        if (Strings.isNullOrEmpty(createTime))    return;
        if (price == 0)    return;
        if (goodNum == 0)    return;

        PromoterModel promoterModel = PromoterDao.getOne(promoterId);
        if (promoterModel == null)    return;

        Date d = DateUtils.String2DateTime(createTime);
        String orderId = System.currentTimeMillis() + "" + promoterModel.getId();
        PromoterPayModel order = new PromoterPayModel();
        order.setGameId(promoterModel.getGameId());
        order.setPromoterId(promoterModel.getId());
        order.setpLevel(promoterModel.getpLevel());
        order.setNickName(promoterModel.getNickName());
        order.setParentId(promoterModel.getParentId());
        order.setOrderId(orderId);
        order.setPayType(PayType.WXPAY.getType());
        order.setPrice(price);
        order.setGoodNum(goodNum);
        order.setGoodGivingNum(0);
        order.setIsSuccess(true);
        order.setCreateTime(d);
        order.setCreateDate(d);
        PromoterPayDao.add(order);

        int rebateDiamond = 0;
        if (order.getpLevel() == PromoterLevel.SUPER || order.getpLevel() == PromoterLevel.FIRST) {
            //更新代理商
            DictBonusModel dictBonus = DictBonusDao.getByGameId(order.getGameId());
            //如果没有配置，启用默认配置
            double levelSuperPercent = dictBonus == null ? 0.15  : dictBonus.getNonDirectPercent();    //特级
            double levelOnePercent   = dictBonus == null ? 0.1   : dictBonus.getDirectPercent();       //1级

            if (order.getpLevel() == PromoterLevel.SUPER) {
                rebateDiamond = (int) (order.getGoodNum() * levelSuperPercent);
            }

            if (order.getpLevel() == PromoterLevel.FIRST) {
                rebateDiamond = (int) (order.getGoodNum() * levelOnePercent);
            }
        }

        PromoterDao.updateRebateForPay(order.getPromoterId(), order.getGoodNum() + order.getGoodGivingNum(), rebateDiamond, order.getPrice());
        Date date = new Date();
        //记录返利信息
        LogRebateModel rebate = new LogRebateModel();
        rebate.setGameId(order.getGameId());
        rebate.setPayId(order.getId());
        rebate.setPromoterId(order.getPromoterId());
        rebate.setpLevel(order.getpLevel());
        rebate.setDiamond(order.getGoodNum());
        rebate.setRebateDiamond(rebateDiamond);
        rebate.setCreateDate(date);
        rebate.setCreateTime(date);
        LogRebateDao.add(rebate);

        //记录钻石日志
        LogGamecardModel log = new LogGamecardModel();
        log.setGameId(order.getGameId());
        log.setPromoterId(order.getPromoterId());
        log.setNickName(order.getNickName());
        log.setSource(GamecardSource.BUYCARD.getType());
        log.setChangeNum(order.getGoodNum() + order.getGoodGivingNum());
        log.setChangeBefore(promoterModel.getGameCard());
        log.setChangeAfter(promoterModel.getGameCard() + order.getGoodGivingNum() + order.getGoodNum());
        log.setCreateTime(date);
        log.setCreateDate(date);
        LogGamecardDao.add(log);
        return;
    }
}
