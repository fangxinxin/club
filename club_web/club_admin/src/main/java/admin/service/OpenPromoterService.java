package admin.service;

import admin.vo.NewAgentModel;
import dsqp.db_club.model.ClubUserModel;

/**
 * Created by jeremy on 2017/8/21.
 */
public interface OpenPromoterService {

    //在clubUser中加入推广员自己
    int addOne(ClubUserModel model);

    //开通代理商
    long newAgent(NewAgentModel agent);
}
