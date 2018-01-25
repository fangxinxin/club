package admin.service.impl;

import admin.operation.OperationServiceLog;
import admin.service.CardPriceService;
import dsqp.db_club_dict.dao.DictGoodPriceDao;
import dsqp.db_club_dict.model.DictGoodPriceModel;
import org.springframework.stereotype.Service;

/**
 * Created by jeremy on 2017/8/16.
 */
@Service
public class CardPriceServiceImpl implements CardPriceService {

    @OperationServiceLog(menuItem = "operatingConfig",menuName = "运营配置",recordType =32,typeName = "钻石售价调整")
    public int add(DictGoodPriceModel model) {
        return DictGoodPriceDao.add(model);
    }

    @OperationServiceLog(menuItem = "operatingConfig",menuName = "运营配置",recordType =32,typeName = "钻石售价调整")
    public int update(DictGoodPriceModel model) {
        return DictGoodPriceDao.update(model);
    }

    @OperationServiceLog(menuItem = "operatingConfig",menuName = "运营配置",recordType =32,typeName = "钻石售价调整")
    public int deleteById(int id,DictGoodPriceModel model) {
        return DictGoodPriceDao.deleteById(id);
    }
}
