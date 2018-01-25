package dsqp.db_club.dao;

import dsqp.db.model.DataTable;
import dsqp.db.model.SplitPage;
import dsqp.db_club.impl.ClubUserDaoImpl;
import dsqp.db_club.model.ClubUserModel;

import java.util.List;

/**
 * Created by ds on 2017/7/17.
 */
public class ClubUserDao {
    private static final ClubUserDaoImpl impl = new ClubUserDaoImpl();

    public static int addOne(ClubUserModel model) {
        return impl.add(model);
    }

    public static int update(long clubUserId, ClubUserModel model) {
        return impl.update(model);
    }

    public static ClubUserModel getOne(long id) {
        return impl.getOne(id);
    }

    public static ClubUserModel getByGameIdAndGameUserId(int gameId, long gameUserId) {
        return impl.getByGameIdAndGameUserId(gameId, gameUserId);
    }

    public static ClubUserModel getByGameIdGameUserIdAndClubId(int gameId, long gameUserId, long clubId) {
        return impl.getByGameIdGameUserIdAndClubId(gameId, gameUserId, clubId);
    }

    public static ClubUserModel getClubUserInfo(long clubId, long gameUserId){
        return impl.getClubUserInfo(clubId, gameUserId);
    }

    public static DataTable getByClubIdAndUserId(long clubId, long userId) {
        return impl.getByClubIdAndUserId(clubId, userId);
    }

    public static List<Long> listUserId(long clubId) {
        return impl.listUserId(clubId);
    }

    public static List<Long> listClubId(int gameId, long gameUserId) {
        return impl.listClubId(gameId, gameUserId);}

    public static DataTable listByClubId(long clubId) {
        return impl.listByClubId(clubId);
    }

    public static DataTable listByNickname(int gameId, String nickname) {
        return impl.listByNickname(gameId, nickname);
    }

    public static DataTable getList() {
        return impl.getList();
    }

    public static int getUserNum(long promoterId) {
        return impl.getUserNum(promoterId);
    }

    public static SplitPage getPage(long clubId, int pageNum, int pageSize) {
        return impl.getPage(clubId, pageNum, pageSize);
    }

    //慎用
    public static int unBundleGameUser(long gameId, long gameUserId) {
        return impl.unBundleGameUser(gameId, gameUserId);
    }

    public static int unBundleGameUserByClubIdAndGameUserId(long clubId, long gameUserId) {
        return impl.unBundleGameUserByClubIdAndGameUserId(clubId, gameUserId);
    }

    public static int removeByClubIdAndGameUserId(long clubId, long gameUserId) {
        return impl.removeByClubIdAndGameUserId(clubId, gameUserId);
    }

    //清空俱乐部成员
    public static int removeByClubId(long clubId) {
        return impl.removeByClubId(clubId);
    }

    /**
     * 俱乐部审核到期
     * 1.清空俱乐部
     * 2.获取俱乐部玩家
     */
    public static int removeByClubId(List<Long> clubIds) {
        return impl.removeByClubId(clubIds);
    }

    public static DataTable listClubUserByClubId(List<Long> clubIds) {
        return impl.listClubUserByClubId(clubIds);
    }

    public static DataTable listClubUserByPromoterId(List<Long> promoterIds) {
        return impl.listClubUserByPromoterId(promoterIds);
    }

    public static ClubUserModel queryByGameIdAndGameUserId(int gameId, long gameUserId) {
        return impl.queryByGameIdAndGameUserId(gameId, gameUserId);
    }

    public static ClubUserModel queryByClubIdAndGameUserId(long clubId, long gameUserId) {
        return impl.queryByClubIdAndGameUserId(clubId, gameUserId);
    }

    public static DataTable getListByClubId(long clubId) {
        return impl.getListByClubId(clubId);
    }

    public static DataTable getListByClubIdIncludePromoter(long clubId) {
        return impl.getListByClubIdIncludePromoter(clubId);
    }

    public static DataTable getClubUserInfo(long promoterId) {
        return impl.getClubUserInfo(promoterId);
    }

    public static DataTable getDtByGameIdAndGameUserId(int gameId, long gameUserId) {
        return impl.getDtByGameIdAndGameUserId(gameId, gameUserId);
    }

    public static int getJoinClubNums(int gameId,long gameUserId){
        return impl.getJoinClubNums(gameId, gameUserId);
    }


    //获取俱乐部成员信息
    public static DataTable listMembers(long clubId, List<Long> userIds) {
        return impl.listMembers(clubId, userIds);
    }

}
