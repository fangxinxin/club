package admin.controller.exception;

import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 异常控制
 * Created by ds on 2016/12/20.
 */
@Controller("exception")
@RequestMapping("exception")
public class ExceptionController {
    private String PATH1 = "exception/error_info1";
    private String PATH2 = "exception/error_info2";

    @RequestMapping("unauthorized")
    public ModelAndView processUnauthenticatedException(UnauthorizedException e) {
        ModelAndView mv = new ModelAndView(PATH2);
        mv.addObject("code", 401);
        mv.addObject("msg", "当前页面需相应的权限方能访问，您没有权限访问本页面。");

        return mv;
    }

    @RequestMapping("UnknownAccount")
    public ModelAndView processUnknownAccountException(UnknownAccountException e) {
        ModelAndView mv = new ModelAndView(PATH2);
        mv.addObject("code", 500);
        mv.addObject("msg", "当前账号异常。");

        return mv;
    }

    @RequestMapping("error_404")
    public ModelAndView miss() {
        ModelAndView mv = new ModelAndView(PATH1);
        mv.addObject("code", 404);
        mv.addObject("msg", "很抱歉,您正在寻找的页面不存在。");

        return mv;
    }

}
