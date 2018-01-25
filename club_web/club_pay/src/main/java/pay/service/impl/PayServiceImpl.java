package pay.service.impl;

import dsqp.common_const.club.PromoterLevel;
import dsqp.db_club.dao.PromoterDao;
import dsqp.db_club.dao.PromoterPayDao;
import dsqp.db_club.model.BonusDetailModel;
import dsqp.db_club.model.PromoterModel;
import dsqp.db_club.model.PromoterPayModel;
import dsqp.db_club_dict.dao.DictBonusDao;
import dsqp.db_club_dict.model.DictBonusModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pay.service.PayService;

/**
 * Created by Aris on 2016/11/17.
 */
@Service
public class PayServiceImpl implements PayService {

    @Autowired
    private pay.dao.PromoterPayDao payDao;
    @Autowired
    private pay.dao.PromoterDao userDao;
    @Autowired
    private pay.dao.BonusDetailDao bonusDao;

    @Override
    public boolean setOrderSuccess(PromoterPayModel order, double totalFee, String payOrderId, PromoterModel promoter) {
        //校验金额，以分为单位
        int price = (int) (order.getPrice() * 100);//将元转换为分
        //只要支付金额大于我们的订单金额，都认为他正确
        if (price > totalFee) {
            return false;
        }

        //更新订单
        if (payDao.updateSuccessById(payOrderId, order.getId()) != 1) {
            //如果更新失败，检测订单状态是否成功，返回成功信息给支付平台
            if (PromoterPayDao.getOne(order.getId()).getIsSuccess()) {
                return true;
            } else {
                return false;
            }
        }

        int gameCard = order.getGoodNum() + order.getGoodGivingNum();
        PromoterModel parent = PromoterDao.getOne(promoter.getParentId());
        if (parent != null) {
            //计算出返利
            double rebatePercent = 0;
            //更新代理商
            DictBonusModel dictBonus = DictBonusDao.getByGameId(order.getGameId());
            //如果没有配置，启用默认配置
            /**
             * 1. 特级可以吃15%
             */
            double levelOnePercent   = dictBonus == null ? 0.10 : dictBonus.getDirectPercent();     //1级
            double levelSuperPercent = dictBonus == null ? 0.15 : dictBonus.getNonDirectPercent();  //特级
            if (parent.getpLevel() == PromoterLevel.FIRST) {
                rebatePercent = levelOnePercent;
            }
            if (parent.getpLevel() == PromoterLevel.SUPER) {
                rebatePercent = levelSuperPercent;
            }

            int rebate = (int) (order.getGoodNum() * rebatePercent);
            //添加返利记录信息
            BonusDetailModel model = new BonusDetailModel();
            model.setGameId(order.getGameId());
            model.setPayId(order.getId());
            model.setpLevel(order.getpLevel());
            model.setPromoterId(parent.getId());       //上级id，谁收返利
            model.setFromPromoterId(promoter.getId()); //下级id，返利来自谁
            model.setDiamond(order.getGoodNum());
            model.setRebateDiamond(rebate);
            model.setRebatePercent(rebatePercent);
            bonusDao.add(model);

            //给代理加返利
            userDao.updateForRebate(rebate, parent.getId());
        }

        if (userDao.updateForPay(gameCard, order.getPrice(), order.getPromoterId()) > 0) {
            return true;
        } else {
            return false;
        }
    }

}
