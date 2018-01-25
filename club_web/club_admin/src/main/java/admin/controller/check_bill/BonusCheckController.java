package admin.controller.check_bill;

import admin.util.OutExcelUtil;
import admin.vo.ExcelVO;
import com.google.common.collect.Maps;
import dsqp.common_const.club_admin.Permission;
import dsqp.db.model.DataTable;
import dsqp.db_club.dao.BonusDao;
import dsqp.db_club.dao.DayBonusDetailDao;
import dsqp.db_club.dao.DayBonusParentDao;
import dsqp.db_club.dao.DayBonusReportDao;
import dsqp.db_club.model.BonusModel;
import dsqp.util.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 提成审核
 * Created by mj on 2017/7/29.
 */

@Controller("bonusCheck")
@RequestMapping("check_bill")
public class BonusCheckController {
    private final static String PATH = "check_bill/";

    @RequestMapping("bonusCheck")
    public ModelAndView bonusCheck() {
        ModelAndView mv = new ModelAndView(PATH + "bonusCheck");

        Subject subject = SecurityUtils.getSubject();
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));

        Date startDate = DateUtils.String2Date(TimeUtil.lastFirday());
        Date endDate = DateUtils.String2Date(TimeUtil.lastSunday());
        BonusModel b1 = BonusDao.getByDate(gameId, startDate, endDate);      //上周审核提成
        if (b1 != null) {
            mv.addObject("bonus", b1);
            mv.addObject("isPass", b1.getIsPass());
        }

        return mv;
    }

    /**
     * 提成审核查询
     * @param bonusId
     * @return
     */
    @RequiresPermissions(Permission.SYS_BONUS_CHECK_QUERY_SHOW)
    @RequestMapping("ajax/dailyTable")
    public ModelAndView ajax_dailyTable(
            @RequestParam(value = "bonusId", defaultValue = "0") long bonusId) {
        ModelAndView mv = new ModelAndView(PATH + "ajax/bonusCheck");

        if (bonusId != 0) {
            DataTable dt = DayBonusReportDao.getDailyTable(bonusId);      //每日简报
            if (dt.rows.length > 0) {
                mv.addObject("dailyTable", dt.rows);
            }
        }
        return mv;
    }

    @RequestMapping("ajax/getTotal")
    @ResponseBody
    public void ajax_getTotal(HttpServletResponse response,
                              @RequestParam(value = "bonusId", defaultValue = "0") long bonusId) {

        if (bonusId != 0) {
            DataTable dt = DayBonusReportDao.getDailyTableTotal(bonusId);      //每日简报汇总信息
            if (dt.rows.length > 0) {
                double payTotal = CommonUtils.getDoubleValue(dt.rows[0].getColumnValue("payTotal"));
                double bonusTotal = CommonUtils.getDoubleValue(dt.rows[0].getColumnValue("bonusTotal"));

                Map<String, Double> map = Maps.newHashMap();
                map.put("payTotal", payTotal);
                map.put("bonusTotal", bonusTotal);
                String json = JsonUtils.getJson(map);
                RequestUtils.write(response, json);
            }
        }
    }

