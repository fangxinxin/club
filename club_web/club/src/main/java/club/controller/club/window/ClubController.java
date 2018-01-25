package club.controller.club.window;

import club.service.SellService;
import club.vo.MsgVO;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import dsqp.common_const.club.LogClubJoinStatus;
import dsqp.common_const.club.LogQuitWay;
import dsqp.common_const.club.PassStatus;
import dsqp.common_const.club.QuitStatus;
import dsqp.db.model.DataRow;
import dsqp.db.model.DataTable;
import dsqp.db_club.dao.*;
import dsqp.db_club.model.*;
import dsqp.db_club_dict.dao.DictClubDao;
import dsqp.db_club_dict.dao.DictGameDbDao;
import dsqp.db_club_dict.model.DictClubModel;
import dsqp.db_club_dict.model.DictGameDbModel;
import dsqp.db_club_log.dao.LogClubJoinDao;
import dsqp.db_club_log.dao.LogClubQuitDao;
import dsqp.db_club_log.model.LogClubJoinModel;
import dsqp.db_club_log.model.LogClubQuitModel;
import dsqp.db_game.dao.dev.UUserInfoDao;
import dsqp.db_game.dao.dev.UUserPointDao;
import dsqp.db_game.dao.log_dev.UPyjUserRecordDao;
import dsqp.util.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
@Controller("club/win")
@RequestMapping("win")
public class ClubController {
    private static final String PATH = "club/window/";

    @Autowired
    private SellService sellService;

    /**
     * 会员管理
     */
    @RequestMapping("member")
    public ModelAndView member() {
        ModelAndView mv = new ModelAndView(PATH + "win_member");

        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        long promoterId = CommonUtils.getLongValue(session.getAttribute("id"));

        DataTable quitRequestList = ClubQuitDao.getRequestListByGameIdPromoterIdAndQuitStatus(gameId, promoterId, QuitStatus.APPLY.getQuitStatus());//离会申请列表
        DataTable joinRequestList = ClubJoinDao.getRequestListByGameIdPromoterIdAndPassStatus(gameId, promoterId, PassStatus.ASK.getJoinStatus());//入会申请列表

        int joinMax = DictClubDao.getByGameId(gameId).getJoinMax();
        if (joinRequestList.rows.length > 0) {
            mv.addObject("hasJoinRequest", true);
        }
        if (quitRequestList.rows.length > 0) {
            mv.addObject("hasQuitRequest", true);
        }
        mv.addObject("isMember", true);//会员管理
        mv.addObject("joinMax", joinMax);//最多加入俱乐部数量


        return mv;
    }

    /**
     * 入会申请
     */
    @RequestMapping("member_joinMsg")
    public ModelAndView joinMsg(Model model) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        ModelAndView mv = new ModelAndView(PATH + "win_member_load");

