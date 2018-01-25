package club.service.impl;

import club.service.LogService;
import dsqp.db_club_log.dao.LogNotifyDao;
import dsqp.db_club_log.model.LogNotifyModel;
import dsqp.util.CommonUtils;
import dsqp.util.RequestUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by Aris on 2016/11/21.
 */
@Service
public class LogServiceImpl implements LogService {

    @Override
    public long addRequestLog(HttpServletRequest request) {
        String url = RequestUtils.getUrl(request,false,true);
        String para;
        if (request.getMethod().equalsIgnoreCase("get")){
            para = RequestUtils.getMapStringFromRequest(request,false);
        }else{
            para = RequestUtils.getInputDataFromRequest(request);
        }

        //这里统一接收参数
        request.setAttribute("para",para);

        LogNotifyModel obj = new LogNotifyModel();
        obj.setRequestParam(para);
        obj.setRequestUrl(url);
        obj.setRequestMethod(request.getMethod());
        obj.setContentType(CommonUtils.getStringValue(request.getContentType()));
        obj.setResponseParam("");
        obj.setCreateTime(new Date());

        LogNotifyDao.add(obj);

        return obj.getId();
    }

    @Override
    public long updateLog(long logID, String content) {
        return LogNotifyDao.update(logID, content);
    }
}
