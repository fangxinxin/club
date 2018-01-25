package admin.controller.after_sale_manage;

import admin.service.PromoterLockService;
import admin.util.OutExcelUtil;
import admin.util.PropertyUtil;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import dsqp.common_const.club.ApiKey;
import dsqp.common_const.club.ClubStatus;
import dsqp.common_const.club.LoginStatus;
import dsqp.common_const.club.PromoterLevel;
import dsqp.common_const.club_admin.Permission;
import dsqp.db.model.DataRow;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db_club.dao.ClubDao;
import dsqp.db_club.dao.PromoterDao;
import dsqp.db_club.dao.PromoterLockDao;
import dsqp.db_club.model.ClubModel;
import dsqp.db_club.model.PromoterLockModel;
import dsqp.db_club.model.PromoterModel;
import dsqp.db_club_dict.dao.DictGameDao;
import dsqp.db_club_log.dao.LogLoginDao;
import dsqp.util.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
 * Created by ds on 2017/7/29.
 */
@Controller("LockPromoter")
@RequestMapping("after_sale_manage")
public class LockPromoterController {
    private final static String PATH = "after_sale_manage/";

    @Autowired
    private PromoterLockService promoterLockService;


    @RequestMapping("lockPromoter")
    public ModelAndView lockPromoter() {
        ModelAndView mv = new ModelAndView(PATH+"lockPromoter");
        return mv;
    }

    /**
     * 查询可封停代理
     * 条件：
     *      代理不可为待激活状态（loginStatus: 3） 、俱乐部为正式俱乐部状态（clubStatus: 1）
     * @param promoterId
     */
    @RequiresPermissions(Permission.SYS_LOCK_PROMOTERS_SHOW)
    @RequestMapping("ajax/queryLockPromoter")
    public ModelAndView ajax_queryLockPromoter(
            @RequestParam(value = "promoterId", defaultValue = "0") long promoterId) {
        ModelAndView mv = new ModelAndView(PATH+"ajax/lockPromoter");

        if (promoterId != 0) {

            PromoterModel promoter = PromoterDao.getOne(promoterId);//代理商
            if (promoter == null || promoter.getIsEnable() == false) {
                return mv;      //代理不存在、代理为不可用状态
            }

            DataTable clubDt = ClubDao.queryByPromoterId(promoter.getId());//俱乐部
            if (clubDt.rows.length <= 0) {
                return mv;      //俱乐部不存在
            }

            ClubModel club = DBUtils.convert2Model(ClubModel.class, clubDt.rows[0]);
            if (club.getClubStatus() == ClubStatus.Init.getClubStatus()) {
                return mv;      //未转正俱乐部，不可封停
            }
            /*俱乐部信息*/
            mv.addObject("club", club);

            /*代理信息*/
            String pLevel = PromoterLevel.getLevelName(promoter.getpLevel());

            mv.addObject("promoter", promoter);
            mv.addObject("pLevel", pLevel);

            if (promoter.getLoginStatus() == LoginStatus.Forbid.getLoginStatus()) {
                PromoterLockModel promoterLock = PromoterLockDao.getByPromoterId(promoterId);
                if (promoterLock != null) {     //代理已封停
                     /*代理已被封停信息*/
                    mv.addObject("promoterLock", promoterLock);
                }
            }

            /*上级信息*/
            PromoterModel parentP = PromoterDao.getOne(promoter.getParentId());
            if (parentP != null) {
                String parentNickName = parentP.getNickName();
                String ppLevel = PromoterLevel.getLevelName(parentP.getpLevel());

                mv.addObject("parentNickName", parentNickName);
                mv.addObject("ppLevel", ppLevel);
            }

            //最后登陆时间
            DataTable logDt = LogLoginDao.queryByPromoterId(promoter.getId());
            if (logDt.rows.length > 0) {
                mv.addObject("loginTime", logDt.rows[0].getColumnValue("createTime"));
            }

        }

        return mv;
    }


    /**
     * 封停列表
     * @return
     */
    @RequiresPermissions(Permission.SYS_LOCK_PROMOTERS_SHOW)
    @RequestMapping("ajax/listLockPromoter")
    public ModelAndView ajax_listLockPromoter() {
        ModelAndView mv = new ModelAndView(PATH+"ajax/lockPromoter");
        Subject subject = SecurityUtils.getSubject();

        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
        DataTable lockPromoterList = PromoterLockDao.listByGameId(gameId);
        if (lockPromoterList.rows.length > 0) {
            mv.addObject("lockPromoterList", lockPromoterList.rows);
        }
        return mv;
    }

    @RequestMapping("ajax/checkLockPromoter")
    public void ajax_checkLockPromoter(HttpServletResponse response
            , @RequestParam(value = "lockDay", defaultValue = "0") int lockDay) {

        if (lockDay != 0) {
            //解封时间
            Date expireTime = DateUtils.addDate("day", lockDay, new Date());
            String startTime = DateUtils.DateTime2String(new Date());
            String endTime = DateUtils.Date2String(expireTime, "yyyy-MM-dd HH:mm:ss");
            Map<String, String> map = Maps.newHashMap();

            map.put("startTime", startTime);
            map.put("endTime", endTime);

            RequestUtils.write(response, JsonUtils.getJson(map));
        }
    }

