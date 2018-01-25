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
import dsqp.util.CommonUtils;
import dsqp.util.DateUtils;
import dsqp.util.FileUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 提成查询
 * Created by mj on 2017/7/29.
 */

@Controller
@RequestMapping("check_bill")
public class BonusQueryController {
    private final static String PATH = "check_bill/";

    @RequestMapping("bonusQuery")
    public ModelAndView bonusReport() {
        ModelAndView mv = new ModelAndView(PATH + "bonusQuery");

        Subject subject = SecurityUtils.getSubject();
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));

        DataTable dt = BonusDao.listByGameIdAndIsPass(gameId, true);
        if (dt.rows.length > 0) {
            mv.addObject("bonusDt", dt.rows);
        }

        return mv;
    }

    /**
     * 提成查询
     * @return
     */
    @RequiresPermissions(Permission.SYS_BONUS_QUERY_SHOW)
    @RequestMapping("ajax/dailyTable_r")
    public ModelAndView ajax_dailyTable_r(
            @RequestParam(value = "bonusId", defaultValue = "0") long bonusId) {
        ModelAndView mv = new ModelAndView(PATH + "ajax/bonusReport");

        Subject subject = SecurityUtils.getSubject();
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
        if (bonusId != 0) {
            DataTable dt = DayBonusReportDao.getDailyTable2(gameId, bonusId);      //每日简报
            if (dt.rows.length > 0) {
                mv.addObject("dailyTable", dt.rows);
            }
        }
        return mv;
    }

    @RequestMapping("ajax/singleDetail_r")
    public ModelAndView ajax_singleDetail_r(
            @RequestParam(value = "bonusId", defaultValue = "0") long bonusId) {
        ModelAndView mv = new ModelAndView(PATH + "ajax/bonusReport");

        if (bonusId != 0) {
            DataTable dt = DayBonusDetailDao.getListByBonusId(bonusId);     //单笔明细
            if (dt.rows.length > 0) {
                mv.addObject("singleDetail", dt.rows);
            }
        }
        return mv;
    }

    /**
     * 导出Excel 提成明细
     */
    @RequiresPermissions(Permission.OPERATE_BONUS_QUERY_EXCEL)
    @RequestMapping(value = "digestDownload")
    @ResponseBody
    public void dailyTableDownload(HttpServletResponse response,
                                   @RequestParam(value = "payCreateDate", defaultValue = "") String payCreateDate,
                                   @RequestParam(value = "bonusId", defaultValue = "0") int bonusId) {
        if (bonusId != 0) {
            DataTable dt = DayBonusParentDao.getDigest(bonusId, DateUtils.String2Date(payCreateDate));      //每日简报 :: 摘要

            if (dt.rows.length > 0) {
                Map<String, String> rowNames = Maps.newLinkedHashMap();
                rowNames.put("promoterId", "代理商ID");
                rowNames.put("parentBonusTime", "直属提成笔数");
                rowNames.put("parentBonus", "直属提成金额");
                rowNames.put("nonParentBonusTime", "非直属提成笔数");
                rowNames.put("nonParentBonus", "非直属提成金额");
                rowNames.put("totalBonus", "提成总额");
                ExcelVO vo = OutExcelUtil.download("提成明细", rowNames, dt);

                FileUtil.FileDownload(vo.getFileName(), vo.getContent(), response);
            }
        }

    }


}


