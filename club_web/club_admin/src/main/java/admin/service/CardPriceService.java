package admin.service;

import dsqp.db_club_dict.model.DictGoodPriceModel;

/**
 * Created by jeremy on 2017/8/16.
 */
public interface CardPriceService {

    int add(DictGoodPriceModel model);

    int update(DictGoodPriceModel model);

    int deleteById(int id,DictGoodPriceModel model);
}
