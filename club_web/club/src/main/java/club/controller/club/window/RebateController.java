package club.controller.club.window;

import club.service.PromoterService;
import club.vo.MsgVO;
import dsqp.db.model.DataTable;
import dsqp.db_club_log.dao.LogRebateGetDao;
import dsqp.util.CommonUtils;
import dsqp.util.JsonUtils;
import dsqp.util.RequestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by ds on 2017/11/22.
 */
@Controller("rebate")
@RequestMapping("win")
public class RebateController {
    private static final String PATH = "club/window/";

    @Autowired
    private PromoterService promoterService;

    /**
     * 提取返钻
     * @param response
     */
    @RequestMapping("getRebate")
    @ResponseBody
    public void getRebate(HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        long promoterId = CommonUtils.getLongValue(subject.getSession().getAttribute("id"));

        MsgVO vo = promoterService.returnDiamond(promoterId);
        RequestUtils.write(response, JsonUtils.getJson(vo));
    }

    /**
     * 返钻提取记录
     */
    @RequestMapping("rebateLog")
    public ModelAndView rebateLog() {
        ModelAndView mv = new ModelAndView(PATH+"win_rebate_load");
        Subject subject = SecurityUtils.getSubject();
        long promoterId = CommonUtils.getLongValue(subject.getSession().getAttribute("id"));

        DataTable list = LogRebateGetDao.list(promoterId);
        mv.addObject("list", list.rows);

        return mv;
    }

}
