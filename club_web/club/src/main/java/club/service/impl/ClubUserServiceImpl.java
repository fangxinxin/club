package club.service.impl;

import club.service.ClubUserService;
import club.vo.MsgVO;
import dsqp.common_const.club.LogQuitWay;
import dsqp.db.model.DataRow;
import dsqp.db.model.DataTable;
import dsqp.db.model.SplitPage;
import dsqp.db.util.DBUtils;
import dsqp.db_club.dao.ClubDao;
import dsqp.db_club.dao.ClubShareDao;
import dsqp.db_club.dao.ClubUserDao;
import dsqp.db_club.model.ClubUserModel;
import dsqp.db_club_dict.dao.DictGameDbDao;
import dsqp.db_club_dict.model.DictGameDbModel;
import dsqp.db_club_log.dao.LogClubQuitDao;
import dsqp.db_club_log.dao.LogWeekPyjUserRecordDao;
import dsqp.db_club_log.model.LogClubQuitModel;
import dsqp.db_game.dao.dev.UUserPointDao;
import dsqp.util.CommonUtils;
import dsqp.util.DateUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
* Created by ds on 2017/6/22.
*/
@Service
public class ClubUserServiceImpl implements ClubUserService {

    @Override
    public DataTable getByClubIdAndUserId(long clubId, long userId, int gameId) {
        DataTable dt = ClubUserDao.getByClubIdAndUserId(clubId, userId);
        if (dt.rows.length > 0){
            DictGameDbModel dbModel = DictGameDbDao.getByGameId(gameId, true);
            DataTable addDt = UUserPointDao.getPointByUserId(dbModel, userId);
            dt.rows[0].addColumn(addDt.rows[0].columns[0]);

            int pyjRoomNum = LogWeekPyjUserRecordDao.
                    getPyjRoomNum(clubId, userId, DateUtils.getYear(), DateUtils.getWeekOfYear(new Date()));

            dt.rows[0].addColumn("pyjRoomNum", pyjRoomNum);
        }
        return dt;
    }

    @Override
    public SplitPage listClubInfo(long clubId, int gameId, int pageNum, int pageSize) {
        SplitPage splitPage = ClubUserDao.getPage(clubId, pageNum, pageSize);
        if (splitPage.getPageDate().rows.length > 0){
            DictGameDbModel dbModel = DictGameDbDao.getByGameId(gameId, true);
            List<Long> userIds = ClubUserDao.listUserId(clubId);

            Date now = new Date();
            int pyjRoomNum = 0;
            DataTable addDt = UUserPointDao.listPoint(dbModel, userIds);
            for (DataRow row: splitPage.getPageDate().rows) {
                long gameUserId = CommonUtils.getLongValue(row.getColumnValue("gameUserId"));
                pyjRoomNum = LogWeekPyjUserRecordDao.
                        getPyjRoomNum(clubId, gameUserId, DateUtils.getYear(), DateUtils.getWeekOfYear(now));

                row.addColumn("pyjRoomNum", pyjRoomNum);
            }

            DBUtils.addColumn(splitPage.getPageDate(), addDt, "gameUserId", "privateRoomDiamond");
        }
        return splitPage;
    }

    @Override
    public MsgVO removeClubUser(long clubId, long gameUserId) {
        MsgVO msg = new MsgVO();
        DataTable dt = ClubUserDao.getByClubIdAndUserId(clubId, gameUserId);
        if (dt.rows.length <= 0) {
            msg.setRemark("用户信息异常！");
            return msg;
        }
        Date now = new Date();
        ClubUserModel c = DBUtils.convert2Model(ClubUserModel.class, dt.rows[0]);
        long promoterId = CommonUtils.getLongValue(dt.rows[0].getColumnValue("promoterId"));

        int result = ClubUserDao.removeByClubIdAndGameUserId(clubId, gameUserId);
        if (result > 0) {
            LogClubQuitModel log = new LogClubQuitModel();
            log.setClubId(clubId);
            log.setGameId(c.getGameId());
            log.setGameUserId(gameUserId);
            log.setGameNickName(c.getGameNickName());
            log.setQuitWay(LogQuitWay.PROMOTER);
            log.setCreateTime(now);
            log.setCreateDate(now);
            LogClubQuitDao.add(log);        //增加踢出成员日志

            ClubDao.updatePeopleNum(clubId, ClubUserDao.getUserNum(promoterId));        //更新俱乐部玩家数量
            ClubShareDao.removeByClubIdAndGameUserId(clubId, gameUserId);               //移除分享中的记录
            msg.setSuccess(true);
            msg.setRemark("已将玩家："+dt.rows[0].getColumnValue("gameNickName")+" 踢出俱乐部！");
            return msg;
        } else {
            msg.setRemark("操作失败，请联系客服!");
        }
        return msg;
    }

}
