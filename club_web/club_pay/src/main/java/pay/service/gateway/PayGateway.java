package pay.service.gateway;


import dsqp.db_club.model.PromoterPayModel;
import dsqp.db_club_dict.model.DictPayGatewayModel;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Aris on 2017/3/10.
 */
public interface PayGateway {
    //显示钻石购买信息
    void showPayInfo(HttpServletRequest request, Model model, DictPayGatewayModel payGatewayModel, PromoterPayModel payModel);

//    boolean isNeedRedirect(HttpServletRequest request, DictPayGatewayModel payGatewayModel, PromoterPayModel payModel);

    //获取支付回调参数
    Map getNotifyParams(HttpServletRequest request);

    //获取订单号
    String getOrderId(Map notifyMap);

    //获取支付网关订单号
    String getGatewayOrderId(Map notifyMap);

    //获取支付金额
    double getTotalFee(Map notifyMap);

    //验证异步支付通知
    boolean validateNotify(Map notifyMap, DictPayGatewayModel payGatewayModel);

    //验证同步支付通知
    void validateSync(Map notifyMap, DictPayGatewayModel payGatewayModel, HttpServletResponse response);

    //异步校验的成功通知
    String notifySuccess();

    //异步校验的失败通知
    String notifyFail(String error);
}
