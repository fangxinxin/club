package admin.controller.after_sale_manage;

import admin.service.FlushService;
import com.google.common.collect.Maps;
import db_admin.model.SysUserModel;
import dsqp.common_const.club.ClubStatus;
import dsqp.common_const.club_admin.Permission;
import dsqp.db.model.DataRow;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db_club.dao.ClubDao;
import dsqp.db_club.dao.ClubUserDao;
import dsqp.db_club.dao.PromoterDao;
import dsqp.db_club.dao.PromoterSellDao;
import dsqp.db_club.model.ClubModel;
import dsqp.db_club.model.PromoterModel;
import dsqp.db_club_dict.dao.DictGameDbDao;
import dsqp.db_club_dict.model.DictGameDbModel;
import dsqp.db_club_log.dao.LogClubJoinDao;
import dsqp.db_club_log.dao.LogLoginDao;
import dsqp.db_game.dao.dev.UUserPointDao;
import dsqp.util.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by mj on 2017/7/29.
 */

@Controller
@RequestMapping("after_sale_manage")
public class FlushClubController {
    @Resource
    private FlushService flushService;

    @RequestMapping("flush")
    public String flush() {
        return "after_sale_manage/flush_club";
    }

    /**
     * 查询俱乐部信息
     * @param inValue
     * @param model
     * @return
     */
    @RequiresPermissions(Permission.SYS_QUERY_CLUB_SHOW)
    @RequestMapping("queryClub")
    public String queryByUserId(@RequestParam(value = "inValue", defaultValue = "") String inValue
            , Model model) {
        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        String gameName = (String) session.getAttribute("gameName");

        long id = Long.parseLong(inValue);
        ClubModel club = ClubDao.queryByIdAndStatus(id, gameId);
        if (club != null) {
            if (club.getClubStatus() == 0) {
                model.addAttribute("clubStatus", 0);
                return "after_sale_manage/flush_club";
            }
            PromoterModel promoter = PromoterDao.getOne(club.getPromoterId());
            DataTable list = ClubUserDao.getListByClubId(club.getId());
            DataTable dt = PromoterSellDao.queryToralByPromoterAndGameId(gameId, promoter.getId());
            //拼接一列，totalPay
            DBUtils.addColumn(list, dt, "gameUserId", "totalPay");
            if (list.rows.length > 0) {
                List gameUserId = DBUtils.convert2List(Long.class, "gameUserId", list);
                DictGameDbModel gameDbModel = DictGameDbDao.getByGameId(gameId, true);
                DataTable remain = UUserPointDao.listPoint(gameDbModel, gameUserId);
                //拼接列 privateRoomDiamond,loginTime
                DBUtils.addColumn(list, remain, "gameUserId", "privateRoomDiamond");
            }
            PromoterModel upPromoter = PromoterDao.getOne(promoter.getParentId());

            DataTable logLogin = LogLoginDao.queryByPromoterId(promoter.getId());
            if (logLogin.rows.length > 0) {
                model.addAttribute("logLogin", logLogin.rows[0]);
            }
            model.addAttribute("gameName", gameName);
            model.addAttribute("club", club);
            model.addAttribute("promoter", promoter);
            model.addAttribute("list", list.rows);
            model.addAttribute("upPromoter", upPromoter);
        } else {
            model.addAttribute("isEmpty", "empty");
        }
        return "after_sale_manage/flush_club";
    }


    /**
     * 解散俱乐部
     * 1.解散俱乐部(清空俱乐部玩家（包含群主）、清空club_share相关信息、删除俱乐部、删除promoter代理信息)
     * 2.log_club_clear加一条记录
     * 3.推送通知 :: 玩家被踢出俱乐部
     * by ds
     */
    @RequiresPermissions(Permission.OPERATE_FLUSH_CLUB_DELETE)
    @RequestMapping("flushUser")
    @ResponseBody
    public void flushUser(@RequestParam(value = "clubId", defaultValue = "") long clubId, HttpServletResponse response) {

        Subject subject = SecurityUtils.getSubject();
        SysUserModel admin = (SysUserModel) subject.getPrincipal();
        boolean result = false;

        ClubModel club = ClubDao.getOne(clubId);
        if (club != null) {
            PromoterModel p = PromoterDao.getOne(club.getPromoterId());
            DataTable clubUserDt = ClubUserDao.getListByClubId(clubId);

            /* 1.解散俱乐部 */
            result = flushService.dissolveClubByClubId(club, p, admin);
            if (result) {

                //释放俱乐部玩家
                if (club.getClubStatus() == ClubStatus.Init.getClubStatus()) {
                    LogClubJoinDao.removeByClubIdAndUserIds(clubId, DBUtils.convert2List(Long.class, "gameUserId", clubUserDt));
                }

                DictGameDbModel m = DictGameDbDao.getByGameId(CommonUtils.getIntValue(SecurityUtils.getSubject().getSession().getAttribute("gameId")), true);
                String url = "http://" + m.getWebDomain() + ":" + m.getWebPort() + "/api";

                //TODO 推送通知 :: 解散俱乐部    2017-10-17
                try {
                    HttpUtils.get(url + "/club/?act=destroy&userId=" + club.getGameUserId() + "&clubId=" + clubId+"&gameId="+club.getGameId());
                } catch (IOException e) {
                    e.printStackTrace();
                }


                StringBuilder sb = new StringBuilder();
                for (DataRow row : clubUserDt.rows) {
                    sb.append(row.getColumnValue("gameUserId")).append(",");
                }
                String u = sb.toString();
                if (u.length() > 0) {
                    Map<String, String> maps = Maps.newHashMap();
                    String users = u.substring(0, u.length() - 1);
                    maps.put("userIds", users);
                    maps.put("title", "通知");
                    maps.put("content", "您所在的俱乐部" + EncodingUtils.urlEncode(club.getClubName()) + "已被解散。");
                    maps.put("reward", "");
                    maps.put("startTime", EncodingUtils.urlEncode(DateUtils.DateTime2String(new Date())));
//                    maps.put("endTime", "");

                    //TODO 推送通知 :: 玩家被踢出俱乐部
                    try {
                        HttpUtils.get(url + "/mail/?" + SignUtil.getParamsStrFromMap(maps) + "&act=newusermails");
                        HttpUtils.get(url + "/user/?act=RefreshUIStatus&userIds=" + maps.get("userIds"));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

        }

        RequestUtils.write(response, String.valueOf(result));

    }

}



