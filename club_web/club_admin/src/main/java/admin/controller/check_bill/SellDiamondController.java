package admin.controller.check_bill;

import com.google.common.base.Strings;
import dsqp.common_const.club_admin.Permission;
import dsqp.db.model.DataRow;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db_club.dao.PromoterPayDao;
import dsqp.db_club.dao.PromoterSellDao;
import dsqp.db_club_log.dao.LogGamecardDao;
import dsqp.db_club_log.dao.LogRebateGetDao;
import dsqp.util.CommonUtils;
import dsqp.util.DateUtils;
import dsqp.util.FileUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 售钻查询
 * Created by fx 2017/9/19
 */
@Controller
@RequestMapping("check_bill")
public class SellDiamondController {
    private final static String PATH = "check_bill/";

    /**
     * 钻石查询 :: 按时间段查询
     */
    @RequiresPermissions(Permission.SYS_SELL_DIAMOND_QUERY_SHOW)
    @RequestMapping("sellDiamondQuery")
    public ModelAndView sellDiamondQuery(
            @RequestParam(value = "startDate", defaultValue = "") String startDate
            , @RequestParam(value = "endDate", defaultValue = "") String endDate) {

        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        //默认取7天的数据
        Date d1 = dsqp.util.DateUtils.String2Date(startDate, "yyyy-MM-dd", dsqp.util.DateUtils.addDay(-6, new Date()));
        Date d2 = dsqp.util.DateUtils.String2Date(endDate, "yyyy-MM-dd", new Date());
        DataTable dt = PromoterPayDao.listPayTotal(gameId, d1, d2);
        ModelAndView mv = new ModelAndView(PATH + "sellDiamond");
        if (dt.rows.length > 0) {
//            DataTable bonusPay = PromoterPayDao.listByDate(gameId, PayType.CASHPAY.getType(), d1, d2);
//            DBUtils.addColumn(dt, bonusPay, "createDate", "prices");//提成金购买钻石总金额
            DataTable changeNum = LogGamecardDao.queryChangeNumByDate(gameId, d1, d2);
            DBUtils.addColumn(dt, changeNum, "createDate", "changeNum");//发放钻石  赠送 删除钻石数量
            LogRebateGetDao.totalRebateGet(gameId, d1, d2);
            DataTable sellNum = PromoterSellDao.getListByCreateDateAndGameId(gameId, d1, d2);
            DBUtils.addColumn(dt, sellNum, "createDate", "sellNum");//代理出售钻石总额
            DataTable rebateGet = LogRebateGetDao.totalRebateGet(gameId, d1, d2);
            DBUtils.addColumn(dt, rebateGet, "createDate", "rebateGet");//提取返钻数量
            mv.addObject("dt", dt.rows);
        }
        mv.addObject("startDate", DateUtils.Date2String(d1));
        mv.addObject("endDate", DateUtils.Date2String(d2));
        return mv;
    }


    /**
     * 钻石查询下载
     */
    @RequiresPermissions(Permission.OPERATE_SELL_DIAMOND_QUERY_EXCEL)
    @RequestMapping("downloadSellDiamond")
    public void downloadDetail(
            @RequestParam(value = "startDate", defaultValue = "") String startDate
            , @RequestParam(value = "endDate", defaultValue = "") String endDate
            , HttpServletResponse response) {

        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        //默认取7天的数据
        Date d1 = dsqp.util.DateUtils.String2Date(startDate, "yyyy-MM-dd", dsqp.util.DateUtils.addDay(-6, new Date()));
        Date d2 = dsqp.util.DateUtils.String2Date(endDate, "yyyy-MM-dd", new Date());
        DataTable dt = PromoterPayDao.listPayTotal(gameId, d1, d2);
        if (dt.rows.length > 0) {
//            DataTable bonusPay = PromoterPayDao.listByDate(gameId, PayType.CASHPAY.getType(), d1, d2);
//            DBUtils.addColumn(dt, bonusPay, "createDate", "prices");//提成金购买钻石总金额

            DataTable changeNum = LogGamecardDao.queryChangeNumByDate(gameId, d1, d2);
            DBUtils.addColumn(dt, changeNum, "createDate", "changeNum");

            DataTable sellNum = PromoterSellDao.getListByCreateDateAndGameId(gameId, d1, d2);
            DBUtils.addColumn(dt, sellNum, "createDate", "sellNum");

            DataTable rebateGet = LogRebateGetDao.totalRebateGet(gameId, d1, d2);
            DBUtils.addColumn(dt, rebateGet, "createDate", "rebateGet");//提取返钻数量
        }
        //拼接要输出的字符串
        StringBuilder sb = new StringBuilder();
        sb.append("\t\t\t推广员人数,")
                .append("\t充值金额,")
                .append("\t提取返钻数量,")
                .append("\t提成金购买金额,")
                .append("\t新增钻石总额,")
                .append("\t购买钻石,")
                .append("\t发放钻石,")
                .append("\t代理出售钻石,")
                .append("\t时间,")
                .append("\r\n");
        //拼接cvs内容
        for (DataRow row : dt.rows) {
            sb.append("\t").append(row.getColumnValue("promoterNum")).append(",")
                    .append(row.getColumnValue("allPrices") + "\t").append(",")
                    .append(row.getColumnValue("rebateGet") + "\t").append(",")
                    .append(Strings.isNullOrEmpty(row.getColumnValue("prices")) ? "0" + "\t" : row.getColumnValue("prices") + "\t").append(",")
                    .append(CommonUtils.getIntValue(row.getColumnValue("goodNums")) + CommonUtils.getIntValue(row.getColumnValue("goodGivingNums")) + CommonUtils.getIntValue(row.getColumnValue("changgeNum")) + "\t").append(",")
                    .append(CommonUtils.getIntValue(row.getColumnValue("goodNums")) + CommonUtils.getIntValue(row.getColumnValue("goodGivingNums")) + "\t").append(",")
                    .append(row.getColumnValue("changeNum") + "\t").append(",")
                    .append(row.getColumnValue("sellNum") + "\t").append(",")
                    .append(row.getColumnValue("createDate")).append(",")
                    .append("\r\n");

        }
        //拼接文件名
        StringBuilder fileName = new StringBuilder(64);
        fileName.append("售钻查询").append(System.currentTimeMillis());
        //返回文件流
        FileUtil.FileDownload(fileName.toString(), sb.toString(), response);
    }

}

