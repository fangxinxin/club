package dsqp.db_club.dao;

import dsqp.db.model.DataTable;
import dsqp.db_club.impl.ClubDaoImpl;
import dsqp.db_club.model.ClubModel;

import java.util.Date;
import java.util.List;

/**
 * Created by ds on 2017/7/17.
 */
public class ClubDao {
    private static final ClubDaoImpl impl = new ClubDaoImpl();

//    public static int add(ClubModel model) {
//        return impl.add(model);
//    }

    public static int updateShareCard(long clubId, long promoterId, long gameCard, int shareCard) {
        return impl.updateShareCard(clubId, promoterId, gameCard, shareCard);
    }

    public static int updateClubCard(long promoterId, long gameCard) {
        return impl.updateClubCard(promoterId, gameCard);
    }

    public static ClubModel getOne(long clubId) {
        return impl.getOne(clubId);
    }

    public static DataTable getOne2(long clubId) {
        return impl.getOne2(clubId);
    }

    public static DataTable getListByGameIdDateAndClubStatus(int gameId, Date date, int clubStatus) {
        return impl.getListByGameIdDateAndClubStatus(gameId, date, clubStatus);
    }

    public static DataTable listByGameId(int gameId) {
        return impl.listByGameId(gameId);
    }
    public static DataTable listByGameId(int gameId, int clubStatus) {
        return impl.listByGameId(gameId, clubStatus);
    }

//    public static DataTable getByGameIdAndUserId(int gameId, long userId) {
//        return impl.getByGameIdAndUserId(gameId, userId);
//    }

    public static DataTable listPeopleNum(List<Long> promoterIds) {
        return impl.listPeopleNum(promoterIds);
    }

    public static DataTable queryByPromoterId(long promoterId) {
        return impl.queryByPromoterId(promoterId);
    }


    public static ClubModel getByPromoterId(long promoterId) {
        return impl.getByPromoterId(promoterId);
    }

    public static ClubModel queryByClubNameAndGameId(int gameId, String clubName) {
        return impl.queryByClubNameAndGameId(gameId, clubName);
    }

    public static ClubModel queryByGameUserIdAndGamId(long gameUserId, int gameId) {
        return impl.queryByGameUserIdAndGamId(gameUserId, gameId);
    }

    public static int newClub(int gameId, long promoterId, long gameUserId, String clubName, boolean isEnable, Date createDate) {
        return impl.newClub(gameId, promoterId, gameUserId, clubName, isEnable, createDate);
    }

    public static ClubModel queryByIdAndGamId(long id, int gameId) {
        return impl.queryByIdAndGamId(id, gameId);
    }

    public static ClubModel queryByIdAndStatus(long id, int gameId) {
        return impl.queryByIdAndStatus(id, gameId);
    }

//    public static int update(ClubModel model) {
//        return impl.update(model);
//    }

    public static int updatePeopleNum(long id, int peopleNum) {
        return impl.updatePeopleNum(id, peopleNum);
    }

    public static int updateClubStatus(long promoterId, int clubStatus) {
        return impl.updateClubStatus(promoterId, clubStatus);
    }

    public static int updateClubURL(String clubURL, long id) {
        return impl.updateClubURL(clubURL, id);
    }

    //更新代理 :: 待转正时间
    public static int updateExpireTime(long promoterId, Date expireTime) {
        return impl.updateExpireTime(promoterId, expireTime);
    }

    //获取预开代理 :: 俱乐部信息
    public static ClubModel getPreClub(long promoterId, int clubStatus) {
        return impl.getPreClub(promoterId, clubStatus);
    }


    //代理审核 :: 待转正列表（Param:游戏ID、转正时间在：当前时间节点上下48小时）
    public static DataTable listByGameIdAndExpireTime(int gameId, Date startTime, Date endTime, int clubStatus) {
        return impl.listByGameIdAndExpireTime(gameId, startTime, endTime, clubStatus);
    }

    /**
     * 代理审核    task
     */
    //获取待转正俱乐部
    public static DataTable getPreFormal(int gameId, int clubStatus) {
        return impl.getPreFormal(gameId, clubStatus);
    }

    //获取到期俱乐部
    public static DataTable getExpireList(int gameId, int clubStatus) {
        return impl.getExpireList(gameId, clubStatus);
    }

    //延长代理时间
    public static int updateClubStatus2More(List<Long> promoterIds, int clubStatus) {
        return impl.updateClubStatus2More(promoterIds, clubStatus);
    }

    //更新俱乐部人数
    public static int updateClubsPeopleNum(List<Long> promoterIds, int peopleNum) {
        return impl.updateClubsPeopleNum(promoterIds, peopleNum);
    }

    //存储过程 :: 待转正俱乐部到期 :: 移除俱乐部相关信息
    public static int removeByClubId(List<Long> clubIds) {
        return impl.removeByClubId(clubIds);
    }

    //存储过程 :: 解散俱乐部(1.删除club、club_user、club_share 中相关信息2.删除 promoter 代理信息)
    public static boolean dissolveClubByClubId(long clubId) {
        return impl.dissolveClubByClubId(clubId);
    }

    //取消待审核俱乐
    public static boolean refuseClub(long clubId) {
        return impl.refuseClub(clubId);
    }

    //获取俱乐部人数
    public static DataTable getClubPeopleNumByClubId(long clubId) {
        return impl.getClubPeopleNumByClubId(clubId);
    }

    public static DataTable listClubInfo(List<Long> promoterIds) {
        return impl.listClubInfo(promoterIds);
    }

    public static DataTable listClubInfoById(List<Long> clubIds) {
        return impl.listClubInfoById(clubIds);
    }

    //俱乐部人数减一
    public static int decreaseClubNum(long clubId) {
        return impl.decreaseClubNum(clubId);
    }

    //俱乐部人数加一
    public static int increaseClubNum(long clubId) {
        return impl.increaseClubNum(clubId);
    }

    //修改俱乐部昵称
    public static int updateClubName(String newClubName, long clubId) {
        return impl.updateClubName(newClubName, clubId);
    }


}
