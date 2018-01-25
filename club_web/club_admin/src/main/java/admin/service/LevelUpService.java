package admin.service;

import dsqp.db_club_dict.model.DictLevelUpModel;

/**
 * Created by jeremy on 2017/8/16.
 */
public interface LevelUpService {

    int add(DictLevelUpModel model);

    int update(DictLevelUpModel model);
}
