package admin.controller.check_bill;

import admin.util.OutExcelUtil;
import admin.vo.ExcelVO;
import com.google.common.collect.Maps;
import dsqp.common_const.club.PayType;
import dsqp.common_const.club_admin.Permission;
import dsqp.db.model.DataRow;
import dsqp.db.model.DataTable;
import dsqp.db_club.dao.MChargeDao;
import dsqp.db_club.dao.PromoterPayDao;
import dsqp.util.CommonUtils;
import dsqp.util.DateUtils;
import dsqp.util.FileUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 充值查询
 * Created by fx on 2017/9/21.
 */

@Controller
@RequestMapping("check_bill")
public class PayQueryController {

    private final static String PATH = "check_bill/";

    /**
     * 充值查询首页
     */
    @RequestMapping("payQuery")
    public String payQuery(Model model) {
        String d1 = DateUtils.Date2String(DateUtils.addDay(-6, new Date()));
        String d2 = DateUtils.Date2String(new Date());
        model.addAttribute("startDate", d1);
        model.addAttribute("endDate", d2);
        return PATH + "payQuery";
    }

    /**
     * 代理商充值
     */
    @RequiresPermissions(Permission.SYS_PROMOTER_PAY_SHOW)
    @RequestMapping("promoterPay")
    public String promoterPay(Model model,
                              @RequestParam(value = "payType", defaultValue = "0") int payType,
                              @RequestParam(value = "startDate", defaultValue = "") String startDate,
                              @RequestParam(value = "endDate", defaultValue = "") String endDate) {

        int gameId = CommonUtils.getIntValue(SecurityUtils.getSubject().getSession().getAttribute("gameId"));

        //默认取7天的数据
        Date d1 = DateUtils.String2Date(startDate, "yyyy-MM-dd", DateUtils.addDay(-6, new Date()));
        Date d2 = DateUtils.String2Date(endDate, "yyyy-MM-dd",new Date());
        DataTable dt = payType == 0 ? PromoterPayDao.listByDateAndGameId(gameId, d1, d2) : PromoterPayDao.listByDateAndGameId(gameId, payType, d1, d2);
        if (dt.rows.length > 0) {
            model.addAttribute("promoterPay", dt.rows);
        }
        model.addAttribute("startDate", DateUtils.Date2String(d1));
        model.addAttribute("endDate", DateUtils.Date2String(d2));
        model.addAttribute("chooseTab", 1);
        model.addAttribute("payType", payType);

        return PATH + "ajax/payQueryDetail";
    }


    /**
     * 导出Excel  代理商充值
     */
    @RequiresPermissions(Permission.OPERATE_PROMOTER_PAY_EXCEL)
    @RequestMapping(value = "promoterPayDownload")
    @ResponseBody
    public void promoterPayDownload(HttpServletResponse response,
                                    @RequestParam(value = "payType", defaultValue = "0") int payType,
                                    @RequestParam(value = "startDate", defaultValue = "") String startDate,
                                    @RequestParam(value = "endDate", defaultValue = "") String endDate) {

        int gameId = CommonUtils.getIntValue(SecurityUtils.getSubject().getSession().getAttribute("gameId"));
        Date d1 = DateUtils.String2Date(startDate, "yyyy-MM-dd");
        Date d2 = DateUtils.String2Date(endDate, "yyyy-MM-dd");
        DataTable dt = payType == 0 ? PromoterPayDao.listByDateAndGameId(gameId, d1, d2) : PromoterPayDao.listByDateAndGameId(gameId, payType, d1, d2);
        Map<String, String> rowNames = Maps.newLinkedHashMap();
        String fileName = "代理商充值";
        if (dt.rows.length > 0) {
            rowNames.put("nums", "充值人数");
            rowNames.put("times", "充值次数");
            rowNames.put("price", "充值金额");
            rowNames.put("createDate", "时间");
            if (payType == 0) {
                fileName = "代理商充值（全部渠道）";
            } else if (payType == PayType.ALIPAY.getType()) {
                fileName = "代理商充值（支付宝支付）";
            } else if (payType == PayType.WXPAY.getType()) {
                fileName = "代理商充值（微信支付）";
            }
        }
        ExcelVO vo = OutExcelUtil.download(fileName, rowNames, dt);
        FileUtil.FileDownload(vo.getFileName(), vo.getContent(), response);
    }


    /**
     * 代理商当日充值明细
     */
    @RequiresPermissions(Permission.SYS_PROMOTER_PAY_BY_DATE_SHOW)
    @RequestMapping("promoterPayByDate")
    public String promoterPayByDate(Model model,
                                    @RequestParam(value = "payType", defaultValue = "0") int payType,
                                    @RequestParam(value = "date", defaultValue = "") String date) {


        int gameId = CommonUtils.getIntValue(SecurityUtils.getSubject().getSession().getAttribute("gameId"));
        DataTable dt;
        if (payType == 0) {
            dt = PromoterPayDao.getListByPayType(gameId, DateUtils.String2Date(date));
        } else {
            dt = PromoterPayDao.getListByPayType(gameId, payType, DateUtils.String2Date(date));
        }

        if (dt.rows.length > 0) {
            model.addAttribute("promoterDetail", dt.rows);
        }
        model.addAttribute("date", date);
        model.addAttribute("payType", payType);
        return PATH + "ajax/payQueryDayDetail";
    }

