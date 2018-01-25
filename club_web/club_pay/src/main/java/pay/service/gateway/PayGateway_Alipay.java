package pay.service.gateway;


import com.google.common.base.Strings;
import dsqp.db_club.model.PromoterPayModel;
import dsqp.db_club_dict.model.DictPayGatewayModel;
import dsqp.util.*;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import pay.util.alipay.config.AlipayConfig;
import pay.util.alipay.util.AlipayNotify;
import pay.util.alipay.util.AlipaySubmit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aris on 2017/3/10.
 */
class PayGateway_Alipay implements PayGateway {

    @Override
    public void showPayInfo(HttpServletRequest request, Model model, DictPayGatewayModel payGatewayModel, PromoterPayModel payModel) {
        boolean isWeiXin = OsUtils.isWeixin(request);
        //把请求参数打包成数组
        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("service", AlipayConfig.service);
        sParaTemp.put("partner", payGatewayModel.getMchId());
        sParaTemp.put("seller_id", payGatewayModel.getMchId());
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
        sParaTemp.put("payment_type", AlipayConfig.payment_type);
        sParaTemp.put("notify_url", payGatewayModel.getNotifyUrl());
        sParaTemp.put("return_url", payGatewayModel.getReturnUrl());
        sParaTemp.put("out_trade_no", payModel.getOrderId());//商户订单号
        sParaTemp.put("subject", "钻石");
        sParaTemp.put("total_fee", String.valueOf(payModel.getPrice()));
        sParaTemp.put("show_url", "");
        sParaTemp.put("app_pay","Y");//启用此参数可唤起钱包APP支付。
        sParaTemp.put("body", "");
        //其他业务参数根据在线开发文档，添加参数.文档地址:https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.2Z6TSk&treeId=60&articleId=103693&docType=1
        //如sParaTemp.put("参数名","参数值");

        //建立请求
        String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,payGatewayModel.getAppKey());

        model.addAttribute("payInfoUrl",sHtmlText);
        model.addAttribute("isWeiXin",isWeiXin);
    }

    @Override
    public Map  getNotifyParams(HttpServletRequest request) {
        String para = EncodingUtils.urlDecode(CommonUtils.getStringValue(request.getAttribute("para")));
        if (Strings.isNullOrEmpty(para)){
            return null;
        }
        Map<String,String> requestMap = SignUtil.getStringToMap(para);

        if (CollectionUtils.isEmpty(requestMap)){
            return null;
        }

        return requestMap;
    }

    @Override
    public String getOrderId(Map notifyMap) {
        return CommonUtils.getStringValue(notifyMap.get("out_trade_no"));
    }

    @Override
    public String getGatewayOrderId(Map notifyMap) {
        return CommonUtils.getStringValue(notifyMap.get("trade_no"));
    }

    @Override
    public double getTotalFee(Map notifyMap) {
        return CommonUtils.getDoubleValue(notifyMap.get("total_fee")) * 100;
    }

    @Override
    public boolean validateNotify(Map notifyMap, DictPayGatewayModel payGatewayModel) {
        //交易状态
        String trade_status = CommonUtils.getStringValue(notifyMap.get("trade_status"));
        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
        if(AlipayNotify.verify(notifyMap,payGatewayModel.getMchId(),payGatewayModel.getAppKey())){
            //验证成功
            if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
                return true;
            }
        }

        return false;
    }

    @Override
    public void validateSync(Map notifyMap, DictPayGatewayModel payGatewayModel, HttpServletResponse response) {
//        RequestUtils.redirect(response, payGatewayModel.getSuccessUrl());
        RequestUtils.write(response, "支付成功，请返回代理商后台查询相关信息");
    }

    @Override
    public String notifySuccess() {
        return "success";
    }

    @Override
    public String notifyFail(String error) {
        return error;
    }
}
