package admin.controller.operate_config;

import admin.service.BonusPercentService;
import dsqp.common_const.club.CommonConfig;
import dsqp.common_const.club_admin.Permission;
import dsqp.db_club_dict.model.DictBonusModel;
import dsqp.util.CommonUtils;
import dsqp.util.RequestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by jeremy on 2017/8/18.
 */
@Controller
@RequestMapping("operate_config/")
public class BonusPercentController {

    @Resource
    private BonusPercentService bonusPercentService;

    @RequestMapping("bonusPercent")
    public String index(Model model){
        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        String gameName = CommonUtils.getStringValue(session.getAttribute("gameName"));
        model.addAttribute("gameName",gameName);
        DictBonusModel dictBonus = bonusPercentService.getByGameId(gameId);
        model.addAttribute("dictBonus", dictBonus);
        //可获取的返钻比例(由原来的提成比例改为返钻比例)
        if(dictBonus!=null){
            int directPercent = (int) (dictBonus.getDirectPercent()*100);
            int nonDirectPercent = (int) (dictBonus.getNonDirectPercent()*100);
            model.addAttribute("directPercent", directPercent);
            model.addAttribute("nonDirectPercent", nonDirectPercent);
        }

        //默认的返钻比例配置
        model.addAttribute("_directPercent", (int)(CommonConfig.FIRST_PERCENT*100));
        model.addAttribute("_nonDirectPercent", (int)(CommonConfig.SUPER_PERCENT*100));

        return "operate_config/bonus_percent";
    }

    /**
     * 保存提成配置
     * @param response
     * @param directPercent
     * @param nonDirectPercent
     */
    @RequiresPermissions(Permission.OPERATE_SAVE_BOUND_PERCENT_UPDATE)
    @RequestMapping("saveBonusPercent")
    public void saveBonusPercent(HttpServletResponse response,
                        @RequestParam(value = "directPercent", defaultValue = "0") double  directPercent,
                        @RequestParam(value = "nonDirectPercent", defaultValue = "0") double  nonDirectPercent) {
        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));

        //存在则更新，不存在则新增
        DictBonusModel dictBonus = new DictBonusModel();
        dictBonus.setGameId(gameId);
        dictBonus.setDirectPercent(directPercent/100);
        dictBonus.setNonDirectPercent(nonDirectPercent/100);
        dictBonus.setIsEnable(true);
        dictBonus.setRemark("");

        DictBonusModel dict = bonusPercentService.getByGameId(gameId);
        if(dict!=null){//修改
            dictBonus.setId(dict.getId());
            int m = bonusPercentService.update(dictBonus);
            RequestUtils.write(response,""+m);
        }else{//新增
            int n = bonusPercentService.add(dictBonus);
            RequestUtils.write(response,""+n);
        }


    }

}
