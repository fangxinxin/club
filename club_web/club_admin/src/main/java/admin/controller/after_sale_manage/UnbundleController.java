package admin.controller.after_sale_manage;

import admin.service.UnbundleService;
import com.google.common.collect.Maps;
import dsqp.common_const.club.LogQuitWay;
import dsqp.common_const.club_admin.Permission;
import dsqp.db.model.DataTable;
import dsqp.db_club.dao.ClubDao;
import dsqp.db_club.dao.ClubShareDao;
import dsqp.db_club.dao.ClubUserDao;
import dsqp.db_club.dao.PromoterDao;
import dsqp.db_club.model.ClubModel;
import dsqp.db_club.model.ClubUserModel;
import dsqp.db_club.model.PromoterModel;
import dsqp.db_club_dict.dao.DictGameDbDao;
import dsqp.db_club_dict.model.DictGameDbModel;
import dsqp.db_club_log.dao.LogClubJoinDao;
import dsqp.db_club_log.dao.LogClubQuitDao;
import dsqp.db_club_log.model.LogClubQuitModel;
import dsqp.db_game.dao.dev.UUserPointDao;
import dsqp.util.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by jeremy on 2017/7/30.
 */
@Controller
@RequestMapping("after_sale_manage/")
public class UnbundleController {

    @Resource
    private UnbundleService unbundleService;

    @RequestMapping("unbundle")
    public String index() {
        return "after_sale_manage/unbundle";
    }

    /**
     * 查询玩家
     *
     * @param response
     * @param gameUserId
     * @return
     */
    @RequiresPermissions(Permission.SYS_QUERY_PLAYER_SHOW)
    @RequestMapping("searchPlayer")
    public ModelAndView search(HttpServletResponse response,
                               @RequestParam(value = "gameUserId", defaultValue = "0") long gameUserId,
                               @RequestParam(value = "clubId", defaultValue = "0") long clubId) {
        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        String gameName = CommonUtils.getStringValue(session.getAttribute("gameName"));
        DictGameDbModel dictDb = DictGameDbDao.getByGameId(gameId, true);
        ModelAndView mv = new ModelAndView("after_sale_manage/ajax/unbundle_dlg");

        if (clubId == 0) {
            ClubUserModel clubUser = ClubUserDao.queryByGameIdAndGameUserId(gameId, gameUserId);//存在的话，则取第一个俱乐部信息
            if (clubUser == null) {
                return mv;
            }
            ClubModel club = ClubDao.getOne(clubUser.getClubId());
            if (club == null) {
                return mv;
            }
            int userNum = ClubUserDao.getUserNum(club.getPromoterId());

            //查所在俱乐部推广员信息
            PromoterModel promoter = PromoterDao.getOne(club.getPromoterId());

            mv.addObject("userNum", userNum);
            mv.addObject("promoter", promoter);
            mv.addObject("clubUser", clubUser);
            mv.addObject("club", club);

        } else {
            ClubModel club = ClubDao.getOne(clubId);
            ClubUserModel clubUser = ClubUserDao.queryByClubIdAndGameUserId(clubId, gameUserId);
            if (club == null) {
                return mv;
            }
            int userNum = ClubUserDao.getUserNum(club.getPromoterId());

            //查所在俱乐部推广员信息
            PromoterModel promoter = PromoterDao.getOne(club.getPromoterId());

            mv.addObject("userNum", userNum);
            mv.addObject("promoter", promoter);
            mv.addObject("clubUser", clubUser);
            mv.addObject("club", club);
        }

        //查当前钻石数
        DataTable pointByUserId = UUserPointDao.getPointByUserId(dictDb, gameUserId);
        int diamond = 0;
        if (pointByUserId.rows.length > 0) {
            diamond = Integer.parseInt(pointByUserId.rows[0].getColumnValue("privateRoomDiamond"));
        }

        List<Long> clubIds = ClubUserDao.listClubId(gameId, gameUserId);//玩家加入的俱乐部集
        DataTable dtClubInfo = ClubDao.listClubInfoById(clubIds);

        mv.addObject("diamond", diamond);
        mv.addObject("gameName", gameName);
        mv.addObject("clubList", dtClubInfo.rows);

        return mv;
    }

    /**
     * 解除绑定
     *
     * @param response
     * @param gameUserId
     */
    @RequiresPermissions(Permission.OPERATE_REMOVE_PLAYER_DELETE)
    @RequestMapping("removePlayer")
    public void remove(HttpServletResponse response,
                       @RequestParam(value = "gameUserId", defaultValue = "0") long gameUserId,
                       @RequestParam(value = "clubId", defaultValue = "0") long clubId) {
        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));

        ClubUserModel clubUser = ClubUserDao.queryByClubIdAndGameUserId(clubId, gameUserId);
        if (gameUserId != 0 && clubId != 0) {
            //promoterId不为0时(普通玩家)，才能解绑（群主不能解绑）
            long promoterId = clubUser.getPromoterId();
            if (promoterId != 0) {
                int i = unbundleService.unBundleGameUserByClubIdAndGameUserId(clubId, gameUserId, clubUser.getGameNickName());
                //解绑成功
                if (i > 0) {
                    //如果俱乐部在审核期，玩家退会的同时删除log_club_join中的记录
                    ClubModel club = ClubDao.getOne(clubId);
                    if(club.getClubStatus()==0){
                        int i2 = LogClubJoinDao.removeByClubIdAndGameUserId(clubId, gameUserId);
                    }


                    //删除club_Share相关记录
                    ClubShareDao.removeByClubIdAndGameUserId(clubId, gameUserId);
                    //俱乐部人数减一
                    int i1 = ClubDao.decreaseClubNum(clubId);

                    //生成日志
                    LogClubQuitModel logClubQuitModel = new LogClubQuitModel();
                    logClubQuitModel.setGameId(gameId);
                    logClubQuitModel.setClubId(clubId);
                    logClubQuitModel.setGameUserId(gameUserId);
                    logClubQuitModel.setGameNickName(clubUser.getGameNickName());
                    logClubQuitModel.setQuitWay(LogQuitWay.CUSTOMER_SERVICE);
                    logClubQuitModel.setCreateTime(new java.util.Date());
                    logClubQuitModel.setCreateDate(DateUtils.String2Date(DateUtils.Date2String(new java.util.Date())));

                    LogClubQuitDao.add(logClubQuitModel);


                    String users = CommonUtils.getStringValue(gameUserId);
                    DictGameDbModel m = DictGameDbDao.getByGameId(CommonUtils.getIntValue(SecurityUtils.getSubject().getSession().getAttribute("gameId")), true);
                    String url = "http://" + m.getWebDomain() + ":" + m.getWebPort() + "/api";
                    //玩家解绑，更新状态
                    try {
                        Map<String, String> maps = Maps.newHashMap();
                        maps.put("title", "通知");
                        maps.put("content", EncodingUtils.urlEncode(ClubDao.getOne(clubId).getClubName()) + "已与您解除关系，请重新加入俱乐部。");
                        maps.put("reward", "");
                        maps.put("startTime", EncodingUtils.urlEncode(DateUtils.DateTime2String(new Date())));
                        HttpUtils.get(url + "/mail/?" + SignUtil.getParamsStrFromMap(maps) + "&act=newusermails&userIds=" + users);
                        HttpUtils.get(url + "/club/?act=quit&way=1&userId=" + users + "&clubId=" + clubId + "&result=1");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                RequestUtils.write(response, "" + i);
            } else {
                RequestUtils.write(response, "qz");
            }
        } else {
            RequestUtils.write(response, "" + 0);
        }
    }

}
