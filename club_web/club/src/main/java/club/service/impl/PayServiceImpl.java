package club.service.impl;

import club.service.PayService;
import club.vo.PaginationVO;
import dsqp.db.model.SplitPage;
import dsqp.db.util.DataTableUtils;
import dsqp.db_club.dao.DayBonusParentDao;
import dsqp.db_club.dao.PromoterDao;
import dsqp.db_club.dao.PromoterPayDao;
import dsqp.db_club.model.PromoterPayModel;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Aris on 2017/7/26.
 */
@Service
public class PayServiceImpl implements PayService {

//    @Override
//    public MsgVO buy(long promoterId, int goodId) {
//        MsgVO msg = new MsgVO();
//
//        /**
//         * 1.判断账户余额是否足够
//         * 3.给用户加钻石
//         * 4.成功后更新订单状态
//         */
//        PromoterModel p = PromoterDao.getOne(promoterId);                   //获取当前账户信息
//        if (p == null) {
//            msg.setRemark("查询不到用户");
//            return msg;
//        }
//
//        DictGoodPriceModel g = DictGoodPriceDao.getOne(goodId);         //商品信息
//        if (p.getDeposit() - (g.getCashPrice() * g.getNonCashDiscount()) < 0) {
//            msg.setRemark("账户余额不足!");
//            return msg;
//        }
//
//        PromoterPayModel pay = new PromoterPayModel();
//        pay.setPromoterId(p.getId());
//        pay.setpLevel(p.getpLevel());
//        pay.setGameId(p.getGameId());
//        pay.setNickName(p.getNickName());
//        pay.setParentId(p.getParentId());
////        pay.setPayType(PayType.CASHPAY.getType());
//        pay.setIsSuccess(true);
//        pay.setPrice(g.getCashPrice() * g.getNonCashDiscount());
//        pay.setGoodNum(g.getGoodNum());
//        pay.setGoodGivingNum(g.getGiftNum());
//        pay.setCreateTime(new Date());
//        pay.setCreateDate(new Date());
//        pay.setOrderId(System.currentTimeMillis() + "" + p.getId());
//        PromoterPayDao.payByDeposit(pay);
//        if (pay.getId() > 0) {
//            Date now = new Date();
//            //设置提示信息
//            msg.setSuccess(true);
//            msg.setRemark("充值成功");
//
//            //记录钻石日志
//            LogGamecardModel log = new LogGamecardModel();
//            log.setGameId(p.getGameId());
//            log.setPromoterId(p.getId());
//            log.setNickName(p.getNickName());
//            log.setSource(GamecardSource.BUYCARD.getType());
//            log.setChangeNum(g.getGoodNum() + g.getGiftNum());
//            log.setChangeBefore(p.getGameCard());
//            log.setChangeAfter((p.getGameCard() + g.getGoodNum() + g.getGiftNum()));
//            log.setCreateTime(now);
//            log.setCreateDate(now);
//            LogGamecardDao.add(log);
//        } else {
//            msg.setRemark("充值失败，请联系客服");
//        }
//
//        return msg;
//    }

    @Override
    public PaginationVO getTotalPayByDate(long promoterId, int pLevel, Date startDate, Date endDate, int pageNum, int pageSize) {
        PaginationVO vo = new PaginationVO();
        SplitPage page = DayBonusParentDao.getBonusByPage(promoterId, startDate, endDate, pageNum, pageSize);

        if (page != null) {
            vo.setRows(DataTableUtils.DataTable2JsonArray(page.getPageDate()));
            vo.setTotal(String.valueOf(page.getTotalRecords()));
        }
        return vo;
    }

    @Override
    public boolean setOrderSuccess(PromoterPayModel order, double totalFee, String payOrderId, int rebate) {
        //校验金额，以分为单位
        int price = (int) (order.getPrice() * 100);//将元转换为分
        //只要支付金额大于我们的订单金额，都认为他正确
        if (price > totalFee) {
            return false;
        }

        //更新订单
        if (PromoterPayDao.setOrderSuccess(order.getId(), payOrderId) != 1) {
            //如果更新失败，检测订单状态是否成功，返回成功信息给支付平台
            if (PromoterPayDao.getOne(order.getId()).getIsSuccess()) {
                return true;
            } else {
                return false;
            }
        }

        if (PromoterDao.updateRebateForPay(order.getPromoterId(), order.getGoodNum() + order.getGoodGivingNum(), rebate, order.getPrice()) > 0) {
            return true;
        } else {
            return false;
        }
    }
}
