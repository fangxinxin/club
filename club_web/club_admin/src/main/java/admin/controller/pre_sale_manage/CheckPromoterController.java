package admin.controller.pre_sale_manage;

import admin.service.CheckPromoterService;
import admin.util.OutExcelUtil;
import admin.vo.ExcelVO;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import dsqp.common_const.club.ClubStatus;
import dsqp.common_const.club.CommonConfig;
import dsqp.common_const.club.LogClubType;
import dsqp.common_const.club.PromoterLevel;
import dsqp.common_const.club_admin.Permission;
import dsqp.db.model.DataRow;
import dsqp.db.model.DataTable;
import dsqp.db_club.dao.ClubDao;
import dsqp.db_club.dao.ClubUserDao;
import dsqp.db_club.dao.PromoterDao;
import dsqp.db_club.model.ClubModel;
import dsqp.db_club.model.PromoterModel;
import dsqp.db_club_dict.dao.DictFormalDao;
import dsqp.db_club_dict.dao.DictGameDbDao;
import dsqp.db_club_dict.model.DictFormalModel;
import dsqp.db_club_dict.model.DictGameDbModel;
import dsqp.db_club_log.dao.LogClubDao;
import dsqp.db_club_log.dao.LogClubJoinDao;
import dsqp.db_club_log.dao.LogLoginDao;
import dsqp.util.CommonUtils;
import dsqp.util.DateUtils;
import dsqp.util.FileUtil;
import dsqp.util.RequestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ds on 2017/8/1.
 */
@Controller("checkPromoter")
@RequestMapping("pre_sale_manage")
public class CheckPromoterController {
    private final static String PATH = "pre_sale_manage/";

    @Resource
    private CheckPromoterService checkPromoterService;


    @RequestMapping("checkPromoter")
    public ModelAndView checkPromoter() {
        ModelAndView mv = new ModelAndView(PATH + "checkPromoter");
        return mv;
    }

    /**
     * 查询待转正代理
     *  条件：
     *          俱乐部状态为：0
     * @param promoterId
     * @return
     */
    @RequiresPermissions(Permission.SYS_QUERY_PROMOTER_SHOW)
    @RequestMapping("ajax/queryCheckPromoter")
    public ModelAndView ajax_checkPromoter(
            @RequestParam(value = "promoterId", defaultValue = "0") long promoterId) {
        ModelAndView mv = new ModelAndView(PATH + "ajax/checkPromoter");
        Subject subject = SecurityUtils.getSubject();
        int gameId = (int) subject.getSession().getAttribute("gameId");
        DictGameDbModel dictDb = DictGameDbDao.getByGameId(gameId, true);

        if (promoterId == 0) {
            return mv;
        }

        PromoterModel promoter = PromoterDao.getOne(promoterId);
        if (promoter == null || promoter.getIsEnable() == false) {
            return mv;
        }

        ClubModel club = ClubDao.getPreClub(promoterId, ClubStatus.Init.getClubStatus());
        if (club == null) {
            return mv;
        }
        mv.addObject("promoter", promoter);
        mv.addObject("club", club);

        //获取新成员人数
        List<Long> listUserId = ClubUserDao.listUserId(club.getId());
        int newMemberNum = listUserId.size() > 0 ? LogClubJoinDao.getNewMemberNums(gameId, listUserId) : 0;//获取新成员人数
        mv.addObject("newMemberNum", newMemberNum);

        //查询成员参与总局数
        DataTable clubUserInfoDt = ClubUserDao.getClubUserInfo(promoterId);
        int totalPlayGameNum = checkPromoterService.getTotalPlayGameNum(clubUserInfoDt, dictDb);
        mv.addObject("totalPlayGameNum", totalPlayGameNum);

        //新成员参与局数
        DataTable newClueUser = LogClubJoinDao.getNewMember(gameId, listUserId);
        int totalNewPlayGameNum = checkPromoterService.getTotalPlayGameNum(newClueUser, dictDb);
        mv.addObject("totalNewPlayGameNum", totalNewPlayGameNum);

        //最后登陆时间
        DataTable logDt = LogLoginDao.queryByPromoterId(promoter.getId());
        if (logDt.rows.length > 0) {
            mv.addObject("loginTime", logDt.rows[0].getColumnValue("createTime"));
        }

        //审核条件
        DictFormalModel f = DictFormalDao.getByGameId(promoter.getGameId());
        int newPeopleNum = f != null ? f.getNewPeopleNum() : CommonConfig.NEW_PEOPLE_NUM;
        int peopleNum = f != null ? f.getPeopleNum() : CommonConfig.PEOPLE_NUM;
        Double expireDay = f != null ? f.getExpireDay() : CommonConfig.EXPIRE_DAY;
        int pyjRoomNum = f != null ? f.getPyjRoomNum() : CommonConfig.PYJ_ROOM_NUM;
        int nonPyjRoomNum = f != null ? f.getNonPyjRoomNum() : CommonConfig.NON_PYJ_ROOM_NUM;
        mv.addObject("newPeopleNum", newPeopleNum);     //新玩家人数
        mv.addObject("peopleNum", peopleNum);     //俱乐部人数
        mv.addObject("expireDay", expireDay);     //过期时间
        mv.addObject("pyjRoomNum", pyjRoomNum);     //代开房局数
        mv.addObject("nonPyjRoomNum", nonPyjRoomNum);     //非代开房局数

        return mv;
    }

