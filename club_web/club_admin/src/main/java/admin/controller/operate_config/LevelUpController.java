package admin.controller.operate_config;

import admin.service.LevelUpService;
import dsqp.common_const.club.CommonConfig;
import dsqp.common_const.club.LevelUp;
import dsqp.common_const.club_admin.Permission;
import dsqp.db_club_dict.dao.DictLevelUpDao;
import dsqp.db_club_dict.model.DictLevelUpModel;
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
 * Created by jeremy on 2017/7/30.
 */
@Controller
@RequestMapping("operate_config/")
public class LevelUpController {
    @Resource
    private LevelUpService levelUpService;

    @RequestMapping("levelUp")
    public String index(Model model){
        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        String gameName = CommonUtils.getStringValue(session.getAttribute("gameName"));
        model.addAttribute("gameName",gameName);
        DictLevelUpModel upFirst = DictLevelUpDao.getConditionByGameIdAndLevelUpType(gameId, LevelUp.SECOND2FIRST);
        model.addAttribute("upFirst", upFirst);
        DictLevelUpModel upSuper = DictLevelUpDao.getConditionByGameIdAndLevelUpType(gameId, LevelUp.FIRST2SUPER);
        model.addAttribute("upSuper", upSuper);
        DictLevelUpModel downFirst = DictLevelUpDao.getConditionByGameIdAndLevelUpType(gameId, LevelUp.SUPER2FIRST);
        model.addAttribute("downFirst",downFirst);

        //默认的配置
        model.addAttribute("toSuper_totalPay",CommonConfig.FIRST2SUPER_TOTALPAY);
        model.addAttribute("toSuper_totalPromoter",CommonConfig.FIRST2SUPER_TOTALPROMOTER);
        model.addAttribute("toFirst_totalPay",CommonConfig.SECOND2FIRST_TOTALPAY);
        model.addAttribute("toFirst_totalPromoter",CommonConfig.SECOND2FIRST_TOTALPROMOTER);
        model.addAttribute("superToFirst_totalPay",CommonConfig.SUPER2FIRST_TOTALPAY);



        return "operate_config/levelup";
    }

    @RequiresPermissions(Permission.OPERATE_LEVEL_UP_UPDATE)
    @RequestMapping("levelUpConditon")
    public void update(Model model,
                         HttpServletResponse response,
                         @RequestParam(value = "levelUpType", defaultValue = "0") int  levelUpType,
                         @RequestParam(value = "totalPay", defaultValue = "0") double  totalPay,
                         @RequestParam(value = "totalPromoter", defaultValue = "0") int  totalPromoter) {
        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        DictLevelUpModel levelUp = new DictLevelUpModel();
        levelUp.setGameId(gameId);
        levelUp.setLevelUpType(levelUpType);
        levelUp.setTotalPay(totalPay);
        levelUp.setTotalPromoter(totalPromoter);
        levelUp.setIsEnable(true);

        DictLevelUpModel levelUpModel = DictLevelUpDao.getConditionByGameIdAndLevelUpType(gameId, levelUpType);

        //不存在就新增  存在就更新
        if(levelUpModel == null){
            int m = levelUpService.add(levelUp);
            RequestUtils.write(response,""+m);
        }else {
            levelUp.setId(levelUpModel.getId());
            int n = levelUpService.update(levelUp);
            RequestUtils.write(response,""+n);
        }

    }

}
