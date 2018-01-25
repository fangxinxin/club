package admin.service.impl;

import admin.operation.OperationServiceLog;
import admin.service.PromoterLockService;
import dsqp.db_club.dao.PromoterLockDao;
import dsqp.db_club.model.PromoterLockModel;
import org.springframework.stereotype.Service;

/**
 * Created by ds on 2017/7/30.
 */
@Service
public class PromoterLockServiceImpl implements PromoterLockService {

    @OperationServiceLog(menuItem = "afterSaleManage",menuName = "售后管理",recordType =15,typeName = "封停账号")
    public int add(PromoterLockModel model) {
        return PromoterLockDao.add(model);
    }


    @OperationServiceLog(menuItem = "afterSaleManage",menuName = "售后管理",recordType =16,typeName = "解封账号")
    public int deleteByPromoterId(long promoterId) {
        return PromoterLockDao.deleteByPromoterId(promoterId);
    }


}
