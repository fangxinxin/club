package club.service.impl;

import club.service.BonusService;
import dsqp.common_const.club.CommonConfig;
import dsqp.common_const.club.PromoterLevel;
import dsqp.db_club.dao.DayBonusDetailDao;
import dsqp.db_club.dao.PromoterDao;
import dsqp.db_club.model.DayBonusDetailModel;
import dsqp.db_club.model.PromoterModel;
import dsqp.db_club.model.PromoterPayModel;
import dsqp.db_club_dict.dao.DictBonusDao;
import dsqp.db_club_dict.model.DictBonusModel;
import org.springframework.stereotype.Service;

/**
 * Created by Aris on 2017/8/11.
 */
@Service
public class BonusServiceImpl implements BonusService {

    public void addBonus(PromoterModel own, PromoterPayModel pay){
        DictBonusModel dictBonus = DictBonusDao.getByGameId(pay.getGameId());
        //如果没有配置相关信息给0.2还有0.1的
        double directPercent    = dictBonus == null ? CommonConfig.DIRECT_PERCENT : dictBonus.getDirectPercent();
        double nonDirectPercent = dictBonus == null ? CommonConfig.NON_DIRECT_PERCENT : dictBonus.getNonDirectPercent();
        //没有上级，直接计算这里
        if (own.getParentId() == 0) {
            DayBonusDetailModel  model = new DayBonusDetailModel();
            model.setBonusId(0);
            model.setPayId(pay.getId());
            model.setGameId(pay.getGameId());
            model.setPromoterId(pay.getPromoterId());
            model.setpLevel(pay.getpLevel());
            model.setPay(pay.getPrice());
            model.setParentId(0);
            model.setParentLevel(0);
            model.setParentBonus(0);
            model.setNonParentId(0);
            model.setNonParentLevel(0);
            model.setNonParentBonus(0);
            model.setPayCreateTime(pay.getCreateTime());
            model.setPayCreateDate(pay.getCreateTime());
            DayBonusDetailDao.add(model);
            return;
        }

        PromoterModel parent = PromoterDao.getOne(own.getParentId());
        if (parent == null)    return;

        //二级代理不能领
        if (parent.getpLevel() == PromoterLevel.SECOND){
            DayBonusDetailModel  model = new DayBonusDetailModel();
            model.setBonusId(0);
            model.setPayId(pay.getId());
            model.setGameId(pay.getGameId());
            model.setPromoterId(pay.getPromoterId());
            model.setpLevel(pay.getpLevel());
            model.setPay(pay.getPrice());
            model.setParentId(parent.getId());
            model.setParentLevel(parent.getpLevel());
            model.setParentBonus(0);
            model.setNonParentId(0);
            model.setNonParentLevel(0);
            model.setNonParentBonus(0);
            model.setPayCreateTime(pay.getCreateTime());
            model.setPayCreateDate(pay.getCreateTime());
            DayBonusDetailDao.add(model);
            return;
        }

        //算出直属有多少返利
        double directBonus = pay.getPrice() * directPercent;
        //添加返利信息
        DayBonusDetailModel  model = new DayBonusDetailModel();
        model.setBonusId(0);
        model.setPayId(pay.getId());
        model.setGameId(pay.getGameId());
        model.setPromoterId(pay.getPromoterId());
        model.setpLevel(pay.getpLevel());
        model.setPay(pay.getPrice());
        model.setParentId(pay.getParentId());
        model.setParentLevel(parent.getpLevel());
        model.setParentBonus(directBonus);
        model.setPayCreateTime(pay.getCreateTime());
        model.setPayCreateDate(pay.getCreateTime());
        //这里先给默认值
        model.setNonParentId(0);
        model.setNonParentLevel(0);
        model.setNonParentBonus(0);
        if (parent.getParentId() != 0){
            PromoterModel nonParent = PromoterDao.getOne(parent.getParentId());
            if (nonParent != null){
                if (nonParent.getpLevel() == PromoterLevel.SUPER){
                    model.setNonParentId(nonParent.getId());
                    model.setNonParentLevel(nonParent.getpLevel());
                    model.setNonParentBonus(pay.getPrice() * nonDirectPercent);
                } else{
                    model.setNonParentId(nonParent.getId());
                    model.setNonParentLevel(nonParent.getpLevel());
                    model.setNonParentBonus(0);
                }
            }
        }

        DayBonusDetailDao.add(model);
    }
}
