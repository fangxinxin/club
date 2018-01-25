package admin.controller.pre_sale_manage;

import admin.service.ClubQueryService;
import admin.vo.MemberDetailVO;
import admin.vo.MemberDiamondsVO;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import db_admin.dao.SysUserDao;
import db_admin.model.SysUserModel;
import dsqp.common_const.club.LogClubType;
import dsqp.common_const.club_admin.Permission;
import dsqp.common_const.club_admin.WithdrawRequest;
import dsqp.db.model.DataRow;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db.util.DataTableUtils;
import dsqp.db_club.dao.*;
import dsqp.db_club.model.ClubModel;
import dsqp.db_club.model.PromoterModel;
import dsqp.db_club_log.dao.LogClubDao;
import dsqp.db_club_log.dao.LogClubJoinDao;
import dsqp.db_club_log.dao.LogPromoterReportDao;
import dsqp.db_club_log.model.LogClubModel;
import dsqp.util.CommonUtils;
import dsqp.util.DateUtils;
import dsqp.util.FileUtil;
import dsqp.util.RequestUtils;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jeremy on 2017/9/19.
 */
@Controller
@RequestMapping("pre_sale_manage/")
public class ClubQueryController {

    @Resource
    private ClubQueryService clubQueryService;

    @RequestMapping("clubQuery")
    public String index() {
        return "pre_sale_manage/club_query";
    }


