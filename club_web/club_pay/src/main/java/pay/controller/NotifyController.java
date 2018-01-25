package pay.controller;

import com.google.common.base.Strings;
import dsqp.common_const.club.GamecardSource;
import dsqp.db_club.dao.PromoterDao;
import dsqp.db_club.model.PromoterModel;
import dsqp.db_club.model.PromoterPayModel;
import dsqp.db_club_dict.model.DictPayGatewayModel;
import dsqp.db_club_log.dao.LogGamecardDao;
import dsqp.db_club_log.model.LogGamecardModel;
import dsqp.util.RequestUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pay.service.LogService;
import pay.service.PayService;
import pay.service.gateway.PayGateway;
import pay.service.gateway.PayGatewayFactory;

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
    private PayService payService;
    @Autowired
    private LogService logService;

    @RequestMapping(value = "/{className}/notify")
    public void Notify(HttpServletRequest request, HttpServletResponse response
        , @PathVariable("className") String className){
        long logID = logService.addRequestLog(request);

        if (Strings.isNullOrEmpty(className)){
            logService.updateLog(logID,"支付类名缺失");
            RequestUtils.write(response,"fail");
            return;
        }

        //根据className生成具体的支付网关
        PayGateway payGateway = PayGatewayFactory.create(className);
        if (payGateway == null){
            fail(response,logID,payGateway.notifyFail("初始化失败"));
            return;
        }

        Map<String,String> notifyMap = payGateway.getNotifyParams(request);
        if (MapUtils.isEmpty(notifyMap)){
            fail(response,logID,payGateway.notifyFail("获取参数失败"));
            return;
        }
        //获取支付网关配置信息
        DictPayGatewayModel dictPayGateway = dsqp.db_club_dict.dao.DictPayGatewayDao.getByClassName(className);
        boolean isSuccess = payGateway.validateNotify(notifyMap,dictPayGateway);
        if (!isSuccess){
            fail(response,logID,payGateway.notifyFail("参数校验失败"));
            return;
        }
        //获取我们的自己的订单号
        String orderId = payGateway.getOrderId(notifyMap);
        if (Strings.isNullOrEmpty(orderId)){
            fail(response,logID,payGateway.notifyFail("商户订单号缺失"));
            return;
        }
        //查询订单信息
        PromoterPayModel order = dsqp.db_club.dao.PromoterPayDao.getByOrderId(orderId);
        if (order == null){
            fail(response, logID,payGateway.notifyFail("订单不存在"));
            return;
        }
        //订单已经设置成功,就返回成功
        if (order.getIsSuccess() == true){
            success(response,logID,payGateway.notifySuccess());
            return;
        }
        //获取玩家信息
        PromoterModel p = PromoterDao.getOne(order.getPromoterId());
        if(p == null){
            fail(response, logID, payGateway.notifyFail("玩家信息缺失"));
            return;
        }

        //获取渠道订单号
        String gatewayOrderId = payGateway.getGatewayOrderId(notifyMap);
        if (Strings.isNullOrEmpty(gatewayOrderId)){
            fail(response, logID,payGateway.notifyFail("支付网关订单号缺失"));
            return;
        }
        //用户付款金额
        double totalFee = payGateway.getTotalFee(notifyMap);

        //开始设置订单成功
        boolean isOrderSuccess = payService.setOrderSuccess(order, totalFee, gatewayOrderId, p);
        if (isOrderSuccess){
            success(response,logID,payGateway.notifySuccess());
            Date date = new Date();
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
            fail(response,logID,payGateway.notifyFail("设置订单失败"));
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
