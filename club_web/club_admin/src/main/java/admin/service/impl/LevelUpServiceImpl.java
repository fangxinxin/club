package admin.service.impl;

import admin.operation.OperationServiceLog;
import admin.service.LevelUpService;
import dsqp.db_club_dict.dao.DictLevelUpDao;
import dsqp.db_club_dict.model.DictLevelUpModel;
import org.springframework.stereotype.Service;

/**
 * Created by jeremy on 2017/8/16.
 */
@Service
public class LevelUpServiceImpl implements LevelUpService {

    @OperationServiceLog(menuItem = "operatingConfig",menuName = "运营配置",recordType =31,typeName = "升降级条件修改")
    public int add(DictLevelUpModel model) {
        return DictLevelUpDao.add(model);
    }

    @OperationServiceLog(menuItem = "operatingConfig",menuName = "运营配置",recordType =31,typeName = "升降级条件修改")
    public int update(DictLevelUpModel model) {
        return DictLevelUpDao.update(model);
    }
}
