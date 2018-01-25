package dsqp.db_club.impl;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db_club.model.ClubQuitModel;

/**
 * Created by jeremy on 2017/9/28.
 */
public class ClubQuitDaoImpl implements dsqp.db.service.BaseDao<ClubQuitModel> {

    private static final String CONNECTION = "club";

    public int add(ClubQuitModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "INSERT INTO club_quit (" +
                "  gameId," +
                "  gameUserId," +
                "  gameNickName," +
                "  clubId," +
                "  promoterId," +
                "  quitStatus," +
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
        db.addParameter(model.getQuitStatus());
        db.addParameter(model.getHandleTime());
        db.addParameter(model.getCreateTime());
        db.addParameter(model.getCreateDate());
        return db.executeNonQuery();
    }

    public int update(ClubQuitModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sb = new StringBuilder(128);

        if (model.getQuitStatus() != 0) {
            sb.append(",quitStatus=?");
            db.addParameter(model.getQuitStatus());
        }

        if (model.getHandleTime() != null) {
            sb.append(",handleTime=?");
            db.addParameter(model.getHandleTime());
        }

        if (sb.length() == 0) {
            return 0;
        } else {
            db.createCommand("UPDATE club_quit set " + sb.substring(1) + " where id=?");
            db.addParameter(model.getId());

            return db.executeNonQuery();
        }
    }

    public ClubQuitModel getOne(long id) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM club_quit WHERE id = ?;");
        db.addParameter(id);
        DataTable dt = db.executeQuery();
        if (dt.rows.length > 0) {
            return DBUtils.convert2Model(ClubQuitModel.class, dt.rows[0]);
        } else {
            return null;
        }
    }

    public DataTable getList() {
        return null;
    }

    public DataTable getRequestListByGameIdPromoterIdAndQuitStatus(int gameId, long promoterId, int quitStatus) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("SELECT * FROM club_quit WHERE gameId = ? AND promoterId = ? AND quitStatus = ?");
        db.addParameter(gameId);
        db.addParameter(promoterId);
        db.addParameter(quitStatus);

        return db.executeQuery();
    }


}
