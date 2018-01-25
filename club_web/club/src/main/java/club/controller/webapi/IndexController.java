package club.controller.webapi;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import dsqp.common_const.club.JoinStatus;
import dsqp.db.model.DataTable;
import dsqp.db_club.dao.ClubDao;
import dsqp.db_club.dao.ClubShareDao;
import dsqp.db_club.dao.ClubUserDao;
import dsqp.db_club.model.ClubModel;
import dsqp.db_club.model.ClubShareModel;
import dsqp.db_club_dict.dao.DictMpDao;
import dsqp.db_club_dict.model.DictGameDbModel;
import dsqp.db_club_dict.model.DictMpModel;
import dsqp.util.CommonUtils;
import dsqp.util.EncodingUtils;
import dsqp.util.HttpUtils;
import dsqp.util.SignUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * Created by Aris on 2017/8/1.
 */
@Controller("webpai_index")
@RequestMapping("webapi")
public class IndexController {
    private static final String PATH = "webapi/";
    private static final String AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize";

    @RequestMapping(value = "{mpClassName}/{clubId}/join_club")
    public String JoinClub(HttpServletRequest request, Model model
            , @PathVariable("mpClassName") String mpClassName
            , @PathVariable("clubId") long clubId
//        , @RequestParam(value = "openid", defaultValue = "") String openid
            , @RequestParam(value = "unionid", defaultValue = "") String unionid){
        if (Strings.isNullOrEmpty(mpClassName)) {
            model.addAttribute("error","缺少类名配置");
            return "error";
        }

        DictMpModel mp = DictMpDao.getByClassName(mpClassName);
        if (mp == null) {
            model.addAttribute("error","该公众号未配置");
            return "error";
        }

        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        basePath = basePath + "/webapi/" + mpClassName + "/" + clubId + "/join_club?token=" + System.currentTimeMillis();
//        if (Strings.isNullOrEmpty(openid) || Strings.isNullOrEmpty(unionid)){
        if (Strings.isNullOrEmpty(unionid)) {
            String redirectUrl = getCode(mp.getAppId(), mp.getRedirectUri(), EncodingUtils.urlEncode(basePath));
            return "redirect:" + redirectUrl;
        }

        //获取不到unionid
        if (unionid.equalsIgnoreCase("miss")) {
            model.addAttribute("error","获取unionid失败");
            return "error";
        }

        model.addAttribute("className", mpClassName);
        model.addAttribute("clubId", clubId);
        model.addAttribute("gameId", mp.getGameId());
        model.addAttribute("unionid", unionid);

        if (clubId <= 0) {
            model.addAttribute("error","俱乐部id格式有误");
            return "error";
        }

        ClubModel club = ClubDao.getOne(clubId);
        if (club == null) {
            model.addAttribute("error","俱乐部不存在");
            return "error";
        }

        //在游戏里走一波，查看信息
        DictGameDbModel db = dsqp.db_club_dict.dao.DictGameDbDao.getByGameId(mp.getGameId(), true);
        long gameUserId = 0;
        DataTable gameUser = new DataTable();
        if (db != null) {
            gameUser = dsqp.db_game.dao.dev.UUserInfoDao.getByWxUnionID(db, unionid);
        }

        if (gameUser.rows.length > 0) {
            gameUserId = CommonUtils.getLongValue(gameUser.rows[0].getColumnValue("userId"));
        }

        //先判断这个家伙是不是代理商
        if (club.getGameUserId() == gameUserId) {
            model.addAttribute("error", "您已经成为代理商，不能加入其他俱乐部");
            return "error";
        }

        if (mp.getGameId() == 4443) {
            if (ClubUserDao.getByGameIdAndGameUserId(mp.getGameId(), gameUserId) != null){
                model.addAttribute("error", "你已经加入过俱乐部");
                return "error";
            }

            ClubShareModel share = ClubShareDao.getByUnionid(unionid, mp.getGameId());
            if (share == null) {
                share = new ClubShareModel();
                share.setMpClassName(mpClassName);
                share.setGameId(mp.getGameId());
                share.setGameUserId(gameUserId);
                share.setUnionid(unionid);
                share.setPromoterId(club.getPromoterId());
                share.setClubId(clubId);
                share.setClubURL(basePath);
                share.setHeadImg("");
                share.setJoinStatus(JoinStatus.Disagree.getJoinStatus());
                share.setCreateTime(new Date());
                share.setCreateDate(new Date());
                //采集信息完毕
                ClubShareDao.add(share);
                model.addAttribute("mpName",   mp.getMpName());
                model.addAttribute("clubName", club.getClubName());
                return PATH + "join_club";
            } else if (share.getJoinStatus() == JoinStatus.Disagree.getJoinStatus()) {
                model.addAttribute("mpName",   mp.getMpName());
                model.addAttribute("clubName", club.getClubName());
                return PATH + "join_club";
            } else {
                model.addAttribute("error","你已经加入过俱乐部");
                return "error";
            }
        } else {
            //先判断玩家是否在这个俱乐部
            if (ClubUserDao.getByGameIdGameUserIdAndClubId(mp.getGameId(), gameUserId, clubId) != null) {
                model.addAttribute("error", "您已经加入该俱乐部");
                return "error";
            }

            //判断有无重复申请
            ClubShareModel share = ClubShareDao.getByUnionidAndClubId(unionid, mp.getGameId(), clubId);
            if (share == null) {
                share = new ClubShareModel();
                share.setMpClassName(mpClassName);
                share.setGameId(mp.getGameId());
                share.setGameUserId(gameUserId);
                share.setUnionid(unionid);
                share.setPromoterId(club.getPromoterId());
                share.setClubId(clubId);
                share.setClubURL(basePath);
                share.setHeadImg("");
                share.setJoinStatus(JoinStatus.Disagree.getJoinStatus());
                share.setCreateTime(new Date());
                share.setCreateDate(new Date());
                //采集信息完毕
                if (ClubShareDao.add(share) > 0) {
                    model.addAttribute("mpName",   mp.getMpName());
                    model.addAttribute("clubName", club.getClubName());
                    return PATH + "join_club";
                } else {
                    model.addAttribute("error","加入失败，请联系客服");
                    return "error";
                }
            } else if (share.getJoinStatus() == JoinStatus.Disagree.getJoinStatus()) {
                model.addAttribute("mpName",   mp.getMpName());
                model.addAttribute("clubName", club.getClubName());
                return PATH + "join_club";
            } else if (share.getJoinStatus() == JoinStatus.Agree.getJoinStatus()) {
                model.addAttribute("error","申请成功,请等待群主同意");
                return "error";
            } else if (share.getJoinStatus() == JoinStatus.Registerd.getJoinStatus()) {
                model.addAttribute("error", "您已经加入该俱乐部");
                return "error";
            } else {
                model.addAttribute("error","异常错误，请联系客服");
                return "error";
            }
        }

    }

