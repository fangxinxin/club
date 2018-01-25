package club.controller.club;

import club.service.ClubUserService;
import club.service.SellService;
import club.vo.MsgVO;
import club.vo.PaginationVO;
import com.google.common.collect.Maps;
import dsqp.common_const.club.GamecardSource;
import dsqp.common_const.club.PassStatus;
import dsqp.common_const.club.QuitStatus;
import dsqp.db.model.DataRow;
import dsqp.db.model.DataTable;
import dsqp.db.model.SplitPage;
import dsqp.db.util.DataTableUtils;
import dsqp.db.util.DateUtils;
import dsqp.db_club.dao.*;
import dsqp.db_club.model.ClubModel;
import dsqp.db_club.model.PromoterModel;
import dsqp.db_club_dict.dao.DictClubDao;
import dsqp.db_club_dict.dao.DictGameDbDao;
import dsqp.db_club_dict.model.DictClubModel;
import dsqp.db_club_dict.model.DictGameDbModel;
import dsqp.db_club_log.dao.LogGamecardDao;
import dsqp.db_club_log.model.LogGamecardModel;
import dsqp.util.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * Created by ds on 2017/7/21.
 */
@Controller
public class ClubController {
    private static final String PATH = "club/";

    @Autowired
    private ClubUserService clubUserService;
    @Autowired
    private SellService sellService;

    @RequestMapping("clubView")
    public ModelAndView clubView() {
        ModelAndView mv = new ModelAndView(PATH + "clubView");

        Subject subject = SecurityUtils.getSubject();
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
        long promoterId = CommonUtils.getLongValue(subject.getSession().getAttribute("id"));

        PromoterModel promoter = PromoterDao.getOne(promoterId);
        ClubModel club = ClubDao.getByPromoterId(promoterId);
        if (promoter == null && club == null) {
            return mv;
        }
        mv.addObject("club", club);
        mv.addObject("promoter", promoter);
        mv.addObject("num", ClubUserDao.getUserNum(promoterId));

        //是否可以售给任意玩家
        DictClubModel dictClubModel = DictClubDao.getByGameId(gameId);
        boolean isAllowSell = false;
        if (dictClubModel != null) {
            isAllowSell = dictClubModel.getIsAllowSell();
        }
        mv.addObject("isAllowSell", isAllowSell);

        //是否存在会员管理信息
        DataTable quitRequestList = ClubQuitDao.getRequestListByGameIdPromoterIdAndQuitStatus(gameId, promoterId, QuitStatus.APPLY.getQuitStatus());//离会申请列表
        DataTable joinRequestList = ClubJoinDao.getRequestListByGameIdPromoterIdAndPassStatus(gameId, promoterId, PassStatus.ASK.getJoinStatus());//入会申请列表
        if (quitRequestList.rows.length > 0 || joinRequestList.rows.length > 0) {
            mv.addObject("hasRequest", true);
        }

        return mv;
    }

    @RequestMapping("getClubDataGrid")
    @ResponseBody
    public void table(HttpServletResponse response
            , @RequestParam(value = "userId", defaultValue = "0") long userId
            , @RequestParam(value = "page", defaultValue = "1") int pageNum
            , @RequestParam(value = "size", defaultValue = "10") int pageSize) {

        Subject subject = SecurityUtils.getSubject();
        long clubId = CommonUtils.getLongValue(subject.getSession().getAttribute("clubId"));
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
        PaginationVO vo = new PaginationVO();

        DataTable clubDt = ClubDao.getOne2(clubId);
        if (clubDt.rows.length <= 0) {
            return;//俱乐部不存在
        }

        if (userId != 0) {
            DataTable dt = clubUserService.getByClubIdAndUserId(clubId, userId, gameId);
            vo.setRows(DataTableUtils.DataTable2JsonArray(dt));
            vo.setTotal("1");
        } else {
            SplitPage splitPage = clubUserService.listClubInfo(clubId, gameId, pageNum, pageSize);
            if (splitPage.getPageDate().rows.length > 0) {
                vo.setRows(DataTableUtils.DataTable2JsonArray(splitPage.getPageDate()));
                vo.setTotal(String.valueOf(splitPage.getTotalRecords()));
            }
        }

        RequestUtils.write(response, JsonUtils.getJson(vo));
    }

