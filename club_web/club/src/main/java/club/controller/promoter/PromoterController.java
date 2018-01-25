package club.controller.promoter;

import club.service.PromoterService;
import club.vo.PaginationVO;
import club.vo.VerifyCode;
import dsqp.db.model.DataRow;
import dsqp.db.model.DataTable;
import dsqp.db.model.SplitPage;
import dsqp.db.util.DataTableUtils;
import dsqp.db_club.dao.ClubDao;
import dsqp.db_club.dao.ClubUserDao;
import dsqp.db_club.dao.PromoterDao;
import dsqp.db_club.model.ClubModel;
import dsqp.db_club.model.PromoterModel;
import dsqp.util.CommonUtils;
import dsqp.util.JsonUtils;
import dsqp.util.RequestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by ds on 2017/7/21.
 */
@Controller
public class PromoterController {

    @Resource
    private PromoterService promoterService;

    //解绑银行卡
    @RequestMapping("cancelBound")
    public void cancelBound(@RequestParam(value = "verifyCode", defaultValue = "") String verifyCode
            , HttpServletResponse response) {

        Session session = SecurityUtils.getSubject().getSession();
        String cellPhone = (String) session.getAttribute("cellPhone");
        VerifyCode code = (VerifyCode) session.getAttribute("verifyCode" + cellPhone);
        if (code != null && code.getXpiration() > new Date().getTime() && code.getContent().equals(verifyCode)) {
            session.removeAttribute("verifyCode" + cellPhone);
            PromoterDao.updateAccount(0, CommonUtils.getLongValue(session.getAttribute("id")));
            RequestUtils.write(response, "YES");
        } else {
            RequestUtils.write(response, "NO");
        }
    }

    @RequestMapping("updateBankNum")
    public void updateBankNum(@RequestParam(value = "bankNum", defaultValue = "0") long bankAccount
            , @RequestParam(value = "verifyCode", defaultValue = "") String verifyCode
            , @RequestParam(value = "realName", defaultValue = "") String realName
            , @RequestParam(value = "IDCard", defaultValue = "") String IDCard
            , @RequestParam(value = "bankArea", defaultValue = "") String bankArea
            , HttpServletResponse response) {

        Session session = SecurityUtils.getSubject().getSession();
        String cellPhone = (String) session.getAttribute("cellPhone");
        VerifyCode code = (VerifyCode) session.getAttribute("verifyCode" + cellPhone);
        if (code != null && code.getXpiration() > new Date().getTime() && code.getContent().equals(verifyCode)) {
            session.removeAttribute("verifyCode" + cellPhone);
            PromoterDao.updateAccountAndBankArea(bankAccount, realName, IDCard, bankArea, CommonUtils.getLongValue(session.getAttribute("id")));
            RequestUtils.write(response, "YES");
        } else {
            RequestUtils.write(response, "NO");
        }
    }

    @RequestMapping("promoterView")
    public ModelAndView promoter() {
        Subject subject = SecurityUtils.getSubject();
        ModelAndView mv = new ModelAndView("promoter/promoterView");

        long promoterId = CommonUtils.getLongValue(subject.getSession().getAttribute("id"));
        PromoterModel promoter = PromoterDao.getOne(promoterId);
        ClubModel club = ClubDao.getByPromoterId(promoterId);
        if (promoter == null && club == null) {
            return mv;
        }
        mv.addObject("club", club);
        mv.addObject("promoter", promoter);
        mv.addObject("num", ClubUserDao.getUserNum(promoterId));

        return mv;
    }

    @RequestMapping("promoterDialog")
    public ModelAndView promoterDialog(
            @RequestParam(value = "promoterId", defaultValue = "0") long promoterId) {
        Subject subject = SecurityUtils.getSubject();
        ModelAndView mv = new ModelAndView("promoter/dialog");

        if (promoterId == 0) {
            return mv;
        }

        long parentId = CommonUtils.getLongValue(subject.getSession().getAttribute("id"));
        PromoterModel p = PromoterDao.getOne(parentId);

        DataTable promoterDt = promoterService.getByParentIdAndId(parentId, promoterId, p.getpLevel());
        DataTable clubDt = ClubDao.queryByPromoterId(promoterId);

        if (clubDt.rows.length <= 0 || p == null) {
            return mv;
        }

        int num = 0;
        DataRow club = clubDt.rows[0];

        if (promoterDt.rows.length > 0) {
            DataRow promoter = promoterDt.rows[0];
            num = ClubUserDao.getUserNum(promoterId);

            mv.addObject("num", num);
            mv.addObject("club", club);
            mv.addObject("promoter", promoter);
        }

        return mv;
    }

    @RequestMapping("getPromoterDataGrid")
    @ResponseBody
    public void getPromoterDataGrid(HttpServletResponse response
            , @RequestParam(value = "promoterId", defaultValue = "0") long promoterId
            , @RequestParam(value = "page", defaultValue = "1") int page
            , @RequestParam(value = "size", defaultValue = "10") int size) {

        Subject subject = SecurityUtils.getSubject();
        long parentId = CommonUtils.getLongValue(subject.getSession().getAttribute("id"));

        PaginationVO vo = new PaginationVO();
        DataTable promoter = PromoterDao.getOne2(parentId);

        if (promoter.rows.length > 0) {
            int pLevel = CommonUtils.getIntValue(promoter.rows[0].getColumnValue("pLevel"));

            if (0 != promoterId) {
                DataTable dt = promoterService.getByParentIdAndId(parentId, promoterId, pLevel);
                vo.setRows(DataTableUtils.DataTable2JsonArray(dt));
                vo.setTotal("1");
            } else {
                SplitPage splitPage = promoterService.listByParentId(parentId, page, size, pLevel);
                vo.setRows(DataTableUtils.DataTable2JsonArray(splitPage.getPageDate()));
                vo.setTotal(String.valueOf(splitPage.getTotalRecords()));
            }
        }
//        System.out.println(JsonUtils.getJson(vo));
        RequestUtils.write(response, JsonUtils.getJson(vo));
    }

}
