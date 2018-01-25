package admin.service.impl;

import admin.operation.OperationServiceLog;
import admin.service.ClubService;
import dsqp.db_club_dict.dao.DictClubDao;
import dsqp.db_club_dict.model.DictClubModel;
import org.springframework.stereotype.Service;

/**
 * Created by jeremy on 2017/9/27.
 */
@Service
public class ClubServiceImpl implements ClubService{

    @OperationServiceLog(menuItem = "operatingConfig",menuName = "运营配置",recordType =35,typeName = "俱乐部配置修改")
    public int add(DictClubModel model) {
        return DictClubDao.add(model);
    }

    @OperationServiceLog(menuItem = "operatingConfig",menuName = "运营配置",recordType =35,typeName = "俱乐部配置修改")
    public int update(DictClubModel model) {
        return DictClubDao.update(model);
    }
}
