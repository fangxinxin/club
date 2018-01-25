package admin.controller.operate_config;

import admin.service.ConvertService;
import dsqp.common_const.club.CommonConfig;
import dsqp.common_const.club_admin.Permission;
import dsqp.db_club_dict.dao.DictFormalDao;
import dsqp.db_club_dict.model.DictFormalModel;
import dsqp.util.CommonUtils;
import dsqp.util.DateUtils;
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
import java.util.Date;

/**
 * Created by jeremy on 2017/7/30.
 */
@Controller
@RequestMapping("operate_config/")
public class ConvertController {

    @Resource
    private ConvertService convertService;

    @RequestMapping("convert")
    public String index(Model model){
        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        String gameName = CommonUtils.getStringValue(session.getAttribute("gameName"));
        model.addAttribute("gameName",gameName);
        DictFormalModel dictFormal = DictFormalDao.getByGameId(gameId);
        model.addAttribute("dictFormal", dictFormal);

        //默认的配置
        model.addAttribute("expireDay", CommonConfig.EXPIRE_DAY);
        model.addAttribute("newPeopleNum", CommonConfig.NEW_PEOPLE_NUM);
        model.addAttribute("peopleNum", CommonConfig.PEOPLE_NUM);
        model.addAttribute("pyjRoomNum", CommonConfig.PYJ_ROOM_NUM);
        model.addAttribute("nonPyjRoomNum", CommonConfig.NON_PYJ_ROOM_NUM);
        model.addAttribute("award", CommonConfig.AWARD);
        model.addAttribute("refreshDay", CommonConfig.REFRESH_DAY);

        return "operate_config/convert";
    }

    /**
     * 保存转正配置修改
     * @param response
     * @param expireDay
     * @param newPeopleNum
     * @param pyjRoomNum
     * @param nonPyjRoomNum
     */
    @RequiresPermissions(Permission.OPERATE_CONVERT_UPDATE)
    @RequestMapping("convertCondition")
    public void convert(HttpServletResponse response,
                          @RequestParam(value = "expireDay", defaultValue = "0") Double  expireDay,
                          @RequestParam(value = "newPeopleNum", defaultValue = "0") int  newPeopleNum,
                          @RequestParam(value = "peopleNum", defaultValue = "0") int  peopleNum,
                          @RequestParam(value = "pyjRoomNum", defaultValue = "0") int  pyjRoomNum,
                          @RequestParam(value = "nonPyjRoomNum", defaultValue = "0") int  nonPyjRoomNum,
                          @RequestParam(value = "award", defaultValue = "0") int  award
//                          ,@RequestParam(value = "refreshDay", defaultValue = "0") int  refreshDay
    ) {
        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));

        //存在则更新，不存在则新增
        DictFormalModel dictFormal = new DictFormalModel();
        dictFormal.setGameId(gameId);
        dictFormal.setExpireDay(expireDay);
        dictFormal.setNewPeopleNum(newPeopleNum);
        dictFormal.setPeopleNum(peopleNum);
        dictFormal.setPyjRoomNum(pyjRoomNum);
        dictFormal.setNonPyjRoomNum(nonPyjRoomNum);
        dictFormal.setAward(award);
//        dictFormal.setRefreshDay(refreshDay);
        dictFormal.setCreateDate(DateUtils.String2Date(DateUtils.Date2String(new Date())));
        dictFormal.setIsEnable(true);

        DictFormalModel dict = DictFormalDao.getByGameId(gameId);

        if(dict!=null){
            dictFormal.setId(dict.getId());
            int m = convertService.update(dictFormal);
            RequestUtils.write(response,""+m);
        }else {
            int n = convertService.add(dictFormal);
            RequestUtils.write(response,""+n);
        }

    }


}