/*    @RequestMapping("ajax/isCheckedBonus")
    @ResponseBody
    public void ajax_isCheckedBonus(HttpServletResponse response,
            @RequestParam(value = "bonusId", defaultValue = "0") long bonusId) {
            RequestUtils.write(response, String.valueOf(DayBonusReportDao.isCheckedBonus(bonusId)));
    }*/

    /**
     * 确认单日审核
     * by ds
     */
    @RequiresPermissions(Permission.OPERATE_CHECK_ONE_DAY_BONUS_UPDATE)
    @RequestMapping("ajax/checkOneDayBonus")
    @ResponseBody
    public void ajax_checkDailyBonus(HttpServletResponse response,
                                     @RequestParam(value = "bonusId", defaultValue = "0") long bonusId,
                                     @RequestParam(value = "payCreateDate", defaultValue = "") String payCreateDate) {

        if (bonusId != 0) {
            int result = DayBonusReportDao.updateIsPass(bonusId, DateUtils.String2Date(payCreateDate), true);      //每日简报
            RequestUtils.write(response, String.valueOf(result));
        }
    }

    /**
     * 确认审核通过
     * by ds
     */
    @RequiresPermissions(Permission.OPERATE_GIVING_BONUS_UPDATE)
    @RequestMapping("ajax/givingBonus")
    @ResponseBody
    public void ajax_givingBonus(HttpServletResponse response,
                                 @RequestParam(value = "bonusId", defaultValue = "0") long bonusId) {

        boolean isCheckBonus = DayBonusReportDao.isCheckedBonus(bonusId);
        if (bonusId != 0 && isCheckBonus) {
//            System.out.println("发送");
            boolean result = DayBonusDetailDao.givingBonusByBonusId(bonusId);      //每日简报
            RequestUtils.write(response, String.valueOf(result));
        }
    }

    /**
     * 摘要
     */
    @RequiresPermissions(Permission.SYS_BONUS_DIGEST_SHOW)
    @RequestMapping("ajax/digestTable")
    public ModelAndView ajax_digestTable(
            @RequestParam(value = "payCreateDate", defaultValue = "") String payCreateDate,
            @RequestParam(value = "bonusId", defaultValue = "0") int bonusId) {
        ModelAndView mv = new ModelAndView(PATH + "ajax/modal/digest");

        DataTable dt = DayBonusParentDao.getDigest(bonusId, DateUtils.String2Date(payCreateDate));      //每日简报 :: 摘要
        if (dt.rows.length > 0) {
            mv.addObject("digestTable", dt.rows);
        }
        mv.addObject("date", payCreateDate);
        mv.addObject("bonusId", bonusId);

        return mv;
    }

    /**
     * 单笔明细
     * by ds
     */
    @RequestMapping("ajax/singleDetail")
    public ModelAndView ajax_singleDetail(
            @RequestParam(value = "bonusId", defaultValue = "0") long bonusId) {
        ModelAndView mv = new ModelAndView(PATH + "ajax/bonusCheck");
        if (bonusId != 0) {
            DataTable dt = DayBonusParentDao.getListByBonusId(bonusId);
            if (dt.rows.length > 0) {
                mv.addObject("singleDetail", dt.rows);
            }
        }
        return mv;
    }

    /**
     * 导出Excel
     *
     * @param response
     */
    @RequiresPermissions(Permission.OPERATE_BONUS_CHECK_EXCEL)
    @RequestMapping(value = "dailyTableDownload")
    @ResponseBody
    public void dailyTableDownload(HttpServletResponse response,
                                   @RequestParam(value = "bonusId", defaultValue = "0") long bonusId) {
        if (bonusId != 0) {

            Subject subject = SecurityUtils.getSubject();
            int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));

            DataTable dt = DayBonusReportDao.getDailyTable2(gameId, bonusId);      //每日简报
            if (dt.rows.length > 0) {
                Map<String, String> rowNames = Maps.newLinkedHashMap();
                rowNames.put("bonusPeopleNum", "提成人数");
                rowNames.put("payTotal", "充值总额");
                rowNames.put("allPayTotal", "累计代理商充值");
                rowNames.put("depositRemain", "累计代理商提成剩余");
                rowNames.put("bonusBuyTotal", "累计提成金购买");
                rowNames.put("bonusPayTotal", "可提成充值总额");
                rowNames.put("directBonus", "直属提成");
                rowNames.put("nonDirectBonus", "非直属提成");
                rowNames.put("payCreateDate", "时间");
                ExcelVO vo = OutExcelUtil.download("提成查询", rowNames, dt);

                FileUtil.FileDownload(vo.getFileName(), vo.getContent(), response);
            }
        }

    }

    /*    @RequestMapping(value = "pay_dailyTableDownload")
        @ResponseBody
        public void pay_dailyTableDownload(HttpServletResponse response,
                             @RequestParam(value = "bonusId", defaultValue = "0") long bonusId,
                             @RequestParam(value = "fileName", defaultValue = "") String fileName){
            Subject subject = SecurityUtils.getSubject();
            int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));

            if (bonusId != 0) {
                DataTable dt = DayBonusDetailDao.getPayByBonusId(bonusId, gameId);      //每日简报
                if (dt.rows.length > 0) {
                    Map<String, String> rowNames = Maps.newLinkedHashMap();
                    rowNames.put("totalPromoter", "代理商人数");
                    rowNames.put("totalBonusPromoter", "提成人数");
                    rowNames.put("totalPayPeople", "充值人数");
                    rowNames.put("totalPay", "充值金额");
                    rowNames.put("parentBonus", "直属提成");
                    rowNames.put("nonParentBonus", "非直属提成");
                    rowNames.put("payCreateDate", "时间");
                    ExcelVO vo = OutExcelUtil.download("充值报表_每日简表", rowNames, dt);

                    FileUtil.FileDownload(vo.getFileName(), vo.getContent(), response);
                }
            }

        }*/
    @RequestMapping(value = "singleDetailDownload")
    @ResponseBody
    public void singleDetailDownload(HttpServletResponse response,
                                     @RequestParam(value = "bonusId", defaultValue = "0") long bonusId,
                                     @RequestParam(value = "fileName", defaultValue = "") String fileName) {

        if (bonusId != 0) {
            DataTable dt = DayBonusDetailDao.getListByBonusId(bonusId);      //单笔明细
            if (dt.rows.length > 0) {
                Map<String, String> rowNames = Maps.newLinkedHashMap();
                rowNames.put("promoterId", "代理商ID");
                rowNames.put("pay", "充值金额");
                rowNames.put("parentId", "直属上线ID");
                rowNames.put("parentBonus", "直属上线提成");
                rowNames.put("nonParentId", "非直属上线ID");
                rowNames.put("nonParentBonus", "非直属上线提成");
                rowNames.put("payCreateTime", "时间");
                ExcelVO vo = OutExcelUtil.download(fileName, rowNames, dt);

                FileUtil.FileDownload(vo.getFileName(), vo.getContent(), response);
            }
        }

    }

}

