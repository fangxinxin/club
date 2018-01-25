package admin.service.impl;

import admin.service.ClubQueryService;
import admin.vo.MemberDetailVO;
import admin.vo.MemberDiamondsVO;
import dsqp.db.model.DataRow;
import dsqp.db.model.DataTable;
import dsqp.db.util.DateUtils;
import dsqp.db_club.dao.*;
import dsqp.db_club.model.ClubModel;
import dsqp.db_club.model.PromoterModel;
import dsqp.db_club_dict.dao.DictGameDbDao;
import dsqp.db_club_dict.model.DictGameDbModel;
import dsqp.db_game.dao.dev.UUserPointDao;
import dsqp.db_game.dao.log_dev.UPropsLogDao;
import dsqp.db_game.dao.log_dev.UPyjUserRecordDao;
import dsqp.util.CommonUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by jeremy on 2017/9/20.
 */
@Service
public class ClubQueryServiceImpl implements ClubQueryService {

    @Override
    public List<MemberDetailVO> getMemberDetailList(int gameId, long promoterId) {
        DataTable dtClubUserInfo = ClubUserDao.getClubUserInfo(promoterId);
        DictGameDbModel dictDb = DictGameDbDao.getByGameId(gameId, true);
        List<MemberDetailVO> list = new ArrayList<MemberDetailVO>();
        if(dtClubUserInfo.rows.length>0){
            for(DataRow row : dtClubUserInfo.rows){
                long userId = CommonUtils.getLongValue(row.getColumnValue("gameUserId")) ;
                String userNickName = row.getColumnValue("gameNickName");
                Date joinClubTime = DateUtils.String2DateTime(row.getColumnValue("createTime"));

                DataTable dtUserInfo = dsqp.db_game.dao.dev.UUserInfoDao.getByUserId(dictDb, userId);
                Date createTime = null;
                if(dtUserInfo.rows.length>0){
                    createTime = dsqp.util.DateUtils.String2Date(dtUserInfo.rows[0].getColumnValue("createTime"), "yyyy-MM-dd HH:mm:ss");
                }

                DataTable dtDiamonds = UUserPointDao.getPointByUserId(dictDb, userId);//查询剩余钻石
                int remainDiaNums = 0;
                if (dtDiamonds.rows.length > 0) {
                    remainDiaNums = Integer.parseInt(dtDiamonds.rows[0].getColumnValue("privateRoomDiamond"));
                }

                int partiGameNums = UPyjUserRecordDao.getPyjCreateNums(dictDb, userId, joinClubTime);//查询玩家对局数

                DataTable dtGameCardConsus = UPropsLogDao.getGameCardConsus(dictDb, userId, joinClubTime);//查询玩家消耗钻石数
                int consumeDiamNums = 0;
                if (dtGameCardConsus.rows.length > 0) {
                    consumeDiamNums = Integer.parseInt(dtGameCardConsus.rows[0].getColumnValue("diamondConsus"));
                }

                DataTable dtDiamondSellNums = PromoterSellDao.getGameDiamondSellByUserId(promoterId, userId);//查询玩家在俱乐部购买钻石总数（被发钻石）
                int giveDiamNums = 0;
                if (dtDiamondSellNums.rows.length > 0) {
                    giveDiamNums = Integer.parseInt(dtDiamondSellNums.rows[0].getColumnValue("sellNums"));
                }

                MemberDetailVO vo = new MemberDetailVO();
                vo.setUserId(userId);
                vo.setUserNickName(userNickName);
                vo.setPartiGameNums(partiGameNums);
                vo.setConsumeDiamNums(consumeDiamNums);
                vo.setGiveDiamNums(giveDiamNums);
                vo.setRemainDiaNums(remainDiaNums);
                vo.setCreateTime(createTime);
                vo.setJoinClubTime(joinClubTime);

                list.add(vo);
            }

        }
        if (list.size() > 0) {
            Collections.sort(list, new Comparator<MemberDetailVO>() {
                @Override
                public int compare(MemberDetailVO o1, MemberDetailVO o2) {
                    return -o1.getJoinClubTime().compareTo(o2.getJoinClubTime());
                }
            });
        }
        return list;
    }

