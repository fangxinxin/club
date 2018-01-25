package admin.service.impl;

import admin.operation.OperationServiceLog;
import admin.service.BonusPercentService;
import dsqp.db_club_dict.dao.DictBonusDao;
import dsqp.db_club_dict.model.DictBonusModel;
import org.springframework.stereotype.Service;

/**
 * Created by jeremy on 2017/8/18.
 */
@Service
public class BonusPercentServiceImpl implements BonusPercentService {

    public DictBonusModel getByGameId(int gameId) {
        return DictBonusDao.getByGameId(gameId);
    }

    @OperationServiceLog(menuItem = "operatingConfig",menuName = "运营配置",recordType =331,typeName = "返钻比例修改")
    public int add(DictBonusModel model) {
        return DictBonusDao.add(model);
    }

    @OperationServiceLog(menuItem = "operatingConfig",menuName = "运营配置",recordType =331,typeName = "返钻比例修改")
    public int update(DictBonusModel model) {
        return DictBonusDao.update(model);
    }
}
