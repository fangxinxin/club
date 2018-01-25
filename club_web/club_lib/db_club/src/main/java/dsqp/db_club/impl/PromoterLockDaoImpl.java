package dsqp.db_club.impl;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DBCommandType;
import dsqp.db.model.DataTable;
import dsqp.db.service.BaseDao;
import dsqp.db.util.DBUtils;
import dsqp.db.util.SqlUtils;
import dsqp.db_club.model.PromoterLockModel;

import java.util.Date;
import java.util.List;

/**
 * Created by ds on 2017/7/30.
 */
public class PromoterLockDaoImpl implements BaseDao<PromoterLockModel> {
    private static final String CONNECTION = "club";

    public int add(PromoterLockModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("INSERT INTO promoter_lock " +
                "(promoterId, nickName, peopleNum, pLevel, gameId, lockDay, remark, editAdmin, expireTime, createTime) " +
                " VALUES(?,?,?,?,?,?,?,?,?,?)", DBCommandType.Text, true);
        DBUtils.addSqlParameters(db, model);

        int result = db.executeNonQuery();
        model.setId(db.generatedValue);
        return result;
    }

    public int update(PromoterLockModel model) {
        return 0;
    }

    public PromoterLockModel getOne(long id) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from promoter_lock where id = ?");
        db.addParameter(id);

        DataTable dt = db.executeQuery();
        if (dt.rows.length > 0) {
            return DBUtils.convert2Model(PromoterLockModel.class, dt.rows[0]);
        } else {
            return null;
        }
    }


    public PromoterLockModel getByPromoterId(long promoterId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from promoter_lock where promoterId = ?;");
        db.addParameter(promoterId);

        DataTable dt = db.executeQuery();
        if (dt.rows.length > 0) {
            return DBUtils.convert2Model(PromoterLockModel.class, dt.rows[0]);
        } else {
            return null;
        }
    }

    public int deleteByPromoterId(long promoterId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("DELETE FROM `promoter_lock` WHERE promoterId = ?;");
        db.addParameter(promoterId);

        return db.executeNonQuery();
    }

    public DataTable getList() {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from promoter_lock");
        return db.executeQuery();
    }

    public DataTable listByGameId(int gameId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from promoter_lock where gameId = ?;");
        db.addParameter(gameId);
        return db.executeQuery();
    }



    /** 代理解封    task
     */
    public DataTable getExpireList() {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from promoter_lock where expireTime <= ?;");
        db.addParameter(new Date());

        return db.executeQuery();
    }
    public int deleteByPromoterIds(List<Long> promoterIds) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuffer sb = new StringBuffer(128);
        sb.append("DELETE FROM `promoter_lock` WHERE expireTime <= ? ")
                .append("and promoterId in ")
                .append(SqlUtils.buildInConditions(promoterIds.size()));

        db.addParameter(new Date());
        for (long id : promoterIds) {
            db.addParameter(id);
        }
        db.createCommand(sb.toString());
        return db.executeNonQuery();
    }


}
