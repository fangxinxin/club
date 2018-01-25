package club.service.impl;

import club.service.PromoterService;
import club.vo.MsgVO;
import dsqp.common_const.club.GamecardSource;
import dsqp.common_const.club.PromoterLevel;
import dsqp.db.model.DataTable;
import dsqp.db.model.SplitPage;
import dsqp.db_club.dao.PromoterDao;
import dsqp.db_club.model.PromoterModel;
import dsqp.db_club_log.dao.LogGamecardDao;
import dsqp.db_club_log.dao.LogRebateGetDao;
import dsqp.db_club_log.model.LogGamecardModel;
import dsqp.db_club_log.model.LogRebateGetModel;
import dsqp.util.CommonUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by ds on 2017/6/22.
 */

@Service
public class PromoterServiceImpl implements PromoterService {
    @Override
    public DataTable getByParentIdAndId(long parentId, long promoterId, int pLevel) {
        if (pLevel == PromoterLevel.SUPER) {
            DataTable dt1 = PromoterDao.getNonDirect(parentId, promoterId);

            return dt1.rows.length > 0 ? dt1 : PromoterDao.getDirect(parentId, promoterId);
        } else {
            return PromoterDao.getDirect(parentId, promoterId);
        }
    }

    @Override
    public SplitPage listByParentId(long parentId, int pageNum, int pageSize, int pLevel) {
        if (pLevel == PromoterLevel.SUPER) {
            return PromoterDao.getPageAllDirect(parentId, pageNum, pageSize);
        } else {
            return PromoterDao.getPageDirect(parentId, pageNum, pageSize);
        }
    }

    @Override
    public MsgVO returnDiamond(long promoterId) {
        MsgVO vo = new MsgVO();
        String msg = "提取返钻失败，请联系客服";

        PromoterModel p = PromoterDao.getOne(promoterId);
        if (p != null && p.getRebate() > 0) {
            int result = PromoterDao.updateGameCardByIdFromRebate(promoterId);
            if (result > 0) {
                Date now = new Date();
                int gameId = p.getGameId();
                int changeNum = CommonUtils.getIntValue(p.getRebate());
                msg = "提取成功";
                vo.setSuccess(true);

                //记录钻石日志
                LogGamecardModel log = new LogGamecardModel();
                log.setGameId(gameId);
                log.setPromoterId(promoterId);
                log.setNickName(p.getNickName());
                log.setSource(GamecardSource.REBATE_GET.getType());
                log.setChangeNum(changeNum);
                log.setChangeBefore(p.getGameCard());
                log.setChangeAfter(p.getGameCard() + changeNum);
                log.setCreateTime(now);
                log.setCreateDate(now);
                LogGamecardDao.add(log);

                //记录提取返钻日志
                LogRebateGetModel logRebateGetModel = new LogRebateGetModel();
                logRebateGetModel.setGameId(gameId);
                logRebateGetModel.setPromoterId(promoterId);
                logRebateGetModel.setGetDiamond(changeNum);
                logRebateGetModel.setCreateTime(now);
                logRebateGetModel.setCreateDate(now);
                LogRebateGetDao.add(logRebateGetModel);
            }
        }
        vo.setRemark(msg);

        return vo;
    }

}

