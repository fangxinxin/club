package club.controller.salelog;

import club.service.PayService;
import club.vo.PaginationVO;
import dsqp.db.model.SplitPage;
import dsqp.db.util.DataTableUtils;
import dsqp.db_club.dao.PromoterDao;
import dsqp.db_club.dao.PromoterPayDao;
import dsqp.db_club.dao.PromoterSellDao;
import dsqp.db_club.dao.WithdrawRequestDao;
import dsqp.db_club.model.PromoterModel;
import dsqp.db_club_log.dao.LogPromoterReportDao;
import dsqp.util.CommonUtils;
import dsqp.util.DateUtils;
import dsqp.util.JsonUtils;
import dsqp.util.RequestUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 销售记录
 * Created by aris on 2017/7/24.
 */

@Controller("salelog_index")
@RequestMapping("salelog")
public class IndexController {

    private static final String PATH = "salelog/";

    @Autowired
    PayService payService;

    @RequestMapping("index")
    public String index(Model model
            , @RequestParam(value = "startDate", defaultValue = "") String startDate
            , @RequestParam(value = "endDate", defaultValue = "") String endDate) {
        //默认取7天的数据
        Date d1 = DateUtils.String2Date(startDate, "yyyy-MM-dd", DateUtils.addDay(-7, new Date()));
        Date d2 = DateUtils.String2Date(endDate, "yyyy-MM-dd", new Date());

        long id = CommonUtils.getLongValue(SecurityUtils.getSubject().getSession().getAttribute("id"));
        PromoterModel promoter = PromoterDao.getOne(id);
        model.addAttribute("startDate", DateUtils.Date2String(d1, "yyyy-MM-dd"));
        model.addAttribute("endDate", DateUtils.Date2String(d2, "yyyy-MM-dd"));
        model.addAttribute("promoter", promoter);
        if (promoter.getpLevel() == 2) {
            return PATH + "indexSecond";//二级代理专用页面
        } else {
            return PATH + "index";
        }

    }

    //日报
    @RequestMapping(value = "promoter_report")
    public void promoterReport(
            @RequestParam(value = "startDate", defaultValue = "") String startDate
            , @RequestParam(value = "endDate", defaultValue = "") String endDate
            , @RequestParam(value = "page", defaultValue = "1") int pageNum
            , @RequestParam(value = "rows", defaultValue = "10") int pageSize
            , HttpServletResponse response) {
        long clubId = CommonUtils.getLongValue(SecurityUtils.getSubject().getSession().getAttribute("clubId"));
        PaginationVO vo = new PaginationVO();
        //默认取7天的数据
        Date d1 = DateUtils.String2Date(startDate, "yyyy-MM-dd", DateUtils.addDay(-7, new Date()));
        Date d2 = DateUtils.String2Date(endDate, "yyyy-MM-dd", new Date());

        SplitPage splitPage = LogPromoterReportDao.getListByDate(clubId, d1, d2, pageNum, pageSize);

        vo.setRows(DataTableUtils.DataTable2JsonArray(splitPage.getPageDate()));
        vo.setTotal(LogPromoterReportDao.getCountByDate(clubId, d1, d2));

        RequestUtils.write(response, JsonUtils.getJson(vo));
    }

    //销售记录
    @RequestMapping(value = "sell_record")
    public void sellRecord(
            @RequestParam(value = "startDate", defaultValue = "") String startDate
            , @RequestParam(value = "endDate", defaultValue = "") String endDate
            , @RequestParam(value = "page", defaultValue = "1") int pageNum
            , @RequestParam(value = "rows", defaultValue = "10") int pageSize
            , HttpServletResponse response) {
        long id = CommonUtils.getLongValue(SecurityUtils.getSubject().getSession().getAttribute("id"));
        PaginationVO vo = new PaginationVO();
        //默认取7天的数据
        Date d1 = DateUtils.String2Date(startDate, "yyyy-MM-dd", DateUtils.addDay(-7, new Date()));
        Date d2 = DateUtils.String2Date(endDate, "yyyy-MM-dd", new Date());

        SplitPage splitPage = PromoterSellDao.getPageByDate(id, d1, d2, pageNum, pageSize);
        vo.setRows(DataTableUtils.DataTable2JsonArray(splitPage.getPageDate()));
        vo.setTotal(String.valueOf(splitPage.getTotalRecords()));

        RequestUtils.write(response, JsonUtils.getJson(vo));
    }