    @RequestMapping("clubDialog")
    public ModelAndView clubDialog(
            @RequestParam(value = "dlgId", defaultValue = "0") int dlgId
            , @RequestParam(value = "gameUserId", defaultValue = "0") long gameUserId) {
        Subject subject = SecurityUtils.getSubject();
        ModelAndView mv = new ModelAndView("club/dialog");

        long promoterId = CommonUtils.getLongValue(subject.getSession().getAttribute("id"));
        long clubId = CommonUtils.getLongValue(subject.getSession().getAttribute("clubId"));
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));

        DataTable clubDt = ClubUserDao.getByClubIdAndUserId(clubId, gameUserId);
        DataTable promoterDt = PromoterDao.getOne2(promoterId);
        if (clubDt.rows.length <= 0 && promoterDt.rows.length <= 0) {       //俱乐部，推广员不存在
            return mv;
        }

        DictGameDbModel db = dsqp.db_club_dict.dao.DictGameDbDao.getByGameId(gameId, true);
        DataTable gameUser = dsqp.db_game.dao.dev.UUserPointDao.getPointByUserId(db, gameUserId);

        DataRow club = clubDt.rows[0];
        mv.addObject("club", club);
        if (dlgId == 1) {//踢出俱乐部
            mv.addObject("totalSellNum", PromoterSellDao.getTotalSell(promoterId, gameUserId, true));
            mv.addObject("dlgId", 1);
        } else if (dlgId == 2) {//销售钻石
            mv.addObject("gameCard", promoterDt.rows[0].getColumnValue("gameCard"));
            mv.addObject("dlgId", 2);
        }
        long point = 0;
        if (gameUser.rows.length > 0) {
            point = CommonUtils.getLongValue(gameUser.rows[0].getColumnValue("privateRoomDiamond"));
        }
        mv.addObject("point", point);

        return mv;
    }

    @RequestMapping("sellGameCard")
    public void sellGameCard(HttpServletResponse response
            , @RequestParam(value = "sellNum", defaultValue = "0") int sellNum
            , @RequestParam(value = "gameUserId", defaultValue = "0") long gameUserId) {
        long promoterId = CommonUtils.getLongValue(SecurityUtils.getSubject().getSession().getAttribute("id"));
        long clubId = CommonUtils.getLongValue(SecurityUtils.getSubject().getSession().getAttribute("clubId"));
        MsgVO msg = sellService.sell(clubId, promoterId, sellNum, gameUserId);
        RequestUtils.write(response, JsonUtils.getJson(msg));
    }

    /**
     * 给游戏发邮件 :: 群主踢出俱乐部玩家
     * API :: /api/mail/?act=newusermails&userIds=&title=&content=&reward=&startTime=&endTime=
     *
     * @param gameUserId
     */
    @RequestMapping("removeClubUser")
    public void removeClubUser(HttpServletResponse response
            , @RequestParam(value = "gameUserId", defaultValue = "0") long gameUserId) {
        Subject subject = SecurityUtils.getSubject();
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
        long clubId = CommonUtils.getLongValue(SecurityUtils.getSubject().getSession().getAttribute("clubId"));
        ClubModel club = ClubDao.getOne(clubId);
        if (club == null) {
            return;
        }

        MsgVO msg = clubUserService.removeClubUser(clubId, gameUserId);
        if (msg.isSuccess()) {
            Map<String, String> maps = Maps.newHashMap();
            maps.put("userIds", String.valueOf(gameUserId));
            maps.put("title", "通知");
            maps.put("content", EncodingUtils.urlEncode(club.getClubName()) + "已与您解除关系，请重新加入俱乐部。");
            maps.put("reward", "");
            maps.put("startTime", EncodingUtils.urlEncode(DateUtils.DateTime2String(new Date())));

            DictGameDbModel m = DictGameDbDao.getByGameId(gameId, true);
            String url = "http://" + m.getWebDomain() + ":" + m.getWebPort() + "/api";       //推送通知 :: 踢出俱乐部
            //TODO 推送通知 :: 踢出俱乐部
            try {
                HttpUtils.get(url + "/mail/?" + SignUtil.getParamsStrFromMap(maps) + "&act=newusermails");
                HttpUtils.get(url + "/club/?act=quit&way=1&userId=" + maps.get("userIds") + "&clubId=" + clubId + "&result=1");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        RequestUtils.write(response, JsonUtils.getJson(msg));
    }

    /**
     * 更改可用房卡  代理商房卡数量
     * 2017-10-24   by fx
     */
    @RequestMapping("updateShareCard")
    @ResponseBody
    public void updateShareCard(HttpServletResponse response
            , @RequestParam(value = "shareCard", defaultValue = "0") int shareCard
            , @RequestParam(value = "gameCard", defaultValue = "0") long gameCard) {
        Date date = new Date();
        Subject subject = SecurityUtils.getSubject();
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
        long clubId = CommonUtils.getLongValue(SecurityUtils.getSubject().getSession().getAttribute("clubId"));
        long promoterId = CommonUtils.getLongValue(SecurityUtils.getSubject().getSession().getAttribute("id"));
        PromoterModel promoter = PromoterDao.getOne(promoterId);

        int oldShareCard = ClubDao.getOne(clubId).getShareCard();
        if ((gameCard + shareCard) == (promoter.getGameCard() + oldShareCard) && gameCard >= 0 && shareCard >= 0) {
            ClubDao.updateShareCard(clubId, promoterId, gameCard, shareCard);//更新可用房卡 代理商剩余房卡

            LogGamecardModel model = new LogGamecardModel();
            model.setGameId(gameId);
            model.setPromoterId(promoterId);
            model.setNickName(promoter.getNickName());
            model.setSource(GamecardSource.BYSHARE.getType());//设置类型为5
            model.setChangeNum(CommonUtils.getIntValue(gameCard - promoter.getGameCard()));
            model.setChangeBefore(promoter.getGameCard());
            model.setChangeAfter(gameCard);
            model.setCreateDate(date);
            model.setCreateTime(date);
            LogGamecardDao.add(model);//插入一条房卡变动记录
            RequestUtils.write(response, "OK");

        } else {
            RequestUtils.write(response, "更新失败，请重试");
        }
    }

}
