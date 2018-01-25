package pay.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import dsqp.common_const.club.PayType;
import dsqp.db_club.dao.PromoterPayDao;
import dsqp.db_club.model.PromoterPayModel;
import dsqp.db_club_dict.dao.DictPayGatewayDao;
import dsqp.db_club_dict.model.DictPayGatewayModel;
import dsqp.util.EncodingUtils;
import dsqp.util.OsUtils;
import dsqp.util.SignUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pay.service.gateway.PayGateway;
import pay.service.gateway.PayGatewayFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class OrderController {

    private final static String PATH = "pay/";
    //网页授权
    private static final String AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize";

    @RequestMapping(value = "/{className}/prepareOrder",method = RequestMethod.GET)
    public String createOrder(HttpServletRequest request, final Model model
            , @PathVariable("className")                           String className
            , @RequestParam(value = "payid",   defaultValue = "0") long   payid
            , @RequestParam(value = "openId",  defaultValue = "")  String openId) {
        boolean isWeixin = OsUtils.isWeixin(request);
        if (Strings.isNullOrEmpty(className)) {
            model.addAttribute("errorInfo", "支付类名缺失");
            return PATH + "error";
        }

        if (payid <= 0) {
            model.addAttribute("errorInfo", "订单号缺失");
            return PATH + "error";
        }

        //根据订单记录ID找到订单信息
        PromoterPayModel order = PromoterPayDao.getOne(payid);
        if (order == null) {
            model.addAttribute("errorInfo", "订单不存在");
            return PATH + "error";
        }

        if (order.getIsSuccess()){
            model.addAttribute("errorInfo", "已经支付成功");
            return PATH + "error";
        }

        //失效时间为5min
        if ((System.currentTimeMillis() - order.getCreateTime().getTime()) > (5 * 60 * 1000) ){
            model.addAttribute("errorInfo", "支付超时");
            return PATH + "error";
        }

        //通过支付方式获得是哪个支付网关,目前有支付宝，微信
        DictPayGatewayModel dictPayGateway = DictPayGatewayDao.getByClassName(className);
        if (dictPayGateway == null) {
            model.addAttribute("errorInfo", "支付网关不存在");
            return PATH + "error";
        }

        //根据className生成具体的支付网关
        PayGateway payGateway = PayGatewayFactory.create(className);
        if (payGateway == null){
            model.addAttribute("errorInfo","支付网关不存在");
            return "error";
        }

        int payType = PayType.getType(className);
        if (payType == PayType.WXPAY.getType()) {
            if (isWeixin) {
                if (Strings.isNullOrEmpty(openId)) {
                    //定义获取openid后重定向的页面
                    String redirectUrl = getCode( dictPayGateway.getAppId()
                            , dictPayGateway.getReturnUrl()
                            , EncodingUtils.urlEncode(dictPayGateway.getOrderUrl() + "?payid=" + order.getId() + "&payType=" + PayType.WXPAY.getType()));
                    return "redirect:" + redirectUrl;
                }
            }
        }

        if (payType == 0) {
            model.addAttribute("errorInfo","支付方式错误");
            return "error";
        }

        //把需要的参数传入model
        payGateway.showPayInfo(request,model,dictPayGateway, order);
        model.addAttribute("successUrl", dictPayGateway.getSuccessUrl());
        if (payType == PayType.WXPAY.getType()) {
            return PATH + "wxpay/index";
        } else if (payType == PayType.ALIPAY.getType()) {
            return PATH + "alipay/index";
        } else {
            model.addAttribute("errorInfo","支付网关不存在");
            return "error";
        }
    }

    private String getCode(String appid, String redirect_uri,String state) {

        Map<String,String> requestMap = Maps.newHashMap();
        requestMap.put("appid", appid);
        requestMap.put("redirect_uri", EncodingUtils.urlEncode(redirect_uri));
        requestMap.put("response_type","code");
        requestMap.put("scope","snsapi_base");
        requestMap.put("state",state);
        String getUrl = AUTHORIZE_URL + "?" + SignUtil.getParamsStrFromMap(requestMap) + "#wechat_redirect";

        return getUrl;
    }
}
