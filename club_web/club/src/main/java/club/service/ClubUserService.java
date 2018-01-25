package club.service;

import club.vo.MsgVO;
import dsqp.db.model.DataTable;
import dsqp.db.model.SplitPage;

/**
* Created by ds on 2017/6/22.
*/
public interface ClubUserService extends BaseService {
    DataTable getByClubIdAndUserId(long clubId, long userId, int gameId);

    SplitPage listClubInfo(long clubId, int gameId, int pageNum, int pageSize);

    MsgVO removeClubUser(long clubId, long gameUserId);

}
