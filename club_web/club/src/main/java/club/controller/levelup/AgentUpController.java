package club.controller.levelup;

import dsqp.common_const.club.CommonConfig;
import dsqp.common_const.club.LevelUp;
import dsqp.common_const.club.PromoterLevel;
import dsqp.db.model.DataTable;
import dsqp.db_club.dao.PromoterDao;
import dsqp.db_club.dao.PromoterLevelUpInfoDao;
import dsqp.db_club.dao.PromoterPayDao;
import dsqp.db_club.model.PromoterLevelUpInfoModel;
import dsqp.db_club_dict.dao.DictBonusDao;
import dsqp.db_club_dict.dao.DictLevelUpDao;
import dsqp.db_club_dict.model.DictBonusModel;
import dsqp.db_club_dict.model.DictLevelUpModel;
import dsqp.util.CommonUtils;
import dsqp.util.DateUtils;
import dsqp.util.RequestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/24.
 */
@Controller
public class AgentUpController {

    //代理升级
    @RequestMapping("agentUp")
    public String upgrade(Model model) {

        Subject subject = SecurityUtils.getSubject();
        long promoterId = CommonUtils.getLongValue(subject.getSession().getAttribute("id"));
        int pLevel = CommonUtils.getIntValue(subject.getSession().getAttribute("pLevel"));
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));

        DataTable dtPromoter = PromoterDao.getOne2(promoterId);
        if(dtPromoter.rows.length>0){
            pLevel = Integer.parseInt(dtPromoter.rows[0].getColumnValue("pLevel"));
            gameId = Integer.parseInt(dtPromoter.rows[0].getColumnValue("gameId"));
        }

        subject.getSession().setAttribute("pLevel", pLevel);

        model.addAttribute("pLevel", pLevel);
        //获取一个月前的日期
        Date startDate = DateUtils.addDate("month", -1, new Date());

        //获取当前月第一天
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        Date firstDate = c.getTime();

        model.addAttribute("firstDate", DateUtils.Date2String(firstDate, "yyyy年MM月dd日"));
        model.addAttribute("startDate", DateUtils.Date2String(startDate, "yyyy年MM月dd日"));
        model.addAttribute("endDate",   DateUtils.Date2String(new Date(), "yyyy年MM月dd日"));

        //备注显示可获取的返钻比例(由原来的提成比例改为返钻比例)
        DictBonusModel dictBonus = DictBonusDao.getByGameId(gameId);
        model.addAttribute("dictBonus", dictBonus);
        if(dictBonus!=null){
            int directPercent = (int) (dictBonus.getDirectPercent()*100); //一级代理商返钻比例
            int nonDirectPercent = (int) (dictBonus.getNonDirectPercent()*100); //一级代理商返钻比例
            model.addAttribute("directPercent", directPercent);
            model.addAttribute("nonDirectPercent", nonDirectPercent);
        }
        //默认的返钻比例配置
        int _directPercent = (int)(CommonConfig.FIRST_PERCENT*100);//一级代理商返钻比例
        int _nonDirectPercent = (int)(CommonConfig.SUPER_PERCENT*100);//特级代理商返钻比例
        model.addAttribute("_directPercent",_directPercent);
        model.addAttribute("_nonDirectPercent",_nonDirectPercent);



        //近一个月代理商自己购买钻石总金额
        DataTable lateMonthTotalPay = PromoterPayDao.getPayOfLateMonthById(promoterId);
        double totalPay = 0;
        if (lateMonthTotalPay.rows[0].getColumnValue("totalPrice") != "") {
            totalPay = Double.parseDouble(lateMonthTotalPay.rows[0].getColumnValue("totalPrice"));
        }
        //近一个月代理商下线购买钻石总金额
        DataTable directTotalPay = PromoterPayDao.getDirectPayOfLateMonthById(promoterId);
        double dTotalPay = 0;
        if (directTotalPay.rows[0].getColumnValue("totalPrice") != "") {
            dTotalPay = Double.parseDouble(directTotalPay.rows[0].getColumnValue("totalPrice"));
        }

