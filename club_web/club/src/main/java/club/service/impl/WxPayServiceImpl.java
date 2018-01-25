package club.service.impl;

import club.service.WxPayService;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import dsqp.db_club.model.PromoterPayModel;
import dsqp.db_club_dict.model.DictPayGatewayModel;
import dsqp.util.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Aris on 2017/7/27.
 */
@Service
public class WxPayServiceImpl implements WxPayService{

    //商户系统先调用该接口在微信支付服务后台生成预支付交易单
    public static final String CREATE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    //查询订单连接
    public static final String SEARCH_ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery";

    private static final String JSAPI  = "JSAPI";

    public Map<String,String> showPayInfo(HttpServletRequest request, DictPayGatewayModel payGateway, PromoterPayModel order) {
        Map<String,String> requestMap = Maps.newHashMap();

        requestMap.put("appid",             payGateway.getAppId());
        requestMap.put("mch_id",            payGateway.getMchId());
        requestMap.put("nonce_str",         RndUtils.getRandString());
        requestMap.put("sign",              "");
        requestMap.put("body",              "北京大圣掌游-钻石充值");
        requestMap.put("out_trade_no",      order.getOrderId());
        requestMap.put("total_fee",         String.valueOf( (int) (order.getPrice() * 100) ));
        requestMap.put("spbill_create_ip",  request.getRemoteAddr());
        requestMap.put("notify_url",        payGateway.getNotifyUrl());

        Map<String,String> userMap = RequestUtils.getMapFromRequest(request);
        if (MapUtils.isEmpty(userMap)){
            return null;
        }

        String openId = CommonUtils.getStringValue(userMap.get("openId"));
        if (Strings.isNullOrEmpty(openId)){
            return null;
        }

        requestMap.put("openid",openId);
        requestMap.put("trade_type",JSAPI);

        //生产签名
        String signText = SignUtil.getSignText(requestMap) + "&key=" + payGateway.getAppKey();
        String sign = DigestUtils.md5Hex(signText).toUpperCase();
        //将签名放入请求的map中
        requestMap.put("sign",sign);
        String result = "";
        try {
            result = HttpUtils.postXML(CREATE_ORDER_URL, XMLUtil.mapToXML(requestMap));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Strings.isNullOrEmpty(result))
            return null;

        Map<String,String> map = XMLUtil.xmlToMap(result);
        if (MapUtils.isEmpty(map))
            return null;

        //如果为错误就直接返回
        if (!map.get("return_code").equalsIgnoreCase("SUCCESS")){
            return null;
        }

        if (!map.get("result_code").equalsIgnoreCase("SUCCESS")){
            return null;
        }

        String prepay_id = CommonUtils.getStringValue(map.get("prepay_id"));
        if (Strings.isNullOrEmpty(prepay_id)){
            return null;
        }

        Map<String,String> wxMap = Maps.newHashMap();
        wxMap.put("appId",payGateway.getAppId());
        wxMap.put("timeStamp",String.valueOf(System.currentTimeMillis() / 1000));
        wxMap.put("nonceStr",RndUtils.getRandString());
        wxMap.put("package","prepay_id=" + prepay_id);
        wxMap.put("signType","MD5");
        //生产签名
        String paySignText = SignUtil.getSignText(wxMap) + "&key=" + payGateway.getAppKey();
        String paySign = DigestUtils.md5Hex(paySignText).toUpperCase();
        //将签名放入请求的map中
        wxMap.put("paySign",paySign);
        wxMap.put("pkg","prepay_id=" + prepay_id);//这里传package貌似el拿不到

        return wxMap;
    }

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

    public String getOrderId(Map notifyMap) {
        return CommonUtils.getStringValue(notifyMap.get("out_trade_no"));
    }

    public String getGatewayOrderId(Map notifyMap) {
        return CommonUtils.getStringValue(notifyMap.get("transaction_id"));
    }

    public double getTotalFee(Map notifyMap) {
        return CommonUtils.getDoubleValue(notifyMap.get("total_fee"));
    }

    public boolean validateNotify(Map notifyMap,DictPayGatewayModel payGateway) {
        String return_code = CommonUtils.getStringValue(notifyMap.get("return_code"));
        //如果失败就直接返回
        if (!return_code.equalsIgnoreCase("SUCCESS")){
            return false;
        }

        //支付设置订单成功的逻辑
        String sign = CommonUtils.getStringValue(notifyMap.get("sign"));
        String generateSign = DigestUtils.md5Hex(SignUtil.getSignText(notifyMap) + "&key=" + payGateway.getAppKey()).toUpperCase();
        if (!sign.equalsIgnoreCase(generateSign)){
            return false;
        }

        return true;
    }

    @Override
    public void validateSync(Map notifyMap, DictPayGatewayModel payGateway, HttpServletResponse response) {

    }

    public String notifySuccess() {
        Map<String,String> responseMap = Maps.newHashMap();
        responseMap.put("return_code","<![CDATA[SUCCESS]]>");
        responseMap.put("return_msg","<![CDATA[SUCCESS]]>");
        return  XMLUtil.mapToXML(responseMap);
    }

    public String notifyFail(String error) {
        Map<String,String> responseMap = Maps.newHashMap();
        responseMap.put("return_code","<![CDATA[FAIL]]>");
        responseMap.put("return_msg","<![CDATA[" + error + "]]>");
        return XMLUtil.mapToXML(responseMap);
    }

}