    /**
     * 封停
     * @param response
     * @param promoterId
     * @param lockDay
     * @param startTime
     * @param endTime
     * @param lockReason
     */
    @RequiresPermissions(Permission.OPERATE_ADD_LOCK_PROMOTER_ADD)
    @RequestMapping("ajax/addLockPromoter")
    @ResponseBody
    public void ajax_addLockPromoter(HttpServletResponse response
            , @RequestParam(value = "promoterId", defaultValue = "0") long promoterId
            , @RequestParam(value = "lockDay", defaultValue = "0") int lockDay
            , @RequestParam(value = "startTime", defaultValue = "") String startTime
            , @RequestParam(value = "endTime", defaultValue = "") String endTime
            , @RequestParam(value = "lockReason", defaultValue = "") String lockReason) {
        Subject subject = SecurityUtils.getSubject();
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));

        if(lockDay == 0) {
            RequestUtils.write(response, String.valueOf(-1));
            return;
        }

        if (promoterId != 0 && !Strings.isNullOrEmpty(startTime) && !Strings.isNullOrEmpty(endTime)) {

            PromoterModel p = PromoterDao.getOne(promoterId);
            String editAdmin = CommonUtils.getStringValue(SecurityUtils.getSubject().getSession().getAttribute("remark"));
            DataTable clubDt = ClubDao.queryByPromoterId(promoterId);

            if (p != null && clubDt.rows.length > 0
                    && !Strings.isNullOrEmpty(startTime) && !Strings.isNullOrEmpty(endTime)) {
                PromoterLockModel model = new PromoterLockModel();

                model.setPromoterId(p.getId());
                model.setNickName(p.getNickName());
                model.setPeopleNum(Integer.valueOf(clubDt.rows[0].getColumnValue("peopleNum")));
                model.setpLevel(p.getpLevel());
                model.setGameId(p.getGameId());
                model.setLockDay(lockDay);
                model.setEditAdmin(editAdmin);
                model.setRemark(lockReason);
                model.setCreateTime(DateUtils.String2DateTime(startTime));
                model.setExpireTime(DateUtils.String2DateTime(endTime));

                int r1 = promoterLockService.add(model);
                if (r1 > 0) {
                    int r2 = PromoterDao.updateLoginStatusById(p.getId(), LoginStatus.Forbid.getLoginStatus());     //封停代理商
                    int r3 = ClubDao.updateClubStatus(p.getId(), ClubStatus.Forbid.getClubStatus());       //更改俱乐部状态为：2

                    //封停用户 :: 发送通知 （【%s麻将】您的代理商帐号：%s，经官方查证有违规行为，已做冻结处理，期限为%s天。如有疑问，请咨询官方客服。）
                    if (r2 > 0 && r3 > 0) {
                        String remark = DictGameDao.queryRemarkByGameId(gameId);

                        String apiKey = "";
                        if ("66".equals(remark)) {
                            apiKey = ApiKey.LIULIUKEY;
                        } else if ("来来".equals(remark)) {
                            apiKey = ApiKey.LAILAIKEY;
                        } else if ("大圣".equals(remark)) {
                            apiKey = ApiKey.DASHENGQIPAI;
                        }
                        String op = null;
                        try {
                            op = SMSReqSender.sendSms(apiKey,
                                    String.format(PropertyUtil.getProperty("sms.lock"), remark, p.getCellPhone(), lockDay),
                                    String.valueOf(p.getCellPhone()));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (!Strings.isNullOrEmpty(op))
                            System.out.println(op);
                    }
                    RequestUtils.write(response, String.valueOf(r2));
                }
            }
        }
    }


    /**
     * 解封
     * @param response
     * @param promoterId
     */
    @RequiresPermissions(Permission.OPERATE_UNLOCK_PROMOTER_UNLOCK)
    @RequestMapping("ajax/unlockPromoter")
    @ResponseBody
    public void ajax_unlockPromoter(HttpServletResponse response
            , @RequestParam(value = "promoterId", defaultValue = "0") long promoterId) {
        if (promoterId == 0)
            return;

        PromoterModel promoter = PromoterDao.getOne(promoterId);
        if (promoter == null)
            return;

        int r1 = promoterLockService.deleteByPromoterId(promoter.getId());
        if (r1 > 0) {
            int r2 = PromoterDao.updateLoginStatusById(promoter.getId(), LoginStatus.Normal.getLoginStatus());
            int r3 = ClubDao.updateClubStatus(promoter.getId(), ClubStatus.Normal.getClubStatus());
            boolean result = r2+r3 >= 2 ? true : false;
            RequestUtils.write(response, String.valueOf(result));
        }

    }


    //excel 下载
    @RequiresPermissions(Permission.OPERATE_LOCK_PROMOTER_DOWNLOAD_EXCEL)
    @RequestMapping("LockPromoterExcel")
    @ResponseBody
    public void LockPromoterExcel(HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));

        if (gameId != 0) {
            Map<String, String> rowNames = Maps.newLinkedHashMap();
            rowNames.put("promoterId", "代理商ID");
            rowNames.put("nickName", "代理商昵称");
            rowNames.put("pLevel", "代理商级别");
            rowNames.put("peopleNum", "俱乐部成员");
            rowNames.put("lockDay", "封停时间");
            rowNames.put("createTime", "操作时间");
            rowNames.put("editAdmin", "操作人员");

            StringBuilder sb = new StringBuilder(128);
            String thead = OutExcelUtil.getThead(rowNames);
            sb.append(thead);

            DataTable dt = PromoterLockDao.listByGameId(gameId);      //封停列表

            if (dt.rows.length > 0) {
                for (DataRow row: dt.rows) {
                    for (String key: rowNames.keySet()) {
                        String value = row.getColumnValue(key);
                        if (key.equals("pLevel")) {
                            value = PromoterLevel.getLevelName(CommonUtils.getIntValue(value));
                        }
                        sb.append("\t").append(value).append(",");
                    }
                    sb.append("\r\n");
                }
            }

            FileUtil.FileDownload(
                    OutExcelUtil.getFileName("封停列表").toString(),
                    sb.toString(),
                    response
            );
        }

    }

}
