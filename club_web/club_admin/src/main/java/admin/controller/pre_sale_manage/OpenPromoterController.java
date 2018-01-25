package admin.controller.pre_sale_manage;

import admin.service.OpenPromoterService;
import admin.util.PropertyUtil;
import admin.vo.NewAgentModel;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import db_admin.model.SysUserModel;
import dsqp.common_const.club.*;
import dsqp.common_const.club_admin.Permission;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db_club.dao.*;
import dsqp.db_club.model.ClubModel;
import dsqp.db_club.model.ClubUserModel;
import dsqp.db_club.model.PromoterModel;
import dsqp.db_club.model.PromoterOrderModel;
import dsqp.db_club_dict.dao.DictFormalDao;
import dsqp.db_club_dict.dao.DictGameDao;
import dsqp.db_club_dict.dao.DictGameDbDao;
import dsqp.db_club_dict.dao.DictMpDao;
import dsqp.db_club_dict.model.DictFormalModel;
import dsqp.db_club_dict.model.DictGameDbModel;
import dsqp.db_club_dict.model.DictMpModel;
import dsqp.db_club_log.dao.LogClubDao;
import dsqp.db_club_log.model.LogClubModel;
import dsqp.db_game.dao.dev.UUserPointDao;
import dsqp.util.*;
import org.apache.commons.codec.digest.DigestUtils;
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
import java.util.Map;

/**
 * Created by fx on 2017/7/29.
 */

@Controller
@RequestMapping("pre_sale_manage")
public class OpenPromoterController {

    @Resource
    private OpenPromoterService openPromoterService;

    @RequestMapping("open")
    public String open() {
        return "pre_sale_manage/open";
    }

    /**
     * 查询玩家信息
     *
     * @param gameUserId
     * @param model
     * @return
     */
    @RequiresPermissions(Permission.SYS_QUERY_PLAYER_SHOW)
    @RequestMapping("queryByUserId")
    public String queryByUserId(
            @RequestParam(value = "gameUserId", defaultValue = "0") long gameUserId
            , Model model) {

        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        DataTable dtPro = PromoterDao.queryByGameIdAndUserId(gameId, gameUserId, true);                       //查看是否已经是代理商

        if (dtPro.rows.length > 0) {
            model.addAttribute("dtPro", dtPro.rows.length);
            return "pre_sale_manage/open_promoter";
        }

        DictGameDbModel dictDb = DictGameDbDao.getByGameId(gameId, true);
        DataTable dt = dsqp.db_game.dao.dev.UUserInfoDao.getByUserId(dictDb, gameUserId);                    //游戏表中查询数据.

        if (dt.rows.length == 0) {
            model.addAttribute("dtLength", "0");
            return "pre_sale_manage/open_promoter";
        } else {
            DataTable pointByUserId = UUserPointDao.getPointByUserId(dictDb, gameUserId);
            model.addAttribute("diamond", pointByUserId.rows.length > 0 ? pointByUserId.rows[0].getColumnValue("privateRoomDiamond") : "0");        //钻石数
            model.addAttribute("gameName", CommonUtils.getStringValue(session.getAttribute("gameName")));
            model.addAttribute("gameUserId", gameUserId);
            model.addAttribute("gameNickName", dt.rows[0].getColumnValue("nickName").replace("\'", "").replace("\"", "").replace("”", "").replace("“", "").replace("‘", "").replace("’", ""));

            DataTable clubUserDt = ClubUserDao.getDtByGameIdAndGameUserId(gameId, gameUserId);                  //查看是否加入俱乐部
            if (clubUserDt.rows.length > 0) {
                {
                    DataTable clubList = PromoterSellDao.getDetailByUserIdAndGameId(gameId, gameUserId);
                    model.addAttribute("clubList", clubList.rows);
                }
            }
            return "pre_sale_manage/open_promoter";
        }
    }

