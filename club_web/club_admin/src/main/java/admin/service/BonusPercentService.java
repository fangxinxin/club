package admin.service;

import dsqp.db_club_dict.model.DictBonusModel;

/**
 * Created by jeremy on 2017/8/18.
 */
public interface BonusPercentService {
    public DictBonusModel getByGameId(int gameId);

    public int add(DictBonusModel model);

    public int update(DictBonusModel model);
}
