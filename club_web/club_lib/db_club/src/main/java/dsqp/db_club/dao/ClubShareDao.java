package dsqp.db_club.dao;

import dsqp.db_club.impl.ClubShareDaoImpl;
import dsqp.db_club.model.ClubShareModel;

import java.util.List;

/**
 * Created by ds on 2017/7/17.
 */
public class ClubShareDao {

    private static final ClubShareDaoImpl impl = new ClubShareDaoImpl();

    public static int add(ClubShareModel model) {
        return impl.add(model);
    }

    public static ClubShareModel getByUnionid(String unionId, int gameId) {
        return impl.getByUnionid(unionId, gameId);
    }

    public static ClubShareModel getByUnionidAndClubId(String unionId, int gameId, long clubId) {
        return impl.getByUnionidAndClubId(unionId, gameId, clubId);
    }

    public static ClubShareModel getByUnionidAndStatus(String unionId, int gameId, long clubId, int joinStatus) {
        return impl.getByUnionidAndStatus(unionId, gameId, clubId, joinStatus);
    }

    public static int updateByJoinStatus(int joinStatus, long id) {
        return impl.updateByJoinStatus(joinStatus, id);
    }

    public static int removeByClubId(long clubId) {
        return impl.removeByClubId(clubId);
    }

    /** 群主将玩家踢出俱乐部 **/
    public static int removeByClubIdAndGameUserId(long clubId, long gameUserId) {
        return impl.removeByClubIdAndGameUserId(clubId, gameUserId);
    }

    public static int removeByClubId(List<Long> clubIds) {
        return impl.removeByClubId(clubIds);
    }

}
