package club.controller.buy;

import club.service.BonusService;
import club.service.LogService;
import club.service.PayService;
import club.service.WxPayService;
import com.google.common.base.Strings;
import dsqp.common_const.club.GamecardSource;
import dsqp.common_const.club.PromoterLevel;
import dsqp.db_club.dao.PromoterDao;
import dsqp.db_club.dao.PromoterPayDao;
import dsqp.db_club.model.PromoterModel;
import dsqp.db_club.model.PromoterPayModel;
import dsqp.db_club_dict.dao.DictBonusDao;
import dsqp.db_club_dict.dao.DictPayGatewayDao;
import dsqp.db_club_dict.model.DictBonusModel;
import dsqp.db_club_dict.model.DictPayGatewayModel;
import dsqp.db_club_log.dao.LogGamecardDao;
import dsqp.db_club_log.dao.LogNotifyDao;
import dsqp.db_club_log.dao.LogRebateDao;
import dsqp.db_club_log.model.LogGamecardModel;
import dsqp.db_club_log.model.LogRebateModel;
import dsqp.util.RequestUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 异步通知URL
 * Created by lian123 on 2016/11/16.
 */
@Controller
public class NotifyController {

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private PayService payService;

    @Autowired
    private LogService logService;

    @Autowired
    private BonusService bonusService;

    @RequestMapping(value = "/{className}/notify")
    public void Notify(HttpServletRequest request, HttpServletResponse response
        , @PathVariable("className") String className){
        long logID = logService.addRequestLog(request);

        if (Strings.isNullOrEmpty(className)){
            LogNotifyDao.update(logID,"支付类名缺失");
            RequestUtils.write(response,"fail");
            return;
        }

        //获取支付网关配置信息
        DictPayGatewayModel dictPayGateway = DictPayGatewayDao.getByClassName(className);
        if (dictPayGateway == null){
            fail(response, logID, "获取配置信息失败");
            return;
        }

        Map<String,String> notifyMap = wxPayService.getNotifyParams(request);
        if (MapUtils.isEmpty(notifyMap)){
            fail(response, logID, wxPayService.notifyFail("获取参数失败"));
            return;
        }

        boolean isSuccess = wxPayService.validateNotify(notifyMap,dictPayGateway);
        if (!isSuccess){
            fail(response, logID, wxPayService.notifyFail("参数校验失败"));
            return;
        }

        //获取我们的自己的订单号
        String orderId = wxPayService.getOrderId(notifyMap);
        if (Strings.isNullOrEmpty(orderId)){
            fail(response, logID, wxPayService.notifyFail("商户订单号缺失"));
            return;
        }

        //查询订单信息
        PromoterPayModel order = PromoterPayDao. getByOrderId(orderId);
        if (order == null){
            fail(response, logID, wxPayService.notifyFail("订单不存在"));
            return;
        }
        //订单已经设置成功,就返回成功
        if (order.getIsSuccess()){
            success(response, logID, wxPayService.notifySuccess());
            return;
        }

        String gatewayOrderId = wxPayService.getGatewayOrderId(notifyMap);
        if (Strings.isNullOrEmpty(gatewayOrderId)){
            fail(response, logID, wxPayService.notifyFail("支付网关订单号缺失"));
            return;
        }

        //用户付款金额
        double totalFee = wxPayService.getTotalFee(notifyMap);

        PromoterModel p = PromoterDao.getOne(order.getPromoterId());
        if(p == null){
            fail(response, logID, wxPayService.notifyFail("找不到用户"));
            return;
        }

        //计算出返利
        int rebateDiamond = 0;
        //更新代理商
        DictBonusModel dictBonus = DictBonusDao.getByGameId(order.getGameId());
        //如果没有配置，启用默认配置
        double levelSuperPercent = dictBonus == null ? 0.15 : dictBonus.getNonDirectPercent();  //特级
        double levelOnePercent   = dictBonus == null ? 0.10 : dictBonus.getDirectPercent();     //1级
        if (order.getpLevel() == PromoterLevel.SUPER) {
            rebateDiamond = (int) (order.getGoodNum() * levelSuperPercent);
        }

        if (order.getpLevel() == PromoterLevel.FIRST) {
            rebateDiamond = (int) (order.getGoodNum() * levelOnePercent);
        }

        //开始设置订单成功
        boolean isOrderSuccess = payService.setOrderSuccess(order, totalFee,gatewayOrderId, rebateDiamond);
        if (isOrderSuccess){
            Date date = new Date();
            //返回订单成功
            success(response, logID, wxPayService.notifySuccess());
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
            log.setChangeBefore(p.getGameCard());
            log.setChangeAfter(p.getGameCard() + order.getGoodGivingNum() + order.getGoodNum());
            log.setCreateTime(date);
            log.setCreateDate(date);
            LogGamecardDao.add(log);
        }else {
            fail(response, logID, wxPayService.notifyFail("设置订单失败"));
        }
    }

    /**------------------------------------私有方法-----------------------------------------**/
    private void fail(HttpServletResponse response,long logID,String msg){
        logService.updateLog(logID,msg);
        RequestUtils.write(response,msg);
    }

    private void success(HttpServletResponse response,long logID,String msg){
        logService.updateLog(logID,msg);
        RequestUtils.write(response,msg);
    }
}
