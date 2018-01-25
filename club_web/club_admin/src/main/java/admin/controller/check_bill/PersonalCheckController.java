package admin.controller.check_bill;

import admin.service.PersonalCheckService;
import admin.util.OutExcelUtil;
import admin.vo.ExcelVO;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import dsqp.common_const.club.PromoterLevel;
import dsqp.common_const.club_admin.Permission;
import dsqp.common_const.club_admin.WithdrawRequest;
import dsqp.db.model.DataRow;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db_club.dao.*;
import dsqp.db_club.model.ClubModel;
import dsqp.db_club.model.PromoterModel;
import dsqp.db_club_log.dao.LogRebateDao;
import dsqp.db_club_log.dao.LogRebateGetDao;
import dsqp.util.CommonUtils;
import dsqp.util.DateUtils;
import dsqp.util.FileUtil;
import dsqp.util.RequestUtils;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * Created by mj on 2017/7/29.
 */

/**
 * 代理查询
 * Created by fx on 2017/9/21
 */

@Controller
@RequestMapping("check_bill/personalCheck")
public class PersonalCheckController {

    @Resource
    PersonalCheckService personalCheckService;

    @RequestMapping("queryInfo")
    public String queryInfo() {
        return "check_bill/personalCheck";
    }

    /**
     * 代理商查询 :: 按俱乐部ID
     */
    @RequiresPermissions(Permission.SYS_QUERY_PROMOTER_SHOW)
    @RequestMapping("queryByClub")
    public String queryByClub(Model model
            , @RequestParam(value = "clubId", defaultValue = "0") long clubId) {
        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        ClubModel club = ClubDao.queryByIdAndGamId(clubId, gameId);
        if (club != null) {
            PromoterModel promoter = PromoterDao.getOne(club.getPromoterId());
            if (promoter != null) {
                DataTable dt = ClubDao.queryByPromoterId(promoter.getId());
                if (dt.rows.length > 0) {
                    PromoterModel upPromoter = PromoterDao.getOne(promoter.getParentId());
                    String totalRequest = WithdrawRequestDao.queryTotalByPromoterId(promoter.getId(), WithdrawRequest.SUCCESS.getWithdrawStatus());//累计提现
                    String requestPay = "";//提成金购买钻石金额
                    Date startDate = DateUtils.addDay(-6, DateUtils.String2Date(DateUtil.formatDate(new Date(), "yyyy-MM-dd")));
                    Date endDate = DateUtils.addDay(1, DateUtils.String2Date(DateUtil.formatDate(new Date(), "yyyy-MM-dd")));
                    model.addAttribute("endDate", DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
                    model.addAttribute("startDate", DateUtil.formatDate(DateUtils.addDay(-6, new Date()), "yyyy-MM-dd"));
                    DataTable payList = PromoterPayDao.getPayListByDate(promoter.getId(), startDate, endDate);
                    DataTable rebateList = LogRebateDao.getPayListByDate(promoter.getId(), startDate, endDate);
                    DBUtils.addColumn(payList, rebateList, "payId", "rebateDiamond");
                    DataTable withdraw = WithdrawRequestDao.queryByCreateTime(promoter.getId(), startDate, endDate);
//                    DataTable logLogin = LogLoginDao.queryByPromoterId(promoter.getId());//查询最后登录数据
//                    if (logLogin.rows.length > 0) {
//                        model.addAttribute("logLogin", logLogin.rows[0]);
//                    }
                    DataTable bonusTotalByEndDate = personalCheckService.getBonusTotalByEndDate(DateUtil.formatDate(DateUtils.addDay(1, new Date()), "yyyy-MM-dd"), promoter.getId(), promoter.getpLevel() == PromoterLevel.SUPER);
                    DataTable bonusList = BonusDao.listByGameId(gameId);
                    DataTable bonusTotalWeek = new DataTable();
                    for (DataRow row : bonusList.rows) {
                        DataTable l = personalCheckService.getBonusTotaWeek(row.getColumnValue("startDate"), row.getColumnValue("endDate"), promoter.getId(), promoter.getpLevel() == PromoterLevel.SUPER);
                        for (DataRow row1 : l.rows) {
                            bonusTotalWeek.addRow(row1);
                        }
                    }

                    DataTable rebate = LogRebateGetDao.list(promoter.getId());
                    int rebateGetTotal = 0;
                    if (rebate.rows.length > 0) {
                        for (DataRow row : rebate.rows) {
                            rebateGetTotal += CommonUtils.getIntValue(row.getColumnValue("getDiamond"));
                        }
                    }
                    model.addAttribute("rebateGetTotal", rebateGetTotal);//累计提取返钻数量
                    model.addAttribute("rebate", rebate.rows);
                    DataTable under = personalCheckService.getUnderDT(promoter);
                    model.addAttribute("under", under != null ? under.rows : null);//查询下线明细
                    model.addAttribute("bonusList", bonusTotalByEndDate.rows);//按下属代理商显示提成明细
                    model.addAttribute("totalWeek", bonusTotalWeek.rows);//按日期显示提成明细
                    model.addAttribute("withdraw", withdraw.rows);
                    model.addAttribute("list", payList.rows);
                    model.addAttribute("promoter", promoter);
                    model.addAttribute("club", club);
                    model.addAttribute("upPromoter", upPromoter);
                    model.addAttribute("totalRequest", totalRequest.length() > 0 ? totalRequest : "0");
                    model.addAttribute("requestPay", requestPay.length() > 0 ? requestPay : "0");
                }
            }
        } else {
            model.addAttribute("isEmpty", "俱乐部ID不存在,请重新输入");
        }
        return "check_bill/personalCheck";
    }

    /**
     * 代理商查询 :: 按代理商ID
     */
    @RequiresPermissions(Permission.SYS_QUERY_PROMOTER_SHOW)
    @RequestMapping("queryByPromoter")
    public String queryByPromoter(Model model
            , @RequestParam(value = "userId", defaultValue = "0") long userId
            , @RequestParam(value = "start", defaultValue = "") String startDate
            , @RequestParam(value = "end", defaultValue = "") String endDate) {

        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));

