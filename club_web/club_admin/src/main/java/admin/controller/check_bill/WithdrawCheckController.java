package admin.controller.check_bill;

import admin.service.WithdrawCheckService;
import com.google.common.base.Strings;
import dsqp.common_const.club_admin.Permission;
import dsqp.common_const.club_admin.WithdrawRequest;
import dsqp.db.model.DataRow;
import dsqp.db.model.DataTable;
import dsqp.db.util.DateUtils;
import dsqp.db_club.dao.WithdrawRequestDao;
import dsqp.db_club.model.WithdrawRequestModel;
import dsqp.util.CommonUtils;
import dsqp.util.FileUtil;
import dsqp.util.RequestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * 提现审核
 * Created by fx on 2017/7/29.
 */
@Controller
@RequestMapping("check_bill/withdrawCheck")
public class WithdrawCheckController {

    @Resource
    WithdrawCheckService withdrawCheckService;

    private final static String PATH = "check_bill/";

    /**
     * 提现审核查询
     * @return
     */
    //按时间段查询提现统计数据
    @RequiresPermissions(Permission.SYS_QUERY_WITHDRAW_CHECK_SHOW)
    @RequestMapping("queryWithdrawCheck")
    public ModelAndView queryWithdrawCheck(
            @RequestParam(value = "startDate", defaultValue = "") String startDate
            , @RequestParam(value = "endDate", defaultValue = "") String endDate) {

        Session session = SecurityUtils.getSubject().getSession();
        String gameName = CommonUtils.getStringValue(session.getAttribute("gameName"));
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        ModelAndView mv = new ModelAndView(PATH + "withdrawCheck");
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
        mv.addObject("dt", dt.rows);
        return mv;
    }

    //下载提现审核数据
    @RequiresPermissions(Permission.OPERATE_WITHDRAW_QUERY_EXCEL)
    @RequestMapping("download")
    public void download(
            @RequestParam(value = "startDate", defaultValue = "") String startDate
            , @RequestParam(value = "endDate", defaultValue = "") String endDate
            , HttpServletResponse response) {

        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        endDate = DateUtils.Date2String(dsqp.util.DateUtils.addDay(1, dsqp.util.DateUtils.String2Date(endDate)));
        DataTable dt = withdrawCheckService.listRequestByDate(startDate, endDate, gameId);

        //拼接要输出的字符串
        StringBuilder sb = new StringBuilder();
        sb.append("\t\t\t提现笔数,")
                .append("\t提现人数,")
                .append("\t提现金额,")
                .append("\t可提现余额,")
                .append("\t时间,")
                .append("\t是否打款,")
                .append("\r\n");
        //拼接cvs内容
        for (DataRow row : dt.rows) {
            sb.append("\t").append(row.getColumnValue("requestNum")).append(",")
                    .append(row.getColumnValue("peopleNum")).append(",")
                    .append(!Strings.isNullOrEmpty(row.getColumnValue("withdrawTotal")) ? row.getColumnValue("withdrawTotal") : "0").append(",")
                    .append(!Strings.isNullOrEmpty(row.getColumnValue("remainTotal")) ? row.getColumnValue("remainTotal") : "0").append(",")
                    .append(row.getColumnValue("createTime")).append(",")
                    .append(row.getColumnValue("withdrawStatus").equals("1") ? "已打款" : "未打款").append(",")
                    .append("\r\n");
        }
        //拼接文件名
        StringBuilder fileName = new StringBuilder(64);
        fileName.append("提现审核")
                .append(System.currentTimeMillis());
        //返回文件流
        FileUtil.FileDownload(fileName.toString(), sb.toString(), response);
    }

    /**
     * 提现审核查询
     * @return
     */
    //按天查询提现明细
    @RequiresPermissions(Permission.SYS_DAILY_WITHDRAW_CHECK_SHOW)
    @RequestMapping("queryDayDetail")
    public ModelAndView queryDayDetail(
            @RequestParam(value = "date", defaultValue = "") String date) {

        ModelAndView mv = new ModelAndView(PATH + "ajax/withdrawDetail");
        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        if (!Strings.isNullOrEmpty(date)) {
            DataTable dt = withdrawCheckService.listRequestByDate(date, gameId);
            if (dt.rows.length > 0) {
                mv.addObject("dt", dt.rows);
                mv.addObject("date", date);
                mv.addObject("date2", date.split("-")[0] + "年" + date.split("-")[1] + "月" + date.split("-")[2] + "日");
            }
        }
        return mv;
    }

