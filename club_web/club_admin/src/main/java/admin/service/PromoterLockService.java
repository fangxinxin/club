package admin.service;

import dsqp.db_club.model.PromoterLockModel;

/**
 * Created by ds on 2017/7/30.
 */
public interface PromoterLockService {

    int add(PromoterLockModel model);

    int deleteByPromoterId(long id);



}
