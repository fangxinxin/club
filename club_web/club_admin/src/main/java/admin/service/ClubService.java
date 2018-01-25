package admin.service;

import dsqp.db_club_dict.model.DictClubModel;

/**
 * Created by jeremy on 2017/9/27.
 */
public interface ClubService {
    int add(DictClubModel model);

    int update(DictClubModel model);
}
