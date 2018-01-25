package admin.controller.pre_sale_manage;


import dsqp.db.model.DataTable;
import dsqp.db_club_dict.dao.DictGameDbDao;
import dsqp.db_club_dict.model.DictGameDbModel;
import dsqp.db_game.dao.log_dev.UPyjUserRecordDao;
import dsqp.util.CommonUtils;
import dsqp.util.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

/**
 * Created by jeremy on 2017/9/19.
 */
@Controller
@RequestMapping("pre_sale_manage/")
public class QueryRecordController {


    @RequestMapping("queryRecord")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("pre_sale_manage/queryRecord");

        mv.addObject("startDate", DateUtils.Date2String(DateUtils.addDay(-6, new Date()), "yyyy-MM-dd HH:mm"));
        mv.addObject("endDate", DateUtils.Date2String(new Date(), "yyyy-MM-dd HH:mm"));
        return mv;
    }

    /**
     * 查询玩家所有的对局明细
     */
    @RequestMapping("allDetail")
    public ModelAndView allDetail(@RequestParam(value = "startDate", defaultValue = "") String startDate,
                                  @RequestParam(value = "endDate", defaultValue = "") String endDate,
                                  @RequestParam(value = "userId", defaultValue = "0") long userId) {

        Session session = SecurityUtils.getSubject().getSession();
        ModelAndView mv = new ModelAndView("pre_sale_manage/ajax/queryDetail");

        Date start = DateUtils.String2Date(startDate, "yyyy-MM-dd HH:mm");
        Date end = DateUtils.String2Date(endDate, "yyyy-MM-dd HH:mm");
        DictGameDbModel dictDb = DictGameDbDao.getByGameId(CommonUtils.getIntValue(session.getAttribute("gameId")), true);
        DataTable userDetail = UPyjUserRecordDao.getUserDetail(dictDb, userId, start, end);

        if (userDetail.rows.length > 0) {
            mv.addObject("userDetail", userDetail.rows);
        }
        return mv;
    }

    /**
     * 查询玩家是房主的对局明细
     */
    @RequestMapping("ownerDetail")
    public ModelAndView ownerDetail(@RequestParam(value = "startDate", defaultValue = "") String startDate,
                                    @RequestParam(value = "endDate", defaultValue = "") String endDate,
                                    @RequestParam(value = "userId", defaultValue = "0") long userId) {

        Session session = SecurityUtils.getSubject().getSession();
        ModelAndView mv = new ModelAndView("pre_sale_manage/ajax/queryDetail");

        Date start = DateUtils.String2Date(startDate, "yyyy-MM-dd HH:mm");
        Date end = DateUtils.String2Date(endDate, "yyyy-MM-dd HH:mm");
        DictGameDbModel dictDb = DictGameDbDao.getByGameId(CommonUtils.getIntValue(session.getAttribute("gameId")), true);
        DataTable userDetail = UPyjUserRecordDao.getUserOwnerDetail(dictDb, userId, start, end);

        if (userDetail.rows.length > 0) {
            mv.addObject("userDetail", userDetail.rows);
        }
        return mv;
    }

    /**
     * 查询玩家是大赢家的对局明细
     */
    @RequestMapping("winnerDetail")
    public ModelAndView winnerDetail(@RequestParam(value = "startDate", defaultValue = "") String startDate,
                                     @RequestParam(value = "endDate", defaultValue = "") String endDate,
                                     @RequestParam(value = "userId", defaultValue = "0") long userId) {

        Session session = SecurityUtils.getSubject().getSession();
        ModelAndView mv = new ModelAndView("pre_sale_manage/ajax/queryDetail");

        Date start = DateUtils.String2Date(startDate, "yyyy-MM-dd HH:mm");
        Date end = DateUtils.String2Date(endDate, "yyyy-MM-dd HH:mm");
        DictGameDbModel dictDb = DictGameDbDao.getByGameId(CommonUtils.getIntValue(session.getAttribute("gameId")), true);
        DataTable userDetail = UPyjUserRecordDao.getUserWinDetail(dictDb, userId, start, end);

        if (userDetail.rows.length > 0) {
            mv.addObject("userDetail", userDetail.rows);
        }
        return mv;
    }

}