    /**
     * 延长审核时间
     * @param response
     * @param promoterId
     * @param addTime
     */
    @RequiresPermissions(Permission.OPERATE_CHECK_PROMOTER_TIME_ADD)
    @RequestMapping("ajax/addCheckPromoterTime")
    @ResponseBody
    public void ajax_addCheckPromoterTime(HttpServletResponse response
            , @RequestParam(value = "promoterId", defaultValue = "0") long promoterId
            , @RequestParam(value = "addTime", defaultValue = "0") int addTime) {
        Subject subject = SecurityUtils.getSubject();
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
        if (promoterId != 0 && addTime != 0) {
            ClubModel c = ClubDao.getPreClub(promoterId, ClubStatus.Init.getClubStatus());
            Date expireTime = DateUtils.addDate("hour", addTime, c.getExpireTime());

            int result = checkPromoterService.updateExpireTime(promoterId, expireTime,addTime);
/*            if (result > 0) {
                Map<String, String> maps = Maps.newHashMap();
                maps.put("act", "newusermails");
                maps.put("userIds", String.valueOf(c.getGameUserId()));
                maps.put("title", "");
                maps.put("content", "");
                maps.put("reward", "");
                maps.put("startTime", "");
                maps.put("endTime", "");

                DictGameDbModel m = DictGameDbDao.getByGameId(gameId, true);
                String url = "http://" + m.getWebDomain() + ":" + m.getWebPort() + "/api/mail/?";       //推送通知 ::延长审核时间
                try {
                    HttpUtils.get(url + SignUtil.getParamsStrFromMap(maps));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/
            RequestUtils.write(response, String.valueOf(result));
        }
    }


    /**
     * 成员明细
     * @param clubId
     * @return
     */
    @RequiresPermissions(Permission.OPERATE_REFUSE_CHECK_PROMOTER_REFUSE)
    @RequestMapping("ajax/memberDetail")
    public ModelAndView ajax_memberDetail(
            @RequestParam(value = "clubId", defaultValue = "0") long clubId,
            @RequestParam(value = "memberType", defaultValue = "0") int memberType) {
        ModelAndView mv = new ModelAndView(PATH+"ajax/memberDetail");

        DataTable clubUserDt = checkPromoterService.listNewMember(clubId, memberType);

        mv.addObject("clubId", clubId);
        mv.addObject("memberType", memberType);
        mv.addObject("memberList", clubUserDt.rows);

        return mv;
    }

    /**
     * 待转正列表
     * @return
     */
    @RequiresPermissions(Permission.SYS_CHECK_PROMOTERS_SHOW)
    @RequestMapping("ajax/listCheckPromoter")
    public ModelAndView ajax_listCheckPromoter(
              @RequestParam(value = "startDate", defaultValue = "") String _startDate
            , @RequestParam(value = "endDate", defaultValue = "") String _endDate) {
        ModelAndView mv = new ModelAndView(PATH+"ajax/checkPromoter");
        Subject subject = SecurityUtils.getSubject();

        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
        Date now = new Date();
        Date startDate = null;
        Date endDate = null;
        if(Strings.isNullOrEmpty(_startDate) && Strings.isNullOrEmpty(_endDate)){
            startDate =  dsqp.util.DateUtils.addDate("day",-2,now);
            endDate = dsqp.util.DateUtils.addDate("day",2,now);
        }else{
            startDate = dsqp.util.DateUtils.String2Date(_startDate);
            endDate = dsqp.util.DateUtils.String2Date(_endDate);
        }

        mv.addObject("startDate", dsqp.util.DateUtils.Date2String(startDate));
        mv.addObject("endDate", dsqp.util.DateUtils.Date2String(endDate));
        DataTable dt = checkPromoterService.listCheckPromoter(gameId,startDate,endDate);
        if (dt.rows.length > 0) {
            mv.addObject("checkPromoterList", dt.rows);
        }

        mv.addObject("flag", 1);

        return mv;
    }



    /**
     * 立即转正
     * @param promoterId
     * @param clubId
     * @return
     */
    @RequiresPermissions(Permission.OPERATE_CHECK_PROMOTER_BECOME_FORMAL)
    @RequestMapping("ajax/becomeFormalView")
    public ModelAndView ajax_becomeFormal(
            @RequestParam(value = "promoterId", defaultValue = "0") long promoterId,
            @RequestParam(value = "clubId", defaultValue = "0") long clubId) {
        ModelAndView mv = new ModelAndView(PATH+"ajax/formalCheckPromoter");

        ClubModel clubModel = ClubDao.getOne(clubId);
        PromoterModel promoterModel = PromoterDao.getOne(promoterId);

        String level = "-";
        if (promoterModel != null) {
            level = PromoterLevel.getLevelName(promoterModel.getpLevel());
        }

        mv.addObject("club", clubModel);
        mv.addObject("promoter", promoterModel);
        mv.addObject("level", level);

        return mv;
    }
    @RequiresPermissions(Permission.OPERATE_CHECK_PROMOTER_BECOME_FORMAL)
    @RequestMapping("ajax/becomeFormal")
    public void ajax_becomeFormal(HttpServletResponse response,
                                         @RequestParam(value = "promoterId", defaultValue = "0") long promoterId) {

        boolean result = checkPromoterService.becomeFormal(promoterId);
        RequestUtils.write(response, String.valueOf(result));
    }


    /**
     * 取消审核
     * @param promoterId
     * @param clubId
     * @return
     */
    @RequiresPermissions(Permission.OPERATE_REFUSE_CHECK_PROMOTER_REFUSE)
    @RequestMapping("ajax/refuseCheckPromoter")
    public ModelAndView ajax_refuseCheckPromoter(
            @RequestParam(value = "promoterId", defaultValue = "0") long promoterId,
            @RequestParam(value = "clubId", defaultValue = "0") long clubId) {
        ModelAndView mv = new ModelAndView(PATH+"ajax/refuseCheckPromoter");

        ClubModel clubModel = ClubDao.getOne(clubId);
        PromoterModel promoterModel = PromoterDao.getOne(promoterId);

        String level = "-";
        if (promoterModel != null) {
            level = PromoterLevel.getLevelName(promoterModel.getpLevel());
        }

        mv.addObject("clubId", clubId);
        mv.addObject("club", clubModel);
        mv.addObject("promoter", promoterModel);
        mv.addObject("level", level);

        return mv;
    }
    @RequiresPermissions(Permission.OPERATE_REFUSE_CHECK_PROMOTER_REFUSE)
    @RequestMapping("ajax/refuseCheckClub")
    public void ajax_refuseCheckClub(HttpServletResponse response,
                                         @RequestParam(value = "clubId", defaultValue = "0") long clubId) {
        Subject subject = SecurityUtils.getSubject();
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));

        boolean result = checkPromoterService.refuseCheckPromoter(gameId, clubId);
        RequestUtils.write(response, String.valueOf(result));
    }


