package club.controller.notice;

import club.service.NoticeService;
import dsqp.db.model.DataRow;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db_club.dao.NoticeDao;
import dsqp.db_club.dao.NoticeDetailDao;
import dsqp.util.CommonUtils;
import dsqp.util.DateUtils;
import dsqp.util.RequestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by jeremy on 2017/7/21.
 */
@Controller
public class NoticeController {

    @Resource
    private NoticeService noticeService;


    @RequestMapping("noticeList")
    public String findList(Model model) {

        Subject subject = SecurityUtils.getSubject();
        long promoterId = CommonUtils.getLongValue(subject.getSession().getAttribute("id"));
        long gameId = CommonUtils.getLongValue(subject.getSession().getAttribute("gameId"));

        DataTable dtNoticeNums = NoticeDetailDao.getNoticeNumsById(promoterId);
        int noticeNums = Integer.parseInt(dtNoticeNums.rows[0].getColumnValue("nums"));

        //删除超过20条的消息记录
        if(noticeNums>20){
            //直接保留排序后前20条记录，其余删掉
            DataTable dtNoticeList = NoticeDetailDao.getNoticeList2Delete(promoterId, 20, 100);
            List noticeDetailIdList = DBUtils.convert2List(Long.class, "dId", dtNoticeList);  //notice_detail表中的id
            int m = NoticeDetailDao.deleteNoticeDetailByListId(noticeDetailIdList);
        }

        DataTable dtNoticeList = noticeService.findNoticeListByGameIdAndPromoterId(gameId,promoterId);

        for(DataRow row:dtNoticeList.rows){
            String createTime = row.getColumnValue("createTime");
//            createTime = createTime.substring(5,7)+"月"+createTime.substring(8,10)+"日";
            createTime = DateUtils.Date2String(DateUtils.String2DateTime(createTime), "yyyy年MM月dd日");
            row.setColumnValue("createTime",createTime);
        }

        model.addAttribute("dtNoticeList", dtNoticeList.rows);
        model.addAttribute("length", dtNoticeList.rows.length);

        return "notice/notice";
    }

    @RequestMapping("checkNotice")
    public void check(
                    HttpServletResponse response,
                    @RequestParam(value = "dId", defaultValue = "0") long id) {
        int m = NoticeDetailDao.updateIsReadById(id);

        RequestUtils.write(response,String.valueOf(m));
    }


    @RequestMapping("noticeDialog")
    public ModelAndView promoterDialog(
            @RequestParam(value = "id", defaultValue = "0") long id
            , @RequestParam(value = "title", defaultValue = "") String title
            , @RequestParam(value = "createTime", defaultValue = "") String createTime) {
        ModelAndView mv = new ModelAndView("notice/dialog");

        mv.addObject("title", title);
        mv.addObject("notice", NoticeDao.getOne(id));
        mv.addObject("createTime", createTime);

        return mv;
    }

}
