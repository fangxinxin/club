package admin.controller.pre_sale_manage;

import admin.util.OutExcelUtil;
import admin.vo.ExcelVO;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import dsqp.common_const.club_admin.Permission;
import dsqp.db.model.DataRow;
import dsqp.db.model.DataTable;
import dsqp.db_club.dao.PromoterOrderDao;
import dsqp.util.CommonUtils;
import dsqp.util.DateUtils;
import dsqp.util.FileUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * Created by jeremy on 2017/9/21.
 */
@Controller
@RequestMapping("pre_sale_manage/")
public class OrderQueryController {

    @RequestMapping("orderQuery")
    public String index(Model model,
                        @RequestParam(value = "startDate", defaultValue = "") String startDate,
                        @RequestParam(value = "endDate", defaultValue = "") String endDate) {
        //默认取7天的数据
        Date d1 = DateUtils.String2Date(startDate, "yyyy-MM-dd", DateUtils.addDay(-6, new Date()));
        Date d2 = DateUtils.String2Date(endDate, "yyyy-MM-dd", new Date());
        model.addAttribute("startDate", DateUtils.Date2String(d1));
        model.addAttribute("endDate", DateUtils.Date2String(d2));
        return "pre_sale_manage/order_query";
    }


    /**
     * 工单查询 按日期
     */
    @RequiresPermissions(Permission.SYS_QUERY_ORDEER_BY_DATE_SHOW)
    @RequestMapping("dateQuery")
    public String dateQuery(Model model,
                            @RequestParam(value = "startDate", defaultValue = "") String startDate,
                            @RequestParam(value = "endDate", defaultValue = "") String endDate) {

        Subject subject = SecurityUtils.getSubject();
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
        //默认取7天的数据
        Date d1 = DateUtils.String2Date(startDate, "yyyy-MM-dd", DateUtils.addDay(-6, new Date()));
        Date d2 = DateUtils.String2Date(endDate, "yyyy-MM-dd", new Date());
        DataTable dt = PromoterOrderDao.queryListByDate(gameId, d1, d2);

        if (dt.rows.length > 0) {
            model.addAttribute("dateDt", dt.rows);
        }
        model.addAttribute("startDate", DateUtils.Date2String(d1));
        model.addAttribute("endDate", DateUtils.Date2String(d2));

        return "pre_sale_manage/ajax/order_query_detail";

    }


    /**
     * 导出Excel  工单查询 按日期
     */
    @RequiresPermissions(Permission.OPERATE_ORDER_QUERY_EXCEL)
    @RequestMapping(value = "byDateDownload")
    @ResponseBody
    public void promoterPayDownload(HttpServletResponse response,
                                    @RequestParam(value = "startDate", defaultValue = "") String startDate,
                                    @RequestParam(value = "endDate", defaultValue = "") String endDate) {

        Subject subject = SecurityUtils.getSubject();
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
        //默认取7天的数据
        Date d1 = DateUtils.String2Date(startDate, "yyyy-MM-dd", DateUtils.addDay(-6, new Date()));
        Date d2 = DateUtils.String2Date(endDate, "yyyy-MM-dd", new Date());
        DataTable dt = PromoterOrderDao.queryListByDate(gameId, d1, d2);
        Map<String, String> rowNames = Maps.newLinkedHashMap();
        if (dt.rows.length > 0) {
            rowNames.put("totalNum", "累计处理工单数");
            rowNames.put("newNum", "当天新增工单");
            rowNames.put("totalFormalNum", "累计转正工单");
            rowNames.put("formalNum", "当天新增转正");
            rowNames.put("createDate", "时间");
            rowNames.put("monthInitNum", "当月未转正");
            rowNames.put("totalInitNum", "累计未转正");
        }
        String fileName = "工单查询（按日期）";
        ExcelVO vo = OutExcelUtil.download(fileName, rowNames, dt);
        FileUtil.FileDownload(vo.getFileName(), vo.getContent(), response);
    }