    @RequiresPermissions(Permission.SYS_QUERY_CLUB_SHOW)
    @RequestMapping("queryByClub")
    public String queryByClub(Model model
            , @RequestParam(value = "clubId", defaultValue = "0") long clubId) {

        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));

        ClubModel club = ClubDao.queryByIdAndGamId(clubId, gameId);
        if (club != null) {
            PromoterModel promoter = PromoterDao.getOne(club.getPromoterId());
            if (promoter != null) {

                long editAdminId = promoter.getEditAdminId();
                SysUserModel sysUserModel = SysUserDao.getOne(editAdminId);

//                LogGamecardModel logGamecardModel = LogGamecardDao.getLateOpreTime(promoter.getId());//用于获取最后操作钻石时间

                LogClubModel logClubModel = LogClubDao.getByCludIdAndType(clubId, LogClubType.SUCCESS.getClubType());//用于获取转正时间点
                String roomNums = LogPromoterReportDao.getRoomCreateNumsByClubId(clubId);//俱乐部成员累计对局数


                List<Long> listUserId = ClubUserDao.listUserId(clubId);
                int memberNums = listUserId.size() > 0 ? LogClubJoinDao.getNewMemberNums(gameId, listUserId) : 0;//获取新成员人数


                DataTable dt = ClubDao.queryByPromoterId(promoter.getId());
                if (dt.rows.length > 0) {
                    PromoterModel upPromoter = PromoterDao.getOne(promoter.getParentId());
//                    String totalRequest = WithdrawRequestDao.queryTotalByPromoterId(promoter.getId(), WithdrawRequest.SUCCESS.getWithdrawStatus());//累计提现
                    String requestPay = "";//提成金购买钻石金额

//                    DataTable logLogin = LogLoginDao.queryByPromoterId(promoter.getId());//查询最后登录数据
//                    if (logLogin.rows.length > 0) {
//                        model.addAttribute("logLogin", logLogin.rows[0]);
//                    }
                    model.addAttribute("endDate", DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
                    model.addAttribute("startDate", DateUtil.formatDate(DateUtils.addDay(-6, new Date()), "yyyy-MM-dd"));
                    model.addAttribute("promoter", promoter);
                    model.addAttribute("adminInfoModel", sysUserModel);
                    model.addAttribute("logClubModel", logClubModel);
//                    model.addAttribute("logGamecardModel", logGamecardModel);
                    model.addAttribute("memberNums", memberNums);
                    model.addAttribute("roomNums", roomNums);
                    model.addAttribute("club", club);
                    model.addAttribute("upPromoter", upPromoter);
//                    model.addAttribute("totalRequest", totalRequest.length() > 0 ? totalRequest : "0");
                    model.addAttribute("requestPay", requestPay.length() > 0 ? requestPay : "0");
                }
            }
        } else {
            model.addAttribute("isEmpty", "俱乐部ID不存在,请重新输入");
        }
        return "pre_sale_manage/club_query";
    }


    @RequiresPermissions(Permission.SYS_QUERY_CLUB_SHOW)
    @RequestMapping("queryByPromoter")
    public String queryByPromoter(Model model
            , @RequestParam(value = "userId", defaultValue = "0") long userId
            , @RequestParam(value = "start", defaultValue = "") String startDate
            , @RequestParam(value = "end", defaultValue = "") String endDate) {

        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));

        DataTable dtPromoter = PromoterDao.queryByGameIdAndUserId(gameId, userId, true);
        PromoterModel promoter = dtPromoter.rows.length > 0 ? DBUtils.convert2Model(PromoterModel.class, dtPromoter.rows[0]) : null;
        if (promoter != null) {
            long editAdminId = promoter.getEditAdminId();
            SysUserModel sysUserModel = SysUserDao.getOne(editAdminId);

//            LogGamecardModel logGamecardModel = LogGamecardDao.getLateOpreTime(promoter.getId());//用于获取最后操作钻石时间

            DataTable dt = ClubDao.queryByPromoterId(promoter.getId());
            if (dt.rows.length > 0) {
                ClubModel club = DBUtils.convert2Model(ClubModel.class, dt.rows[0]);
                LogClubModel logClubModel = LogClubDao.getByCludIdAndType(club.getId(), LogClubType.SUCCESS.getClubType());//用于获取转正时间点
                String roomNums = LogPromoterReportDao.getRoomCreateNumsByClubId(club.getId());//俱乐部成员累计对局数

                List<Long> listUserId = ClubUserDao.listUserId(club.getId());
                int memberNums = listUserId.size() > 0 ? LogClubJoinDao.getNewMemberNums(gameId, listUserId) : 0;//获取新成员人数

                PromoterModel upPromoter = PromoterDao.getOne(promoter.getParentId());
                String totalRequest = WithdrawRequestDao.queryTotalByPromoterId(promoter.getId(), WithdrawRequest.SUCCESS.getWithdrawStatus());//累计提现（2.提现成功）
                String requestPay = "";//提成金购买钻石金额
                model.addAttribute("promoter", promoter);
                model.addAttribute("adminInfoModel", sysUserModel);
                model.addAttribute("logClubModel", logClubModel);
//                model.addAttribute("logGamecardModel", logGamecardModel);
                model.addAttribute("memberNums", memberNums);
                model.addAttribute("roomNums", roomNums);
                model.addAttribute("club", club);
                model.addAttribute("upPromoter", upPromoter);
                model.addAttribute("totalRequest", totalRequest.length() > 0 ? totalRequest : "0");
                model.addAttribute("requestPay", requestPay.length() > 0 ? requestPay : "0");

//                DataTable logLogin = LogLoginDao.queryByPromoterId(promoter.getId());//查询最后登录数据
//                if (logLogin.rows.length > 0) {
//                    model.addAttribute("logLogin", logLogin.rows[0]);
//                }

                if (Strings.isNullOrEmpty(startDate) && Strings.isNullOrEmpty(endDate)) {
                    model.addAttribute("endDate", DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
                    model.addAttribute("startDate", DateUtil.formatDate(DateUtils.addDay(-6, new Date()), "yyyy-MM-dd"));
                    return "pre_sale_manage/club_query";
                } else {
                    model.addAttribute("endDate", endDate);
                    model.addAttribute("startDate", startDate);
                    return "pre_sale_manage/club_query";
                }
            }
        } else {
            model.addAttribute("isEmpty", "该Id暂无俱乐部信息,请重新输入");
        }
        return "pre_sale_manage/club_query";
    }

    /**
     * 对局统计
     *
     * @param response
     * @param clubId
     * @param startDate
     * @param endDate
     */
    @RequiresPermissions(Permission.SYS_QUERY_ROOM_STATIS_SHOW)
    @RequestMapping("queryRoomStatisAjax")
    @ResponseBody
    public void queryRoomStatis(HttpServletResponse response
            , @RequestParam(value = "clubId", defaultValue = "") long clubId
            , @RequestParam(value = "start", defaultValue = "") String startDate
            , @RequestParam(value = "end", defaultValue = "") String endDate) {

        Date d1 = DateUtils.String2Date(startDate);
        Date d2 = DateUtils.String2Date(endDate);

        //对局统计
        DataTable dtRoomStatis = LogPromoterReportDao.getRoomStatis(clubId, d1, d2);
        StringBuilder sb = new StringBuilder();
        for (DataRow row : dtRoomStatis.rows) {
            sb.append("<tr><td align='center'>" +
                    row.getColumnValue("totalNums") + "</td><td align='center'>" +
                    row.getColumnValue("roomCreateNum") + "</td><td align='center'>" +
                    row.getColumnValue("statDate") + "</td></tr>");
        }
        RequestUtils.write(response, new Gson().toJson(sb.toString()));
    }

    //导出excel
    @RequiresPermissions(Permission.OPERATE_CLUB_QUERY_EXCEL)
    @RequestMapping("roomStatis_download")
    public void roomStatis_download(HttpServletResponse response
            , @RequestParam(value = "clubId", defaultValue = "") long clubId
            , @RequestParam(value = "start", defaultValue = "") String startDate
            , @RequestParam(value = "end", defaultValue = "") String endDate) {
        Date d1 = DateUtils.String2Date(startDate);
        Date d2 = DateUtils.String2Date(endDate);
        //对局统计
        DataTable dtRoomStatis = LogPromoterReportDao.getRoomStatis(clubId, d1, d2);
        StringBuilder sb = new StringBuilder();
        sb.append("\t\t\t成员累计对局数,")
                .append("\t成员新增对局数,")
                .append("\t时间,")
                .append("\r\n");
        //拼接cvs内容
        for (DataRow row : dtRoomStatis.rows) {
            sb.append("\t").append(row.getColumnValue("totalNums")).append(",")
                    .append(row.getColumnValue("roomCreateNum")).append(",")
                    .append(row.getColumnValue("statDate")).append(",")
                    .append("\r\n");
        }
        //拼接文件名
        StringBuilder fileName = new StringBuilder(64);
        fileName.append("对局统计")
                .append(System.currentTimeMillis());
        //返回文件流
        FileUtil.FileDownload(fileName.toString(), sb.toString(), response);

    }

    /**
     * 流水统计
     *
     * @param response
     * @param clubId
     * @param startDate
     * @param endDate
     */
    @RequiresPermissions(Permission.SYS_QUERY_DIAMOND_STATIS_SHOW)
    @RequestMapping("queryDiamondStatisAjax")
    @ResponseBody
    public void queryDiamondStatis(HttpServletResponse response
            , @RequestParam(value = "clubId", defaultValue = "") long clubId
            , @RequestParam(value = "start", defaultValue = "") String startDate
            , @RequestParam(value = "end", defaultValue = "") String endDate) {

        Date d1 = DateUtils.String2Date(startDate);
        Date d2 = DateUtils.String2Date(endDate);

        //流水统计
        DataTable dtDiamondStatis = LogPromoterReportDao.getDiamondStatis(clubId, d1, d2);
        StringBuilder sb = new StringBuilder();
        for (DataRow row : dtDiamondStatis.rows) {
            sb.append("<tr><td align='center'>" +
                    row.getColumnValue("totalNums") + "</td><td align='center'>" +
                    row.getColumnValue("gameCardConsume") + "</td><td align='center'>" +
                    row.getColumnValue("statDate") + "</td></tr>");
        }
        RequestUtils.write(response, new Gson().toJson(sb.toString()));
    }

    //导出excel
    @RequiresPermissions(Permission.OPERATE_CLUB_QUERY_EXCEL)
    @RequestMapping("diamondStatis_download")
    public void diamondStatis_download(HttpServletResponse response
            , @RequestParam(value = "clubId", defaultValue = "") long clubId
            , @RequestParam(value = "start", defaultValue = "") String startDate
            , @RequestParam(value = "end", defaultValue = "") String endDate) {

        Date d1 = DateUtils.String2Date(startDate);
        Date d2 = DateUtils.String2Date(endDate);
        //流水统计
        DataTable dtDiamondStatis = LogPromoterReportDao.getDiamondStatis(clubId, d1, d2);
        StringBuilder sb = new StringBuilder();
        sb.append("\t\t\t成员累计消耗钻石,")
                .append("\t成员新增消耗钻石,")
                .append("\t时间,")
                .append("\r\n");
        //拼接cvs内容
        for (DataRow row : dtDiamondStatis.rows) {
            sb.append("\t").append(row.getColumnValue("totalNums")).append(",")
                    .append(row.getColumnValue("gameCardConsume")).append(",")
                    .append(row.getColumnValue("statDate")).append(",")
                    .append("\r\n");
        }
        //拼接文件名
        StringBuilder fileName = new StringBuilder(64);
        fileName.append("流水统计")
                .append(System.currentTimeMillis());
        //返回文件流
        FileUtil.FileDownload(fileName.toString(), sb.toString(), response);

    }

    @RequiresPermissions(Permission.OPERATE_CLUB_QUERY_EXCEL)
    @RequestMapping("memberDetail_download")
    public void memberDetail_download(HttpServletResponse response
            , @RequestParam(value = "promoterId", defaultValue = "") long promoterId) {
        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));

        List<MemberDetailVO> memberDetailVOList = clubQueryService.getMemberDetailList(gameId, promoterId);//俱乐部成员明细
        StringBuilder sb = new StringBuilder();
        sb.append("\t\t\t成员ID,")
                .append("\t成员昵称,")
                .append("\t参与对局,")
                .append("\t消耗钻石,")
                .append("\t被发钻石,")
                .append("\t剩余钻石,")
                .append("\t账号创建时间,")
                .append("\t加入俱乐部时间,")
                .append("\r\n");
        //拼接cvs内容
        for (MemberDetailVO vo : memberDetailVOList) {
            sb.append("\t").append(vo.getUserId()).append(",")
                    .append(vo.getUserNickName()).append(",")
                    .append(vo.getPartiGameNums()).append(",")
                    .append(vo.getConsumeDiamNums()).append(",")
                    .append(vo.getGiveDiamNums()).append(",")
                    .append(vo.getRemainDiaNums()).append(",")
                    .append(DateUtils.Date2String(vo.getCreateTime(), "yyyy-MM-dd HH:mm:ss")).append(",")
                    .append(DateUtils.Date2String(vo.getJoinClubTime(), "yyyy-MM-dd HH:mm:ss")).append(",")
                    .append("\r\n");
        }
        //拼接文件名
        StringBuilder fileName = new StringBuilder(64);
        fileName.append("成员明细")
                .append(System.currentTimeMillis());
        //返回文件流
        FileUtil.FileDownload(fileName.toString(), sb.toString(), response);

    }


    /**
     * 钻石流水
     *
     * @param userId
     * @param promoterId
     * @param joinClubTime
     * @return
     */
    @RequiresPermissions(Permission.SYS_QUERY_DIAMOND_CHANGE_SHOW)
    @RequestMapping("queryDiamondChangeAjax")
    public ModelAndView queryDiamondChange(@RequestParam(value = "userId", defaultValue = "0") long userId
            , @RequestParam(value = "promoterId", defaultValue = "0") long promoterId
            , @RequestParam(value = "joinClubTime", defaultValue = "") String joinClubTime) {
        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        Date joinTime = DateUtils.String2Date(joinClubTime);
        ModelAndView mv = new ModelAndView("pre_sale_manage/ajax/table/club_query");
        List<MemberDiamondsVO> memberDiamondsList = clubQueryService.getMemberDiamondsList(gameId, promoterId, userId, joinTime);

        Date endDate = DateUtils.String2Date(DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
        Date end = DateUtils.addDay(1, endDate);
        Date startDate = DateUtils.addDay(-6, DateUtils.String2Date(DateUtil.formatDate(new Date(), "yyyy-MM-dd")));


        List<MemberDiamondsVO> memberDiamondsListByDate = new ArrayList<MemberDiamondsVO>();

        //按时间段筛选数据
        for (MemberDiamondsVO vo : memberDiamondsList) {
            if (vo.getCreateTime().getTime() >= startDate.getTime() && vo.getCreateTime().getTime() < end.getTime()) {
                memberDiamondsListByDate.add(vo);
            }
        }

        mv.addObject("memberDiamondsListByDate", memberDiamondsListByDate);
        mv.addObject("promoterId", promoterId);
        mv.addObject("userId", userId);
        mv.addObject("joinClubTime", joinClubTime);
        mv.addObject("endDate", DateUtils.Date2String(new Date(), "yyyy-MM-dd"));
        mv.addObject("startDate", DateUtils.Date2String(DateUtils.addDay(-6, new Date()), "yyyy-MM-dd"));


        return mv;
    }


    @RequestMapping("queryChangeInfoAjax")
    @ResponseBody
    public void queryChangeInfo(HttpServletResponse response
            , @RequestParam(value = "promoterId", defaultValue = "0") long promoterId
            , @RequestParam(value = "userId", defaultValue = "0") long userId
            , @RequestParam(value = "joinClubTime", defaultValue = "") String joinClubTime
            , @RequestParam(value = "startDate", defaultValue = "") String startDate
            , @RequestParam(value = "endDate", defaultValue = "") String endDate) {
        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        Date joinTime = DateUtils.String2Date(joinClubTime);
        Date start = DateUtils.String2Date(startDate);
        Date end = DateUtils.addDay(1, DateUtils.String2Date(endDate));

        List<MemberDiamondsVO> memberDiamondsList = clubQueryService.getMemberDiamondsList(gameId, promoterId, userId, joinTime);

        List<MemberDiamondsVO> memberDiamondsListByDate = new ArrayList<MemberDiamondsVO>();

        //按时间段筛选数据
        for (MemberDiamondsVO vo : memberDiamondsList) {
            if (vo.getCreateTime().getTime() >= start.getTime() && vo.getCreateTime().getTime() < end.getTime()) {
                memberDiamondsListByDate.add(vo);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (MemberDiamondsVO vo : memberDiamondsListByDate) {
            sb.append("<tr><td align='center'>" +
                    vo.getUserId() + "</td><td align='center'>" +
                    vo.getUserNickName() + "</td><td align='center'>" +
                    vo.getDiamondChangeNums() + "</td><td align='center'>" +
                    vo.getChangeReason() + "</td><td align='center'>" +
                    vo.getRemainDiamondNums() + "</td><td align='center'>" +
                    DateUtils.Date2String(vo.getCreateTime(), "yyyy-MM-dd HH:mm:ss") + "</td></tr>");
        }
        RequestUtils.write(response, new Gson().toJson(sb.toString()));

    }


    @RequestMapping("diamondChange_download")
    public void diamondChange_download(HttpServletResponse response
            , @RequestParam(value = "promoterId", defaultValue = "0") long promoterId
            , @RequestParam(value = "userId", defaultValue = "0") long userId
            , @RequestParam(value = "joinClubTime", defaultValue = "") String joinClubTime
            , @RequestParam(value = "startDate", defaultValue = "") String startDate
            , @RequestParam(value = "endDate", defaultValue = "") String endDate) {

        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        Date joinTime = DateUtils.String2Date(joinClubTime);
        Date start = DateUtils.String2Date(startDate);
        Date end = DateUtils.addDay(1, DateUtils.String2Date(endDate));

        List<MemberDiamondsVO> memberDiamondsList = clubQueryService.getMemberDiamondsList(gameId, promoterId, userId, joinTime);

        List<MemberDiamondsVO> memberDiamondsListByDate = new ArrayList<MemberDiamondsVO>();

        //按时间段筛选数据
        for (MemberDiamondsVO vo : memberDiamondsList) {
            if (vo.getCreateTime().getTime() >= start.getTime() && vo.getCreateTime().getTime() < end.getTime()) {
                memberDiamondsListByDate.add(vo);
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\t\t\t成员ID,")
                .append("\t成员昵称,")
                .append("\t钻石变动,")
                .append("\t变动缘由,")
                .append("\t剩余钻石,")
                .append("\t时间,")
                .append("\r\n");
        //拼接cvs内容
        for (MemberDiamondsVO vo : memberDiamondsList) {
            sb.append("\t").append(vo.getUserId()).append(",")
                    .append(vo.getUserNickName()).append(",")
                    .append(vo.getDiamondChangeNums()).append(",")
                    .append(vo.getChangeReason()).append(",")
                    .append(vo.getRemainDiamondNums()).append(",")
                    .append(DateUtils.Date2String(vo.getCreateTime(), "yyyy-MM-dd HH:mm:ss")).append(",")
                    .append("\r\n");
        }
        //拼接文件名
        StringBuilder fileName = new StringBuilder(64);
        fileName.append("成员钻石流水")
                .append(System.currentTimeMillis());
        //返回文件流
        FileUtil.FileDownload(fileName.toString(), sb.toString(), response);

    }


    //对局统计
    @RequestMapping("query_game_count")
    public ModelAndView query_game_count(HttpServletResponse response
            , @RequestParam(value = "clubId", defaultValue = "0") long clubId) {
        ModelAndView mv = new ModelAndView("pre_sale_manage/ajax/table/game_count");
        Date d1 = DateUtils.addDay(-6, DateUtils.String2Date(DateUtil.formatDate(new Date(), "yyyy-MM-dd")));
        Date d2 = DateUtils.addDay(1, DateUtils.String2Date(DateUtil.formatDate(new Date(), "yyyy-MM-dd")));
        //对局统计
        DataTable dtRoomStatis = LogPromoterReportDao.getRoomStatis(clubId, d1, d2);
        if (dtRoomStatis.rows.length > 0) {
            mv.addObject("dtRoomStatis", dtRoomStatis.rows);
        }
        return mv;

    }


    //流水统计
    @RequestMapping("query_economic_count")
    public ModelAndView query_economic_count(HttpServletResponse response
            , @RequestParam(value = "clubId", defaultValue = "0") long clubId) {
        ModelAndView mv = new ModelAndView("pre_sale_manage/ajax/table/economic_count");
        Date d1 = DateUtils.addDay(-6, DateUtils.String2Date(DateUtil.formatDate(new Date(), "yyyy-MM-dd")));
        Date d2 = DateUtils.addDay(1, DateUtils.String2Date(DateUtil.formatDate(new Date(), "yyyy-MM-dd")));
        //流水统计
        DataTable dtDiamondStatis = LogPromoterReportDao.getDiamondStatis(clubId, d1, d2);
        if (dtDiamondStatis.rows.length > 0) {
            mv.addObject("diamondStatis", dtDiamondStatis.rows);
        }
        return mv;

    }

    //成员明细
    @RequestMapping("query_member_detail")
    public ModelAndView query_member_detail(HttpServletResponse response
            , @RequestParam(value = "promoterId", defaultValue = "0") long promoterId) {
        ModelAndView mv = new ModelAndView("pre_sale_manage/ajax/table/member_detail");
        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));

        List<MemberDetailVO> memberDetailVOList = clubQueryService.getMemberDetailList(gameId, promoterId);//俱乐部成员明细
        mv.addObject("memberDetailVOList", memberDetailVOList);
        return mv;

    }

    //关系详情
    @RequestMapping("query_relation_detail")
    public ModelAndView query_relation_detail(
            @RequestParam(value = "promoterId", defaultValue = "0") long promoterId){
        ModelAndView mv = new ModelAndView("pre_sale_manage/ajax/table/relation_detail");
        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        DataTable relationDetail = clubQueryService.getRelationDetail(promoterId);
        if(relationDetail.rows.length>0){
            mv.addObject("relationDetail", relationDetail.rows);
        }

        return mv;

    }


    @RequestMapping("relationDetail_download")
    public void relationDetail_download(HttpServletResponse response
            , @RequestParam(value = "promoterId", defaultValue = "") long promoterId) {
        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));

        DataTable relationDetail = clubQueryService.getRelationDetail(promoterId);
        StringBuilder sb = new StringBuilder();
        sb.append("\t\t\t下级代理昵称,")
                .append("\t下级代理ID,")
                .append("\t下级代理手机号,")
                .append("\t时间,")
                .append("\r\n");
        //拼接cvs内容
        for (DataRow row : relationDetail.rows) {
            sb.append("\t").append(row.getColumnValue("nickName")).append(",")
                    .append(row.getColumnValue("promoterId")).append(",")
                    .append(row.getColumnValue("cellPhone")).append(",")
                    .append(row.getColumnValue("createDate")).append(",")
                    .append("\r\n");
        }
        //拼接文件名
        StringBuilder fileName = new StringBuilder(64);
        fileName.append("关系详情")
                .append(System.currentTimeMillis());
        //返回文件流
        FileUtil.FileDownload(fileName.toString(), sb.toString(), response);

    }



    //返钻明细
    @RequestMapping("query_rebate_detail")
    public ModelAndView query_rebate_detail(
            @RequestParam(value = "promoterId", defaultValue = "0") long promoterId
            , @RequestParam(value = "startDate", defaultValue = "") String _startDate
            , @RequestParam(value = "endDate", defaultValue = "") String _endDate) {
        ModelAndView mv = new ModelAndView("pre_sale_manage/ajax/table/rebate_detail");
        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        Date now = new Date();
        Date startDate = null;
        Date endDate = null;
        if(Strings.isNullOrEmpty(_startDate) && Strings.isNullOrEmpty(_endDate)){
            startDate =  dsqp.util.DateUtils.addDate("day",-6,now);
            endDate = now;
        }else{
            startDate = dsqp.util.DateUtils.String2Date(_startDate);
            endDate = dsqp.util.DateUtils.String2Date(_endDate);
        }
        mv.addObject("startDate", dsqp.util.DateUtils.Date2String(startDate));
        mv.addObject("endDate", dsqp.util.DateUtils.Date2String(endDate));
        //返钻总数
        int totalRebate = BonusDetailDao.getTotalRebateByPromoterId(promoterId);
        mv.addObject("totalRebate",totalRebate);
        //返钻明细
        DataTable DtRebateDetail = clubQueryService.getRebateDetail(promoterId, startDate, endDate);
        if(DtRebateDetail.rows.length>0){
            mv.addObject("DtRebateDetail", DtRebateDetail.rows);
        }

        return mv;

    }

    @RequestMapping("query_rebate_detailAjax")
    @ResponseBody
    public void query_rebate_detailAjax(HttpServletResponse response
            , @RequestParam(value = "promoterId", defaultValue = "0") long promoterId
            , @RequestParam(value = "startDate", defaultValue = "") String startDate
            , @RequestParam(value = "endDate", defaultValue = "") String endDate) {
        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        Date start = DateUtils.String2Date(startDate);
        Date end = DateUtils.String2Date(endDate);

        DataTable DtRebateDetail = clubQueryService.getRebateDetail(promoterId, start, end);
        StringBuilder sb = new StringBuilder();
        if(DtRebateDetail.rows.length>0){
            for (DataRow row : DtRebateDetail.rows) {
                sb.append("<tr><td align='center'>" +
                        row.getColumnValue("nickName") + "</td><td align='center'>" +
                        row.getColumnValue("fromPromoterId")  + "</td><td align='center'>" +
                        row.getColumnValue("diamond")  + "</td><td align='center'>" +
                        row.getColumnValue("rebateDiamond")  + "</td><td align='center'>" +
                        row.getColumnValue("createTime")  + "</td></tr>");
            }
        }

        RequestUtils.write(response, new Gson().toJson(sb.toString()));

    }



    @RequestMapping("rebateDetail_download")
    public void rebateDetail_download(HttpServletResponse response
            , @RequestParam(value = "promoterId", defaultValue = "0") long promoterId
            , @RequestParam(value = "startDate", defaultValue = "") String startDate
            , @RequestParam(value = "endDate", defaultValue = "") String endDate) {

        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        Date start = DateUtils.String2Date(startDate);
        Date end = DateUtils.String2Date(endDate);
        DataTable DtRebateDetail = clubQueryService.getRebateDetail(promoterId, start, end);
        StringBuilder sb = new StringBuilder();
        sb.append("\t\t\t购钻代理昵称,")
                .append("\t购钻代理ID,")
                .append("\t购买金额,")
                .append("\t提供返钻数量,")
                .append("\t时间,")
                .append("\r\n");
        //拼接cvs内容
        for (DataRow row : DtRebateDetail.rows) {
            sb.append("\t").append(row.getColumnValue("nickName")).append(",")
                    .append(row.getColumnValue("fromPromoterId")).append(",")
                    .append(row.getColumnValue("diamond")).append(",")
                    .append(row.getColumnValue("rebateDiamond")).append(",")
                    .append(row.getColumnValue("createDate")).append(",")
                    .append("\r\n");
        }

        //拼接文件名
        StringBuilder fileName = new StringBuilder(64);
        fileName.append("返钻明细")
                .append(System.currentTimeMillis());
        //返回文件流
        FileUtil.FileDownload(fileName.toString(), sb.toString(), response);

    }





}
