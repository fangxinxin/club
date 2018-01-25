package admin.controller.operate_config;

import admin.service.ClubService;
import dsqp.common_const.club_admin.Permission;
import dsqp.db_club_dict.dao.DictClubDao;
import dsqp.db_club_dict.model.DictClubModel;
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
 * Created by jeremy on 2017/9/27.
 */
@Controller
@RequestMapping("operate_config/")
public class ClubController {

    @Resource
    private ClubService clubService;

    @RequestMapping("clubConfig")
    public String index(Model model){
        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        String gameName = CommonUtils.getStringValue(session.getAttribute("gameName"));
        model.addAttribute("gameName",gameName);
        DictClubModel dictClub = DictClubDao.getByGameId(gameId);
        model.addAttribute("dictClub", dictClub);

        return "operate_config/club";
    }

    /**
     * 俱乐部配置修改
     * @param response
     * @param isAllow
     */
    @RequiresPermissions(Permission.OPERATE_MODIFICATION_UPDATE)
    @RequestMapping("modification")
    public void modification(HttpServletResponse response,
                        @RequestParam(value = "isAllowSell", defaultValue = "") String  isAllow) {
        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        boolean isAllowSell = isAllow.equals("开启中") ? true : false;

        //存在则更新，不存在则新增
        DictClubModel dictClub = new DictClubModel();
        dictClub.setGameId(gameId);
        dictClub.setRemark("");
        dictClub.setAllowSell(isAllowSell);

        DictClubModel dict = DictClubDao.getByGameId(gameId);

        if (dict != null) {
            dictClub.setId(dict.getId());
            int m = clubService.update(dictClub);
            RequestUtils.write(response, "" + m);
        } else {
            dictClub.setJoinMax(1);
            int n = clubService.add(dictClub);
            RequestUtils.write(response, "" + n);
        }

    }



}
