package admin.controller.after_sale_manage;

import admin.service.ClubQueryService;
import admin.service.PromoterHandleService;
import admin.util.OutExcelUtil;
import admin.vo.ExcelVO;
import admin.vo.MemberDetailVO;
import admin.vo.MemberDiamondsVO;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import db_admin.dao.SysUserDao;
import db_admin.model.SysUserModel;
import dsqp.common_const.club.GamecardSource;
import dsqp.common_const.club.LogClubType;
import dsqp.common_const.club_admin.Permission;
import dsqp.common_const.club_admin.WithdrawRequest;
import dsqp.db.model.DataRow;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db_club.dao.*;
import dsqp.db_club.model.ClubModel;
import dsqp.db_club.model.PromoterModel;
import dsqp.db_club.model.PromoterSellModel;
import dsqp.db_club_dict.dao.DictGameDbDao;
import dsqp.db_club_dict.model.DictGameDbModel;
import dsqp.db_club_log.dao.*;
import dsqp.db_club_log.model.LogAdminGameCardModel;
import dsqp.db_club_log.model.LogClubModel;
import dsqp.db_club_log.model.LogGamecardModel;
import dsqp.util.CommonUtils;
import dsqp.util.DateUtils;
import dsqp.util.FileUtil;
import dsqp.util.RequestUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
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
import java.util.Map;

/**
 * Created by jeremy on 2017/10/11.
 */
@Controller
@RequestMapping("after_sale_manage/promoterHandle")
public class ClubHandleController {

    @Resource
    private ClubQueryService clubQueryService;
    @Resource
    private PromoterHandleService promoterHandleService;

    @RequestMapping("handle")
    public String index() {
        return "after_sale_manage/club_handle";
    }

    /**
     * 查询俱乐部信息
     */
    @RequestMapping("queryByClub")
    @RequiresPermissions(Permission.SYS_QUERY_CLUB_SHOW)
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
                    String totalRequest = WithdrawRequestDao.queryTotalByPromoterId(promoter.getId(), WithdrawRequest.SUCCESS.getWithdrawStatus());//累计提现
                    String requestPay = "";//提成金购买钻石金额
                    Date d1 = DateUtils.addDay(-6, DateUtils.String2Date(DateUtil.formatDate(new Date(), "yyyy-MM-dd")));
                    Date d2 = DateUtils.addDay(1, DateUtils.String2Date(DateUtil.formatDate(new Date(), "yyyy-MM-dd")));