    /**
     * 查询当日工单明细
     */
    @RequiresPermissions(Permission.SYS_QUERY_ORDEER_DETAIL_SHOW)
    @RequestMapping("queryByDate")
    public String queryByDate(Model model,
                              @RequestParam(value = "date", defaultValue = "") String date) {


        int gameId = CommonUtils.getIntValue(SecurityUtils.getSubject().getSession().getAttribute("gameId"));
        DataTable dt = PromoterOrderDao.getListByDate(gameId, DateUtils.String2Date(date));
        if (dt.rows.length > 0) {
            model.addAttribute("dt", dt.rows);
            for (DataRow row : dt.rows) {
                if (Strings.isNullOrEmpty(row.getColumnValue("peopleNum"))) {
                    row.setColumnValue("peopleNum", "0");
                }
            }
        }
        model.addAttribute("date", date);
        return "pre_sale_manage/ajax/dateDetail";
    }

    /**
     * 导出Excel  工单查询 按日期
     */
    @RequiresPermissions(Permission.OPERATE_ORDER_QUERY_EXCEL)
    @RequestMapping(value = "detailDownload")
    @ResponseBody
    public void byDateDownload(HttpServletResponse response,
                               @RequestParam(value = "date", defaultValue = "") String date) {

        int gameId = CommonUtils.getIntValue(SecurityUtils.getSubject().getSession().getAttribute("gameId"));
        DataTable dt = PromoterOrderDao.getListByDate(gameId, DateUtils.String2Date(date));
        Map<String, String> rowNames = Maps.newLinkedHashMap();
        if (dt.rows.length > 0) {
            for (DataRow row : dt.rows) {
                if ("0".equals(row.getColumnValue("promoterStatus"))) {
                    row.setColumnValue("promoterStatus", "待转正");
                } else if ("1".equals(row.getColumnValue("promoterStatus"))) {
                    row.setColumnValue("promoterStatus", "已转正");
                } else if ("2".equals(row.getColumnValue("promoterStatus"))||"3".equals(row.getColumnValue("promoterStatus"))) {
                    row.setColumnValue("promoterStatus", "已取消");
                }
                if (Strings.isNullOrEmpty(row.getColumnValue("peopleNum"))) {
                    row.setColumnValue("peopleNum", "0");
                }
            }
            rowNames.put("editAdmin", "员工名称");
            rowNames.put("gameUserId", "玩家ID");
            rowNames.put("gameNickName", "玩家昵称");
            rowNames.put("clubId", "俱乐部ID");
            rowNames.put("peopleNum", "俱乐部人数");
            rowNames.put("createTime", "开通时间");
            rowNames.put("changeTime", "转正时间");
            rowNames.put("promoterStatus", "俱乐部状态");
        }
        String fileName = "工单明细";
        ExcelVO vo = OutExcelUtil.download(fileName, rowNames, dt);
        FileUtil.FileDownload(vo.getFileName(), vo.getContent(), response);
    }

    /**
     * 工单查询 按员工
     */
    @RequiresPermissions(Permission.SYS_QUERY_ORDEER_BY_PEOPLE_SHOW)
    @RequestMapping("editAdminQuery")
    public String editAdminQuery(Model model,
                                 @RequestParam(value = "editAdmin", defaultValue = "") String editAdmin) {

        Subject subject = SecurityUtils.getSubject();
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
        DataTable dt = Strings.isNullOrEmpty(editAdmin) ? PromoterOrderDao.queryAllEditAdmin(gameId) : PromoterOrderDao.queryAllByEditAdmin(gameId, editAdmin);
        if (dt.rows.length > 0) {
            model.addAttribute("editAdminDt", dt.rows);
        }
        return "pre_sale_manage/ajax/order_query_detail";

    }