        DataTable dtPromoter = PromoterDao.queryByGameIdAndUserId(gameId, userId,true);
        PromoterModel promoter = dtPromoter.rows.length > 0 ? DBUtils.convert2Model(PromoterModel.class, dtPromoter.rows[0]) : null;
        if (promoter != null) {
            Date d1, d2;
            DataTable dt = ClubDao.queryByPromoterId(promoter.getId());
            if (dt.rows.length > 0) {
                ClubModel club = DBUtils.convert2Model(ClubModel.class, dt.rows[0]);
                PromoterModel upPromoter = PromoterDao.getOne(promoter.getParentId());
                String totalRequest = WithdrawRequestDao.queryTotalByPromoterId(promoter.getId(), WithdrawRequest.SUCCESS.getWithdrawStatus());//累计提现（2.提现成功）
                String requestPay = "";//提成金购买钻石金额
//                DataTable logLogin = LogLoginDao.queryByPromoterId(promoter.getId());//查询最后登录数据
//                if (logLogin.rows.length > 0) {
//                    model.addAttribute("logLogin", logLogin.rows[0]);
//                }
                model.addAttribute("promoter", promoter);
                model.addAttribute("club", club);
                model.addAttribute("upPromoter", upPromoter);
                model.addAttribute("totalRequest", totalRequest.length() > 0 ? totalRequest : "0");
                model.addAttribute("requestPay", requestPay.length() > 0 ? requestPay : "0");
                if (Strings.isNullOrEmpty(startDate) && Strings.isNullOrEmpty(endDate)) {
                    d1 = DateUtils.addDay(-6, DateUtils.String2Date(DateUtil.formatDate(new Date(), "yyyy-MM-dd")));
                    d2 = DateUtils.addDay(1, DateUtils.String2Date(DateUtil.formatDate(new Date(), "yyyy-MM-dd")));
                    model.addAttribute("endDate", DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
                    model.addAttribute("startDate", DateUtil.formatDate(DateUtils.addDay(-6, new Date()), "yyyy-MM-dd"));
                } else {
                    d1 = DateUtils.String2Date(startDate);
                    d2 = DateUtils.addDay(1, DateUtils.String2Date(endDate));
                    model.addAttribute("endDate", endDate);
                    model.addAttribute("startDate", startDate);
                }
                DataTable bonusTotalByEndDate = personalCheckService.getBonusTotalByEndDate(DateUtil.formatDate(DateUtils.addDay(1, new Date()), "yyyy-MM-dd"), promoter.getId(), promoter.getpLevel() == PromoterLevel.SUPER);
                DataTable bonusList = BonusDao.listByGameId(gameId);
                DataTable bonusTotalWeek = new DataTable();
                for (DataRow row : bonusList.rows) {
                    DataTable l = personalCheckService.getBonusTotaWeek(row.getColumnValue("startDate"), row.getColumnValue("endDate"), promoter.getId(), promoter.getpLevel() == PromoterLevel.SUPER);
                    for (DataRow row1 : l.rows) {
                        bonusTotalWeek.addRow(row1);
                    }
                }
                DataTable payList = PromoterPayDao.getPayListByDate(promoter.getId(), d1, d2);
                DataTable rebateList = LogRebateDao.getPayListByDate(promoter.getId(), d1, d2);
                DBUtils.addColumn(payList, rebateList, "payId", "rebateDiamond");
                DataTable rebate = LogRebateGetDao.list(promoter.getId());
                int rebateGetTotal = 0;
                if (rebate.rows.length > 0) {
                    for (DataRow row : rebate.rows) {
                        rebateGetTotal += CommonUtils.getIntValue(row.getColumnValue("getDiamond"));
                    }
                }
                model.addAttribute("rebateGetTotal", rebateGetTotal);//累计提取返钻数量
                model.addAttribute("bonusList", bonusTotalByEndDate.rows);//按下属代理商显示提成明细
                model.addAttribute("totalWeek", bonusTotalWeek.rows);//按日期显示提成明细
                model.addAttribute("rebate", rebate.rows);
                model.addAttribute("list", payList.rows);
                DataTable under = personalCheckService.getUnderDT(promoter);
                model.addAttribute("under", under != null ? under.rows : null);//查询下线明细
            }
        } else {
            model.addAttribute("isEmpty", "该Id暂无俱乐部信息,请重新输入");
        }
        return "check_bill/personalCheck";
    }

