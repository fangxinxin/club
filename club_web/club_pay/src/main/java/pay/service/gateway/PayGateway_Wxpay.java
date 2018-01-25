package pay.service.gateway;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import dsqp.db_club.model.PromoterPayModel;
import dsqp.db_club_dict.model.DictPayGatewayModel;
import dsqp.util.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Aris on 2017/3/10.
 */
class PayGateway_Wxpay implements PayGateway {
    //商户系统先调用该接口在微信支付服务后台生成预支付交易单
    public static final String CREATE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    //网页授权
    public static final String AUTHORIZE_URL    = "https://open.weixin.qq.com/connect/oauth2/authorize";

    private static final String NATIVE = "NATIVE";
    private static final String JSAPI  = "JSAPI";
    private static final String MWEB   = "MWEB";

    private static final String SCENE_INFO = "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"%s\",\"wap_name\": \"腾讯充值\"}}";
    @Override
    public void showPayInfo(HttpServletRequest request, Model model, DictPayGatewayModel payGatewayModel, PromoterPayModel payModel) {

        Map<String,String> requestMap = Maps.newHashMap();
        boolean isWeiXin = OsUtils.isWeixin(request);
        //是否是微信浏览器
        model.addAttribute("isWeiXin",isWeiXin);

        requestMap.put("appid",            payGatewayModel.getAppId());
        requestMap.put("mch_id",           payGatewayModel.getMchId());
        requestMap.put("nonce_str",        RndUtils.getRandString());
        requestMap.put("sign",             "");
        requestMap.put("body",             "北京大圣掌游-钻石充值");
        requestMap.put("out_trade_no",     payModel.getOrderId());
        requestMap.put("total_fee",        String.valueOf( (int) (payModel.getPrice() * 100) ));
        requestMap.put("spbill_create_ip", request.getRemoteAddr());
        requestMap.put("notify_url",       payGatewayModel.getNotifyUrl());

        if (isWeiXin){
            Map<String,String> userMap = RequestUtils.getMapFromRequest(request);
            if (MapUtils.isEmpty(userMap)){
                return;
            }

            String openId = CommonUtils.getStringValue(userMap.get("openId"));
            if (Strings.isNullOrEmpty(openId)){
                return;
            }

            requestMap.put("openid",openId);
            requestMap.put("trade_type",JSAPI);
        }else{
//            requestMap.put("scene_info", payGatewayModel.getSuccessUrl());
//            requestMap.put("trade_type", MWEB);
            requestMap.put("trade_type", NATIVE);
        }

        //生产签名
        String signText = SignUtil.getSignText(requestMap) + "&key=" + payGatewayModel.getAppKey();
        String sign = DigestUtils.md5Hex(signText).toUpperCase();
        //将签名放入请求的map中
        requestMap.put("sign",sign);
        String result = "";
        try {
            result = HttpUtils.postXML(CREATE_ORDER_URL, XMLUtil.mapToXML(requestMap));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Strings.isNullOrEmpty(result)) {
            return;
        }

        Map<String,String> map = XMLUtil.xmlToMap(result);
        if (MapUtils.isEmpty(map)) {
            return;
        }

        //如果为错误就直接返回
        if (!map.get("return_code").equalsIgnoreCase("SUCCESS")){
            return;
        }

        if (!map.get("result_code").equalsIgnoreCase("SUCCESS")){
            return;
        }

        if (isWeiXin){
            String prepay_id = CommonUtils.getStringValue(map.get("prepay_id"));
            if (Strings.isNullOrEmpty(prepay_id)){
                return;
            }

            Map<String,String> wxMap = Maps.newHashMap();
            wxMap.put("appId",payGatewayModel.getAppId());
            wxMap.put("timeStamp",String.valueOf(System.currentTimeMillis() / 1000));
            wxMap.put("nonceStr", RndUtils.getRandString());
            wxMap.put("package","prepay_id=" + prepay_id);
            wxMap.put("signType","MD5");
            //生产签名
            String paySignText = SignUtil.getSignText(wxMap) + "&key=" + payGatewayModel.getAppKey();
            String paySign = DigestUtils.md5Hex(paySignText).toUpperCase();
            //将签名放入请求的map中
            wxMap.put("paySign",paySign);

            wxMap.put("pkg","prepay_id=" + prepay_id);//这里传package貌似el拿不到
            model.addAllAttributes(wxMap);
        }else{
            String code_url = CommonUtils.getStringValue(map.get("code_url"));
            if (Strings.isNullOrEmpty(code_url)){
                return;
            }
            model.addAttribute("code_url",code_url);
        }

    }

    @Override
    public Map getNotifyParams(HttpServletRequest request) {
        String para = CommonUtils.getStringValue(request.getAttribute("para"));
        if (Strings.isNullOrEmpty(para)){
            return null;
        }

        Map<String,String> requestMap = XMLUtil.xmlToMap(para);
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
        return CommonUtils.getStringValue(notifyMap.get("transaction_id"));
    }

    @Override
    public double getTotalFee(Map notifyMap) {
        return CommonUtils.getDoubleValue(notifyMap.get("total_fee"));
    }

    @Override
    public boolean validateNotify(Map notifyMap,DictPayGatewayModel payGatewayModel) {
        String return_code = CommonUtils.getStringValue(notifyMap.get("return_code"));
        //如果失败就直接返回
        if (!return_code.equalsIgnoreCase("SUCCESS")){
            return false;
        }

        //支付设置订单成功的逻辑
        String sign = CommonUtils.getStringValue(notifyMap.get("sign"));
        String generateSign = DigestUtils.md5Hex(SignUtil.getSignText(notifyMap) + "&key=" + payGatewayModel.getAppKey()).toUpperCase();
        if (!sign.equalsIgnoreCase(generateSign)){
            return false;
        }

        return true;
    }

    @Override
    public void validateSync(Map notifyMap, DictPayGatewayModel payGatewayModel, HttpServletResponse response) {

    }

    @Override
    public String notifySuccess() {
        Map<String,String> responseMap = Maps.newHashMap();
        responseMap.put("return_code","<![CDATA[SUCCESS]]>");
        responseMap.put("return_msg","<![CDATA[SUCCESS]]>");
        return  XMLUtil.mapToXML(responseMap);
    }

    @Override
    public String notifyFail(String error) {
        Map<String,String> responseMap = Maps.newHashMap();
        responseMap.put("return_code","<![CDATA[FAIL]]>");
        responseMap.put("return_msg","<![CDATA[" + error + "]]>");
        return XMLUtil.mapToXML(responseMap);
    }

}
