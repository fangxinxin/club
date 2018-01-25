package admin.service;

import db_admin.model.SysUserModel;
import dsqp.db_club.model.ClubModel;
import dsqp.db_club.model.PromoterModel;

/**
 * Created by jeremy on 2017/8/22.
 */
public interface FlushService {

//    public int removeByClubId(long clubId, ClubModel model);

    boolean dissolveClubByClubId(ClubModel club, PromoterModel promoter, SysUserModel admin);

}
