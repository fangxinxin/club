package club.controller.forget;

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
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

/**
 * Created by mj on 2017/7/26.
 */

@Controller
public class ForgetController {

    @RequestMapping(value = "forget", method = RequestMethod.GET)
    public String forget() {
        return "forget/forget";
    }

    @RequestMapping(value = "forgetTwo", method = RequestMethod.GET)
    public String forget(@RequestParam(value = "cellPhone", defaultValue = "") long cellPhone) throws IOException {
        DataTable dt = PromoterDao.queryByCellPhone(cellPhone);

        String newPass = RndUtils.getValidateCode(6, true);
        PromoterDao.updatePass(cellPhone, DigestUtils.md5Hex(newPass));
        String apiKey = "";
        String remark = DictGameDao.queryRemarkByGameId(CommonUtils.getIntValue(dt.rows[0].getColumnValue("gameId")));
        if ("66".equals(remark)) {
            apiKey = ApiKey.LIULIUKEY;
        } else if ("来来".equals(remark)) {
            apiKey = ApiKey.LAILAIKEY;
        } else if ("大圣".equals(remark)) {
            apiKey = ApiKey.DASHENGQIPAI;
        }
        String op = "";
        op = SMSReqSender.sendSms(apiKey,
                String.format(PropertyUtil.getProperty("sms.password"), remark, newPass), String.valueOf(cellPhone));
        if (!Strings.isNullOrEmpty(op))
            System.out.println(op);
        return "forget/success";

    }

    @RequestMapping(value = "testVerifyCode", method = RequestMethod.POST)
    @ResponseBody
    public void testVerifyCode(@RequestParam(value = "verifyCode", defaultValue = "") String verifyCode
            , @RequestParam(value = "cellPhone", defaultValue = "") long cellPhone
            , HttpSession session
            , HttpServletResponse response) {

        VerifyCode code = (VerifyCode) session.getAttribute("verifyCode" + CommonUtils.getStringValue(cellPhone));
        if (code != null && code.getXpiration() > new Date().getTime() && code.getContent().equals(verifyCode)) {
            session.removeAttribute("verifyCode" + CommonUtils.getStringValue(cellPhone));
            RequestUtils.write(response, "true");
        } else {
            RequestUtils.write(response, "验证码错误");
        }
    }

    @RequestMapping(value = "testCellPhone", method = RequestMethod.POST)
    @ResponseBody
    public void testCellPhone(@RequestParam(value = "cellPhone", defaultValue = "") long cellPhone
            , HttpServletResponse response) {

        DataTable dt = PromoterDao.queryByCellPhone(cellPhone);
        if (dt.rows.length == 0) {
            RequestUtils.write(response, "手机号不存在");
        } else {
            RequestUtils.write(response, "true");
        }
    }


}