    //下载每日提现明细
    @RequiresPermissions(Permission.OPERATE_WITHDRAW_DETAIL_EXCEL)
    @RequestMapping("downloadDetail")
    public void downloadDetail(
            @RequestParam(value = "date", defaultValue = "") String date
            , HttpServletResponse response) {

        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        DataTable dt = withdrawCheckService.listRequestByDate(date, gameId);
        if (dt.rows.length > 0) {
            for (DataRow row : dt.rows) {
                String bankArea = row.getColumnValue("bankArea");
                if (!Strings.isNullOrEmpty(bankArea)) {
                    row.setColumnValue("bankArea", bankArea.split("-")[2]);
                    row.setColumnValue("createDate", bankArea.split("-")[0] + "-" + bankArea.split("-")[1]);//作为开户地字段
                } else {
                    row.setColumnValue("bankArea", "暂无数据");
                    row.setColumnValue("createDate", "暂无数据");
                }
            }
        }
        //拼接要输出的字符串
        StringBuilder sb = new StringBuilder();
        sb.append("\t\t\t代理商ID,")
                .append("\t昵称,")
                .append("\t提现金额,")
                .append("\t姓名,")
                .append("\t开户地,")
                .append("\t开户银行,")
                .append("\t银行卡号,")
                .append("\t身份证号,")
                .append("\t手机号,")
                .append("\t流水号,")
                .append("\t回执号,")
                .append("\t备注,")
                .append("\t时间,")
                .append("\r\n");
        //拼接cvs内容
        for (DataRow row : dt.rows) {
            sb.append("\t").append(row.getColumnValue("promoterId")).append(",")
                    .append(row.getColumnValue("nickName")).append(",")
                    .append(row.getColumnValue("withdrawAmount")).append(",")
                    .append(row.getColumnValue("realName")).append(",")
                    .append(row.getColumnValue("createDate")).append(",")
                    .append(row.getColumnValue("bankArea")).append(",")
                    .append(row.getColumnValue("bankAccount") + "\t").append(",")
                    .append(row.getColumnValue("IDCard") + "\t").append(",")
                    .append(row.getColumnValue("cellPhone") + "\t").append(",")
                    .append(row.getColumnValue("serialNo") + "\t").append(",")
                    .append(row.getColumnValue("receiptNo") + "\t").append(",")
                    .append(row.getColumnValue("remark")).append(",")
                    .append(row.getColumnValue("createTime") + "\t").append(",")
                    .append("\r\n");
        }
        //拼接文件名
        StringBuilder fileName = new StringBuilder(64);
        fileName.append("提现明细").append(System.currentTimeMillis());
        //返回文件流
        FileUtil.FileDownload(fileName.toString(), sb.toString(), response);
    }


    //表单导入
    @RequiresPermissions(Permission.OPERATE_WITHDRAW_UPLOAD_EXCEL)
    @RequestMapping("upload")
    public void upload(@RequestParam("file") MultipartFile file,
                       @RequestParam(value = "createTime", defaultValue = "") String createTime,
                       HttpServletResponse response) throws IOException {

        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        DataTable dt = withdrawCheckService.listRequestByDate(createTime, gameId);
        if (dt != null && dt.rows.length > 0) {
            if (!file.isEmpty()) {
                try {
                    byte[] bs = file.getBytes();
                    String[] split = new String(bs, "gbk").split("\n");
                    if (split.length != dt.rows.length + 1) {
                        RequestUtils.write(response, "行数不匹配，请重新上传");
                        return;
                    } else {
                        boolean flag = true;
                        for (int i = 0; i < split.length - 1; i++) {
                            String[] tds = split[i + 1].split(",");
                            String serialNo = tds[9].replace("\"", "").replace("\t", "");
                            String bankAccount = tds[6].replace("\"", "").replace("\t", "");
                            String cellPhone = tds[8].replace("\"", "").replace("\t", "");
                            if (!dt.rows[i].getColumnValue("serialNo").equals(serialNo)) {
                                flag = false;
                            }
                            if (!dt.rows[i].getColumnValue("realName").equals(tds[3])) {
                                flag = false;
                            }
                            if (Double.parseDouble(dt.rows[i].getColumnValue("withdrawAmount")) != Double.parseDouble(tds[2])) {
                                flag = false;
                            }
                            if (!dt.rows[i].getColumnValue("bankAccount").equals(bankAccount)) {
                                flag = false;
                            }
                            if (!dt.rows[i].getColumnValue("cellPhone").equals(cellPhone)) {
                                flag = false;
                            }
                            if (!flag) {
                                break;
                            }
                        }
                        //确保全部匹配上才更新回执.备注，提现状态改为成功
                        if (!flag) {
                            RequestUtils.write(response, "数据不匹配，请重新上传");
                            return;
                        } else {
                            for (int i = 0; i < split.length - 1; i++) {
                                String[] tds = split[i + 1].split(",");
                                String receiptNo = tds[10].replace("\"", "").replace("\t", "");
                                WithdrawRequestModel withdraw = new WithdrawRequestModel();
                                withdraw.setId(CommonUtils.getLongValue(dt.rows[i].getColumnValue("id")));
                                withdraw.setReceiptNo(receiptNo);
                                withdraw.setRemark(tds[11]);
                                if (!Strings.isNullOrEmpty(receiptNo)) {
                                    withdraw.setWithdrawStatus(WithdrawRequest.SUCCESS.getWithdrawStatus());//提现成功
                                } else {
                                    withdraw.setWithdrawStatus(WithdrawRequest.ERROR.getWithdrawStatus());//回执号为空则提现异常
                                }
                                WithdrawRequestDao.updateRemark(withdraw);//更新回执单号和备注,提现状态改为成功
                            }
                        }
                    }
                    RequestUtils.write(response, "上传成功");
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            RequestUtils.write(response, "当日无提现数据");
            return;
        }
    }

}

