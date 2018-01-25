package club.controller.buy;

import club.service.WxPayService;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import dsqp.db_club.dao.PromoterPayDao;
import dsqp.db_club.model.PromoterPayModel;
import dsqp.db_club_dict.dao.DictPayGatewayDao;
import dsqp.db_club_dict.model.DictPayGatewayModel;
import dsqp.util.EncodingUtils;
import dsqp.util.OsUtils;
import dsqp.util.RequestUtils;
import dsqp.util.SignUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by aris on 2016/11/15.
 * 业务流程说明：
 （1）商户后台系统根据用户选购的商品生成订单。
 （2）用户确认支付后调用微信支付【统一下单API】生成预支付交易；
 （3）微信支付系统收到请求后生成预支付交易单，并返回交易会话的二维码链接code_url。
 （4）商户后台系统根据返回的code_url生成二维码。
 （5）用户打开微信“扫一扫”扫描二维码，微信客户端将扫码内容发送到微信支付系统。
 （6）微信支付系统收到客户端请求，验证链接有效性后发起用户支付，要求用户授权。
 （7）用户在微信客户端输入密码，确认支付后，微信客户端提交授权。
 （8）微信支付系统根据用户授权完成支付交易。
 （9）微信支付系统完成支付交易后给微信客户端返回交易结果，并将交易结果通过短信、微信消息提示用户。微信客户端展示支付交易结果页面。
 （10）微信支付系统通过发送异步消息通知商户后台系统支付结果。商户后台系统需回复接收情况，通知微信后台系统不再发送该单的支付通知。
 （11）未收到支付通知的情况，商户后台系统调用【查询订单API】。
 （12）商户确认订单已支付后给用户发货。
 */
@Controller
public class OrderController {

    private static final String PATH = "buy/";
    //网页授权
    private static final String AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize";

    @Autowired
    private WxPayService payService;

    @RequestMapping(value = "/{className}/prepareOrder",method = RequestMethod.GET)
    public String CreateOrder(HttpServletRequest request, final Model model
            , @PathVariable("className") String className
            , @RequestParam(value = "payid",defaultValue = "0") long payid
            , @RequestParam(value = "openId",defaultValue = "") String openId) {
        System.out.println(RequestUtils.getUrl(request));
        boolean isWeixin = OsUtils.isWeixin(request);
        if (!isWeixin){
            model.addAttribute("errorInfo", "请在微信中发起支付");
            return PATH + "error";
        }

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

        if ( (System.currentTimeMillis() - order.getCreateTime().getTime()) > (5 * 60 * 1000) ){
            model.addAttribute("errorInfo", "支付超时");
            return PATH + "error";
        }

        if (order.getIsSuccess()){
            model.addAttribute("errorInfo", "已经支付成功");
            return PATH + "error";
        }

        //通过支付方式获得是哪个支付网关,目前有支付宝，微信
        DictPayGatewayModel dictPayGateway = DictPayGatewayDao.getByClassName(className);
        if (dictPayGateway == null) {
            model.addAttribute("errorInfo", "支付网关不存在");
            return PATH + "error";
        }

        if (Strings.isNullOrEmpty(openId)) {
            //定义获取openid后重定向的页面
            String redirectUrl = getCode( dictPayGateway.getAppId()
                                        , dictPayGateway.getReturnUrl()
                                        , EncodingUtils.urlEncode(dictPayGateway.getOrderUrl() + "?payid=" + order.getId() )
            );
            return "redirect:" + redirectUrl;
        }else {
            Map<String,String> map = payService.showPayInfo(request, dictPayGateway, order);
            //把需要的参数传入model
            model.addAllAttributes(map);
        }

        return PATH + "wxpay";
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