/*        //当月代理商自己购买总额
        DataTable totalPayCurrentMonth = PromoterPayDao.getPayOfCurrentMonthById(promoterId);
        double totalPayCurrent = 0;
        if (totalPayCurrentMonth.rows[0].getColumnValue("totalPrice") != "") {
            totalPayCurrent = Double.parseDouble(totalPayCurrentMonth.rows[0].getColumnValue("totalPrice"));
        }
        //当月直属代理商购买总额
        DataTable directTotalPayCurrentMonth = PromoterPayDao.getDirectPayOfCurrentMonthById(promoterId);
        double totalPayDirectCurrent = 0;
        if (directTotalPayCurrentMonth.rows[0].getColumnValue("totalPrice") != "") {
            totalPayDirectCurrent = Double.parseDouble(directTotalPayCurrentMonth.rows[0].getColumnValue("totalPrice"));
        }*/

        if (pLevel == -1) {
//            String totalCurrent2 = new DecimalFormat("#.00").format(totalPayCurrent + totalPayDirectCurrent);
            String totalCurrent2 = new DecimalFormat("#.00").format(totalPay + dTotalPay);
            double totalCurrent = Double.parseDouble(totalCurrent2);
            model.addAttribute("dTotalPay", totalCurrent);

            DictLevelUpModel levelUpModel = DictLevelUpDao.getConditionByGameIdAndLevelUpType(gameId, LevelUp.SUPER2FIRST);
            if(levelUpModel!=null){
                double totalPayCondition = levelUpModel.getTotalPay();
                model.addAttribute("totalPayCondition", totalPayCondition);
            }else {//升级条件未配置,使用默认配置
                model.addAttribute("totalPayCondition", CommonConfig.SUPER2FIRST_TOTALPAY);
            }

        } else if (pLevel == 1) {
            DataTable directNums = PromoterDao.getDirectNumsByParentId(promoterId);//名下代理商人数
            int dNums = Integer.parseInt(directNums.rows[0].getColumnValue("nums"));
            model.addAttribute("dNums", dNums);
            model.addAttribute("dTotalPay", dTotalPay);

            DictLevelUpModel levelUpModel = DictLevelUpDao.getConditionByGameIdAndLevelUpType(gameId, LevelUp.FIRST2SUPER);
            if(levelUpModel!=null){
                double totalPayCondition = levelUpModel.getTotalPay();
                int totalPromoter = levelUpModel.getTotalPromoter();
                model.addAttribute("totalPayCondition", totalPayCondition);
                model.addAttribute("totalPromoter", totalPromoter);
            }else {//升级条件未配置,使用默认配置
                model.addAttribute("totalPayCondition", CommonConfig.FIRST2SUPER_TOTALPROMOTER);
                model.addAttribute("totalPromoter", CommonConfig.FIRST2SUPER_TOTALPROMOTER);
            }

        } else if (pLevel == 2) {
            DataTable directNums = PromoterDao.getDirectNumsByParentId(promoterId);//名下代理商人数
            int dNums = Integer.parseInt(directNums.rows[0].getColumnValue("nums"));
            model.addAttribute("dNums", dNums);
            model.addAttribute("totalPay", totalPay);

            DictLevelUpModel levelUpModel = DictLevelUpDao.getConditionByGameIdAndLevelUpType(gameId, LevelUp.SECOND2FIRST);
            if(levelUpModel!=null){
                double totalPayCondition = levelUpModel.getTotalPay();
                int totalPromoter = levelUpModel.getTotalPromoter();
                model.addAttribute("totalPayCondition", totalPayCondition);
                model.addAttribute("totalPromoter", totalPromoter);
            }else{//升级条件未配置,使用默认配置
                model.addAttribute("totalPayCondition", CommonConfig.SECOND2FIRST_TOTALPAY);
                model.addAttribute("totalPromoter", CommonConfig.SECOND2FIRST_TOTALPAY);
            }
        }

        return "agentUp/agentUp";
    }

    //达到升级条件修改等级
    @RequestMapping(value = "agentUpCheck", method = RequestMethod.GET)
    public void check(HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();

        int pLevel = CommonUtils.getIntValue(subject.getSession().getAttribute("pLevel"));
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));

        long promoterId = CommonUtils.getLongValue(subject.getSession().getAttribute("id"));
        PromoterLevelUpInfoModel model = new PromoterLevelUpInfoModel();
        model.setGameId(gameId);
        model.setPromoterId(promoterId);
        model.setpLevel(pLevel);
        model.setCreateTime(new Date());

        if (pLevel == 2) {
            int m = PromoterDao.updateLevelById(promoterId, PromoterLevel.FIRST);
            model.setLevelUpType(LevelUp.SECOND2FIRST);
            PromoterLevelUpInfoDao.add(model);
            RequestUtils.write(response, "" + m);
        } else if (pLevel == 1) {
            int n = PromoterDao.updateLevelById(promoterId, PromoterLevel.SUPER);
            model.setLevelUpType(LevelUp.FIRST2SUPER);
            PromoterLevelUpInfoDao.add(model);
            RequestUtils.write(response, "" + n);
        }

    }


}
