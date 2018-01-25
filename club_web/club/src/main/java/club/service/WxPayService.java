package club.service;

import dsqp.db_club.model.PromoterPayModel;
import dsqp.db_club_dict.model.DictPayGatewayModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 公众号支付相关方法
 * Created by Aris on 2017/7/27.
 */
public interface WxPayService {
    //展示购买信息
    Map<String,String> showPayInfo(HttpServletRequest request, DictPayGatewayModel payGateway, PromoterPayModel order);

    //获取支付回调参数
    Map getNotifyParams(HttpServletRequest request);

    //获取订单号
    String getOrderId(Map notifyMap);

    //获取支付网关订单号
    String getGatewayOrderId(Map notifyMap);

    //获取支付金额
    double getTotalFee(Map notifyMap);

    //验证异步支付通知
    boolean validateNotify(Map notifyMap,DictPayGatewayModel payGateway);

    //验证同步支付通知
    void validateSync(Map notifyMap, DictPayGatewayModel payGateway, HttpServletResponse response);

    //异步校验的成功通知
    String notifySuccess();

    //异步校验的失败通知
    String notifyFail(String error);
}
