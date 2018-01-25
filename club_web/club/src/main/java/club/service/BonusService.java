package club.service;

import dsqp.db_club.model.PromoterModel;
import dsqp.db_club.model.PromoterPayModel;

/**
 * Created by Aris on 2017/8/11.
 */
public interface BonusService {
    void addBonus(PromoterModel own, PromoterPayModel pay);
}
