package admin.service;

import dsqp.db_club_dict.model.DictGameDbModel;

/**
 * Created by ds on 2017/7/30.
 */
public interface PromoterHandleService {

    //赠送、删除房卡
    int updateGameCardById(int gameCard, long promoterId);

    //赠送俱乐部专属房卡
    int updateClubCard(int gameCard, long promoterId, int type);

    //补发房卡
    int updateGameCardByUserId(DictGameDbModel dictDb, int gameCard, long userId);

    int updateClubName(long clubId, String clubName, int type);

    int updateCellPhone(long cellphone, long newCellphone, int type);

    int updateRealName(long cellphone, String realName, int type);

    int updatePass(long cellphone, String password, int type);

    int updateWechat(long promoterId, String staffWechat, int type);

    int updateRemark(long promoterId, String remark, int type);
}
