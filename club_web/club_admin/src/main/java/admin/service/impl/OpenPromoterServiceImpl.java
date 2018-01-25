package admin.service.impl;

import admin.operation.OperationServiceLog;
import admin.service.OpenPromoterService;
import admin.vo.NewAgentModel;
import dsqp.db_club.dao.ClubUserDao;
import dsqp.db_club.dao.PromoterDao;
import dsqp.db_club.model.ClubUserModel;
import org.springframework.stereotype.Service;

/**
 * Created by jeremy on 2017/8/21.
 */
@Service
public class OpenPromoterServiceImpl implements OpenPromoterService {

    @Override
    public int addOne(ClubUserModel model) {
        return ClubUserDao.addOne(model);
    }

    @Override
    //开通代理
    @OperationServiceLog(menuItem = "preSaleManage", menuName = "售前管理", recordType = 11, typeName = "开通代理")
    public long newAgent(NewAgentModel agent) {
        return PromoterDao.newAgent(agent.getGameId(), agent.getRealName(), agent.getCellphone(), agent.getLoginPassword(), agent.getParentId(), agent.getNickName(),
                agent.getpLevel(), agent.getGameUserId(), agent.getLoginStatus(), agent.getClubId(), agent.getClubName(), agent.getExpireDay(), agent.getClassName(), agent.getEditAdmin(), agent.getEditAdminId(),agent.getClubStatus());
    }

}
