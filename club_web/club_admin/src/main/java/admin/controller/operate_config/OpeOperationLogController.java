package admin.controller.operate_config;

import admin.service.CusOperationLogService;
import com.google.common.base.Strings;
import dsqp.common_const.club_admin.Menu;
import dsqp.common_const.club_admin.Permission;
import dsqp.db.model.DataRow;
import dsqp.db.model.DataTable;
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
 * Created by jeremy on 2017/7/30.
 */
@Controller
@RequestMapping("operate_config/")
public class OpeOperationLogController {
    private final static String PATH = "operate_config/";

    @Resource
    private CusOperationLogService cusOperationLogService;

    @RequestMapping("operationLog")
    public String index(Model model){
        String startDate = dsqp.db.util.DateUtils.Date2String(dsqp.util.DateUtils.addDay(-7, new Date()), "yyyy-MM-dd");
        String endDate = dsqp.db.util.DateUtils.Date2String(new Date(), "yyyy-MM-dd");
        model.addAttribute("startDate",startDate);
        model.addAttribute("endDate",endDate);
        return "operate_config/operationLog";
    }

    /**
     * 运营配置操作记录查询
     * @return
     */
    @RequiresPermissions(Permission.SYS_QUERY_OPERATE_CONFIG_OPERATION_SHOW)
    @RequestMapping("listOperation")
    public ModelAndView list() {
        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        ModelAndView mv = new ModelAndView(PATH +"ajax/operationLog");
        String menuItem = Menu.operatingConfig.getMenuItem();
        String startDate = dsqp.db.util.DateUtils.Date2String(dsqp.util.DateUtils.addDay(-7, new Date()), "yyyy-MM-dd");
        String endDate = dsqp.db.util.DateUtils.Date2String(new Date(), "yyyy-MM-dd");
        DataTable dt = cusOperationLogService.getListByGameIdAndDate(gameId, menuItem,startDate,endDate);
        if (dt.rows.length > 0) {
            mv.addObject("operationList", dt.rows);
        }else {
            mv.addObject("operationList", null);
        }
        return mv;
    }

    /**
     * 运营配置操作记录查询
     * @return
     */
    @RequiresPermissions(Permission.SYS_QUERY_OPERATE_CONFIG_OPERATION_SHOW)
    @RequestMapping("queryOperation")
    public ModelAndView query(
            @RequestParam(value = "checkType", defaultValue = "") String checkType
            ,@RequestParam(value = "startDate", defaultValue = "") String startDate
            , @RequestParam(value = "endDate", defaultValue = "") String endDate) {
        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        ModelAndView mv = new ModelAndView(PATH +"ajax/operationLog");

        if(!Strings.isNullOrEmpty(checkType) && !Strings.isNullOrEmpty(startDate) && !Strings.isNullOrEmpty(endDate)){
            if(Menu.operatingConfig.getMenuItem().equals(checkType)){
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


    /**
     * 运营配置操作记录导出Excel
     */
    @RequiresPermissions(Permission.OPERATE_OPERATE_CONFIG_OPERATEION_DOWNLOAD_EXCEL)
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
            if(Menu.operatingConfig.getMenuItem().equals(checkType)){
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
        fileName.append("后台操作记录")
                .append(System.currentTimeMillis());
        //返回文件流
        FileUtil.FileDownload(fileName.toString(), sb.toString(), response);


    }


}
