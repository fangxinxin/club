package club.controller;

import club.util.PropertyUtil;
import club.vo.VerifyCode;
import com.google.common.base.Strings;
import dsqp.common_const.club.ApiKey;
import dsqp.db.model.DataTable;
import dsqp.db_club.dao.PromoterDao;
import dsqp.db_club_dict.dao.DictGameDao;
import dsqp.util.CommonUtils;
import dsqp.util.RequestUtils;
import dsqp.util.RndUtils;
import dsqp.util.SMSReqSender;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

/**
 * Created by mj on 2017/6/21.
 */

@Controller
public class VerifyCodeController {

    @RequestMapping(value = "verifyCode")
    @ResponseBody
    public void forgetPassword(@RequestParam(value = "cellPhone", defaultValue = "") String cellPhone, HttpSession session, HttpServletResponse response) throws IOException {
        DataTable dt = PromoterDao.queryByCellPhone(CommonUtils.getLongValue(cellPhone));
        if (dt.rows.length > 0) {
            int gameId = CommonUtils.getIntValue(dt.rows[0].getColumnValue("gameId"));
            String remark = DictGameDao.queryRemarkByGameId(gameId);
            String apiKey = "";
            if ("66".equals(remark)) {
                apiKey = ApiKey.LIULIUKEY;
            } else if ("来来".equals(remark)) {
                apiKey = ApiKey.LAILAIKEY;
            } else if ("大圣".equals(remark)) {
                apiKey = ApiKey.DASHENGQIPAI;
            }
            String op = "";
            String randNum = RndUtils.getValidateCode(6, true);
            System.out.println("验证码："+randNum);
            VerifyCode verifyCode = new VerifyCode();
            verifyCode.setContent(randNum);
            verifyCode.setXpiration(new Date().getTime() + 5 * 60 * 1000);//设置有效时间5分钟
            session.setAttribute("verifyCode" + cellPhone, verifyCode);
            op = SMSReqSender.sendSms(apiKey, String.format(PropertyUtil.getProperty("sms.codetxt"), remark, randNum), cellPhone);
            if (!Strings.isNullOrEmpty(op))
                System.out.println(op);
            RequestUtils.write(response, "OK");
        } else {
            RequestUtils.write(response, "手机号码输入错误！");
        }

    }

    @RequestMapping(value = "verifyCodeTwo")
    @ResponseBody
    public void verifyCode(@RequestParam(value = "cellPhone", defaultValue = "") String cellPhone, HttpSession session) throws IOException {
        int gameId = CommonUtils.getIntValue(SecurityUtils.getSubject().getSession().getAttribute("gameId"));
        String remark = DictGameDao.queryRemarkByGameId(gameId);
        String apiKey = "";
        if ("66".equals(remark)) {
            apiKey = ApiKey.LIULIUKEY;
        } else if ("来来".equals(remark)) {
            apiKey = ApiKey.LAILAIKEY;
        } else if ("大圣".equals(remark)) {
            apiKey = ApiKey.DASHENGQIPAI;
        }
        String op = "";
        String randNum = RndUtils.getValidateCode(6, true);
        VerifyCode verifyCode = new VerifyCode();
        verifyCode.setContent(randNum);
        verifyCode.setXpiration(new Date().getTime() + 5 * 60 * 1000);//设置有效时间5分钟
        session.setAttribute("verifyCode" + cellPhone, verifyCode);
        op = SMSReqSender.sendSms(apiKey, String.format(PropertyUtil.getProperty("sms.codetxt"), remark, randNum), cellPhone);
        if (!Strings.isNullOrEmpty(op))
            System.out.println(op);
    }

}