    /**
     * 购买明细 (代理商当日充值明细)
     */
    @RequiresPermissions(Permission.SYS_PROMOTER_PAY_BY_DATE_SHOW)
    @RequestMapping("buyListAjax")
    @ResponseBody
    public void buyListAjax(HttpServletResponse response
            , @RequestParam(value = "promoterId", defaultValue = "0") long promoterId
            , @RequestParam(value = "start", defaultValue = "") String startDate
            , @RequestParam(value = "end", defaultValue = "") String endDate) {

        Date d1 = DateUtils.String2Date(startDate);
        Date d2 = DateUtils.String2Date(endDate);
        DataTable payList = PromoterPayDao.getPayListByDate(promoterId, d1, d2);
        DataTable rebateList = LogRebateDao.getPayListByDate(promoterId, d1, d2);
        DBUtils.addColumn(payList, rebateList, "payId", "rebateDiamond");
        StringBuilder sb = new StringBuilder();
        for (DataRow row : payList.rows) {
            String goodNum = row.getColumnValue("goodNum");
            String goodGivingNum = row.getColumnValue("goodGivingNum");
            int total = CommonUtils.getIntValue(goodNum) + CommonUtils.getIntValue(goodGivingNum);
            String payType = "1".equals(row.getColumnValue("payType")) ? "提成金支付" : "微信支付";
            String isSuccess = "true".equals(row.getColumnValue("isSuccess")) ? "是" : "否";
            sb.append("<tr><td align='center'>" +
                    row.getColumnValue("price") + "</td><td align='center'>" +
                    payType + "</td><td align='center'>" +
                    CommonUtils.getStringValue(total) + "</td><td align='center'>" +
                    goodNum + "</td><td align='center'>" +
                    goodGivingNum + "</td><td align='center'>" +
                    isSuccess + "</td><td align='center'>" +
                    row.getColumnValue("createTime") + "</td></tr>");
        }
        RequestUtils.write(response, new Gson().toJson(sb.toString()));
    }

