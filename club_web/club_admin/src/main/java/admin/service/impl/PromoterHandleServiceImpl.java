package admin.service.impl;

import admin.operation.OperationServiceLog;
import admin.service.PromoterHandleService;
import dsqp.db_club.dao.ClubDao;
import dsqp.db_club.dao.PromoterDao;
import dsqp.db_club_dict.model.DictGameDbModel;
import dsqp.db_game.dao.dev.UUserPointDao;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

/**
 * Created by ds on 2017/7/30.
 */
@Service
public class PromoterHandleServiceImpl implements PromoterHandleService {

    @Override
    //赠送、删除钻石
    @OperationServiceLog(menuItem = "afterSaleManage", menuName = "售后管理", recordType = 12, typeName = "赠送/删除钻石")
    public int updateGameCardById(int gameCard, long promoterId) {
        return PromoterDao.updateGameCardById(gameCard, promoterId);
    }

    @Override
    //赠送俱乐部专属房卡
    @OperationServiceLog(menuItem = "afterSaleManage", menuName = "售后管理", recordType = 191, typeName = "赠送/删除俱乐部专属钻石")
    public int updateClubCard(int gameCard, long promoterId, int type) {
        return ClubDao.updateClubCard(promoterId, gameCard);
    }

    @Override
    //补发钻石
    @OperationServiceLog(menuItem = "afterSaleManage", menuName = "售后管理", recordType = 14, typeName = "补发钻石")
    public int updateGameCardByUserId(DictGameDbModel dictDb, int gameCard, long userId) {
        return UUserPointDao.updateGameCardByUserId(dictDb, gameCard, userId);
    }

    @Override
    @OperationServiceLog(menuItem = "afterSaleManage", menuName = "售后管理", recordType = 36, typeName = "修改代理商信息")
    public int updateClubName(long clubId, String clubName, int type) {
        return ClubDao.updateClubName(clubName, clubId);
    }

    @Override
    @OperationServiceLog(menuItem = "afterSaleManage", menuName = "售后管理", recordType = 36, typeName = "修改代理商信息")
    public int updateCellPhone(long cellphone, long newCellphone, int type) {
        return PromoterDao.updateCellPhone(newCellphone, cellphone);
    }

    @Override
    @OperationServiceLog(menuItem = "afterSaleManage", menuName = "售后管理", recordType = 36, typeName = "修改代理商信息")
    public int updateRealName(long cellphone, String realName, int type) {
        return PromoterDao.updateRealName(realName, cellphone);
    }

    @Override
    @OperationServiceLog(menuItem = "afterSaleManage", menuName = "售后管理", recordType = 36, typeName = "修改代理商信息")
    public int updatePass(long cellphone, String password, int type) {
        return PromoterDao.updatePass(cellphone, DigestUtils.md5Hex(password));
    }

    @Override
    @OperationServiceLog(menuItem = "afterSaleManage", menuName = "售后管理", recordType = 36, typeName = "修改代理商信息")
    public int updateWechat(long promoterId, String staffWechat, int type) {
        return PromoterDao.updateStaffWechat(promoterId, staffWechat);
    }

    @Override
    @OperationServiceLog(menuItem = "afterSaleManage", menuName = "售后管理", recordType = 36, typeName = "修改代理商信息")
    public int updateRemark(long promoterId, String remark, int type) {
        return PromoterDao.updateRemark(promoterId, remark);
    }
}
