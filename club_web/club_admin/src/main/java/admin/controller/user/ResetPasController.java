package admin.controller.user;

import admin.service.UserService;
import com.google.common.base.Strings;
import db_admin.dao.SysUserDao;
import db_admin.model.SysUserModel;
import dsqp.util.RequestUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by jeremy on 2017/6/29.
 */
@Controller
@RequestMapping("/user")
public class ResetPasController {
    @Autowired
    private UserService userService;

    private static final String PATH = "user/";


    @RequestMapping(value = "password")
    public String index(){
        return PATH + "password";
    }


    @RequestMapping("changePass")
    @ResponseBody
    public void changePass(HttpServletResponse response
            , @RequestParam(value = "loginPass", defaultValue = "") String loginPass
            , @RequestParam(value = "newPass", defaultValue = "") String newPass) {
        Subject subject = SecurityUtils.getSubject();
        SysUserModel admin = (SysUserModel) subject.getPrincipal();
        SysUserModel a = SysUserDao.getOne(admin.getId());
        if (a == null) {
            return;
        }

        if (!Strings.isNullOrEmpty(loginPass) && !Strings.isNullOrEmpty(newPass)) {
            if (DigestUtils.md5Hex(loginPass).equalsIgnoreCase(a.getLoginPassword())) {
                RequestUtils.write(response, String.valueOf(SysUserDao.updatePassById(admin.getId(), DigestUtils.md5Hex(newPass))));
            }
        }
    }


    @RequestMapping(value = "search")
    public void search (HttpServletResponse response,
                        @RequestParam(value = "loginPass", defaultValue = "") String loginPass){
        SysUserModel admin = (SysUserModel) SecurityUtils.getSubject().getPrincipal();
        boolean user = userService.login(admin.getUserName(), loginPass);
        int result = 1;
        if (user != true) {
            if (user != true) {
                result = 0;
            }
        }
        RequestUtils.write(response, String.valueOf(result));
    }

}
