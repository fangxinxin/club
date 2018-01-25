package admin.service.impl;

import admin.operation.OperationServiceLog;
import admin.service.UnbundleService;
import dsqp.db_club.dao.ClubUserDao;
import org.springframework.stereotype.Service;

/**
 * Created by jeremy on 2017/8/16.
 */
@Service
public class UnbundleServiceImpl implements UnbundleService {
    @OperationServiceLog(menuItem = "afterSaleManage",menuName = "售后管理",recordType =21,typeName = "玩家解绑")
    public int unBundleGameUserByClubIdAndGameUserId(long clubId, long gameUserId,String gameNickName) {
        return ClubUserDao.unBundleGameUserByClubIdAndGameUserId(clubId,gameUserId);
    }
}
