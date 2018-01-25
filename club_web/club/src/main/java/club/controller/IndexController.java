package club.controller;

import dsqp.common_const.club.LoginStatus;
import dsqp.common_const.manager.Msg;
import dsqp.db.model.DataRow;
import dsqp.db.model.DataTable;
import dsqp.db_club.dao.ClubDao;
import dsqp.db_club.dao.ClubUserDao;
import dsqp.db_club.dao.NoticeDao;
import dsqp.db_club.dao.PromoterDao;
import dsqp.db_club.model.ClubModel;
import dsqp.db_club.model.PromoterModel;
import dsqp.db_club_dict.dao.DictGameDao;
import dsqp.db_club_log.dao.LogLoginDao;
import dsqp.util.CommonUtils;
import dsqp.util.DesUtil;
import dsqp.util.IPUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.support.DefaultWebSubjectContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


@Controller("index")
public class IndexController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String welcome() {
        Session session = SecurityUtils.getSubject().getSession();
        if (CommonUtils.getBooleanValue(session.getAttribute(DefaultWebSubjectContext.AUTHENTICATED_SESSION_KEY))) {
             return "redirect:goSelect";
        }
        return "login";
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isRemembered()) {
            long cellPhone = CommonUtils.getLongValue(subject.getPrincipal());
            subject.getSession().setAttribute("cellPhone", cellPhone);
        }
        return "login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(final Model model
            , @RequestParam(value = "save", defaultValue = "false") boolean save
            , @RequestParam(value = "cellPhone", defaultValue = "") long cellPhone
            , @RequestParam(value = "loginPassword", defaultValue = "") String loginPassword
            , HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException {

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(String.valueOf(cellPhone), DigestUtils.md5Hex(loginPassword));

        DataTable dt = PromoterDao.queryByCellPhone(cellPhone);
        if (dt.rows.length == 0) {
            model.addAttribute("msg", "手机号码或密码错误！");
            return "login";
        }

        DataRow row = dt.rows[0];
        int loginStatus = Integer.parseInt(row.getColumnValue("loginStatus"));
        if (loginStatus == LoginStatus.Init.getLoginStatus()) {
            if (row.getColumnValue("loginPassword").equals(DigestUtils.md5Hex(loginPassword))) {
                model.addAttribute("cellPhone", cellPhone);
                return "redirect:changePass";
            } else {
                model.addAttribute("msg", "手机号码或密码错误！");
                return "login";
            }
        }

        try {
            token.setRememberMe(save);
            subject.login(token);
            subject.getSession().setAttribute("cellPhone", row.getColumnValue("cellPhone"));
        } catch (AuthenticationException e) {
            if (e instanceof UnknownAccountException) {
                model.addAttribute("msg", "手机号码不存在！");
            } else {
                model.addAttribute("msg", "手机号码或密码错误！");
            }
            return "login";
        }

        DataTable gameList = DictGameDao.getList();
        model.addAttribute("promoter", dt.rows);
        model.addAttribute("gameList", gameList.rows);
        try {
            addCookie(CommonUtils.getStringValue(cellPhone), DesUtil.encrypt(loginPassword), response, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "select";
    }

    /**
     * Cookie的实现
     **/
    private void addCookie(String name, String password, HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException {
        if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(password)) {
            //创建Cookie
            Cookie nameCookie = new Cookie("name", URLEncoder.encode(name, "utf-8"));
            Cookie pswCookie = new Cookie("psw", password);

            //设置Cookie的父路径
            nameCookie.setPath(request.getContextPath() + "/");
            pswCookie.setPath(request.getContextPath() + "/");

            //获取是否保存Cookie
            String rememberMe = request.getParameter("save");
            if (rememberMe == null) {//不保存Cookie
                nameCookie.setMaxAge(0);
                pswCookie.setMaxAge(0);
            } else {//保存Cookie的时间长度，单位为秒
                nameCookie.setMaxAge(7 * 24 * 60 * 60);
                pswCookie.setMaxAge(7 * 24 * 60 * 60);
            }
            //加入Cookie到响应头
            response.addCookie(nameCookie);
            response.addCookie(pswCookie);
        }
    }


    @RequestMapping("select")
    public String select(final Model model, HttpServletRequest request
            , @RequestParam(value = "id", defaultValue = "") long id) {
        Subject subject = SecurityUtils.getSubject();
        PromoterModel promoter = PromoterDao.getOne(id);
        long promoterId = promoter.getId();
        int gameId = promoter.getGameId();

        //封停
        if (promoter.getLoginStatus() == LoginStatus.Forbid.getLoginStatus()) {
            model.addAttribute("msg", Msg.getFail("账户被封停，请联系客服处理"));
            return "login";
        }

        DataRow club = ClubDao.queryByPromoterId(promoterId).rows[0];
        int num = ClubUserDao.getUserNum(promoterId);
        LogLoginDao.add(promoterId, IPUtils.getIp(request));//加登录日志

        subject.getSession().setAttribute("id", promoterId);
        subject.getSession().setAttribute("gameId", gameId);
        subject.getSession().setAttribute("pLevel", promoter.getpLevel());
        subject.getSession().setAttribute("clubId", club.getColumnValue("id"));

        DataTable game = DictGameDao.getByGameId(gameId);
        if (game.rows.length > 0) {
            subject.getSession().setAttribute("preName", game.rows[0].getColumnValue("preName"));
            subject.getSession().setAttribute("gameName", game.rows[0].getColumnValue("remark"));
        }

        model.addAttribute("num", num);
        model.addAttribute("club", club);
        model.addAttribute("promoter", promoter);

        return "redirect:index";
    }

    @RequestMapping("index")
    public String index(final Model model) {
        Subject subject = SecurityUtils.getSubject();
        long promoterId = CommonUtils.getLongValue(subject.getSession().getAttribute("id"));
//        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
//        int pLevel = CommonUtils.getIntValue(subject.getSession().getAttribute("pLevel"));
        if (promoterId != 0) {
//            DataTable dt = PromoterPayDao.queryPayInfo(promoterId, true);
//            boolean canLevelUp = false;
//            if (dt.rows.length > 0) {
//                //获取最近一个月的购买总额
//                double lateTotalPrice = CommonUtils.getDoubleValue(dt.rows[0].getColumnValue("lateTotalPrice"));
//                //近一个月下线购买钻石总金额
//                double dLateTotalPrice = CommonUtils.getDoubleValue(dt.rows[0].getColumnValue("dLateTotalPrice"));
//                //当月购买总额
//                double currentTotalPrice = CommonUtils.getDoubleValue(dt.rows[0].getColumnValue("currentTotalPrice"));
//                //当月直属代理商购买总额
//                double dCurrentTotalPrice = CommonUtils.getDoubleValue(dt.rows[0].getColumnValue("dCurrentTotalPrice"));
//
//                DictLevelUpModel dictLevelUp;
//                int              directNum = 0;
//
//                DataTable directNumDt = PromoterDao.getDirectNumsByParentId(promoterId);//名下代理商人数
//                if (directNumDt.rows.length > 0) {
//                    directNum = CommonUtils.getIntValue(directNumDt.rows[0].getColumnValue("nums"));
//                }
//
//                switch(pLevel) {
//                    case (PromoterLevel.FIRST): {
//                        dictLevelUp = DictLevelUpDao.getConditionByGameIdAndLevelUpType(gameId, LevelUp.FIRST2SUPER);
//
//                        //一级升特级 :: 近一个月，直属下属充值金额、直属人数 >= 升级条件
//                        if (dictLevelUp != null) {
//                            canLevelUp = dictLevelUp.getTotalPay() <= dCurrentTotalPrice && dictLevelUp.getTotalPromoter() <= directNum;
//                        }
//                        break;
//                    }
//                    case (PromoterLevel.SECOND): {
//                        dictLevelUp = DictLevelUpDao.getConditionByGameIdAndLevelUpType(gameId, LevelUp.SECOND2FIRST);
//
//                        //二级升一级 :: 近一个月代理商充值金额 >= 升级条件
//                        if (dictLevelUp != null) {
//                            canLevelUp = dictLevelUp.getTotalPay() <= lateTotalPrice && dictLevelUp.getTotalPromoter() <= directNum;
//                        }
//                        break;
//                    }
//                }
//
//            }
//
//            model.addAttribute("levelUpTips", canLevelUp);
            model.addAttribute("noticeTips", NoticeDao.hasNewNotice(promoterId, false));
        }
        return "index";
    }

    @RequestMapping(value = "logout")
    public String loginOut(final Model model) {
        SecurityUtils.getSubject().logout();

        model.addAttribute("size", 0);
        return "login";
    }

    @RequestMapping("goSelect")
    public String goSelect(final Model model) {

        Session session = SecurityUtils.getSubject().getSession();
        long cellPhone = CommonUtils.getLongValue(session.getAttribute("cellPhone"));
        DataTable dt = PromoterDao.queryByCellPhone(cellPhone);
        DataTable gameList = DictGameDao.getList();
        model.addAttribute("promoter", dt.rows);
        model.addAttribute("gameList", gameList.rows);
        return "select";
    }

    /**
     * 获取用户信息
     **/
    @RequestMapping("getUserInfo")
    public ModelAndView getUserInfo() {
        ModelAndView mv = new ModelAndView("userInfo");

        Subject subject = SecurityUtils.getSubject();
        long promoterId = CommonUtils.getLongValue(subject.getSession().getAttribute("id"));

        PromoterModel promoter = PromoterDao.getOne(promoterId);
        ClubModel club = ClubDao.getByPromoterId(promoterId);
        if (promoter == null && club == null) {
            return mv;
        }
        mv.addObject("club", club);
        mv.addObject("promoter", promoter);

        String levelName = "";
        switch (promoter.getpLevel()) {
            case -1:
                levelName = "特级";
                break;
            case 1:
                levelName = "一级";
                break;
            case 2:
                levelName = "二级";
                break;
        }

        mv.addObject("levelName", levelName);
        mv.addObject("num", ClubUserDao.getUserNum(promoterId));

        return mv;
    }

}
