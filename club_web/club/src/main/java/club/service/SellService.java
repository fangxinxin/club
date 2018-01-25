package club.service;

import club.vo.MsgVO;

/**
 * 推广员给群员卖钻石
 * Created by Aris on 2017/7/25.
 */
public interface SellService {

    /**
     * 销售本俱乐部玩家
     * @param clubId
     * @param promoterId
     * @param sellNum
     * @param gameUserId
     * @return
     */
    MsgVO sell(long clubId, long promoterId, int sellNum, long gameUserId);

    /**
     * 可销售全部玩家
     * @param promoterId
     * @param sellNum
     * @param gameUserId
     * @return
     */
    MsgVO sellAll(long promoterId, int sellNum, long gameUserId);

}