    /**
     * 导出Excel  工单查询 按员工
     */
    @RequiresPermissions(Permission.OPERATE_ORDER_QUERY_EXCEL)
    @RequestMapping(value = "byEditAdminDownload")
    @ResponseBody
    public void byEditAdminDownload(HttpServletResponse response,
                                    @RequestParam(value = "editAdmin", defaultValue = "") String editAdmin){

        Subject subject = SecurityUtils.getSubject();
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
        DataTable dt = Strings.isNullOrEmpty(editAdmin) ? PromoterOrderDao.queryAllEditAdmin(gameId) : PromoterOrderDao.queryAllByEditAdmin(gameId, editAdmin);

        if (dt.rows.length > 0) {
            for (DataRow row : dt.rows) {
                if (Strings.isNullOrEmpty(row.getColumnValue("peopleNum"))) {
                    row.setColumnValue("peopleNum", "0");
                }
            }
        }
        Map<String, String> rowNames = Maps.newLinkedHashMap();
        if (dt.rows.length > 0) {
            rowNames.put("editAdmin", "员工姓名");
            rowNames.put("num", "累计处理工单数");
            rowNames.put("monthNew", "当天新增工单");
            rowNames.put("lastMonthNew", "上月新增工单");
            rowNames.put("allFormal", "累计转正工单");
            rowNames.put("monthFormal", "当月新增转正");
            rowNames.put("lastMonthFormal", "上月新增转正");
            rowNames.put("monthInit", "当月未转正");
            rowNames.put("allInit", "累计未转正");
        }
        String fileName = "工单查询（按员工）";
        ExcelVO vo = OutExcelUtil.download(fileName, rowNames, dt);
        FileUtil.FileDownload(vo.getFileName(), vo.getContent(), response);
    }

    /**
     * 查询指定员工工单明细
     */
    @RequiresPermissions(Permission.SYS_QUERY_ORDEER_DETAIL_SHOW)
    @RequestMapping("queryOneDetail")
    public String queryOneDetail(Model model,
                                 @RequestParam(value = "editAdmin", defaultValue = "") String editAdmin) {


        int gameId = CommonUtils.getIntValue(SecurityUtils.getSubject().getSession().getAttribute("gameId"));
        DataTable dt = PromoterOrderDao.getOneDetail(gameId, editAdmin);
        if (dt.rows.length > 0) {
            for (DataRow row : dt.rows) {
                if (Strings.isNullOrEmpty(row.getColumnValue("peopleNum"))) {
                    row.setColumnValue("peopleNum", "0");
                }
            }
            model.addAttribute("editAdminDt", dt.rows);
        }
        model.addAttribute("editAdmin", editAdmin);
        return "pre_sale_manage/ajax/dateDetail";
    }

    /**
     * 导出Excel  工单明细 按员工
     */
    @RequiresPermissions(Permission.OPERATE_ORDER_QUERY_EXCEL)
    @RequestMapping(value = "editAdminDetailDownload")
    @ResponseBody
    public void editAdminDetailDownload(HttpServletResponse response,
                                        @RequestParam(value = "editAdmin", defaultValue = "") String editAdmin){

        int gameId = CommonUtils.getIntValue(SecurityUtils.getSubject().getSession().getAttribute("gameId"));
        DataTable dt = PromoterOrderDao.getOneDetail(gameId, editAdmin);
        Map<String, String> rowNames = Maps.newLinkedHashMap();
        if (dt.rows.length > 0) {
            for (DataRow row : dt.rows) {
                if ("0".equals(row.getColumnValue("promoterStatus"))) {
                    row.setColumnValue("promoterStatus", "待转正");
                } else if ("1".equals(row.getColumnValue("promoterStatus"))) {
                    row.setColumnValue("promoterStatus", "已转正");
                } else if ("2".equals(row.getColumnValue("promoterStatus"))||"3".equals(row.getColumnValue("promoterStatus"))) {
                    row.setColumnValue("promoterStatus", "已取消");
                }
                if (Strings.isNullOrEmpty(row.getColumnValue("peopleNum"))) {
                    row.setColumnValue("peopleNum", "0");
                }
            }
            rowNames.put("editAdmin", "员工姓名");
            rowNames.put("gameUserId", "玩家ID");
            rowNames.put("gameNickName", "玩家昵称");
            rowNames.put("clubId", "俱乐部ID");
            rowNames.put("peopleNum", "俱乐部人数");
            rowNames.put("createTime", "开通时间");
            rowNames.put("changeTime", "转正时间");
            rowNames.put("promoterStatus", "俱乐部状态");
        }
        String fileName = "工单明细（按员工）";
        ExcelVO vo = OutExcelUtil.download(fileName, rowNames, dt);
        FileUtil.FileDownload(vo.getFileName(), vo.getContent(), response);
    }

}
