package admin.controller.check_bill;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 提成金购买查询
 * Created by mj on 2017/9/19.
 */

@Controller
@RequestMapping("check_bill/bonusPay")
public class BonusPayController {

    private final static String PATH = "check_bill/";

    /**
     * 查询提成金购买
     */
//    @RequiresPermissions(Permission.SYS_QUERY_BONUS_PAY_SHOW)
//    @RequestMapping("queryInfo")
//    public String queryInfo(Model model,
//                              @RequestParam(value = "startDate", defaultValue = "") String startDate,
//                              @RequestParam(value = "endDate", defaultValue = "") String endDate) {
//
//        Subject subject = SecurityUtils.getSubject();
//        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
//        //默认取7天的数据
//        Date d1 = DateUtils.String2Date(startDate, "yyyy-MM-dd", DateUtils.addDay(-6, new Date()));
//        Date d2 = DateUtils.String2Date(endDate, "yyyy-MM-dd", new Date());
//        DataTable dt = PromoterPayDao.listByDate(gameId, PayType.CASHPAY.getType(), d1, d2);
//        if (dt.rows.length > 0) {
//            model.addAttribute("dt", dt.rows);
//        }
//        model.addAttribute("startDate", DateUtils.Date2String(d1));
//        model.addAttribute("endDate", DateUtils.Date2String(d2));
//        return PATH + "bonusPay";
//    }


    /**
     * 每日提成金购买数据
     */
//    @RequiresPermissions(Permission.SYS_BONUS_PAY_DETAIL_SHOW)
//    @RequestMapping("queryByDate")
//    public String queryByDate(Model model,
//                              @RequestParam(value = "date", defaultValue = "") String date) {
//
//        Subject subject = SecurityUtils.getSubject();
//        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
//        Date d1 = DateUtils.String2Date(date);
//        DataTable dt = PromoterPayDao.listByDate(gameId, PayType.CASHPAY.getType(), d1);
//        if (dt.rows.length > 0) {
//            List promoterId = DBUtils.convert2List(Long.class, "promoterId", dt);
//            DataTable listByPromoter = PromoterDao.getListByPromoter(promoterId);
//            DBUtils.addColumn(dt, listByPromoter, "promoterId", "deposit");
//            for (DataRow row : dt.rows) {
//                if (Strings.isNullOrEmpty(row.getColumnValue("deposit"))) {
//                    row.setColumnValue("deposit", "0");
//                }
//            }
//            model.addAttribute("dt", dt.rows);
//            model.addAttribute("date", date);
//            model.addAttribute("date2", date.split("-")[0] + "年" + date.split("-")[1] + "月" + date.split("-")[2] + "日");
//        }
//        return PATH + "ajax/bonusPayDetail";
//    }

    /**
     * 导出Excel  下载提成金购买查询数据
     */
//    @RequiresPermissions(Permission.OPERATE_QUERY_BONUS_PAY_EXCEL)
//    @RequestMapping("download")
//    public void download(
//            @RequestParam(value = "startDate", defaultValue = "") String startDate
//            , @RequestParam(value = "endDate", defaultValue = "") String endDate
//            , HttpServletResponse response) {
//
//        Subject subject = SecurityUtils.getSubject();
//        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
//        //默认取7天的数据
//        Date d1 = DateUtils.String2Date(startDate, "yyyy-MM-dd", DateUtils.addDay(-6, new Date()));
//        Date d2 = DateUtils.String2Date(endDate, "yyyy-MM-dd", new Date());
//        DataTable dt = PromoterPayDao.listByDate(gameId, PayType.CASHPAY.getType(), d1, d2);
//
//        //拼接要输出的字符串
//        StringBuilder sb = new StringBuilder();
//        sb.append("\t\t\t代理商人数,")
//                .append("\t提成金购买人数,")
//                .append("\t提成金购买次数,")
//                .append("\t提成金购买总额,")
//                .append("\t时间,")
//                .append("\r\n");
//        //拼接cvs内容
//        for (DataRow row : dt.rows) {
//            sb.append("\t").append(row.getColumnValue("promoterNum")).append(",")
//                    .append(row.getColumnValue("payNum") + "\t").append(",")
//                    .append(row.getColumnValue("times") + "\t").append(",")
//                    .append(row.getColumnValue("prices") + "\t").append(",")
//                    .append(row.getColumnValue("createDate")).append(",")
//                    .append("\r\n");
//        }
//        //拼接文件名
//        StringBuilder fileName = new StringBuilder(64);
//        fileName.append("提成金购买查询")
//                .append(System.currentTimeMillis());
//        //返回文件流
//        FileUtil.FileDownload(fileName.toString(), sb.toString(), response);
//    }

    /**
     * 导出Excel  下载每日提成金购买查询数据
     */
//    @RequiresPermissions(Permission.OPERATE_BONUS_PAY_DETAIL_EXCEL)
//    @RequestMapping("downloadDay")
//    public void downloadDay(
//            @RequestParam(value = "date", defaultValue = "") String date
//            , HttpServletResponse response) {
//
//        Subject subject = SecurityUtils.getSubject();
//        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
//        Date d1 = DateUtils.String2Date(date);
//        DataTable dt = PromoterPayDao.listByDate(gameId, PayType.CASHPAY.getType(), d1);
//        List promoterId = DBUtils.convert2List(Long.class, "promoterId", dt);
//        if (promoterId.size() > 0) {
//            DataTable listByPromoter = PromoterDao.getListByPromoter(promoterId);
//            DBUtils.addColumn(dt, listByPromoter, "promoterId", "deposit");
//        }
//
//        //拼接要输出的字符串
//        StringBuilder sb = new StringBuilder();
//        sb.append("\t\t\t代理商ID,")
//                .append("\t代理商昵称,")
//                .append("\t购买金额,")
//                .append("\t剩余提成金,")
//                .append("\t购买订单,")
//                .append("\t时间,")
//                .append("\r\n");
//        //拼接cvs内容
//        for (DataRow row : dt.rows) {
//            sb.append("\t").append(row.getColumnValue("promoterId")).append(",")
//                    .append(row.getColumnValue("nickName") + "\t").append(",")
//                    .append(row.getColumnValue("price") + "\t").append(",")
//                    .append(row.getColumnValue("deposit") + "\t").append(",")
//                    .append(row.getColumnValue("orderId") + "\t").append(",")
//                    .append(row.getColumnValue("createDate")).append(",")
//                    .append("\r\n");
//        }
//        //拼接文件名
//        StringBuilder fileName = new StringBuilder(64);
//        fileName.append("提成金购买明细")
//                .append(System.currentTimeMillis());
//        //返回文件流
//        FileUtil.FileDownload(fileName.toString(), sb.toString(), response);
//    }

}
