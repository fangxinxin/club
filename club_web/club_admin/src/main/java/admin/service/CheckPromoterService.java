package admin.service;

import dsqp.db.model.DataTable;
import dsqp.db_club_dict.model.DictGameDbModel;

import java.util.Date;

/**
 * Created by jeremy on 2017/8/21.
 */
public interface CheckPromoterService {
    int updateExpireTime(long promoterId,Date expireTime,int addTime);

    /**
     * 俱乐部开局数
     * @param dt
     * @param dbModel
     * @return
     */
    int getTotalPlayGameNum(DataTable dt, DictGameDbModel dbModel);

    DataTable listCheckPromoter(int gameId,Date startTime,Date endTime);

    /**
     * 获取新玩家列表
     * @param clubId
     * @param memberType
     * @return
     */
    DataTable listNewMember(long clubId, int memberType);

    boolean becomeFormal(long promoterId);

    boolean refuseCheckPromoter(int gameId, long clubId);

}
