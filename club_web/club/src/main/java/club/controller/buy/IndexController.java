package club.controller.buy;

import club.service.PayService;
import com.google.common.base.Strings;
import dsqp.common_const.club.ClubStatus;
import dsqp.common_const.club.GoodsType;
import dsqp.common_const.club.PayType;
import dsqp.db.model.DataTable;
import dsqp.db_club.dao.ClubDao;
import dsqp.db_club.dao.PromoterDao;
import dsqp.db_club.dao.PromoterPayDao;
import dsqp.db_club.model.ClubModel;
import dsqp.db_club.model.PromoterModel;
import dsqp.db_club.model.PromoterPayModel;
import dsqp.db_club_dict.dao.DictGameDao;
import dsqp.db_club_dict.dao.DictGoodPriceDao;
import dsqp.db_club_dict.dao.DictPayGatewayDao;
import dsqp.db_club_dict.model.DictGoodPriceModel;
import dsqp.db_club_dict.model.DictPayGatewayModel;
import dsqp.util.CommonUtils;
import dsqp.util.RequestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by Aris on 2017/7/22.
 */
@Controller("BuyIndex")
@RequestMapping("buy")
public class IndexController {
    @Autowired
    PayService payService;

    private final static String PATH = "buy/";

    @RequestMapping("index")
    public String index(final Model model){
        Subject subject = SecurityUtils.getSubject();
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
        long promoterId = CommonUtils.getLongValue(subject.getSession().getAttribute("id"));

        PromoterModel p = PromoterDao.getOne(promoterId);
        if (p == null) {
            return "logout";//数据异常，登出
        }

        //校验 :: 是否为待转正代理
        ClubModel c = ClubDao.getPreClub(promoterId, ClubStatus.Init.getClubStatus());
        int goodsType = 0;
        if (c != null) {
            goodsType = GoodsType.PRE_PROMOTER.getGoodsType();
            model.addAttribute("isNormalClub", false);
        } else {
            goodsType = GoodsType.PROMOTER.getGoodsType();
            model.addAttribute("isNormalClub", true);
        }

        DataTable dt = DictGoodPriceDao.getByGameId(gameId, true, goodsType);
        model.addAttribute("goodPrice", dt.rows);

        return PATH + "index";
    }


    @RequestMapping("buyDialog")
    public ModelAndView buyDialog(HttpServletRequest request
            , @RequestParam(value = "dlgId", defaultValue = "0") int dlgId
            , @RequestParam(value = "id", defaultValue = "0") int id
            , @RequestParam(value = "isNormalClub", defaultValue = "false") boolean isNormalClub
            , @RequestParam(value = "gameCardNum", defaultValue = "0") int gameCardNum) {
        Subject subject = SecurityUtils.getSubject();
        ModelAndView mv = new ModelAndView(PATH+"dialog");
        PromoterModel promoterModel = PromoterDao.getOne(CommonUtils.getLongValue(subject.getSession().getAttribute("id")));
        double deposit = promoterModel.getDeposit();

        if (promoterModel == null) {
            return mv;
        }

        //商品信息
        DictGoodPriceModel goodPriceModel = DictGoodPriceDao.getOne(id);

        if (goodPriceModel != null) {
            mv.addObject("isNormalClub", isNormalClub);
            mv.addObject("dlgId", dlgId);
            mv.addObject("goodPrice", goodPriceModel);
            mv.addObject("deposit", deposit);
            mv.addObject("pLevel", promoterModel.getpLevel());
            if (!isNormalClub) {
                mv.addObject("gameCardNum", gameCardNum);
            }
            if (dlgId == 2) {
                boolean canBuy = deposit - (goodPriceModel.getCashPrice() * goodPriceModel.getNonCashDiscount()) > 0;
                mv.addObject("canBuy", canBuy);
            }
        }

        return mv;
    }

    @RequestMapping("pay_weixin")
    public void WxPay(HttpServletRequest request, HttpServletResponse response
            , @RequestParam(value = "id", defaultValue = "0") int id
            , @RequestParam(value = "payType", defaultValue = "0") int payType
            , @RequestParam(value = "isNormalClub", defaultValue = "false") boolean isNormalClub
            , @RequestParam(value = "gameCardNum", defaultValue = "0") int gameCardNum){
        Subject subject = SecurityUtils.getSubject();
        PromoterModel promoterModel = PromoterDao.getOne(CommonUtils.getLongValue(subject.getSession().getAttribute("id")));
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
        //商品信息
        DictGoodPriceModel goodPriceModel = DictGoodPriceDao.getOne(id);
        if (goodPriceModel == null) {
            RequestUtils.write(response, "缺少价格配置");
            return;
        }

        PromoterPayModel order = new PromoterPayModel();
        order.setGameId(promoterModel.getGameId());
        order.setPromoterId(promoterModel.getId());
        order.setpLevel(promoterModel.getpLevel());
        order.setNickName(promoterModel.getNickName());
        order.setParentId(promoterModel.getParentId());
        order.setOrderId(System.currentTimeMillis() + "" + promoterModel.getId());
        order.setPayType(payType);
        if (isNormalClub) {
            order.setPrice(goodPriceModel.getCashPrice());
            order.setGoodNum(goodPriceModel.getGoodNum());
        } else {
            if (gameCardNum <= 0) {
                RequestUtils.write(response, "错误商品数量");
                return;
            }
            order.setPrice(goodPriceModel.getCashPrice() * gameCardNum);
            order.setGoodNum(goodPriceModel.getGoodNum() * gameCardNum);
        }

        order.setGoodGivingNum(goodPriceModel.getGiftNum());
        order.setIsSuccess(false);
        order.setCreateTime(new Date());
        order.setCreateDate(new Date());
        PromoterPayDao.add(order);

        String wxPrefix = DictGameDao.queryRemarkByGameId(gameId);
        if (Strings.isNullOrEmpty(wxPrefix)){
            wxPrefix = "lailai_";
        }

        if (wxPrefix.equals("66")){
            wxPrefix = "liuliu_";
        } else if (wxPrefix.equals("来来")) {
            wxPrefix = "lailai_";
        } else {
            wxPrefix = "liuliu_";
        }

        String className = wxPrefix + PayType.getClassName(payType);
        DictPayGatewayModel payGateway = DictPayGatewayDao.getByClassName(className);
        if (payGateway == null) {
            RequestUtils.write(response, "缺少支付配置");
            return;
        }

        RequestUtils.redirect(response, payGateway.getOrderUrl() + "?payid=" + order.getId());
    }

    //账户余额购买钻石
//    @RequestMapping("buyGameCard")
//    public void buyGameCard(HttpServletResponse response
//            , @RequestParam(value = "goodId", defaultValue = "0") int goodId) {
//        long promoterId = CommonUtils.getLongValue(SecurityUtils.getSubject().getSession().getAttribute("id"));
//        MsgVO msg = payService.buy(promoterId, goodId);
//        RequestUtils.write(response, JsonUtils.getJson(msg));
//    }

}
