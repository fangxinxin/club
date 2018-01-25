package admin.service;

/**
 * Created by jeremy on 2017/8/16.
 */
public interface UnbundleService {
    int unBundleGameUserByClubIdAndGameUserId(long clubId,long gameUserId,String gameNickName);
}
