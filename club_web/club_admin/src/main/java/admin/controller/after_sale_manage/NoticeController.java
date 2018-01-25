package admin.controller.after_sale_manage;

import admin.service.NoticeService;
import com.google.common.collect.Maps;
import dsqp.common_const.club_admin.Permission;
import dsqp.db_club.dao.NoticeDetailDao;
import dsqp.db_club.dao.PromoterDao;
import dsqp.db_club.model.NoticeDetailModel;
import dsqp.db_club.model.NoticeModel;
import dsqp.util.CommonUtils;
import dsqp.util.DateUtils;
import dsqp.util.JsonUtils;
import dsqp.util.RequestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by jeremy on 2017/7/29.
 */
@Controller
@RequestMapping("after_sale_manage/")
public class NoticeController {

    @Resource
    private NoticeService noticeService;


    @RequestMapping("notice")
    public String index(){
        return "after_sale_manage/notice";
    }

    /**
     * 发布公告
     * @param response
     * @param title
     * @param content
     */
    @RequiresPermissions(Permission.OPERATE_NOTICE_PUBLISH_ADD)
    @RequestMapping("noticePublish")
    public void publish( HttpServletResponse response,
                           @RequestParam(value = "title", defaultValue = "") String  title,
                           @RequestParam(value = "content", defaultValue = "") String  content) {
        Session session = SecurityUtils.getSubject().getSession();
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));

        NoticeModel noticeModel = new NoticeModel();
        noticeModel.setGameId(gameId);
        noticeModel.setNoticeType(1);
        noticeModel.setTitle(title);
        noticeModel.setContent(content);
        noticeModel.setCreatTime(new Date());
        long noticeId = noticeService.add(noticeModel);

        if(noticeId>0){
            NoticeDetailModel noticeDetailModel = new NoticeDetailModel();
            noticeDetailModel.setGameId(gameId);
            noticeDetailModel.setNoticeId(noticeModel.getId());
            noticeDetailModel.setIsRead(false);
            noticeDetailModel.setCreateTime(noticeModel.getCreatTime());
            List<Long> idList = PromoterDao.getPromoterIdListByGameId(gameId);
            if(idList!=null){
                for(long promoterId : idList){
                    noticeDetailModel.setPromoterId(promoterId);
                    NoticeDetailDao.add(noticeDetailModel);
                }
            }
            RequestUtils.write(response,"1");
        }else {
            RequestUtils.write(response,"0");
        }

    }

    @RequestMapping("ajax/getCurrentTime")
    public void getCurrentTime(HttpServletResponse response) {

        String currentTime = DateUtils.Date2String(new Date(),"yyyy年MM月dd日");
        Map<String, String> map = Maps.newHashMap();
        map.put("currentTime", currentTime);
        RequestUtils.write(response, JsonUtils.getJson(map));

    }



}
