package dsqp.db_club.impl;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DBCommandType;
import dsqp.db.model.DataTable;
import dsqp.db.model.ParameterDirection;
import dsqp.db.model.SplitPage;
import dsqp.db.service.BaseDao;
import dsqp.db.util.DBUtils;
import dsqp.db_club.model.PromoterSellModel;
import dsqp.util.CommonUtils;

import java.util.Date;

public class PromoterSellDaoImpl implements BaseDao<PromoterSellModel> {

    private static final String CONNECTION = "club";

    public int add(PromoterSellModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("TRAN_promoter_sell_add", DBCommandType.Procedure);

        db.addParameter("IN_gameId", model.getGameId());
        db.addParameter("IN_promoterId", model.getPromoterId());
        db.addParameter("IN_sellNum", model.getSellNum());
        db.addParameter("IN_sellAfter", model.getSellAfter());
        db.addParameter("IN_gameUserId", model.getGameUserId());
        db.addParameter("IN_gameNickName", model.getGameNickName());
        db.addParameter("IN_isSuccess", model.getIsSuccess());
        db.addParameter("IN_createTime", model.getCreateTime());
        db.addParameter("IN_createDate", model.getCreateDate());

        //int参数在方法里面添加,out参数在外面添加
        db.addParameter("OUT_lastId", ParameterDirection.Output);

        int result = db.executeNonQuery();
        model.setId(Integer.parseInt(db.getParamValue("OUT_lastId")));
        return result;
    }

    public int update(PromoterSellModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("update promoter_sell set isSuccess = ? where id = ?");

        db.addParameter(model.getIsSuccess());
        db.addParameter(model.getId());

        return db.executeNonQuery();
    }

    public PromoterSellModel getOne(long id) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from promoter_sell where id = ?");

        db.addParameter(id);
        DataTable dt = db.executeQuery();

        return dt.rows.length > 0 ? (DBUtils.convert2Model(PromoterSellModel.class, dt.rows[0])) : null;
    }

    public DataTable getList() {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from promoter_sell");
        return db.executeQuery();
    }

    public DataTable getListByCreateTime(long promoterId, Date startTime, Date endTime) {

        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM promoter_sell WHERE promoterId= ?  AND (createTime BETWEEN ? AND ?) and isSuccess = true ORDER BY createTime DESC");

        db.addParameter(promoterId);
        db.addParameter(startTime);
        db.addParameter(endTime);
        return db.executeQuery();
    }

    public DataTable getListByCreateDateAndGameId(int gameId, Date startTime, Date endTime) {

        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT IFNULL(SUM(sellNUm),0) sellNum ,createDate FROM promoter_sell " +
                " WHERE gameId= ? AND isSuccess=TRUE AND createDate BETWEEN ? AND ? GROUP BY createDate ORDER BY createDate DESC");

        db.addParameter(gameId);
        db.addParameter(startTime);
        db.addParameter(endTime);
        return db.executeQuery();
    }

    public int getTotalSell(long promoterId, long gameUserId, boolean isSuccess) {

        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT SUM(sellNum) totalSellNum FROM promoter_sell WHERE promoterId = ? AND gameUserId = ? AND isSuccess = ?;");

        db.addParameter(promoterId);
        db.addParameter(gameUserId);
        db.addParameter(isSuccess);
        DataTable dt = db.executeQuery();

        return dt.rows.length > 0 ? CommonUtils.getIntValue(dt.rows[0].getColumnValue("totalSellNum")) : 0;
    }

    public SplitPage getPageByDate(long promoterId, Date startDate, Date endDate, int pageNum, int pageSize) {
        //pagenum页数
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("CP_promoter_sell_getPage", DBCommandType.Procedure);
        db.addParameter("IN_promoterId", promoterId);
        db.addParameter("IN_pageNum", pageNum);
        db.addParameter("IN_pageSize", pageSize);
        db.addParameter("IN_startDate", startDate);
        db.addParameter("IN_endDate", endDate);
        db.addParameter("OUT_total", ParameterDirection.Output);
        DataTable dt = db.executeQuery();
        //获取记录数
        int totalNum = Integer.parseInt(db.getParamValue("OUT_total"));
        return new SplitPage(pageNum, pageSize, totalNum, dt);
    }

    public DataTable queryToralByPromoterAndGameId(int gameId, long promoterId) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("SELECT gameUserId, SUM(sellnum) totalPay FROM promoter_sell WHERE gameid= ? and promoterid= ? AND isSuccess = true GROUP BY gameuserid ");
        db.addParameter(gameId);
        db.addParameter(promoterId);
        return db.executeQuery();
    }

    public DataTable getAllListByCreateTime(long promoterId, Date startTime, Date endTime) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM promoter_sell WHERE promoterId= ?  AND createTime BETWEEN ? AND ? ORDER BY createTime DESC");

        db.addParameter(promoterId);
        db.addParameter(startTime);
        db.addParameter(endTime);
        return db.executeQuery();
    }

    public DataTable getGameCardSellReportByDate(Date date) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT t2.id as clubId,SUM(sellNum) AS gameCardSell" +
                " FROM promoter_sell t1" +
                " JOIN club        t2" +
                " ON t1.promoterId = t2.promoterId AND t1.isSuccess = TRUE AND t1.createDate = ?" +
                " GROUP BY t1.promoterId");

        db.addParameter(date);

        return db.executeQuery();
    }

    //俱乐部成员购买钻石总数
    public DataTable getGameDiamondSellByUserId(long promoterId, long userId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT IFNULL(SUM(sellNum),0) AS sellNums FROM promoter_sell WHERE promoterId = ? AND gameUserId = ? AND isSuccess = 1");

        db.addParameter(promoterId);
        db.addParameter(userId);

        return db.executeQuery();
    }

    //俱乐部成员购买钻石信息列表
    public DataTable getGameDiamondSellInfo(long promoterId, long userId, Date joinClubTime) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM promoter_sell WHERE promoterId = ? AND gameUserId = ? AND createTime > ? AND isSuccess = 1");

        db.addParameter(promoterId);
        db.addParameter(userId);
        db.addParameter(joinClubTime);

        return db.executeQuery();
    }

    //查询玩家所加入的俱乐部明细
    public DataTable getDetailByUserIdAndGameId(int gameId, long gameUserId) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "SELECT t.*,(SELECT clubName FROM club WHERE promoterId = t.promoterId) clubName,(SELECT realName FROM promoter WHERE id = t.promoterId) realName,(SELECT IFNULL(SUM(sellNum),0) sellTotal FROM promoter_sell WHERE promoterId=t.promoterId AND gameUserId=t.gameUserId) sellNum FROM (SELECT * FROM club_user WHERE gameId= ? AND gameUserId= ? ) t;";
        db.createCommand(sql);
        db.addParameter(gameId);
        db.addParameter(gameUserId);
        return db.executeQuery();
    }

}
