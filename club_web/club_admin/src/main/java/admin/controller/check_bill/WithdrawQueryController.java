package admin.controller.check_bill;

import admin.service.WithdrawCheckService;
import com.google.common.base.Strings;
import dsqp.common_const.club_admin.Permission;
import dsqp.db.model.DataTable;
import dsqp.db.util.DateUtils;
import dsqp.util.CommonUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 提现查询
 * Created by fx on 2017/9/21.
 */
@Controller
@RequestMapping("check_bill")
public class WithdrawQueryController {

    @Resource
    WithdrawCheckService withdrawCheckService;

    private final static String PATH = "check_bill/";

    //按时间段查询
    @RequiresPermissions(Permission.SYS_QUERY_WITHDRAW_SHOW)
    @RequestMapping("withdrawQuery")
    public ModelAndView withdrawQuery(
            @RequestParam(value = "startDate", defaultValue = "") String startDate
            , @RequestParam(value = "endDate", defaultValue = "") String endDate) {

        Session session = SecurityUtils.getSubject().getSession();
        String gameName = CommonUtils.getStringValue(session.getAttribute("gameName"));
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        ModelAndView mv = new ModelAndView(PATH + "withdrawQuery");
        if (Strings.isNullOrEmpty(startDate) && Strings.isNullOrEmpty(endDate)) {
            startDate = DateUtils.Date2String(dsqp.util.DateUtils.addDay(-7, new Date()), "yyyy-MM-dd");
            mv.addObject("endDate", DateUtils.Date2String(dsqp.util.DateUtils.addDay(-1, new Date()), "yyyy-MM-dd"));
            endDate = DateUtils.Date2String(dsqp.util.DateUtils.addDay(-1, new Date()), "yyyy-MM-dd");
        } else {
            mv.addObject("endDate", endDate);
        }
        mv.addObject("startDate", startDate);
        mv.addObject("gameName", gameName);
        DataTable dt = withdrawCheckService.listRequestByDate(startDate, endDate, gameId);
        if (dt.rows.length > 0) {
            mv.addObject("dt", dt.rows);
        }
        return mv;
    }
}

