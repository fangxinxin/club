package admin.service.impl;

import admin.operation.OperationServiceLog;
import admin.service.ConvertService;
import dsqp.db_club_dict.dao.DictFormalDao;
import dsqp.db_club_dict.model.DictFormalModel;
import org.springframework.stereotype.Service;

/**
 * Created by jeremy on 2017/8/16.
 */
@Service
public class ConvertServiceImpl implements ConvertService {

    @OperationServiceLog(menuItem = "operatingConfig",menuName = "运营配置",recordType =34,typeName = "转正条件修改")
    public int add(DictFormalModel model) {
        return DictFormalDao.add(model);
}

    @OperationServiceLog(menuItem = "operatingConfig",menuName = "运营配置",recordType =34,typeName = "转正条件修改")
    public int update(DictFormalModel model) {
        return DictFormalDao.update(model);
    }
}
