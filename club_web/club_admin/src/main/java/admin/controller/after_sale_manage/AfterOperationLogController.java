package admin.controller.after_sale_manage;

import admin.service.CusOperationLogService;
import com.google.common.base.Strings;
import dsqp.common_const.club_admin.Menu;
import dsqp.common_const.club_admin.Permission;
import dsqp.db.model.DataRow;
import dsqp.db.model.DataTable;
import dsqp.db.util.DateUtils;
import dsqp.util.CommonUtils;
import dsqp.util.FileUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by jeremy on 2017/8/14.
 */
@Controller
@RequestMapping("after_sale_manage/")
public class AfterOperationLogController {
    private final static String PATH = "after_sale_manage/";

    @Resource
    private CusOperationLogService cusOperationLogService;

    @RequestMapping("operationLog")
    public String index(Model model){
        String startDate = DateUtils.Date2String(dsqp.util.DateUtils.addDay(-7, new Date()), "yyyy-MM-dd");
        String endDate = DateUtils.Date2String(new Date(), "yyyy-MM-dd");
        model.addAttribute("startDate",startDate);
        model.addAttribute("endDate",endDate);
        return "after_sale_manage/operationLog";
    }


    @RequiresPermissions(Permission.SYS_QUERY_AFTER_SALE_OPERATION_SHOW)
    @RequestMapping("listOperation")
    public ModelAndView list() {
        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        ModelAndView mv = new ModelAndView(PATH +"ajax/operationLog");
        String menuItem = Menu.afterSaleManage.getMenuItem();
        String startDate = DateUtils.Date2String(dsqp.util.DateUtils.addDay(-7, new Date()), "yyyy-MM-dd");
        String endDate = DateUtils.Date2String(new Date(), "yyyy-MM-dd");
        DataTable dt = cusOperationLogService.getListByGameIdAndDate(gameId, menuItem,startDate,endDate);
        if (dt.rows.length > 0) {
            mv.addObject("operationList", dt.rows);
        }else {
            mv.addObject("operationList", null);
        }
        return mv;
    }

    /**
     * 操作记录查询
     * @param checkType
     * @param startDate
     * @param endDate
     * @return
     */
    @RequiresPermissions(Permission.SYS_QUERY_AFTER_SALE_OPERATION_SHOW)
    @RequestMapping("queryOperation")
    public ModelAndView query(
            @RequestParam(value = "checkType", defaultValue = "") String checkType
            ,@RequestParam(value = "startDate", defaultValue = "") String startDate
            , @RequestParam(value = "endDate", defaultValue = "") String endDate) {
        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        ModelAndView mv = new ModelAndView(PATH +"ajax/operationLog");
        if(!Strings.isNullOrEmpty(checkType) && !Strings.isNullOrEmpty(startDate) && !Strings.isNullOrEmpty(endDate)){
            if(Menu.afterSaleManage.getMenuItem().equals(checkType)){
                DataTable dt = cusOperationLogService.getListByGameIdAndDate(gameId, checkType,startDate,endDate);
                if (dt.rows.length > 0) {
                    mv.addObject("operationList", dt.rows);
                }
            }else {
                int _checkType = CommonUtils.getIntValue(checkType);
                DataTable dt = cusOperationLogService.getListByRecordTypeAndDate(gameId, _checkType,startDate,endDate);
                if (dt.rows.length > 0) {
                    mv.addObject("operationList", dt.rows);
                }
            }
        }else {
            mv.addObject("operationList", null);
        }

        return mv;
    }


    //导出excel
    @RequiresPermissions(Permission.OPERATE_AFTER_SALE_OPERATEION_DOWNLOAD_EXCEL)
    @RequestMapping("operation_download")
    public void operation_download(HttpServletResponse response
            ,@RequestParam(value = "checkType", defaultValue = "") String checkType
            ,@RequestParam(value = "startDate", defaultValue = "") String startDate
            , @RequestParam(value = "endDate", defaultValue = "") String endDate) {
        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));

        StringBuilder sb = new StringBuilder();
        sb.append("\t\t\t操作类型,")
                .append("\t操作内容,")
                .append("\t操作账号,")
                .append("\t时间,")
                .append("\r\n");

        if(!Strings.isNullOrEmpty(checkType) && !Strings.isNullOrEmpty(startDate) && !Strings.isNullOrEmpty(endDate)){
            if(Menu.afterSaleManage.getMenuItem().equals(checkType)){
                DataTable dt = cusOperationLogService.getListByGameIdAndDate(gameId, checkType,startDate,endDate);
                if (dt.rows.length > 0) {
                    //拼接cvs内容
                    for (DataRow row : dt.rows) {
                        sb.append("\t").append(row.getColumnValue("typeName")).append(",")
                                .append(row.getColumnValue("content") + "\t").append(",")
                                .append(row.getColumnValue("editAdmin") + "\t").append(",")
                                .append(row.getColumnValue("createTime")).append(",")
                                .append("\r\n");
                    }
                }
            }else {
                int _checkType = CommonUtils.getIntValue(checkType);
                DataTable dt = cusOperationLogService.getListByRecordTypeAndDate(gameId, _checkType,startDate,endDate);
                if (dt.rows.length > 0) {
                    //拼接cvs内容
                    for (DataRow row : dt.rows) {
                        sb.append("\t").append(row.getColumnValue("typeName")).append(",")
                                .append(row.getColumnValue("content") + "\t").append(",")
                                .append(row.getColumnValue("editAdmin") + "\t").append(",")
                                .append(row.getColumnValue("createTime")).append(",")
                                .append("\r\n");
                    }
                }
            }
        }


        //拼接文件名
        StringBuilder fileName = new StringBuilder(64);
        fileName.append("售后操作记录")
                .append(System.currentTimeMillis());
        //返回文件流
        FileUtil.FileDownload(fileName.toString(), sb.toString(), response);


    }



}