    /**
     * 导出Excel  当日充值明细
     */
    @RequiresPermissions(Permission.OPERATE_PROMOTER_PAY_BY_DATE_EXCEL)
    @RequestMapping(value = "promoterByDateDownload")
    @ResponseBody
    public void promoterByDateDownload(HttpServletResponse response,
                                       @RequestParam(value = "payType", defaultValue = "0") int payType,
                                       @RequestParam(value = "date", defaultValue = "") String date) {

        int gameId = CommonUtils.getIntValue(SecurityUtils.getSubject().getSession().getAttribute("gameId"));
        DataTable dt;
        if (payType == 0) {
            dt = PromoterPayDao.getListByPayType(gameId, DateUtils.String2Date(date));
        } else {
            dt = PromoterPayDao.getListByPayType(gameId, payType, DateUtils.String2Date(date));
        }
        Map<String, String> rowNames = Maps.newLinkedHashMap();
        String fileName = "代理商充值明细";
        if (dt.rows.length > 0) {
            for (DataRow row : dt.rows) {
                if ("1".equals(row.getColumnValue("payType"))) {
                    row.setColumnValue("payType", "提成金购买");
                } else if ("2".equals(row.getColumnValue("payType"))) {
                    row.setColumnValue("payType", "微信支付");
                } else if ("3".equals(row.getColumnValue("payType"))) {
                    row.setColumnValue("payType", "支付宝支付");
                }
            }
            rowNames.put("promoterId", "代理商ID");
            rowNames.put("nickName", "代理商昵称");
            rowNames.put("price", "充值金额");
            rowNames.put("payType", "充值渠道");
            rowNames.put("orderId", "充值订单");
            rowNames.put("createTime", "时间");
            if (payType == 0) {
                fileName = "代理商充值明细（全部渠道）";
            } else if (payType == PayType.ALIPAY.getType()) {
                fileName = "代理商充值明细（支付宝支付）";
            } else if (payType == PayType.WXPAY.getType()) {
                fileName = "代理商充值明细（微信支付）";
            }
        }
        ExcelVO vo = OutExcelUtil.download(fileName, rowNames, dt);
        FileUtil.FileDownload(vo.getFileName(), vo.getContent(), response);
    }

    /**
     * 游戏内购
     */
    @RequiresPermissions(Permission.SYS_GAME_PAY_SHOW)
    @RequestMapping("gamePay")
    public String gamePay(Model model) {
        model.addAttribute("gamePay", null);
        return PATH + "ajax/payQueryDetail";
    }


    /**
     * 公众号售钻
     */
    @RequiresPermissions(Permission.SYS_HKMOVIE_PAY_SHOW)
    @RequestMapping("hkmoviePay")
    public String hkmoviePay(Model model,
                             @RequestParam(value = "startDate", defaultValue = "") String startDate,
                             @RequestParam(value = "endDate", defaultValue = "") String endDate) {

//        int gameId = CommonUtils.getIntValue(SecurityUtils.getSubject().getSession().getAttribute("gameId"));
//        //默认取7天的数据
//        Date d1 = DateUtils.String2Date(startDate, "yyyy-MM-dd", DateUtils.addDay(-7, new Date()));
//        Date d2 = DateUtils.String2Date(endDate, "yyyy-MM-dd", DateUtils.addDay(-1, new Date()));
//        DataTable dt = MChargeDao.getListByDate(gameId, d1, d2);
//        if (dt.rows.length > 0) {
//            model.addAttribute("hkmoviePay", dt.rows);
//        }
//        model.addAttribute("startDate", DateUtils.Date2String(d1));
//        model.addAttribute("endDate", DateUtils.Date2String(d2));
//        model.addAttribute("chooseTab", 3);

        return PATH + "ajax/payQueryDetail";
    }

    /**
     * 导出Excel  公众号售钻
     */
    @RequiresPermissions(Permission.OPERATE_HKMOVIE_PAY_EXCEL)
    @RequestMapping(value = "hkmoviePayDownload")
    @ResponseBody
    public void promoterPayDownload(HttpServletResponse response,
                                    @RequestParam(value = "startDate", defaultValue = "") String startDate,
                                    @RequestParam(value = "endDate", defaultValue = "") String endDate) {

        int gameId = CommonUtils.getIntValue(SecurityUtils.getSubject().getSession().getAttribute("gameId"));
        //默认取7天的数据
        Date d1 = DateUtils.String2Date(startDate, "yyyy-MM-dd", DateUtils.addDay(-6, new Date()));
        Date d2 = DateUtils.String2Date(endDate, "yyyy-MM-dd", new Date());
        DataTable dt = MChargeDao.getListByDate(gameId, d1, d2);
        Map<String, String> rowNames = Maps.newLinkedHashMap();
        if (dt.rows.length > 0) {
            rowNames.put("nums", "充值人数");
            rowNames.put("times", "充值次数");
            rowNames.put("price", "充值金额");
            rowNames.put("createDate", "时间");
        }
        String fileName = "公众号售钻";
        ExcelVO vo = OutExcelUtil.download(fileName, rowNames, dt);

        FileUtil.FileDownload(vo.getFileName(), vo.getContent(), response);
    }

    /**
     * 公众号当日充值明细
     */
    @RequiresPermissions(Permission.SYS_HKMOVIE_PAY_BY_DATE_SHOW)
    @RequestMapping("hkmoviePayByDate")
    public String hkmoviePayByDate(Model model,
                                   @RequestParam(value = "date", defaultValue = "") String date) {


        int gameId = CommonUtils.getIntValue(SecurityUtils.getSubject().getSession().getAttribute("gameId"));
        Date d1 = DateUtils.String2Date(date);
        DataTable detailByDate = MChargeDao.getDetailByDate(gameId, d1, DateUtils.addDay(1, d1));
        if (detailByDate.rows.length > 0) {
            model.addAttribute("hkmovieDetail", detailByDate.rows);
        }
        model.addAttribute("date", date);
        return PATH + "ajax/payQueryDayDetail";
    }


}
