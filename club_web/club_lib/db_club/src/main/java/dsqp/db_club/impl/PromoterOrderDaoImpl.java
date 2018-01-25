package dsqp.db_club.impl;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db_club.model.PromoterOrderModel;

import java.util.Date;

/**
 * Created by Aris on 2017/7/21.
 */
public class PromoterOrderDaoImpl implements dsqp.db.service.BaseDao<PromoterOrderModel> {


    private static final String CONNECTION = "club";

    public int add(PromoterOrderModel model) {

        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("INSERT INTO promoter_order " +
                "(gameId, orderNo, clubId, promoterId, gameNickName, gameUserId, promoterStatus, editAdminId, editAdmin, changeTime, changeDate, createTime, createDate ) " +
                " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?);");
        db.addParameter(model.getGameId());
        db.addParameter(model.getOrderNo());
        db.addParameter(model.getClubId());
        db.addParameter(model.getPromoterId());
        db.addParameter(model.getGameNickName());
        db.addParameter(model.getGameUserId());
        db.addParameter(model.getPromoterStatus());
        db.addParameter(model.getEditAdminId());
        db.addParameter(model.getEditAdmin());
        db.addParameter(model.getChangeTime());
        db.addParameter(model.getChangeDate());
        db.addParameter(model.getCreateTime());
        db.addParameter(model.getCreateDate());

        return db.executeNonQuery();
    }

    public DataTable queryListByDate(int gameId, Date startDate, Date endDate) {

        DBHelper db = new DBHelper(CONNECTION);
        String sql = "SELECT t.*, " +
                " (SELECT COUNT(0) FROM promoter_order WHERE gameId=t.gameId AND changeDate <= t.changeDate) totalNum, " +
                " (SELECT COUNT(0) FROM promoter_order WHERE gameId=t.gameId AND changeDate <= t.changeDate AND promoterStatus=1) totalFormalNum, " +
                " (SELECT COUNT(0) FROM promoter_order WHERE gameId=t.gameId AND changeDate = t.changeDate AND promoterStatus=1) formalNum, " +
                " (SELECT COUNT(0) FROM promoter_order WHERE gameId=t.gameId AND changeDate <= t.changeDate AND promoterStatus > 1 ) totalInitNum, " +
                " (SELECT COUNT(0) FROM promoter_order WHERE gameId=t.gameId AND changeDate BETWEEN DATE_ADD(t.changeDate, INTERVAL - DAY(t.changeDate) + 1 DAY) AND t.changeDate AND promoterStatus > 1) monthInitNum " +
                "FROM (SELECT COUNT(0) newNum,changeDate ,gameId FROM promoter_order WHERE gameId = ? AND changeDate BETWEEN ? AND ? GROUP BY changeDate ORDER BY changeDate DESC) t;";
        db.createCommand(sql);
        db.addParameter(gameId);
        db.addParameter(startDate);
        db.addParameter(endDate);
        return db.executeQuery();
    }

    public DataTable queryAllEditAdmin(int gameId) {

        DBHelper db = new DBHelper(CONNECTION);
        String sql = "SELECT " +
                " t.*, " +
                " (SELECT  COUNT(0) FROM promoter_order WHERE gameId = t.gameId AND editAdmin=t.editAdmin AND createDate BETWEEN DATE_ADD(CURDATE(), INTERVAL - DAY(CURDATE()) + 1 DAY) AND CURDATE()) monthNew, " +
                " (SELECT  COUNT(0) FROM promoter_order WHERE gameId = t.gameId AND editAdmin=t.editAdmin AND createDate BETWEEN DATE_ADD(CURDATE()-DAY(CURDATE())+1,INTERVAL -1 MONTH) AND LAST_DAY(DATE_SUB(NOW(),INTERVAL 1 MONTH))) lastMonthNew, " +
                " (SELECT  COUNT(0) FROM promoter_order WHERE gameId = t.gameId AND editAdmin=t.editAdmin AND promoterStatus = 1 ) allFormal, " +
                " (SELECT  COUNT(0) FROM promoter_order WHERE gameId = t.gameId AND editAdmin=t.editAdmin AND promoterStatus = 1 AND changeDate BETWEEN DATE_ADD(CURDATE(), INTERVAL - DAY(CURDATE()) + 1 DAY) AND CURDATE()) monthFormal, " +
                " (SELECT  COUNT(0) FROM promoter_order WHERE gameId = t.gameId AND editAdmin=t.editAdmin AND promoterStatus = 1 AND changeDate BETWEEN DATE_ADD(CURDATE()-DAY(CURDATE())+1,INTERVAL -1 MONTH) AND LAST_DAY(DATE_SUB(NOW(),INTERVAL 1 MONTH))) lastMonthFormal, " +
                " (SELECT  COUNT(0) FROM promoter_order WHERE gameId = t.gameId AND editAdmin=t.editAdmin AND promoterStatus > 1 AND changeDate BETWEEN DATE_ADD(CURDATE(), INTERVAL - DAY(CURDATE()) + 1 DAY) AND CURDATE()) monthInit, " +
                " (SELECT  COUNT(0) FROM promoter_order WHERE gameId = t.gameId AND editAdmin=t.editAdmin AND promoterStatus > 1 ) allInit " +
                " FROM " +
                " (SELECT gameId ,editAdmin ,COUNT(0) num FROM promoter_order WHERE gameId= ? GROUP BY editAdmin HAVING num>0 ) t";
        db.createCommand(sql);
        db.addParameter(gameId);
        return db.executeQuery();
    }