    /**
     * 导出Excel  购买明细(当日充值明细导出Excel)   2017-9-27
     */
    @RequiresPermissions(Permission.OPERATE_PROMOTER_PAY_BY_DATE_EXCEL)
    @RequestMapping(value = "downLoadBuy")
    @ResponseBody
    public void downLoadBuy(HttpServletResponse response
            , @RequestParam(value = "promoterId", defaultValue = "0") long promoterId
            , @RequestParam(value = "start", defaultValue = "") String startDate
            , @RequestParam(value = "end", defaultValue = "") String endDate) {

        Date d1 = DateUtils.String2Date(startDate);
        Date d2 = DateUtils.addDay(1, DateUtils.String2Date(endDate));
        DataTable payList = PromoterPayDao.getPayListByDate(promoterId, d1, d2);
        DataTable rebateList = LogRebateDao.getPayListByDate(promoterId, d1, d2);
        DBUtils.addColumn(payList, rebateList, "payId", "rebateDiamond");
        Map<String, String> rowNames = Maps.newLinkedHashMap();
        if (payList.rows.length > 0) {
            for (DataRow row : payList.rows) {
                if ("1".equals(row.getColumnValue("payType"))) {
                    row.setColumnValue("payType", "提成金支付");
                } else if ("2".equals(row.getColumnValue("payType"))) {
                    row.setColumnValue("payType", "微信支付");
                }
                if ("true".equals(row.getColumnValue("isSuccess"))) {
                    row.setColumnValue("isSuccess", "是");
                } else if ("false".equals(row.getColumnValue("isSuccess"))) {
                    row.setColumnValue("isSuccess", "否");
                }
                row.setColumnValue("payId", CommonUtils.getStringValue(CommonUtils.getIntValue(row.getColumnValue("goodNum")) + CommonUtils.getIntValue(row.getColumnValue("goodGivingNum"))));
            }
            rowNames.put("price", "购买金额");
            rowNames.put("payType", "支付方式");
            rowNames.put("payId", "购买钻石总额");
            rowNames.put("goodNum", "基本钻石");
            rowNames.put("goodGivingNum", "赠予钻石");
            rowNames.put("isSuccess", "钻石到账");
            rowNames.put("createTime", "时间");
        }
        String fileName = "购买明细";
        ExcelVO vo = OutExcelUtil.download(fileName, rowNames, payList);
        FileUtil.FileDownload(vo.getFileName(), vo.getContent(), response);
    }


    /**
     * 导出Excel  返钻明细   2017-9-27
     */
    @RequiresPermissions(Permission.OPERATE_BONUS_QUERY_EXCEL)
    @RequestMapping(value = "downLoadBonusByDate")
    @ResponseBody
    public void downLoadBonusByDate(HttpServletResponse response
            , @RequestParam(value = "promoterId", defaultValue = "0") long promoterId) {
        DataTable rebate = LogRebateGetDao.list(promoterId);

        Map<String, String> rowNames = Maps.newLinkedHashMap();
        if (rebate.rows.length > 0) {
            rowNames.put("getDiamond", "提取返钻数量");
            rowNames.put("createTime", "申请时间");
        }
        String fileName = "返钻明细";
        ExcelVO vo = OutExcelUtil.download(fileName, rowNames, rebate);
        FileUtil.FileDownload(vo.getFileName(), vo.getContent(), response);
    }


}