    /**
     * 转正失败列表
     * @return
     */
    @RequiresPermissions(Permission.SYS_CHECK_PROMOTERS_SHOW)
    @RequestMapping("ajax/listFail")
    public ModelAndView ajax_listFail() {
        ModelAndView mv = new ModelAndView(PATH+"ajax/checkPromoter");
        Subject subject = SecurityUtils.getSubject();

        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));

        DataTable dt = LogClubDao.listFail(gameId);
        if (dt.rows.length > 0) {
            mv.addObject("FailList", dt.rows);
        }

        return mv;
    }

    /**
     * 导出Excel
     *
     * @param response
     */
    @RequiresPermissions(Permission.OPERATE_CHECK_PROMOTER_DOWNLOAD_EXCEL)
    @RequestMapping(value = "checkPromoterDownload")
    @ResponseBody
    public void checkPromoterDownload(HttpServletResponse response,
                                   @RequestParam(value = "tabId", defaultValue = "0") int tabId
                                 , @RequestParam(value = "startDate", defaultValue = "") String _startDate
                                 , @RequestParam(value = "endDate", defaultValue = "") String _endDate) {
        Subject subject = SecurityUtils.getSubject();
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
        Date now = new Date();
        Date startDate = null;
        Date endDate = null;
        if(Strings.isNullOrEmpty(_startDate) && Strings.isNullOrEmpty(_endDate)){
            startDate =  dsqp.util.DateUtils.addDate("day",-2,now);
            endDate = dsqp.util.DateUtils.addDate("day",2,now);
        }else{
            startDate = dsqp.util.DateUtils.String2Date(_startDate);
            endDate = dsqp.util.DateUtils.String2Date(_endDate);
        }        
        if (tabId == 1) {
            DataTable dt = checkPromoterService.listCheckPromoter(gameId,startDate,endDate);
            Map<String, String> rowNames = Maps.newLinkedHashMap();
            rowNames.put("gameUserId", "玩家ID");
            rowNames.put("gameNickName", "玩家昵称");
            rowNames.put("id", "俱乐部ID");
            rowNames.put("promoterId", "代理ID");
            rowNames.put("peopleNum", "人数");
            rowNames.put("newMemberNum", "新成员");
            rowNames.put("totalPlayGameNum", "参与局数");
            rowNames.put("totalNewPlayGameNum", "新成员局数");
            rowNames.put("createTime", "创建时间");
            rowNames.put("expireTime", "转正截止时间");
            rowNames.put("restTime", "转正剩余时间");
            ExcelVO vo = OutExcelUtil.download("待转正列表", rowNames, dt);

            FileUtil.FileDownload(vo.getFileName(), vo.getContent(), response);
        }

        else if (tabId == 2) {
            Map<String, String> rowNames = Maps.newLinkedHashMap();
            rowNames.put("gameUserId", "玩家ID");
            rowNames.put("gameNickName", "玩家昵称");
            rowNames.put("id", "俱乐部ID");
            rowNames.put("peopleNum", "人数");
            rowNames.put("peopleNumNew", "新成员");
            rowNames.put("pyjNum", "参与局数");
            rowNames.put("pyjNumNew", "新成员局数");
            rowNames.put("createTime", "创建时间");
            rowNames.put("expireTime", "转正截止时间");
            rowNames.put("clubType", "申请状态");

            StringBuilder sb = new StringBuilder(128);
            String thead = OutExcelUtil.getThead(rowNames);
            sb.append(thead);

            DataTable dt = LogClubDao.listFail(gameId);
            if (dt.rows.length > 0) {
                for (DataRow row: dt.rows) {
                    for (String key: rowNames.keySet()) {
                        String value = row.getColumnValue(key);
                        if (key.equals("clubType")) {
                            value = LogClubType.getRemark(CommonUtils.getIntValue(value));
                        }
                        sb.append("\t").append(value).append(",");
                    }
                    sb.append("\r\n");
                }
            }

            FileUtil.FileDownload(
                    OutExcelUtil.getFileName("转正失败列表").toString(),
                    sb.toString(),
                    response
            );
        }

    }

}