                    DataTable allSellList = PromoterSellDao.getAllListByCreateTime(promoter.getId(), d1, d2);//赠送明细 销售明细
                    model.addAttribute("allSellList", allSellList.rows);

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
                    model.addAttribute("totalRequest", totalRequest.length() > 0 ? totalRequest : "0");
                    model.addAttribute("requestPay", requestPay.length() > 0 ? requestPay : "0");
                }
            }
        } else {
            model.addAttribute("isEmpty", "俱乐部ID不存在,请重新输入");
        }
        return "after_sale_manage/club_handle";
    }

    /**
     * 查询俱乐部信息
     */
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
        Date d1 = null;
        Date d2 = null;
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
                    d1 = DateUtils.addDay(-6, DateUtils.String2Date(DateUtil.formatDate(new Date(), "yyyy-MM-dd")));
                    d2 = DateUtils.addDay(1, DateUtils.String2Date(DateUtil.formatDate(new Date(), "yyyy-MM-dd")));
                    model.addAttribute("endDate", DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
                    model.addAttribute("startDate", DateUtil.formatDate(DateUtils.addDay(-6, new Date()), "yyyy-MM-dd"));
                } else {
                    d1 = DateUtils.String2Date(startDate);
                    d2 = DateUtils.addDay(1, DateUtils.String2Date(endDate));
                    model.addAttribute("endDate", endDate);
                    model.addAttribute("startDate", startDate);
                }

                DataTable allSellList = PromoterSellDao.getAllListByCreateTime(promoter.getId(), d1, d2);//赠送明细 销售明细
                model.addAttribute("allSellList", allSellList.rows);

                return "after_sale_manage/club_handle";
            }
        } else {
            model.addAttribute("isEmpty", "该Id暂无俱乐部信息,请重新输入");
        }
        return "after_sale_manage/club_handle";
    }

    /**
     * 对局统计
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

    /**
     * 对局统计导出excel
     */
    @RequiresPermissions(Permission.OPERATE_QUERY_ROOM_STATIS_EXCEL)
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

    /**
     * 流水统计导出Excel
     */
    @RequiresPermissions(Permission.OPERATE_QUERY_DIAMOND_STATIS_EXCEL)
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

    /**
     * 俱乐部成员详情导出Excel
     */
    @RequiresPermissions(Permission.OPERATE_MEMBERS_DETAIL_EXCEL)
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


    /**
     * 赠送明细 :: 下载Excel
     */
    @RequiresPermissions(Permission.OPERATE_QUERY_DIAMOND_CHANGE_EXCEL)
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

    /**
     * 赠送删除房卡
     */
    @RequiresPermissions(Permission.OPERATE_GIVING_GAMECARD_UPDATE)
    @RequestMapping("giveGameCard")
    public void giveGameCard(HttpServletResponse response
            , @RequestParam(value = "promoterId", defaultValue = "0") long promoterId
            , @RequestParam(value = "changeNum", defaultValue = "0") int changeNum
            , @RequestParam(value = "changeBefore", defaultValue = "0") long changeBefore
            , @RequestParam(value = "changeAfter", defaultValue = "0") long changeAfter) {
        Subject subject = SecurityUtils.getSubject();
        SysUserModel admin = (SysUserModel) subject.getPrincipal();
        //删除数量不得多于当前钻石数
        if (changeNum < (0 - changeBefore)) {
            RequestUtils.write(response, "NO");
            return;
        }

        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
        long adminId = admin.getId();
        String adminName = admin.getUserName();
        LogAdminGameCardModel pojo = new LogAdminGameCardModel();
        pojo.setAdminId(adminId);
        pojo.setAdminName(adminName);
        pojo.setGameId(gameId);
        pojo.setPromoterId(promoterId);
        pojo.setChangeNum(changeNum);
        pojo.setChangeBefore(changeBefore);
        pojo.setChangeAfter(changeAfter);
        pojo.setCreateTime(new Date());
        pojo.setCreateDate(new Date());
        int i = promoterHandleService.updateGameCardById(changeNum, promoterId);
        int add = LogAdminGameCardDao.add(pojo);

        LogGamecardModel log = new LogGamecardModel();
        PromoterModel one = PromoterDao.getOne(promoterId);

        log.setGameId(gameId);
        log.setCreateTime(new Date());
        log.setPromoterId(promoterId);
        log.setChangeAfter(changeAfter);
        log.setChangeBefore(changeBefore);
        log.setNickName(one.getNickName());
        log.setChangeNum(changeNum);
        log.setCreateDate(new Date());
        if (changeNum > 0) {
            log.setSource(GamecardSource.GIVECARD.getType());
        } else {
            log.setSource(GamecardSource.DELETECARD.getType());
        }
        LogGamecardDao.add(log);
        if (add == 1 && i == 1) {
            RequestUtils.write(response, "YES");
        } else {
            RequestUtils.write(response, "NO");
        }
    }

    /**
     * 赠送/删除俱乐部专属房卡
     */
    @RequiresPermissions(Permission.OPERATE_GIVING_GAMECARD_UPDATE)
    @RequestMapping("giveClubGameCard")
    public void giveClubGameCard(HttpServletResponse response
            , @RequestParam(value = "promoterId", defaultValue = "0") long promoterId
            , @RequestParam(value = "type", defaultValue = "0") int type
            , @RequestParam(value = "changeNum", defaultValue = "0") int changeNum
            , @RequestParam(value = "changeBefore", defaultValue = "0") long changeBefore) {
        Subject subject = SecurityUtils.getSubject();
        SysUserModel admin = (SysUserModel) subject.getPrincipal();

        if (type == 2) {
            changeNum = -changeNum;
        }
        long changeAfter = changeBefore + changeNum;

        //删除数量不得多于当前钻石数
        if (type == 2 && changeAfter < 0) {
            RequestUtils.write(response, "NO");
            return;
        }

        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
        long adminId = admin.getId();
        String adminName = admin.getUserName();
        LogAdminGameCardModel pojo = new LogAdminGameCardModel();
        pojo.setAdminId(adminId);
        pojo.setAdminName(adminName);
        pojo.setGameId(gameId);
        pojo.setPromoterId(promoterId);
        pojo.setChangeNum(changeNum);
        pojo.setChangeBefore(changeBefore);
        pojo.setChangeAfter(changeAfter);
        pojo.setCreateTime(new Date());
        pojo.setCreateDate(new Date());
        int r1 = LogAdminGameCardDao.add(pojo);    //添加日志 log_admin_gameCard

        LogGamecardModel log = new LogGamecardModel();
        PromoterModel one = PromoterDao.getOne(promoterId);
        log.setGameId(gameId);
        log.setCreateTime(new Date());
        log.setPromoterId(promoterId);
        log.setChangeAfter(changeAfter);
        log.setChangeBefore(changeBefore);
        log.setNickName(one.getNickName());
        log.setChangeNum(changeNum);
        log.setCreateDate(new Date());
        log.setSource(GamecardSource.CLUB_CARD.getType());

        int r2 = LogGamecardDao.add(log);    //添加日志 log_gameCard
        if (r1 > 0 && r2 > 0) {
            int result = promoterHandleService.updateClubCard(changeNum, promoterId, type);    //更新俱乐部专属钻石
            if (result > 0) {
                RequestUtils.write(response, "YES");
                return;
            }
        }

        RequestUtils.write(response, "NO");

    }

    /**
     * 补发房卡
     */
    @RequiresPermissions(Permission.OPERATE_REISSUE_GAMECARD_UPDATE)
    @RequestMapping("reissue")
    public void reissue(
            @RequestParam(value = "id", defaultValue = "") long id, HttpServletResponse response) {

        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        DictGameDbModel byGameId = DictGameDbDao.getByGameId(gameId, true);
        PromoterSellModel sell = PromoterSellDao.getOne(id);
        if (!sell.getIsSuccess()) {
            int i = promoterHandleService.updateGameCardByUserId(byGameId, sell.getSellNum(), sell.getGameUserId());
            if (i > 0) {
                sell.setIsSuccess(true);
                PromoterSellDao.update(sell);
                RequestUtils.write(response, "OK");
            }
        } else {
            RequestUtils.write(response, "NO");
        }
    }

    /**
     * ajax 按日期 查询赠送明细  （销售明细）
     */
    @RequiresPermissions(Permission.SYS_SELL_DETAIL)
    @RequestMapping("ajaxSellList")
    @ResponseBody
    public void ajaxSellList(
            HttpServletResponse response
            , @RequestParam(value = "promoterId", defaultValue = "0") long promoterId
            , @RequestParam(value = "startDate", defaultValue = "") String startDate
            , @RequestParam(value = "endDate", defaultValue = "") String endDate) {

        DataTable allList = PromoterSellDao.getAllListByCreateTime(promoterId, DateUtils.String2Date(startDate), DateUtils.addDay(1, DateUtils.String2Date(endDate)));
        StringBuilder sb = new StringBuilder();
        if (allList.rows.length > 0) {
            for (DataRow row : allList.rows) {
                String js = " <td><input type = 'hidden' name = 'id' value = " + row.getColumnValue("id") + " >" +
                        "<input type = 'button' value = '补发' class='btn btn-warning btn-xs' onclick = 'reissueOk(this);'>" +
                        "<input type = 'hidden' name = 'id' value = " + row.getColumnValue("gameNickName") + " >" +
                        "<input type = 'hidden' name = 'id' value = " + row.getColumnValue("sellNum") + "></td>";

                sb.append("<tr><td align='center'>" + row.getColumnValue("gameNickName") + "</td>")
                        .append("<td align='center'>" + row.getColumnValue("gameUserId") + "</td>")
                        .append("<td align='center'>" + row.getColumnValue("sellNum") + "</td>")
                        .append("<td align='center'>" + ("true".equals(row.getColumnValue("isSuccess")) ? "已到账" : "未到账") + "</td>")
                        .append("true".equals(row.getColumnValue("isSuccess")) ? "<td></td>" : js)
                        .append("<td align='center'>" + row.getColumnValue("createTime") + "</td></tr>");
            }
        }
        RequestUtils.write(response, sb.toString());
    }

    /**
     * 导出Excel  销售明细   2017-10-12
     */
    @RequiresPermissions(Permission.OPERATE_SELL_DIAMOND_QUERY_EXCEL)
    @RequestMapping(value = "downLoadSellList")
    @ResponseBody
    public void downLoadSellList(HttpServletResponse response
            , @RequestParam(value = "promoterId", defaultValue = "0") long promoterId
            , @RequestParam(value = "startDate", defaultValue = "") String startDate
            , @RequestParam(value = "endDate", defaultValue = "") String endDate) {

        DataTable sellList = PromoterSellDao.getAllListByCreateTime(promoterId, DateUtils.String2Date(startDate), DateUtils.addDay(1, DateUtils.String2Date(endDate)));
        Map<String, String> rowNames = Maps.newLinkedHashMap();
        if (sellList.rows.length > 0) {
            for (DataRow row : sellList.rows) {
                if ("true".equals(row.getColumnValue("isSuccess"))) {
                    row.setColumnValue("isSuccess", "已到账");
                } else if ("false".equals(row.getColumnValue("isSuccess"))) {
                    row.setColumnValue("isSuccess", "未到账");
                }
            }
            rowNames.put("gameNickName", "赠送对象");
            rowNames.put("gameUserId", "对象ID");
            rowNames.put("sellNum", "赠送数额");
            rowNames.put("isSuccess", "是否到账");
            rowNames.put("createTime", "时间");
        }
        String fileName = "销售明细";
        ExcelVO vo = OutExcelUtil.download(fileName, rowNames, sellList);
        FileUtil.FileDownload(vo.getFileName(), vo.getContent(), response);
    }


    /**
     * ajax 修改俱乐部昵称
     */
    @RequiresPermissions(Permission.OPERATE_MODIFY_CLUB_INFO_UPDATE)
    @RequestMapping("updateClubName")
    @ResponseBody
    public void updateClubName(
            HttpServletResponse response
            , @RequestParam(value = "clubId", defaultValue = "0") long clubId
            , @RequestParam(value = "clubName", defaultValue = "") String clubName) {

        int i = promoterHandleService.updateClubName(clubId, clubName, 1);
        if (i > 0) {
            RequestUtils.write(response, "OK");
        } else {
            RequestUtils.write(response, "NO");
        }

    }

    /**
     * ajax 修改代理商手机号
     */
    @RequiresPermissions(Permission.OPERATE_MODIFY_CLUB_INFO_UPDATE)
    @RequestMapping("updateCellphone")
    @ResponseBody
    public void updateCellPhone(
            HttpServletResponse response
            , @RequestParam(value = "cellphone", defaultValue = "0") long cellphone
            , @RequestParam(value = "newCellphone", defaultValue = "0") long newCellphone) {

        int i = promoterHandleService.updateCellPhone(cellphone, newCellphone, 2);
        if (i > 0) {
            RequestUtils.write(response, "OK");
        } else {
            RequestUtils.write(response, "NO");
        }

    }

    /**
     * ajax 修改代理商姓名
     */
    @RequiresPermissions(Permission.OPERATE_MODIFY_CLUB_INFO_UPDATE)
    @RequestMapping("updateRealName")
    @ResponseBody
    public void updateRealName(
            HttpServletResponse response
            , @RequestParam(value = "cellphone", defaultValue = "0") long cellphone
            , @RequestParam(value = "realName", defaultValue = "") String realName) {

        int i = promoterHandleService.updateRealName(cellphone, realName, 3);
        if (i > 0) {
            RequestUtils.write(response, "OK");
        } else {
            RequestUtils.write(response, "NO");
        }
    }


    /**
     * ajax 修改代理商密码
     */
    @RequiresPermissions(Permission.OPERATE_MODIFY_CLUB_INFO_UPDATE)
    @RequestMapping("updatePass")
    @ResponseBody
    public void updatePass(
            HttpServletResponse response
            , @RequestParam(value = "cellphone", defaultValue = "0") long cellphone
            , @RequestParam(value = "password", defaultValue = "") String password) {

        int i = promoterHandleService.updatePass(cellphone, password, 4);
        if (i > 0) {
            RequestUtils.write(response, "OK");
        } else {
            RequestUtils.write(response, "NO");
        }

    }

    /**
     * 修改代理商备注
     */
    @RequiresPermissions(Permission.OPERATE_MODIFY_CLUB_INFO_UPDATE)
    @RequestMapping("updateRecord")
    @ResponseBody
    public void updateRecord(
            HttpServletResponse response
            , @RequestParam(value = "promoterId", defaultValue = "0") long promoterId
            , @RequestParam(value = "remark", defaultValue = "") String remark) {

        int i = promoterHandleService.updateRemark(promoterId, remark, 5);
        if (i > 0) {
            RequestUtils.write(response, "OK");
        } else {
            RequestUtils.write(response, "NO");
        }

    }

    /**
     * ajax 修改员工微信号
     */
    @RequiresPermissions(Permission.OPERATE_MODIFY_CLUB_INFO_UPDATE)
    @RequestMapping("updateWechat")
    @ResponseBody
    public void updateWechat(
            HttpServletResponse response
            , @RequestParam(value = "promoterId", defaultValue = "0") long promoterId
            , @RequestParam(value = "staffWechat", defaultValue = "") String staffWechat) {

        int i = promoterHandleService.updateWechat(promoterId, staffWechat, 6);
        if (i > 0) {
            RequestUtils.write(response, "OK");
        } else {
            RequestUtils.write(response, "NO");
        }

    }

}