    @Override
    public List<MemberDiamondsVO> getMemberDiamondsList(int gameId, long promoterId, long userId, Date joinClubTime) {
        DictGameDbModel dictDb = DictGameDbDao.getByGameId(gameId, true);
        List<MemberDiamondsVO> list = new ArrayList<MemberDiamondsVO>();

        String userNickName = "";
        String diamondChangeNums = "";
        String changeReason = "";
        int remainDiamondNums = 0;
        Date createTime = null;


        DataTable dtDiamondSellInfo = PromoterSellDao.getGameDiamondSellInfo(promoterId, userId, joinClubTime);//代理销售

        if(dtDiamondSellInfo.rows.length>0){
            for(DataRow row : dtDiamondSellInfo.rows){
                userNickName = row.getColumnValue("gameNickName");
                diamondChangeNums = "+"+row.getColumnValue("sellNum");
                changeReason = "代理销售";
                remainDiamondNums = Integer.parseInt(row.getColumnValue("sellAfter"));
                createTime = dsqp.util.DateUtils.String2Date(row.getColumnValue("createTime"), "yyyy-MM-dd HH:mm:ss");

                MemberDiamondsVO vo = new MemberDiamondsVO();
                vo.setUserId(userId);
                vo.setUserNickName(userNickName);
                vo.setDiamondChangeNums(diamondChangeNums);
                vo.setChangeReason(changeReason);
                vo.setRemainDiamondNums(remainDiamondNums);
                vo.setCreateTime(createTime);

                list.add(vo);
            }
        }


        DataTable dtCardDeduction = UPropsLogDao.getGameCardDeduction(dictDb, gameId, userId, joinClubTime);//开局扣费

        if(dtCardDeduction.rows.length>0){
            for(DataRow row : dtCardDeduction.rows){
                userNickName = row.getColumnValue("nickName");
                diamondChangeNums = "-"+row.getColumnValue("propsNum");
                changeReason = "开局扣费";
                remainDiamondNums = Integer.parseInt(row.getColumnValue("propsAfter"));
                createTime = dsqp.util.DateUtils.String2Date(row.getColumnValue("createTime"), "yyyy-MM-dd HH:mm:ss");

                MemberDiamondsVO vo = new MemberDiamondsVO();
                vo.setUserId(userId);
                vo.setUserNickName(userNickName);
                vo.setDiamondChangeNums(diamondChangeNums);
                vo.setChangeReason(changeReason);
                vo.setRemainDiamondNums(remainDiamondNums);
                vo.setCreateTime(createTime);

                list.add(vo);
            }
        }


        DataTable dtCardShareGain = UPropsLogDao.getGameCardShareGain(dictDb, gameId, userId, joinClubTime);//分享获得


        if(dtCardShareGain.rows.length>0){
            for(DataRow row : dtCardShareGain.rows){
                userNickName = row.getColumnValue("nickName");
                diamondChangeNums = "+"+row.getColumnValue("propsNum");
                changeReason = "分享获得";
                remainDiamondNums = Integer.parseInt(row.getColumnValue("propsAfter"));
                createTime = dsqp.util.DateUtils.String2Date(row.getColumnValue("createTime"), "yyyy-MM-dd HH:mm:ss");

                MemberDiamondsVO vo = new MemberDiamondsVO();
                vo.setUserId(userId);
                vo.setUserNickName(userNickName);
                vo.setDiamondChangeNums(diamondChangeNums);
                vo.setChangeReason(changeReason);
                vo.setRemainDiamondNums(remainDiamondNums);
                vo.setCreateTime(createTime);

                list.add(vo);
            }
        }

        if (list.size() > 0) {
            Collections.sort(list, new Comparator<MemberDiamondsVO>() {
                @Override
                public int compare(MemberDiamondsVO o1, MemberDiamondsVO o2) {
                    return -o1.getCreateTime().compareTo(o2.getCreateTime());
                }
            });
        }

        return list;
    }

    @Override
    public DataTable getRelationDetail(long promoterId) {
        DataTable DtRelationDetail = PromoterDao.getDirectUnder(promoterId);
        if(DtRelationDetail.rows.length>0){
            for (DataRow row : DtRelationDetail.rows){
                long pId = CommonUtils.getLongValue(row.getColumnValue("promoterId"));
                ClubModel clubModel = ClubDao.getByPromoterId(pId);
                if(clubModel!=null){
                    long clubId = clubModel.getId();
                    row.addColumn("cId",clubId);
                }
            }
        }

        return DtRelationDetail;
    }

    @Override
    public DataTable getRebateDetail(long promoterId, Date startDate, Date endDate) {
        DataTable DtRebateDetail = BonusDetailDao.getRebateDetail(promoterId, startDate, endDate);

        if(DtRebateDetail.rows.length>0){
            for (DataRow row : DtRebateDetail.rows){
                long fromPromoterId = CommonUtils.getLongValue(row.getColumnValue("fromPromoterId"));
                PromoterModel promoterModel = PromoterDao.getOne(fromPromoterId);
                String nickName = promoterModel.getNickName();
                row.addColumn("nickName",nickName);
            }
        }
        return DtRebateDetail;
    }



}
