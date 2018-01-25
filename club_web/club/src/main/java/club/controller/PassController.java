package club.controller;

import dsqp.common_const.club.LoginStatus;
import dsqp.db.model.DataTable;
import dsqp.db_club.dao.PromoterDao;
import dsqp.util.RequestUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by mj on 2017/7/22.
 */

@Controller
public class PassController {

    @RequestMapping(value = "changePass", method = RequestMethod.GET)
    public String changePassGet(@RequestParam(value = "cellPhone", defaultValue = "") String cellPhone, Model model) {
        model.addAttribute("cellPhone", cellPhone);
        return "pass/changePass";
    }

    @RequestMapping(value = "changePass", method = RequestMethod.POST)
    public void changePass(@RequestParam(value = "cellPhone", defaultValue = "") long cellPhone
            , @RequestParam(value = "loginPassword", defaultValue = "") String loginPassword
            , @RequestParam(value = "rePass", defaultValue = "") String rePass
            , HttpServletResponse response) throws IOException {
        DataTable promoter = PromoterDao.queryByCellPhone(cellPhone);
        if (!promoter.rows[0].getColumnValue("loginPassword").equals(DigestUtils.md5Hex(loginPassword))) {
            RequestUtils.write(response, "NO");
        } else {
            PromoterDao.updatePass(cellPhone, DigestUtils.md5Hex(rePass));
            PromoterDao.updateLoginStatus(cellPhone, LoginStatus.Normal.getLoginStatus());
            RequestUtils.write(response, "YES");
        }
    }
}