    /**
     * 开通代理
     */
    @RequiresPermissions(Permission.OPERATE_CREATE_PROMOTER_ADD)
    @RequestMapping("createPromoter")
    public void createPromoter(
            @RequestParam(value = "pLevel", defaultValue = "") int pLevel
            , @RequestParam(value = "realName", defaultValue = "") String realName
            , @RequestParam(value = "cellPhone", defaultValue = "0") long cellPhone
            , @RequestParam(value = "parentId", defaultValue = "0") long parentId
            , @RequestParam(value = "gameNickName", defaultValue = "0") String nickName
            , @RequestParam(value = "gameUserId", defaultValue = "0") long gameUserId
            , @RequestParam(value = "clubId", defaultValue = "0") long clubId
            , @RequestParam(value = "type", defaultValue = "0") int type                                 //1-正式代理  2-需转正代理
            , @RequestParam(value = "staffWechat", defaultValue = "") String staffWechat
            , @RequestParam(value = "remark", defaultValue = "") String remarkInfo
            , HttpServletResponse response) throws IOException {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();

        SysUserModel admin = (SysUserModel) subject.getPrincipal();

        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        String editAdmin = admin.getRealName();
        long editAdminId = admin.getId();
        DataTable dt = PromoterDao.queryByCellPhone(cellPhone);
        DictMpModel dictMp = DictMpDao.getByGameId(gameId);                                       //查看微信分享相关配置
        if (dictMp == null) {
            RequestUtils.write(response, "NO");
            return;
        }
        DataTable dtPromoter = PromoterDao.queryByGameIdAndUserId(gameId, gameUserId, true);     //查看该userId是否已经是推广员
        if (dtPromoter.rows.length > 0) {
            RequestUtils.write(response, "NO");
            return;
        }
        DataTable dtPro = PromoterDao.queryByGameIdAndCellPhone(gameId, cellPhone);              //查看该手机号是否已经是推广员
        if (dtPro.rows.length > 0) {
            RequestUtils.write(response, "REPEAT");
            return;
        }

        DictFormalModel f = DictFormalDao.getByGameId(gameId);
        double expireDay = CommonConfig.EXPIRE_DAY;//默认待转正时间为2天
        if (f != null) {
            expireDay = f.getExpireDay();       //待转正时间
        }

        String randNum = RndUtils.getValidateCode(6, true);//随机密码（初次开通代理时使用）
        String isFirstOpen;

        NewAgentModel agent = new NewAgentModel();
        agent.setGameId(gameId);
        agent.setRealName(realName);
        agent.setCellphone(cellPhone);
        agent.setParentId(parentId);
        agent.setNickName(nickName);
        agent.setpLevel(pLevel);
        agent.setGameUserId(gameUserId);
        agent.setClubId(clubId);
        agent.setClubName(nickName.length() > 14 ? nickName.substring(0, 14) + "俱乐部" : nickName + "俱乐部");
        agent.setExpireDay(expireDay);
        agent.setClassName(dictMp.getClassName());
        agent.setEditAdmin(editAdmin);
        agent.setClubStatus(type == 1 ? ClubStatus.Normal.getClubStatus() : ClubStatus.Init.getClubStatus());
        agent.setEditAdminId(editAdminId);

        if (dt.rows.length > 0) {
            agent.setLoginPassword(dt.rows[0].getColumnValue("loginPassword"));//开通过代理则用之前密码
            agent.setLoginStatus(LoginStatus.Normal.getLoginStatus());//不强制要求改密码
            isFirstOpen = ",2";
        } else {
            isFirstOpen = ",1";
            agent.setLoginStatus(LoginStatus.Init.getLoginStatus());//首次登录，强制要求改密码
            agent.setLoginPassword(DigestUtils.md5Hex(randNum));//没开通过代理则用随机密码
        }

        long newClubId = openPromoterService.newAgent(agent);

        if (newClubId != 0) {
            Date date = new Date();

            ClubUserModel model = new ClubUserModel();
            model.setGameId(gameId);
            model.setClubId(newClubId);
            model.setPromoterGameUserId(gameUserId);
            model.setPromoterId(0);
            model.setGameUserId(gameUserId);
            model.setGameNickName(nickName);
            model.setCreateTime(date);
            openPromoterService.addOne(model);                       //club_user加一条记录

            LogClubModel logClubModel = new LogClubModel();
            logClubModel.setGameId(gameId);
            logClubModel.setClubId(newClubId);
            logClubModel.setClubType(type == 1 ? LogClubType.SUCCESS.getClubType() : LogClubType.NEW.getClubType());//生成俱乐部
            logClubModel.setCreateTime(date);
            logClubModel.setCreateDate(date);
            logClubModel.setGameUserId(gameUserId);
            logClubModel.setGameNickName(nickName);
            logClubModel.setPeopleNum(0);
            logClubModel.setPeopleNumNew(0);
            logClubModel.setPyjNum(0);
            logClubModel.setPyjNumNew(0);
            logClubModel.setExpireTime(DateUtils.addDay(1, date));
            LogClubDao.add(logClubModel);                           //log_club加一条记录

            PromoterOrderModel promoterOrder = new PromoterOrderModel();

            long promoterId = ClubDao.getOne(newClubId).getPromoterId();
            promoterOrder.setGameId(gameId);
            promoterOrder.setOrderNo(CommonUtils.getStringValue(System.currentTimeMillis()) + CommonUtils.getStringValue(promoterId));
            promoterOrder.setClubId(newClubId);
            promoterOrder.setPromoterId(promoterId);
            promoterOrder.setGameNickName(nickName);
            promoterOrder.setGameUserId(gameUserId);
            promoterOrder.setPromoterStatus(type == 1 ? LogClubType.SUCCESS.getClubType() : LogClubType.NEW.getClubType());
            promoterOrder.setEditAdminId(admin.getId());
            promoterOrder.setEditAdmin(admin.getRealName());
            promoterOrder.setChangeTime(date);
            promoterOrder.setChangeDate(date);
            promoterOrder.setCreateTime(date);
            promoterOrder.setCreateDate(date);
            PromoterOrderDao.add(promoterOrder);                        //promoter_order新增一条工单记录

            PromoterDao.updateClubId(promoterId, newClubId);            //修改promoter表中clubId字段
            PromoterDao.updateStaffWechat(promoterId, staffWechat);     //修改promoter表中staffWechat字段
            if (!Strings.isNullOrEmpty(remarkInfo)) {
                PromoterDao.updateRemark(promoterId, remarkInfo);       //修改promoter表中remark字段
            }

            if (",1".equals(isFirstOpen)) {                             //发送短信通知代理商
                String remark = DictGameDao.queryRemarkByGameId(gameId);
                String apiKey = "";
                if ("66".equals(remark)) {
                    apiKey = ApiKey.LIULIUKEY;
                }
                if ("来来".equals(remark)) {
                    apiKey = ApiKey.LAILAIKEY;
                }
                if ("大圣".equals(remark)) {
                    apiKey = ApiKey.DASHENGQIPAI;
                }
                String op = SMSReqSender.sendSms(apiKey, String.format(PropertyUtil.getProperty("sms.open"), remark, remark, randNum), String.valueOf(cellPhone));
                if (!Strings.isNullOrEmpty(op)) {
                    System.out.println(op);
                }
            }

            DictGameDbModel m = DictGameDbDao.getByGameId(CommonUtils.getIntValue(subject.getSession().getAttribute("gameId")), true);
            Map<String, String> maps = Maps.newHashMap();
            maps.put("userIds", CommonUtils.getStringValue(gameUserId));
            maps.put("title", "通知");
            maps.put("content", "您已开通代理商。");
            maps.put("reward", "");
            maps.put("startTime", EncodingUtils.urlEncode(DateUtils.DateTime2String(new Date())));
            String urlTwo = "http://" + m.getWebDomain() + ":" + m.getWebPort() + "/api";
            try {
                HttpUtils.get(urlTwo + "/club/?act=create&userId=" + gameUserId + "&clubId=" + newClubId);            // 开通代理，发送邮件通知游戏端   2017/10/17
            } catch (IOException e) {
                e.printStackTrace();
            }
            RequestUtils.write(response, String.valueOf(newClubId) + isFirstOpen);
        } else {
            RequestUtils.write(response, "NO");
        }
    }

    /**
     * 根据clubId查promoterId
     */
    @RequestMapping("queryUpPromoterId")
    @ResponseBody
    public void queryUpPromoterId(HttpServletResponse response, long clubId) {

        ClubModel one = ClubDao.getOne(clubId);
        if (one != null) {
            RequestUtils.write(response, CommonUtils.getStringValue(one.getPromoterId()));
        } else {
            RequestUtils.write(response, "0");
        }
    }


    /**
     * 根据clubId,gameId查promoterId
     */
    @RequestMapping("queryUpPromoter")
    @ResponseBody
    public void queryUpPromoter(HttpServletResponse response, long clubId) {

        Session session = SecurityUtils.getSubject().getSession();

        ClubModel one = ClubDao.queryByIdAndGamId(clubId, CommonUtils.getIntValue(session.getAttribute("gameId")));
        if (one != null) {
            RequestUtils.write(response, CommonUtils.getStringValue(one.getPromoterId()));
        } else {
            RequestUtils.write(response, "0");
        }
    }

}

