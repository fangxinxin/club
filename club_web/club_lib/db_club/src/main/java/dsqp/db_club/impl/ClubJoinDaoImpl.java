package dsqp.db_club.impl;

import com.google.common.base.Strings;
import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db_club.model.ClubJoinModel;
import dsqp.util.CommonUtils;


/**
 * Created by jeremy on 2017/9/28.
 */
public class ClubJoinDaoImpl implements dsqp.db.service.BaseDao<ClubJoinModel> {

    private static final String CONNECTION = "club";

    public int add(ClubJoinModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "INSERT INTO club_join (" +
                "  gameId," +
                "  gameUserId," +
                "  gameNickName," +
                "  clubId," +
                "  promoterId," +
                "  passStatus," +
                "  handleTime," +
                "  createTime," +
                "  createDate" +
                ") VALUES(?,?,?,?,?,?,?,?,?)";

        db.createCommand(sql);
        db.addParameter(model.getGameId());
        db.addParameter(model.getGameUserId());
        db.addParameter(model.getGameNickName());
        db.addParameter(model.getClubId());
        db.addParameter(model.getPromoterId());
        db.addParameter(model.getPassStatus());
        db.addParameter(model.getHandleTime());
        db.addParameter(model.getCreateTime());
        db.addParameter(model.getCreateDate());
        return db.executeNonQuery();
    }

    public int update(ClubJoinModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sb = new StringBuilder(128);

        if (model.getPassStatus() != 0) {
            sb.append(",passStatus=?");
            db.addParameter(model.getPassStatus());
        }

        if (model.getHandleTime() != null) {
            sb.append(",handleTime=?");
            db.addParameter(model.getHandleTime());
        }

        if (sb.length() == 0) {
            return 0;
        } else {
            db.createCommand("UPDATE club_join set " + sb.substring(1) + " where id=?");
            db.addParameter(model.getId());

            return db.executeNonQuery();
        }
    }

    public ClubJoinModel getOne(long id) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM club_join WHERE id = ?;");
        db.addParameter(id);
        DataTable dt = db.executeQuery();
        if (dt.rows.length > 0) {
            return DBUtils.convert2Model(ClubJoinModel.class, dt.rows[0]);
        } else {
            return null;
        }
    }

    public DataTable getList() {
        return null;
    }

    public DataTable getRequestListByGameIdPromoterIdAndPassStatus(int gameId, long promoterId, int passStatus) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("SELECT * FROM club_join WHERE gameId = ? AND promoterId = ? AND passStatus = ?");
        db.addParameter(gameId);
        db.addParameter(promoterId);
        db.addParameter(passStatus);

        return db.executeQuery();
    }

    /**
     * 更新入会申请状态
     */
    public static int updateStatusById(ClubJoinModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sb = new StringBuilder(128);

        if (model.getPassStatus() != 0) {
            sb.append(",passStatus=?");
            db.addParameter(model.getPassStatus());
        }

        if (model.getHandleTime() != null) {
            sb.append(",handleTime=?");
            db.addParameter(model.getHandleTime());
        }

        if (sb.length() == 0) {
            return 0;
        } else {
            db.createCommand("UPDATE club_join set " + sb.substring(1) + " where id= ? ");
            db.addParameter(model.getId());

            return db.executeNonQuery();
        }
    }


    /**
     * 判断是否  第一次被此俱乐部拒绝
     */

    public int getRefuseNum(long clubId, long gameUserId, int passStatus) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT COUNT(0) nums FROM club_join WHERE clubId= ? AND gameUserId = ? AND passStatus = ?;");
        db.addParameter(clubId);
        db.addParameter(gameUserId);
        db.addParameter(passStatus);
        DataTable dt = db.executeQuery();
        if (dt.rows.length > 0) {
            return CommonUtils.getIntValue(dt.rows[0].getColumnValue("nums"));
        } else {
            return 0;
        }
    }

}
