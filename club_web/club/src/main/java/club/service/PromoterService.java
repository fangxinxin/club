package club.service;

import club.vo.MsgVO;
import dsqp.db.model.DataTable;
import dsqp.db.model.SplitPage;

/**
 * Created by ds on 2017/6/22.
 */
public interface PromoterService extends BaseService {
    DataTable getByParentIdAndId(long parentId, long promoterId, int pLevel);

    SplitPage listByParentId(long parentId, int pageNum, int pageSize, int pLevel);

    MsgVO returnDiamond(long promoterId);

}