    @RequestMapping(value = "{mpClassName}/{clubId}/agree")
    public String agree(Model model
            , @PathVariable("clubId") long clubId
            , @RequestParam(value = "gameId", defaultValue = "0") int gameId
            , @RequestParam(value = "unionid", defaultValue = "") String unionid){
        if (gameId <= 0){
            model.addAttribute("error", "游戏id错误");
            return "error";
        }

        if (Strings.isNullOrEmpty(unionid)){
            model.addAttribute("error", "缺少unionid");
            return "error";
        }

        if (clubId <= 0){
            model.addAttribute("error","俱乐部id格式有误");
            return "error";
        }

        ClubShareModel share = null;
        if (gameId == 4443) {
            share = ClubShareDao.getByUnionid(unionid, gameId);
        } else {
            share = ClubShareDao.getByUnionidAndClubId(unionid, gameId, clubId);
        }

        if (share == null){
            model.addAttribute("error", "缺少玩家分享信息");
            return "error";
        }

        int result = ClubShareDao.updateByJoinStatus(JoinStatus.Agree.getJoinStatus(), share.getId());
        if (result <= 0){
            model.addAttribute("error", "加入失败，请稍后重试");
            return "error";
        }

        String downloadLink;
        String url = "http://app74.stevengame.com:8080/share/getUrl?gameId=";
        try {
            downloadLink = HttpUtils.get(url + gameId);
        } catch (IOException e) {
            downloadLink = "";
        }

        if (Strings.isNullOrEmpty(downloadLink)) {
            model.addAttribute("error", "获取下载链接失败");
            return "error";
        }
        /*
        DictGameDbModel db = DictGameDbDao.getByGameId(gameId, true);
        if (db == null){
            model.addAttribute("error", "获取下载链接失败");
            return "error";
        }

        DataTable dt = SPrivateroomconfigCfgDao.getByGameId(db, gameId);
        if (dt.rows.length <= 0){
            model.addAttribute("error", "获取下载链接失败");
            return "error";
        }

        String downloadLink = CommonUtils.getStringValue(dt.rows[0].getColumnValue("downloadLink"));
        */
        model.addAttribute("link", downloadLink);

        return PATH + "agree";
    }

    private static String getCode(String appid, String redirect_uri,String state) {
        Map<String,String> requestMap = Maps.newLinkedHashMap();
        requestMap.put("appid",         appid);
        requestMap.put("redirect_uri",  EncodingUtils.urlEncode(redirect_uri));
        requestMap.put("response_type", "code");
        requestMap.put("scope",         "snsapi_userinfo");
        requestMap.put("state",          state);

        String getUrl = AUTHORIZE_URL + "?" + SignUtil.getParamsStrFromMap(requestMap) + "#wechat_redirect";
        return getUrl;
    }

}
