package admin.controller.operate_config;

import admin.service.CardPriceService;
import dsqp.common_const.club.GoodsType;
import dsqp.common_const.club_admin.Permission;
import dsqp.db.model.DataTable;
import dsqp.db_club_dict.dao.DictGoodPriceDao;
import dsqp.db_club_dict.model.DictGoodPriceModel;
import dsqp.util.CommonUtils;
import dsqp.util.RequestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by jeremy on 2017/7/30.
 */
@Controller
@RequestMapping("operate_config/")
public class CardPriceController {

    @Resource
    private CardPriceService cardPriceService;

    @RequestMapping("cardPrice")
    public String index(Model model) {
        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        //获取已有的钻石销售配置
        DataTable dtPriceList = DictGoodPriceDao.getListByGameId(gameId, true,GoodsType.PROMOTER.getGoodsType());
        if (dtPriceList.rows.length > 0) {
            model.addAttribute("dtPriceList", dtPriceList.rows);
            model.addAttribute("length", dtPriceList.rows.length);
        }
        //获取已有的待转正代理商钻石售价
        DictGoodPriceModel dictGoodPrice = DictGoodPriceDao.getByGameIdAndType(gameId, true, GoodsType.PRE_PROMOTER.getGoodsType());
        model.addAttribute("dictGoodPrice",dictGoodPrice);

        return "operate_config/card_price";
    }

    /**
     * 保存钻石售价修改
     * @param response
     * @param id
     * @param cashPrice
     * @param goodNum
     * @param giftNum
     */
    @RequiresPermissions(Permission.OPERATE_SAVE_CARD_PRICE_UPDATE)
    @RequestMapping("saveCardPriceConditon")
    public void save(HttpServletResponse response,
                     @RequestParam(value = "id", defaultValue = "0") int id,
                     @RequestParam(value = "cashPrice", defaultValue = "0") double cashPrice,
                     @RequestParam(value = "goodNum", defaultValue = "0") int goodNum,
                     @RequestParam(value = "giftNum", defaultValue = "0") int giftNum,
                     @RequestParam(value = "type", defaultValue = "0") int type) {
        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));

        DictGoodPriceModel dictGoodPrice = new DictGoodPriceModel();
        dictGoodPrice.setGameId(gameId);
        dictGoodPrice.setGoodName("钻石");
        dictGoodPrice.setCashPrice(cashPrice);
        dictGoodPrice.setGoodNum(goodNum);
        dictGoodPrice.setGiftNum(giftNum);
        dictGoodPrice.setType(type);
        dictGoodPrice.setIsEnable(true);

        if (id != 0) {//更新
            dictGoodPrice.setId(id);
            int i = cardPriceService.update(dictGoodPrice);
            RequestUtils.write(response, "" + i);
        } else {//新增
            int n = cardPriceService.add(dictGoodPrice);
            RequestUtils.write(response, "" + n);
        }

    }

    /**
     * 删除配置项
     * @param response
     * @param id
     */
    @RequiresPermissions(Permission.OPERATE_DELETE_CARD_PRICE_DELETE)
    @RequestMapping("deleteCardPriceConditon")
    public void delete(HttpServletResponse response,
                       @RequestParam(value = "id", defaultValue = "0") int id) {
        if (id != 0) {
            DictGoodPriceModel model = DictGoodPriceDao.getOne(id);
            int i = cardPriceService.deleteById(id,model);
            RequestUtils.write(response, "" + i);
        } else {
            RequestUtils.write(response, "" + 0);
        }
    }


}
