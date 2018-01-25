
package dsqp.db_club.dao;


import dsqp.db.model.DataTable;
import dsqp.db.model.SplitPage;
import dsqp.db_club.impl.PromoterDaoImpl;
import dsqp.db_club.model.PromoterModel;

import java.util.Date;
import java.util.List;

/**
 * Created by ds on 2017/6/21.
 */
public class PromoterDao {

    private static final PromoterDaoImpl impl = new PromoterDaoImpl();

    public static int updateStaffWechat(long id, String staffWechat) {
        return impl.updateStaffWechat(id, staffWechat);
    }

    public static int updateRemark(long id, String remark) {
        return impl.updateRemark(id, remark);
    }

    public static int updateClubId(long id, long clubId) {
        return impl.updateClubId(id, clubId);
    }

    public static int updateAccount(long bankAccount, long id) {
        return impl.updateAccount(bankAccount, id);
    }

    public static int updateRebateForPay(long id, int gameCard, int rebate, double price) {
        return impl.updateRebateForPay(id, gameCard, rebate, price);
    }

    public static int updateDeposit(double deposit, long id) {
        return impl.updateDeposit(deposit, id);
    }

    public static int updateAccountAndRealName(long bankAccount, String realName, long id) {
        return impl.updateAccountAndRealName(bankAccount, realName, id);
    }

    public static DataTable queryByGameIdAndCellPhone(int gameId, long cellPhone) {
        return impl.queryByGameIdAndCellPhone(gameId, cellPhone);
    }

    public static int update(PromoterModel model) {
        return impl.update(model);
    }

    public static PromoterModel getOne(long id) {
        return impl.getOne(id);
    }

    public static PromoterModel getByClubId(long clubId) {
        return impl.getByClubId(clubId);
    }

    public static DataTable getOne2(long id) {
        return impl.getOne2(id);
    }

    public static DataTable getList() {
        return impl.getList();
    }

    public static DataTable queryByCellPhone(long cellPhone) {
        return impl.queryByCellPhone(cellPhone);
    }

    public static int updatePass(long cellPhone, String pass) {
        return impl.updatePass(cellPhone, pass);
    }

    public int updatePassById(long promoterId, String pass) {
        return impl.updatePassById(promoterId, pass);
    }

    public static DataTable listByParentId(long parentId) {
        return impl.listByParentId(parentId);
    }

    public static int updateLoginStatus(long cellPhone, int loginStatus) {
        return impl.updateLoginStatus(cellPhone, loginStatus);
    }

    public static int updateLoginStatusById(long promoterId, int loginStatus) {
        return impl.updateLoginStatusById(promoterId, loginStatus);
    }

    public static int updateLoginStatus2More(List<Long> promoterIds, int loginStatus) {
        return impl.updateLoginStatus2More(promoterIds, loginStatus);
    }

    public static DataTable getDirectNumsByParentId(long parentId) {
        return impl.getDirectNumsByParentId(parentId);
    }

    public static int updateLevelById(long promoterId, int pLevel) {
        return impl.updateLevelById(promoterId, pLevel);
    }


    /**
     * 代理管理 ::
     * 1.查询下级代理 :: 一级：获取直属、特级：获取直接下级和间接下级
     * 2.分页 :: 一级：获取直属、特级：获取直接下级和间接下级
     */
    public static DataTable getDirect(long parentId, long promoterId) {
        return impl.getDirect(parentId, promoterId);
    }

    public static DataTable getNonDirect(long parentId, long promoterId) {
        return impl.getNonDirect(parentId, promoterId);
    }

    public static SplitPage getPageDirect(long parentId, int pageNum, int pageSize) {
        return impl.getPageDirect(parentId, pageNum, pageSize);
    }

    public static SplitPage getPageAllDirect(long parentId, int pageNum, int pageSize) {
        return impl.getPageAllDirect(parentId, pageNum, pageSize);
    }

    public static long newAgent(int gameId, String realName, long cellPhone, String loginPassword, long parentId
            , String nickName, int pLevel, long gameUserId, int loginStatus, long clubId, String clubName, double expireDay, String mpClassName, String editAdmin, long editAdminId, int clubStatus) {
        return impl.newAgent(gameId, realName, cellPhone, loginPassword, parentId, nickName, pLevel, gameUserId, loginStatus, clubId, clubName, expireDay, mpClassName, editAdmin, editAdminId, clubStatus);
    }

    public static long reopen(int gameId, long promoterId, String nickName, long gameUserId, long clubId, String clubName, String mpClassName, int expireDay) {
        return impl.reopen(gameId, promoterId, nickName, gameUserId, clubId, clubName, mpClassName, expireDay);
    }

    public static int updateGameCardById(int gameCard, long promoterId) {
        return impl.updateGameCardById(gameCard, promoterId);
    }

    public static int updateGameCardByIdFromRebate(long id) {
        return impl.updateGameCardByIdFromRebate(id);
    }

    public static PromoterModel queryByNickName(String nickName) {
        return impl.queryByNickName(nickName);
    }

    public static List<Long> getPromoterIdListByGameId(int gameId) {
        return impl.getPromoterIdListByGameId(gameId);
    }

    public static List<Long> getSuperPromoterIdList() {
        return impl.getSuperPromoterIdList();
    }

    public static PromoterModel queryByIdAndGamId(long id, int gameId) {
        return impl.queryByIdAndGamId(id, gameId);
    }

    public static DataTable queryByGameIdAndUserId(int gameId, long gameUserId, boolean isEnable) {
        return impl.queryByGameIdAndUserId(gameId, gameUserId, isEnable);
    }

    //字段不全
    public static DataTable getListByPromoter(List<Long> promoterIds) {
        return impl.getListByPromoter(promoterIds);
    }

    public static DataTable listByPromoterIds(List<Long> promoterIds) {
        return impl.listByPromoterIds(promoterIds);
    }

    public static List<Long> listPromoterIdByGameId(int gameId) {
        return impl.listPromoterIdByGameId(gameId);
    }

    //查询手下直属和非直属代理商
    public static DataTable getAllUnder(long promoterId) {
        return impl.getAllUnder(promoterId);
    }

    //查询手下直属代理商
    public static DataTable getDirectUnder(long promoterId) {
        return impl.getDirectUnder(promoterId);
    }

    //获取下级代理人数
    public static int getDirectNum(long parentId) {
        return impl.getDirectNum(parentId);
    }

    //查询每日代理商人数
    public static DataTable getPeopleNum(int gameId, Date endDate) {
        return impl.getPeopleNum(gameId, endDate);
    }

    /**
     * 更新银行卡号 真实姓名 身份证号 开户行
     */
    public static int updateAccountAndBankArea(long bankAccount, String realName, String IDCard, String bankArea, long id) {
        return impl.updateAccountAndBankArea(bankAccount, realName, IDCard, bankArea, id);
    }

    public static int updateCellPhone(long newCellphone, long cellphone) {
        return impl.updateCellPhone(newCellphone, cellphone);
    }

    public static int updateRealName(String realName, long cellphone) {
        return impl.updateRealName(realName, cellphone);
    }

    //删除代理
    public static int removeByPromoterId(List<Long> promoterIds) {
        return impl.removeByPromoterId(promoterIds);
    }

}
