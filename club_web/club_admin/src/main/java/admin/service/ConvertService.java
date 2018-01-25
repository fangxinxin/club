package admin.service;

import dsqp.db_club_dict.model.DictFormalModel;

/**
 * Created by jeremy on 2017/8/16.
 */
public interface ConvertService {
    int add(DictFormalModel model);

    int update(DictFormalModel model);
}
