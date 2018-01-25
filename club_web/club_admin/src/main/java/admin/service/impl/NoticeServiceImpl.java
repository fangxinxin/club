package admin.service.impl;

import admin.operation.OperationServiceLog;
import admin.service.NoticeService;
import dsqp.db_club.dao.NoticeDao;
import dsqp.db_club.model.NoticeModel;
import org.springframework.stereotype.Service;

/**
 * Created by jeremy on 2017/8/16.
 */
@Service
public class NoticeServiceImpl implements NoticeService{
    @OperationServiceLog(menuItem = "afterSaleManage",menuName = "售后管理",recordType =22,typeName = "发布公告")
    public long add(NoticeModel model) {
        return NoticeDao.add(model);
    }
}