        long promoterId = CommonUtils.getLongValue(subject.getSession().getAttribute("id"));
        DataTable dt = ClubJoinDao.getRequestListByGameIdPromoterIdAndPassStatus(CommonUtils.getIntValue(session.getAttribute("gameId")), promoterId, PassStatus.ASK.getJoinStatus());
        if (dt.rows.length > 0) {
            model.addAttribute("joinMember", dt.rows);
        }
        return mv;
    }

    /**
     * 同意 拒绝入会申请
     * ds：增加新玩家入会记录
     */
    @RequestMapping("member_joinMsg_handle")
    @ResponseBody
    public void joinMsg_handle(HttpServletResponse response,
                               @RequestParam(value = "id", defaultValue = "0") long id,
                               @RequestParam(value = "gameUserId", defaultValue = "0") long gameUserId,
                               @RequestParam(value = "gameNickName", defaultValue = "0") String gameNickName,
                               @RequestParam(value = "handle", defaultValue = "0") int handle) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        Date date = new Date();
        long clubId = CommonUtils.getLongValue(session.getAttribute("clubId"));
        long promoterId = CommonUtils.getLongValue(subject.getSession().getAttribute("id"));
        DictGameDbModel m = DictGameDbDao.getByGameId(CommonUtils.getIntValue(SecurityUtils.getSubject().getSession().getAttribute("gameId")), true);

        if (handle == 2) {
            int refuseNum = ClubJoinDao.getRefuseNum(clubId, gameUserId, PassStatus.REFUSE.getJoinStatus());

            String urlTwo = "http://" + m.getWebDomain() + ":" + m.getWebPort() + "/api";
            if (refuseNum == 0) {
                Map<String, String> maps = Maps.newHashMap();
                maps.put("userIds", CommonUtils.getStringValue(gameUserId));
                maps.put("title", "通知");
                maps.put("content", EncodingUtils.urlEncode(ClubDao.getOne(clubId).getClubName()) + "拒绝了您的加入请求,请重新加入");
                maps.put("reward", "");
                maps.put("startTime", EncodingUtils.urlEncode(DateUtils.DateTime2String(date)));
//                maps.put("endTime", "");

                //TODO 推送通知 :: 第一次被该俱乐部拒绝，发送邮件通知
                try {
                    System.out.println(HttpUtils.get(urlTwo + "/mail/?" + SignUtil.getParamsStrFromMap(maps) + "&act=newusermails"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //TODO 推送通知 :: 申请加入俱乐部被拒绝，发送邮件通知
            try {
                System.out.println(HttpUtils.get(urlTwo + "/club/?act=join&userId=" + gameUserId + "&clubId=" + clubId + "&way=0" + "&result=0"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            ClubJoinModel model = new ClubJoinModel();
            model.setId(id);
            model.setGameUserId(gameUserId);
            model.setPassStatus(PassStatus.REFUSE.getJoinStatus());
            model.setHandleTime(date);
            model.setClubId(clubId);
            ClubJoinDao.updateStatusById(model);//更新clubJoin状态

            ClubShareDao.removeByClubIdAndGameUserId(clubId, gameUserId);//删除clubShare相关记录

            RequestUtils.write(response, "REFUSE");
        } else {


            DataTable promoter = PromoterDao.queryByGameIdAndUserId(gameId, gameUserId, true);//查看该玩家是否是代理商
            DataTable promoter_two = PromoterDao.queryByGameIdAndUserId(gameId, gameUserId, false);//查看该玩家是否是代理商
            DictClubModel dict = DictClubDao.getByGameId(gameId);
            int clubNum = ClubUserDao.getJoinClubNums(gameId, gameUserId);
            DataTable byClubIdAndUserId = ClubUserDao.getByClubIdAndUserId(clubId, gameUserId);//查看该玩家是否是本俱乐部成员

            if (dict.getJoinMax() > clubNum && byClubIdAndUserId.rows.length == 0 && promoter.rows.length == 0 && promoter_two.rows.length == 0) {
                ClubJoinModel model = new ClubJoinModel();
                model.setPassStatus(PassStatus.AGREE.getJoinStatus());
                model.setHandleTime(date);
                model.setId(id);
                //同意申请 更新club_join状态
                ClubJoinDao.updateStatusById(model);

                ClubUserModel clubUserModel = new ClubUserModel();
                clubUserModel.setGameId(gameId);
                clubUserModel.setClubId(ClubDao.getByPromoterId(promoterId).getId());
                clubUserModel.setPromoterId(promoterId);
                clubUserModel.setPromoterGameUserId(PromoterDao.getOne(promoterId).getGameUserId());
                clubUserModel.setGameUserId(gameUserId);
                clubUserModel.setGameNickName(gameNickName);
                clubUserModel.setCreateTime(date);
                ClubUserDao.addOne(clubUserModel);//club_user加一条记录

                LogClubJoinModel logClubJoinModel = new LogClubJoinModel();
                logClubJoinModel.setGameId(gameId);
                logClubJoinModel.setClubId(clubId);
                logClubJoinModel.setGameUserId(gameUserId);
                logClubJoinModel.setGameNickName(gameNickName);
                if (handle == 1) {
                    logClubJoinModel.setJoinWay(LogClubJoinStatus.SHARE.getLogJoinStatus());
                } else {
                    logClubJoinModel.setJoinWay(LogClubJoinStatus.BYID.getLogJoinStatus());
                }
                logClubJoinModel.setCreateTime(date);
                logClubJoinModel.setCreateDate(date);
                LogClubJoinDao.add(logClubJoinModel);//log_club_join 加一条记录


                ClubDao.updatePeopleNum(clubId, ClubUserDao.getUserNum(promoterId));//更新club peopleNum

                //向前端推送一条记录
                String url = "http://" + m.getWebDomain() + ":" + m.getWebPort() + "/api";
                //TODO 推送通知 :: 加入俱乐部
                try {
                    String way;
                    if (handle == 1) {
                        //玩家申请加入
                        way = "0";
                    } else {
                        //群主拉人进俱乐部
                        way = "1";
                    }
                    String s = HttpUtils.get(url + "/club/?act=join&userId=" + gameUserId + "&clubId=" + clubId + "&way=" + way + "&result=1");
                    System.out.println(s);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Map<String, String> maps = Maps.newHashMap();
                maps.put("userIds", CommonUtils.getStringValue(gameUserId));
                maps.put("title", "通知");
                maps.put("content", "您已加入：" + EncodingUtils.urlEncode(ClubDao.getOne(clubId).getClubName()));
                maps.put("reward", "");
                maps.put("startTime", EncodingUtils.urlEncode(DateUtils.DateTime2String(new Date())));
//                maps.put("endTime", "");

                String urlTwo = "http://" + m.getWebDomain() + ":" + m.getWebPort() + "/api";
                //TODO 推送通知 :: 加入俱乐部 向玩家发一封通知邮件
                try {
                    HttpUtils.get(urlTwo + "/mail/?" + SignUtil.getParamsStrFromMap(maps) + "&act=newusermails");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                RequestUtils.write(response, "OK");
            } else {
                String reason;
                if (byClubIdAndUserId.rows.length > 0) {
                    reason = "该玩家已经是您的俱乐部成员";
                } else if (promoter.rows.length > 0 || promoter_two.rows.length > 0) {
                    reason = "该玩家已经是代理商";
                } else {
                    reason = "该玩家加入俱乐部数量超限";
                }
                ClubJoinModel model = new ClubJoinModel();
                model.setId(id);
                model.setGameUserId(gameUserId);
                model.setHandleTime(date);
                model.setGameUserId(gameUserId);
                model.setClubId(clubId);
                model.setPassStatus(PassStatus.OVERFLOW.getJoinStatus());
                ClubJoinDao.updateStatusById(model);
                RequestUtils.write(response, "加入失败，" + reason);
            }
        }
    }

    /**
     * 离会申请
     */
    @RequestMapping("member_leaveMsg")
    public ModelAndView leaveMsg() {
        Subject subject = SecurityUtils.getSubject();
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
        long promoterId = CommonUtils.getLongValue(subject.getSession().getAttribute("id"));
        ModelAndView mv = new ModelAndView(PATH + "win_member_load");
        DataTable quitRequestList = ClubQuitDao.getRequestListByGameIdPromoterIdAndQuitStatus(gameId, promoterId, QuitStatus.APPLY.getQuitStatus());

        mv.addObject("quitRequestList", quitRequestList.rows);//离会申请列表
        return mv;
    }


    @RequestMapping("agree_quit_request")
    public void agree_request(HttpServletResponse response,
                              @RequestParam(value = "id", defaultValue = "0") long id,
                              @RequestParam(value = "clubId", defaultValue = "0") long clubId,
                              @RequestParam(value = "gameNickName", defaultValue = "") String gameNickName,
                              @RequestParam(value = "gameUserId", defaultValue = "0") long gameUserId) {
        Subject subject = SecurityUtils.getSubject();
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));

        ClubQuitModel model = new ClubQuitModel();
        model.setId(id);
        model.setQuitStatus(QuitStatus.AGREE.getQuitStatus());
        model.setHandleTime(new Date());
        model.setGameUserId(gameUserId);

        int i = ClubQuitDao.update(model);

        if (i > 0) {
            //更新俱乐部信息
            int i1 = ClubUserDao.unBundleGameUserByClubIdAndGameUserId(clubId, gameUserId);

            if (i1 > 0) {
                //如果俱乐部在审核期，玩家退会的同时删除log_club_join中的记录
                ClubModel club = ClubDao.getOne(clubId);
                if (club.getClubStatus() == 0) {
                    int i2 = LogClubJoinDao.removeByClubIdAndGameUserId(clubId, gameUserId);
                }

                //删除club_Share相关记录
                ClubShareDao.removeByClubIdAndGameUserId(clubId, gameUserId);
                //俱乐部人数减1
                int i2 = ClubDao.decreaseClubNum(clubId);

                //生成日志
                LogClubQuitModel logClubQuitModel = new LogClubQuitModel();
                logClubQuitModel.setGameId(gameId);
                logClubQuitModel.setClubId(clubId);
                logClubQuitModel.setGameUserId(gameUserId);
                logClubQuitModel.setGameNickName(gameNickName);
                logClubQuitModel.setQuitWay(LogQuitWay.PLAYER_REQUEST);
                logClubQuitModel.setCreateTime(new java.util.Date());
                logClubQuitModel.setCreateDate(DateUtils.String2Date(DateUtils.Date2String(new java.util.Date())));

                LogClubQuitDao.add(logClubQuitModel);

                DictGameDbModel m = DictGameDbDao.getByGameId(gameId, true);
                String url = "http://" + m.getWebDomain() + ":" + m.getWebPort() + "/api";
                try {
                    HttpUtils.get(url + "/club/?act=quit&way=0&userId=" + gameUserId + "&clubId=" + clubId + "&result=1");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

        RequestUtils.write(response, String.valueOf(i));

    }

    @RequestMapping("refuse_quit_request")
    public void refuse_request(HttpServletResponse response,
                               @RequestParam(value = "id", defaultValue = "0") long id,
                               @RequestParam(value = "clubId", defaultValue = "0") long clubId,
                               @RequestParam(value = "gameNickName", defaultValue = "") String gameNickName,
                               @RequestParam(value = "gameUserId", defaultValue = "0") long gameUserId) {
        Subject subject = SecurityUtils.getSubject();
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
        ClubQuitModel model = new ClubQuitModel();
        model.setId(id);
        model.setQuitStatus(QuitStatus.REFUSE.getQuitStatus());
        model.setHandleTime(new Date());
        model.setGameUserId(gameUserId);

        int i = ClubQuitDao.update(model);

        DictGameDbModel m = DictGameDbDao.getByGameId(gameId, true);
        String url = "http://" + m.getWebDomain() + ":" + m.getWebPort() + "/api";
        try {
            HttpUtils.get(url + "/club/?act=quit&way=0&userId=" + gameUserId + "&clubId=" + clubId + "&result=0");
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestUtils.write(response, String.valueOf(i));

    }

    @RequestMapping("member_addMember")
    public ModelAndView addMember() {
        ModelAndView mv = new ModelAndView(PATH + "win_member_load");
        mv.addObject("isAddMember", true);//会员管理 :: 申请离开俱乐部

        return mv;
    }

    /**
     * 销售钻石
     */
    @RequestMapping("sell")
    public ModelAndView sellGameCard() {
        ModelAndView mv = new ModelAndView(PATH + "win_sell");
        Subject subject = SecurityUtils.getSubject();
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
        mv.addObject("isSell", true);//销售钻石

        //是否可以售给任意玩家
        DictClubModel dictClubModel = DictClubDao.getByGameId(gameId);
        boolean isAllowSell = false;
        if (dictClubModel != null) {
            isAllowSell = dictClubModel.getIsAllowSell();
        }
        mv.addObject("isAllowSell", isAllowSell);

        return mv;
    }

    @RequestMapping("sell_clubUser")
    public ModelAndView sell_clubUser(
            @RequestParam(value = "gameUserId", defaultValue = "0") long gameUserId) {
        Subject subject = SecurityUtils.getSubject();
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
        ModelAndView mv = new ModelAndView(PATH + "win_sell_load");

        mv.addObject("isClubUser", true);//销售钻石:: 显示成员信息

        //是否可以售给任意玩家
        DictClubModel dictClubModel = DictClubDao.getByGameId(gameId);
        boolean isAllowSell = false;
        if (dictClubModel != null) {
            isAllowSell = dictClubModel.getIsAllowSell();
        }
        mv.addObject("isAllowSell", isAllowSell);

        if (gameUserId != 0) {
            long promoterId = CommonUtils.getLongValue(subject.getSession().getAttribute("id"));
            long clubId = CommonUtils.getLongValue(subject.getSession().getAttribute("clubId"));

            //判断是否是本俱乐部成员
            ClubUserModel clubUserModel = ClubUserDao.queryByClubIdAndGameUserId(clubId, gameUserId);
            mv.addObject("clubUserModel", clubUserModel);

            DictGameDbModel db = DictGameDbDao.getByGameId(gameId, true);
            int gameCard = UUserPointDao.getGameCardByUserId(db, gameUserId);
            DataTable dt = UUserInfoDao.getByUserId(db, gameUserId);        //玩家信息

            PromoterModel promoter = PromoterDao.getOne(promoterId);

            if (dt.rows.length > 0 && promoter != null) {
                mv.addObject("clubUser", dt.rows[0]);
                mv.addObject("promoterGameCard", promoter.getGameCard());
                mv.addObject("gameCard", gameCard);
            }
        }

        return mv;
    }

    @RequestMapping("sell_check")
    public ModelAndView sell_check(
            @RequestParam(value = "gameUserId", defaultValue = "0") long gameUserId,
            @RequestParam(value = "sellNum", defaultValue = "0") int sellNum) {
        Subject subject = SecurityUtils.getSubject();
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
        ModelAndView mv = new ModelAndView(PATH + "win_sell_load");

        mv.addObject("isSellCheck", true);//销售钻石:: 二次确认


        //是否可以售给任意玩家
        DictClubModel dictClubModel = DictClubDao.getByGameId(gameId);
        boolean isAllowSell = false;
        if (dictClubModel != null) {
            isAllowSell = dictClubModel.getIsAllowSell();
        }
        mv.addObject("isAllowSell", isAllowSell);


        //判断是否是本俱乐部成员
        long clubId = CommonUtils.getLongValue(subject.getSession().getAttribute("clubId"));
        ClubUserModel clubUserModel = ClubUserDao.queryByClubIdAndGameUserId(clubId, gameUserId);
        mv.addObject("clubUserModel", clubUserModel);


        DictGameDbModel db = DictGameDbDao.getByGameId(gameId, true);
        DataTable dt = UUserInfoDao.getByUserId(db, gameUserId);        //玩家信息


        if (dt.rows.length > 0) {
            mv.addObject("clubUser", dt.rows[0]);
            mv.addObject("sellNum", sellNum);
        }

        return mv;
    }

    /**
     * 可销售全部玩家
     */
    @RequestMapping("sellAllGameCard")
    public void sellAllGameCard(HttpServletResponse response
            , @RequestParam(value = "sellNum", defaultValue = "0") int sellNum
            , @RequestParam(value = "gameUserId", defaultValue = "0") long gameUserId) {
        long promoterId = CommonUtils.getLongValue(SecurityUtils.getSubject().getSession().getAttribute("id"));
        MsgVO msg = sellService.sellAll(promoterId, sellNum, gameUserId);
        RequestUtils.write(response, JsonUtils.getJson(msg));
    }

    /**
     * 战绩查询
     */
    @RequestMapping("gameRecord")
    public ModelAndView gameRecord() {
        ModelAndView mv = new ModelAndView(PATH + "win_gameRecord");

        mv.addObject("isGameRecord", true);//会员管理
        mv.addObject("startDate", DateUtils.Date2String(DateUtils.addDay(-6, new Date()), "yyyy-MM-dd HH:mm"));
        mv.addObject("endDate", DateUtils.Date2String(new Date(), "yyyy-MM-dd HH:mm"));

        return mv;
    }

    /**
     * 战绩明细
     */
    @RequestMapping("gameRecord_detail")
    public ModelAndView gameRecord_detail(@RequestParam(value = "startDate", defaultValue = "") String startDate,
                                          @RequestParam(value = "endDate", defaultValue = "") String endDate) {
        Subject subject = SecurityUtils.getSubject();
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
        long clubId = CommonUtils.getIntValue(subject.getSession().getAttribute("clubId"));

        DictGameDbModel dictDb = DictGameDbDao.getByGameId(gameId, true);
        ModelAndView mv = new ModelAndView(PATH + "win_gameRecord_load");
        DataTable dtRecordDetail = UPyjUserRecordDao.getGameRecordDetail(dictDb, clubId, DateUtils.String2Date(startDate, "yyyy-MM-dd HH:mm"), DateUtils.String2Date(endDate, "yyyy-MM-dd HH:mm"));

        if (dtRecordDetail.rows.length > 0) {
            for (DataRow row : dtRecordDetail.rows) {
                if (CommonUtils.getIntValue(row.getColumnValue("winnerNum")) > 1) {
                    long winnerId = CommonUtils.getLongValue(row.getColumnValue("winnerId"));
                    long userId1 = CommonUtils.getLongValue(row.getColumnValue("userId1"));
                    long userId2 = CommonUtils.getLongValue(row.getColumnValue("userId2"));
                    long userId3 = CommonUtils.getLongValue(row.getColumnValue("userId3"));
                    //若有多个大赢家 把winnerId改为大赢家分数 用于 jsp匹配
                    if (winnerId == userId1) {
                        row.setColumnValue("winnerId", CommonUtils.getStringValue(row.getColumnValue("userCashDiff1")));
                    } else if (winnerId == userId2) {
                        row.setColumnValue("winnerId", CommonUtils.getStringValue(row.getColumnValue("userCashDiff2")));
                    } else if (winnerId == userId3) {
                        row.setColumnValue("winnerId", CommonUtils.getStringValue(row.getColumnValue("userCashDiff3")));
                    } else {
                        row.setColumnValue("winnerId", CommonUtils.getStringValue(row.getColumnValue("userCashDiff4")));
                    }
                }
                String userName1 = CommonUtils.getStringValue(row.getColumnValue("userName1"));
                String userName2 = CommonUtils.getStringValue(row.getColumnValue("userName2"));
                String userName3 = CommonUtils.getStringValue(row.getColumnValue("userName3"));
                String userName4 = CommonUtils.getStringValue(row.getColumnValue("userName4"));
                if (userName1.length() > 7) {
                    row.setColumnValue("userName1", userName1.substring(0, 5) + "..");
                }
                if (userName2.length() > 7) {
                    row.setColumnValue("userName2", userName2.substring(0, 5) + "..");
                }
                if (userName3.length() > 7) {
                    row.setColumnValue("userName3", userName3.substring(0, 5) + "..");
                }
                if (userName4.length() > 7) {
                    row.setColumnValue("userName4", userName4.substring(0, 5) + "..");
                }
            }
            mv.addObject("dtRecordDetail", dtRecordDetail.rows);
        }
        mv.addObject("isGameRecordDetail", true);//战绩明细查询

        return mv;
    }


    /**
     * 查玩家信息
     * 2017/10/23 更新：   判断userId是否是代理商 如果已是代理商则无法邀请加入
     */
    @RequestMapping("queryUserInfo")
    @ResponseBody
    public void queryUserInfo(HttpServletResponse response,
                              @RequestParam(value = "userId", defaultValue = "") long userId) {
        Subject subject = SecurityUtils.getSubject();
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
        DictGameDbModel byGameId = DictGameDbDao.getByGameId(gameId, true);
        DataTable dt = UUserInfoDao.getByUserId(byGameId, userId);
        if (dt.rows.length == 0) {
            //查不到玩家信息
            RequestUtils.write(response, "0");
        } else {
            DataTable promoter = PromoterDao.queryByGameIdAndUserId(gameId, userId, true);
            DataTable promoter_two = PromoterDao.queryByGameIdAndUserId(gameId, userId, false);
            if (promoter.rows.length == 0 && promoter_two.rows.length == 0) {
                // 该玩家不是代理商 可以邀请入会
                RequestUtils.write(response, dt.rows[0].getColumnValue("nickName"));
            } else {
                //玩家已经是代理商 无法邀请
                RequestUtils.write(response, "1");
            }
        }
    }


    /**
     * 战绩查询 :: 大赢家
     */
    @RequestMapping("gameRecord_winner")
    public ModelAndView gameRecord_winner(@RequestParam(value = "startDate", defaultValue = "") String startDate,
                                          @RequestParam(value = "endDate", defaultValue = "") String endDate) {
        Subject subject = SecurityUtils.getSubject();
        ModelAndView mv = new ModelAndView(PATH + "win_gameRecord_load");
        mv.addObject("isGameRecordWinner", true);

        long promoterId = CommonUtils.getLongValue(subject.getSession().getAttribute("id"));
        long clubId = ClubDao.getByPromoterId(promoterId).getId();
        DictGameDbModel m = DictGameDbDao.getByGameId(CommonUtils.getIntValue(SecurityUtils.getSubject().getSession().getAttribute("gameId")), true);
        DataTable winnerList = UPyjUserRecordDao.getWinnerList(m, clubId, DateUtils.String2Date(startDate, "yyyy-MM-dd HH:mm"), DateUtils.String2Date(endDate, "yyyy-MM-dd HH:mm"));
        if (winnerList.rows.length > 0) {
            for (int i = 0; i < winnerList.rows.length; i++) {
                winnerList.rows[i].setColumnValue("no", CommonUtils.getStringValue(i + 1));
                String winnerName = CommonUtils.getStringValue(winnerList.rows[i].getColumnValue("winnerName"));
                if (winnerName.length() > 7) {
                    winnerList.rows[i].setColumnValue("winnerName", winnerName.substring(0, 5) + "..");
                }
            }
            mv.addObject("winnerList", winnerList.rows);
        }
        return mv;
    }

    /**
     * 战绩查询 :: 房主
     */

    @RequestMapping("gameRecord_host")
    public ModelAndView gameRecord_host(@RequestParam(value = "startDate", defaultValue = "") String startDate,
                                        @RequestParam(value = "endDate", defaultValue = "") String endDate) {
        Subject subject = SecurityUtils.getSubject();
        ModelAndView mv = new ModelAndView(PATH + "win_gameRecord_load");
        mv.addObject("isGameRecordHost", true);//战绩查询 :: 房主

        DictGameDbModel m = DictGameDbDao.getByGameId(CommonUtils.getIntValue(SecurityUtils.getSubject().getSession().getAttribute("gameId")), true);
        long promoterId = CommonUtils.getLongValue(subject.getSession().getAttribute("id"));
        long clubId = ClubDao.getByPromoterId(promoterId).getId();
        DataTable ownerList = UPyjUserRecordDao.getOwnerList(m, clubId, DateUtils.String2Date(startDate, "yyyy-MM-dd HH:mm"), DateUtils.String2Date(endDate, "yyyy-MM-dd HH:mm"));
        if (ownerList.rows.length > 0) {
            for (int i = 0; i < ownerList.rows.length; i++) {
                ownerList.rows[i].setColumnValue("no", CommonUtils.getStringValue(i + 1));
                String ownerName = CommonUtils.getStringValue(ownerList.rows[i].getColumnValue("ownerName"));
                if (ownerName.length() > 7) {
                    ownerList.rows[i].setColumnValue("ownerName", ownerName.substring(0, 5) + "..");
                }
            }
            mv.addObject("ownerList", ownerList.rows);
        }
        return mv;
    }

    /**
     * 更新为 已阅 状态
     */
    @RequestMapping("gameRecord_detail_updateStatus")
    public void updateStatus(HttpServletResponse response,
                             @RequestParam(value = "id", defaultValue = "") long id) {
        Subject subject = SecurityUtils.getSubject();
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));

        DictGameDbModel dictDb = DictGameDbDao.getByGameId(gameId, true);
        int i = UPyjUserRecordDao.updateReadStatus(dictDb, id);

        RequestUtils.write(response, String.valueOf(i));
    }

    /**
     * 大赢家报表 对局明细
     */
    @RequestMapping("gameRecord_query_winDetail")
    public ModelAndView gameRecordQueryWinDetail(
            @RequestParam(value = "userId", defaultValue = "0") long userId,
            @RequestParam(value = "startDate", defaultValue = "") String startDate,
            @RequestParam(value = "endDate", defaultValue = "") String endDate) {
        Session session = SecurityUtils.getSubject().getSession();
        DictGameDbModel m = DictGameDbDao.getByGameId(CommonUtils.getIntValue(session.getAttribute("gameId")), true);
        long clubId = CommonUtils.getLongValue(session.getAttribute("clubId"));
        DataTable winnerDetail = UPyjUserRecordDao.getWinnerDetail(m, clubId, userId, DateUtils.String2Date(startDate, "yyyy-MM-dd HH:mm"), DateUtils.String2Date(endDate, "yyyy-MM-dd HH:mm"));

        ModelAndView mv = new ModelAndView(PATH + "dialogWin");
        if (winnerDetail.rows.length > 0) {
            for (DataRow row : winnerDetail.rows) {
                if (CommonUtils.getIntValue(row.getColumnValue("winnerNum")) > 1) {
                    long winnerId = CommonUtils.getLongValue(row.getColumnValue("winnerId"));
                    long userId1 = CommonUtils.getLongValue(row.getColumnValue("userId1"));
                    long userId2 = CommonUtils.getLongValue(row.getColumnValue("userId2"));
                    long userId3 = CommonUtils.getLongValue(row.getColumnValue("userId3"));
                    //若有多个大赢家 把winnerId改为大赢家分数 用于 jsp匹配
                    if (winnerId == userId1) {
                        row.setColumnValue("winnerId", CommonUtils.getStringValue(row.getColumnValue("userCashDiff1")));
                    } else if (winnerId == userId2) {
                        row.setColumnValue("winnerId", CommonUtils.getStringValue(row.getColumnValue("userCashDiff2")));
                    } else if (winnerId == userId3) {
                        row.setColumnValue("winnerId", CommonUtils.getStringValue(row.getColumnValue("userCashDiff3")));
                    } else {
                        row.setColumnValue("winnerId", CommonUtils.getStringValue(row.getColumnValue("userCashDiff4")));
                    }
                }
                String userName1 = CommonUtils.getStringValue(row.getColumnValue("userName1"));
                String userName2 = CommonUtils.getStringValue(row.getColumnValue("userName2"));
                String userName3 = CommonUtils.getStringValue(row.getColumnValue("userName3"));
                String userName4 = CommonUtils.getStringValue(row.getColumnValue("userName4"));
                if (userName1.length() > 7) {
                    row.setColumnValue("userName1", userName1.substring(0, 5) + "..");
                }
                if (userName2.length() > 7) {
                    row.setColumnValue("userName2", userName2.substring(0, 5) + "..");
                }
                if (userName3.length() > 7) {
                    row.setColumnValue("userName3", userName3.substring(0, 5) + "..");
                }
                if (userName4.length() > 7) {
                    row.setColumnValue("userName4", userName4.substring(0, 5) + "..");
                }
            }
            DataTable winnerDetailNoRead = UPyjUserRecordDao.getWinnerDetailNoRead(m, clubId, userId, DateUtils.String2Date(startDate, "yyyy-MM-dd HH:mm"), DateUtils.String2Date(endDate, "yyyy-MM-dd HH:mm"));
            if (winnerDetailNoRead.rows.length > 0) {
                mv.addObject("noReadNum", winnerDetailNoRead.rows[0].getColumnValue("nums"));
            }
            mv.addObject("userId", userId);
            mv.addObject("winDetail", winnerDetail.rows);
        }
        return mv;
    }

    /**
     * 房主报表 对局明细
     */
    @RequestMapping("gameRecord_query_hostDetail")
    public ModelAndView gameRecordQueryHostDetail(
            @RequestParam(value = "userId", defaultValue = "0") long userId,
            @RequestParam(value = "startDate", defaultValue = "") String startDate,
            @RequestParam(value = "endDate", defaultValue = "") String endDate) {
        Session session = SecurityUtils.getSubject().getSession();
        DictGameDbModel m = DictGameDbDao.getByGameId(CommonUtils.getIntValue(session.getAttribute("gameId")), true);
        long clubId = CommonUtils.getLongValue(session.getAttribute("clubId"));
        DataTable hostDetail = UPyjUserRecordDao.getHostDetail(m, clubId, userId, DateUtils.String2Date(startDate, "yyyy-MM-dd HH:mm"), DateUtils.String2Date(endDate, "yyyy-MM-dd HH:mm"));

        ModelAndView mv = new ModelAndView(PATH + "dialogHost");
        if (hostDetail.rows.length > 0) {
            for (DataRow row : hostDetail.rows) {
                if (CommonUtils.getIntValue(row.getColumnValue("winnerNum")) > 1) {
                    long winnerId = CommonUtils.getLongValue(row.getColumnValue("winnerId"));
                    long userId1 = CommonUtils.getLongValue(row.getColumnValue("userId1"));
                    long userId2 = CommonUtils.getLongValue(row.getColumnValue("userId2"));
                    long userId3 = CommonUtils.getLongValue(row.getColumnValue("userId3"));
                    //若有多个大赢家 把winnerId改为大赢家分数 用于 jsp匹配
                    if (winnerId == userId1) {
                        row.setColumnValue("winnerId", CommonUtils.getStringValue(row.getColumnValue("userCashDiff1")));
                    } else if (winnerId == userId2) {
                        row.setColumnValue("winnerId", CommonUtils.getStringValue(row.getColumnValue("userCashDiff2")));
                    } else if (winnerId == userId3) {
                        row.setColumnValue("winnerId", CommonUtils.getStringValue(row.getColumnValue("userCashDiff3")));
                    } else {
                        row.setColumnValue("winnerId", CommonUtils.getStringValue(row.getColumnValue("userCashDiff4")));
                    }
                }
                String userName1 = CommonUtils.getStringValue(row.getColumnValue("userName1"));
                String userName2 = CommonUtils.getStringValue(row.getColumnValue("userName2"));
                String userName3 = CommonUtils.getStringValue(row.getColumnValue("userName3"));
                String userName4 = CommonUtils.getStringValue(row.getColumnValue("userName4"));
                if (userName1.length() > 7) {
                    row.setColumnValue("userName1", userName1.substring(0, 5) + "..");
                }
                if (userName2.length() > 7) {
                    row.setColumnValue("userName2", userName2.substring(0, 5) + "..");
                }
                if (userName3.length() > 7) {
                    row.setColumnValue("userName3", userName3.substring(0, 5) + "..");
                }
                if (userName4.length() > 7) {
                    row.setColumnValue("userName4", userName4.substring(0, 5) + "..");
                }
            }
            DataTable hostDetailNoRead = UPyjUserRecordDao.getHostDetailNoRead(m, clubId, userId, DateUtils.String2Date(startDate, "yyyy-MM-dd HH:mm"), DateUtils.String2Date(endDate, "yyyy-MM-dd HH:mm"));
            if (hostDetailNoRead.rows.length > 0) {
                mv.addObject("noReadNum", hostDetailNoRead.rows[0].getColumnValue("nums"));
            }
            mv.addObject("userId", userId);
            mv.addObject("hostDetail", hostDetail.rows);
        }
        return mv;
    }


    /**
     * 战绩查询 :: 参与局数
     */
    @RequestMapping("gameRecord_playDetail")
    public ModelAndView gameRecord_playDetail(@RequestParam(value = "startDate", defaultValue = "") String startDate,
                                              @RequestParam(value = "endDate", defaultValue = "") String endDate) {
        Subject subject = SecurityUtils.getSubject();
        ModelAndView mv = new ModelAndView(PATH + "win_gameRecord_load");
        mv.addObject("isPlayDetail", true);

        long promoterId = CommonUtils.getLongValue(subject.getSession().getAttribute("id"));
        long clubId = ClubDao.getByPromoterId(promoterId).getId();
        DictGameDbModel m = DictGameDbDao.getByGameId(CommonUtils.getIntValue(SecurityUtils.getSubject().getSession().getAttribute("gameId")), true);
        DataTable playDetail = UPyjUserRecordDao.getPlayDetail(m, clubId, DateUtils.String2Date(startDate, "yyyy-MM-dd HH:mm"), DateUtils.String2Date(endDate, "yyyy-MM-dd HH:mm"));
        if (playDetail.rows.length > 0) {
            for (int i = 0; i < playDetail.rows.length; i++) {
                playDetail.rows[i].setColumnValue("no", CommonUtils.getStringValue(i + 1));
                if (Strings.isNullOrEmpty(playDetail.rows[i].getColumnValue("price"))) {
                    playDetail.rows[i].setColumnValue("price", "0");
                }
                String nickName = CommonUtils.getStringValue(playDetail.rows[i].getColumnValue("nickName"));
                if (nickName.length() > 7) {
                    playDetail.rows[i].setColumnValue("nickName", nickName.substring(0, 5) + "..");
                }
            }
            mv.addObject("playDetail", playDetail.rows);
        }
        return mv;
    }

    /**
     * 参与局数 对局明细
     */
    @RequestMapping("gameRecord_query_playDetail")
    public ModelAndView gameRecord_query_playDetail(
            @RequestParam(value = "userId", defaultValue = "0") long userId,
            @RequestParam(value = "startDate", defaultValue = "") String startDate,
            @RequestParam(value = "endDate", defaultValue = "") String endDate) {
        Session session = SecurityUtils.getSubject().getSession();
        DictGameDbModel m = DictGameDbDao.getByGameId(CommonUtils.getIntValue(session.getAttribute("gameId")), true);
        long clubId = CommonUtils.getLongValue(session.getAttribute("clubId"));
        DataTable playDetail = UPyjUserRecordDao.getPlayDetail(m, clubId, userId, DateUtils.String2Date(startDate, "yyyy-MM-dd HH:mm"), DateUtils.String2Date(endDate, "yyyy-MM-dd HH:mm"));

        ModelAndView mv = new ModelAndView(PATH + "dialogPlayDetail");
        if (playDetail.rows.length > 0) {
            for (DataRow row : playDetail.rows) {
                if (CommonUtils.getIntValue(row.getColumnValue("winnerNum")) > 1) {
                    long winnerId = CommonUtils.getLongValue(row.getColumnValue("winnerId"));
                    long userId1 = CommonUtils.getLongValue(row.getColumnValue("userId1"));
                    long userId2 = CommonUtils.getLongValue(row.getColumnValue("userId2"));
                    long userId3 = CommonUtils.getLongValue(row.getColumnValue("userId3"));
                    //若有多个大赢家 把winnerId改为大赢家分数 用于 jsp匹配
                    if (winnerId == userId1) {
                        row.setColumnValue("winnerId", CommonUtils.getStringValue(row.getColumnValue("userCashDiff1")));
                    } else if (winnerId == userId2) {
                        row.setColumnValue("winnerId", CommonUtils.getStringValue(row.getColumnValue("userCashDiff2")));
                    } else if (winnerId == userId3) {
                        row.setColumnValue("winnerId", CommonUtils.getStringValue(row.getColumnValue("userCashDiff3")));
                    } else {
                        row.setColumnValue("winnerId", CommonUtils.getStringValue(row.getColumnValue("userCashDiff4")));
                    }
                }
                String userName1 = CommonUtils.getStringValue(row.getColumnValue("userName1"));
                String userName2 = CommonUtils.getStringValue(row.getColumnValue("userName2"));
                String userName3 = CommonUtils.getStringValue(row.getColumnValue("userName3"));
                String userName4 = CommonUtils.getStringValue(row.getColumnValue("userName4"));
                if (userName1.length() > 7) {
                    row.setColumnValue("userName1", userName1.substring(0, 5) + "..");
                }
                if (userName2.length() > 7) {
                    row.setColumnValue("userName2", userName2.substring(0, 5) + "..");
                }
                if (userName3.length() > 7) {
                    row.setColumnValue("userName3", userName3.substring(0, 5) + "..");
                }
                if (userName4.length() > 7) {
                    row.setColumnValue("userName4", userName4.substring(0, 5) + "..");
                }
            }

            DataTable playDetailNoRead = UPyjUserRecordDao.getPlayDetailNoRead(m, clubId, userId, DateUtils.String2Date(startDate, "yyyy-MM-dd HH:mm"), DateUtils.String2Date(endDate, "yyyy-MM-dd HH:mm"));
            if (playDetailNoRead.rows.length > 0) {
                mv.addObject("noReadNum", playDetailNoRead.rows[0].getColumnValue("nums"));
            }
            mv.addObject("userId", userId);
            mv.addObject("playDetail", playDetail.rows);
        }
        return mv;
    }

    /**
     * 全部更新为 已阅 状态(参与局数)
     */
    @RequestMapping("gameRecord_detail_updateAllStatus")
    public void updateAllStatus(HttpServletResponse response,
                                @RequestParam(value = "userId", defaultValue = "0") long userId,
                                @RequestParam(value = "startDate", defaultValue = "") String startDate,
                                @RequestParam(value = "endDate", defaultValue = "") String endDate) {
        Subject subject = SecurityUtils.getSubject();
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
        long clubId = CommonUtils.getLongValue(subject.getSession().getAttribute("clubId"));

        DictGameDbModel dictDb = DictGameDbDao.getByGameId(gameId, true);
        int i = UPyjUserRecordDao.updateAllReadStatus(dictDb, clubId, userId, DateUtils.String2Date(startDate, "yyyy-MM-dd HH:mm"), DateUtils.String2Date(endDate, "yyyy-MM-dd HH:mm"));

        RequestUtils.write(response, String.valueOf(i));

    }

    /**
     * 全部更新为 已阅 状态(大赢家表)
     */
    @RequestMapping("gameRecord_winner_updateAllStatus")
    public void gameRecord_winner_updateAllStatus(HttpServletResponse response,
                                                  @RequestParam(value = "userId", defaultValue = "0") long userId,
                                                  @RequestParam(value = "startDate", defaultValue = "") String startDate,
                                                  @RequestParam(value = "endDate", defaultValue = "") String endDate) {
        Subject subject = SecurityUtils.getSubject();
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
        long clubId = CommonUtils.getLongValue(subject.getSession().getAttribute("clubId"));

        DictGameDbModel dictDb = DictGameDbDao.getByGameId(gameId, true);
        int i = UPyjUserRecordDao.updateAllWinnerReadStatus(dictDb, clubId, userId, DateUtils.String2Date(startDate, "yyyy-MM-dd HH:mm"), DateUtils.String2Date(endDate, "yyyy-MM-dd HH:mm"));

        RequestUtils.write(response, String.valueOf(i));

    }


    /**
     * 全部更新为 已阅 状态(房主报表)
     */
    @RequestMapping("gameRecord_owner_updateAllStatus")
    public void gameRecord_owner_updateAllStatus(HttpServletResponse response,
                                                 @RequestParam(value = "userId", defaultValue = "0") long userId,
                                                 @RequestParam(value = "startDate", defaultValue = "") String startDate,
                                                 @RequestParam(value = "endDate", defaultValue = "") String endDate) {
        Subject subject = SecurityUtils.getSubject();
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
        long clubId = CommonUtils.getLongValue(subject.getSession().getAttribute("clubId"));

        DictGameDbModel dictDb = DictGameDbDao.getByGameId(gameId, true);
        int i = UPyjUserRecordDao.updateAllHostReadStatus(dictDb, clubId, userId, DateUtils.String2Date(startDate, "yyyy-MM-dd HH:mm"), DateUtils.String2Date(endDate, "yyyy-MM-dd HH:mm"));

        RequestUtils.write(response, String.valueOf(i));

    }

}