    public DataTable queryAllByEditAdmin(int gameId, String editAdmin) {

        DBHelper db = new DBHelper(CONNECTION);
        String sql = "SELECT " +
                " t.*, " +
                " (SELECT  COUNT(0) FROM promoter_order WHERE gameId = t.gameId AND editAdmin=t.editAdmin AND createDate BETWEEN DATE_ADD(CURDATE(), INTERVAL - DAY(CURDATE()) + 1 DAY) AND CURDATE()) monthNew, " +
                " (SELECT  COUNT(0) FROM promoter_order WHERE gameId = t.gameId AND editAdmin=t.editAdmin AND createDate BETWEEN DATE_ADD(CURDATE()-DAY(CURDATE())+1,INTERVAL -1 MONTH) AND LAST_DAY(DATE_SUB(NOW(),INTERVAL 1 MONTH))) lastMonthNew, " +
                " (SELECT  COUNT(0) FROM promoter_order WHERE gameId = t.gameId AND editAdmin=t.editAdmin AND promoterStatus = 1 ) allFormal, " +
                " (SELECT  COUNT(0) FROM promoter_order WHERE gameId = t.gameId AND editAdmin=t.editAdmin AND promoterStatus = 1 AND changeDate BETWEEN DATE_ADD(CURDATE(), INTERVAL - DAY(CURDATE()) + 1 DAY) AND CURDATE()) monthFormal, " +
                " (SELECT  COUNT(0) FROM promoter_order WHERE gameId = t.gameId AND editAdmin=t.editAdmin AND promoterStatus = 1 AND changeDate BETWEEN DATE_ADD(CURDATE()-DAY(CURDATE())+1,INTERVAL -1 MONTH) AND LAST_DAY(DATE_SUB(NOW(),INTERVAL 1 MONTH))) lastMonthFormal, " +
                " (SELECT  COUNT(0) FROM promoter_order WHERE gameId = t.gameId AND editAdmin=t.editAdmin AND promoterStatus > 1 AND changeDate BETWEEN DATE_ADD(CURDATE(), INTERVAL - DAY(CURDATE()) + 1 DAY) AND CURDATE()) monthInit, " +
                " (SELECT  COUNT(0) FROM promoter_order WHERE gameId = t.gameId AND editAdmin=t.editAdmin AND promoterStatus > 1 ) allInit " +
                " FROM " +
                " (SELECT gameId ,editAdmin ,COUNT(0) num FROM promoter_order WHERE gameId= ? AND editAdmin = ? HAVING num>0 ) t";
        db.createCommand(sql);
        db.addParameter(gameId);
        db.addParameter(editAdmin);
        return db.executeQuery();
    }

    public DataTable queryAll(int gameId) {

        DBHelper db = new DBHelper(CONNECTION);
        String sql = "SELECT * FROM promoter_order WHERE gameId= ? ";
        db.createCommand(sql);
        db.addParameter(gameId);
        return db.executeQuery();
    }

    public DataTable queryByEditAdmin(int gameId, String editAdmin) {

        DBHelper db = new DBHelper(CONNECTION);
        String sql = "SELECT * FROM promoter_order WHERE gameId= ? and editAdmin = ? ";
        db.createCommand(sql);
        db.addParameter(gameId);
        db.addParameter(editAdmin);
        return db.executeQuery();
    }

    public DataTable getOneDetail(int gameId, String editAdmin) {

        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("SELECT t.*,(select peopleNum from club where id=t.clubId) peopleNum FROM (SELECT * FROM promoter_order WHERE gameID = ? AND editAdmin= ? ) t;");
        db.addParameter(gameId);
        db.addParameter(editAdmin);
        return db.executeQuery();
    }

    public int update(PromoterOrderModel promoterOrderModel) {
        return 0;
    }

    public PromoterOrderModel getOne(long id) {
        return null;
    }

    public DataTable getListByDate(int gameId, Date date) {
        DBHelper db = new DBHelper(CONNECTION);
        String sql = "SELECT t.* ,(SELECT peopleNum FROM club WHERE id=t.clubId) peopleNum FROM (SELECT * FROM promoter_order WHERE gameId=? AND changeDate=?) t ;";
        db.createCommand(sql);

        db.addParameter(gameId);
        db.addParameter(date);
        return db.executeQuery();
    }

    public DataTable getList() {
        return null;
    }

    public int updatePromoterStatus(long clubId, int promoterStatus) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("UPDATE promoter_order SET promoterStatus = ?, changeTime = now(), changeDate = now() WHERE clubId = ?;");
        db.addParameter(promoterStatus);
        db.addParameter(clubId);

        return db.executeNonQuery();
    }
}