    //进货记录
    @RequestMapping(value = "pay_record")
    public void payRecord(
            @RequestParam(value = "startDate", defaultValue = "") String startDate
            , @RequestParam(value = "endDate", defaultValue = "") String endDate
            , @RequestParam(value = "page", defaultValue = "1") int pageNum
            , @RequestParam(value = "rows", defaultValue = "10") int pageSize
            , HttpServletResponse response) {
        PaginationVO vo = new PaginationVO();
        long id = CommonUtils.getLongValue(SecurityUtils.getSubject().getSession().getAttribute("id"));
        //默认取7天的数据
        Date d1 = DateUtils.String2Date(startDate, "yyyy-MM-dd", DateUtils.addDay(-7, new Date()));
        Date d2 = DateUtils.String2Date(endDate, "yyyy-MM-dd", new Date());

        SplitPage splitPage = PromoterPayDao.getPageByDate(id, d1, d2, pageNum, pageSize);

        vo.setRows(DataTableUtils.DataTable2JsonArray(splitPage.getPageDate()));
        vo.setTotal(String.valueOf(splitPage.getTotalRecords()));

        RequestUtils.write(response, JsonUtils.getJson(vo));
    }

    //提成明细
    @RequestMapping(value = "bonus_record")
    public void bonusRecord(
            @RequestParam(value = "startDate", defaultValue = "") String startDate
            , @RequestParam(value = "endDate", defaultValue = "") String endDate
            , @RequestParam(value = "page", defaultValue = "1") int pageNum
            , @RequestParam(value = "rows", defaultValue = "10") int pageSize
            , HttpServletResponse response) {
        long id = CommonUtils.getLongValue(SecurityUtils.getSubject().getSession().getAttribute("id"));
        int gameId = CommonUtils.getIntValue(SecurityUtils.getSubject().getSession().getAttribute("gameId"));
        int pLevel = PromoterDao.getOne(id).getpLevel();
        //默认取7天的数据
        Date d1 = DateUtils.String2Date(startDate, "yyyy-MM-dd", DateUtils.addDay(-7, new Date()));
        Date d2 = DateUtils.String2Date(endDate, "yyyy-MM-dd", new Date());
        //取出
        PaginationVO vo = payService.getTotalPayByDate(id, pLevel, d1, d2, pageNum, pageSize);
        if (vo != null) {
            RequestUtils.write(response, JsonUtils.getJson(vo));
        }
    }

    //提现记录
    @RequestMapping(value = "withdraw_record")
    public void WithdrawRecord(HttpServletResponse response
            , @RequestParam(value = "startDate", defaultValue = "") String startDate
            , @RequestParam(value = "endDate", defaultValue = "") String endDate
            , @RequestParam(value = "page", defaultValue = "1") int pageNum
            , @RequestParam(value = "rows", defaultValue = "10") int pageSize) {
        long id = CommonUtils.getLongValue(SecurityUtils.getSubject().getSession().getAttribute("id"));
        PaginationVO vo = new PaginationVO();
        //默认取7天的数据
        Date d1 = DateUtils.String2Date(startDate, "yyyy-MM-dd", DateUtils.addDay(-7, new Date()));
        Date d2 = DateUtils.String2Date(endDate, "yyyy-MM-dd", new Date());

        SplitPage splitPage = WithdrawRequestDao.getListByCreateDate(id, d1, d2, pageNum, pageSize);

        vo.setRows(DataTableUtils.DataTable2JsonArray(splitPage.getPageDate()));
        vo.setTotal(String.valueOf(splitPage.getTotalRecords()));

        RequestUtils.write(response, JsonUtils.getJson(vo));
    }

}
