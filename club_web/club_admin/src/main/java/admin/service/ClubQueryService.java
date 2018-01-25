package admin.service;

import admin.vo.MemberDetailVO;
import admin.vo.MemberDiamondsVO;
import dsqp.db.model.DataTable;

import java.util.Date;
import java.util.List;

/**
 * Created by jeremy on 2017/9/20.
 */
public interface ClubQueryService {
    List<MemberDetailVO> getMemberDetailList(int gameId,long promoterId);

    List<MemberDiamondsVO> getMemberDiamondsList(int gameId, long promoterId, long userId, Date createTime);

    DataTable getRelationDetail(long promoterId);

    DataTable getRebateDetail(long promoterId,Date startDate,Date endDate);

}
