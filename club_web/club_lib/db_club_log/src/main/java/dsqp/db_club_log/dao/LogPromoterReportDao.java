package dsqp.db_club_log.dao;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db.model.SplitPage;
import dsqp.db_club_log.model.LogPromoterReportModel;

import java.util.Date;

/**
 * Created by mj on 2017/9/7.
 */
public class LogPromoterReportDao {
    private static final LogPromoterReportDaoImpl impl = new LogPromoterReportDaoImpl();
    private LogPromoterReportDao(){}
    //insert方法
    public static int add(LogPromoterReportModel model) {
        return impl.add(model);
    }

    //分页查日报
    public static SplitPage getListByDate(long clubId, Date startDate, Date endDate, int pageNum, int pageSize) {
        return impl.getListByDate(clubId, startDate, endDate, pageNum, pageSize);
    }

    //查时间段内日报的总条数
    public static String getCountByDate(long clubId, Date startDate, Date endDate) {
        return impl.getCountByDate(clubId, startDate, endDate);
    }

    public static int updateRoomCreateNumAndgameCardConsume(int roomCreateNum, int gameCardConsume, long clubId, Date statDate) {
        return impl.updateRoomCreateNumAndgameCardConsume(roomCreateNum, gameCardConsume, clubId, statDate);
    }

    public static int updateGameCardSell(int gameCardSell, long clubId, Date statDate) {
        return impl.updateGameCardSell(gameCardSell, clubId, statDate);
    }

    //获取俱乐部成员对局总数
    public static String getRoomCreateNumsByClubId(long clubId) {
        return impl.getRoomCreateNumsByClubId(clubId);
    }

    //对局统计
    public static DataTable getRoomStatis(long clubId, Date startDate, Date endDate) {
        return impl.getRoomStatis(clubId, startDate, endDate);
    }

    //流水统计
    public static DataTable getDiamondStatis(long clubId, Date startDate, Date endDate) {
        return impl.getDiamondStatis(clubId, startDate, endDate);
    }
}

class LogPromoterReportDaoImpl extends BaseDaoImpl implements dsqp.db.service.BaseDao<LogPromoterReportModel> {

    //查日报
    public SplitPage getListByDate(long clubId, Date startDate, Date endDate, int pageNum, int pageSize) {

        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("SELECT * FROM  log_promoter_report WHERE clubId= ? AND statDate BETWEEN ? AND ? ORDER BY statDate DESC limit ?,? ;");
        db.addParameter(clubId);
        db.addParameter(startDate);
        db.addParameter(endDate);
        db.addParameter((pageNum - 1) * pageSize);
        db.addParameter(pageSize);
        DataTable dt = db.executeQuery();
        int totalNum = Integer.parseInt(db.getParamValue("OUT_total"));
        return new SplitPage(pageNum, pageSize, totalNum, dt);
    }

    //查日报的条数
    public String getCountByDate(long clubId, Date startDate, Date endDate) {

        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("SELECT COUNT(0) AS sum FROM  log_promoter_report WHERE clubId= ? AND statDate BETWEEN ? AND ? ORDER BY statDate DESC;");
        db.addParameter(clubId);
        db.addParameter(startDate);
        db.addParameter(endDate);
        DataTable dt = db.executeQuery();
        return dt.rows.length > 0 ? dt.rows[0].getColumnValue("sum") : "0";
    }


    public int add(LogPromoterReportModel model) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("INSERT INTO log_promoter_report" +
                "( gameId," +
                "  clubId," +
                "  roomCreateNum," +
                "  gameCardConsume," +
                "  gameCardSell," +
                "  statDate," +
                "  createDate" +
                ") " +
                "VALUES(?,?,?,?,?,?,?)");
        db.addParameter(model.getGameId());
        db.addParameter(model.getClubId());
        db.addParameter(model.getRoomCreateNum());
        db.addParameter(model.getGameCardConsume());
        db.addParameter(model.getGameCardSell());
        db.addParameter(model.getStatDate());
        db.addParameter(model.getCreateDate());

        return db.executeNonQuery();
    }

    public int update(LogPromoterReportModel model) {
        return 0;
    }

    public int updateRoomCreateNumAndgameCardConsume(int roomCreateNum, int gameCardConsume, long clubId, Date statDate) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("UPDATE log_promoter_report " +
                " SET roomCreateNum = ?,gameCardConsume = ? " +
                " WHERE statDate = ? AND clubId = ? ");
        db.addParameter(roomCreateNum);
        db.addParameter(gameCardConsume);
        db.addParameter(statDate);
        db.addParameter(clubId);

        return db.executeNonQuery();
    }

    public int updateGameCardSell(int gameCardSell, long clubId, Date statDate) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("UPDATE log_promoter_report " +
                " SET gameCardSell = ?" +
                " WHERE statDate = ? AND clubId = ?");
        db.addParameter(gameCardSell);
        db.addParameter(statDate);
        db.addParameter(clubId);

        return db.executeNonQuery();
    }

    public LogPromoterReportModel getOne(long id) {
        return null;
    }

    public DataTable getList() {
        return null;
    }

    //获取俱乐部玩家总对局数
    public String getRoomCreateNumsByClubId(long clubId) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("SELECT IFNULL(SUM(roomCreateNum),0) AS roomNums FROM log_promoter_report WHERE  clubId = ? ");
        db.addParameter(clubId);

        DataTable dt = db.executeQuery();
        return dt.rows.length > 0 ? dt.rows[0].getColumnValue("roomNums") : "0";
    }

    //对局统计
    public DataTable getRoomStatis(long clubId, Date startDate, Date endDate) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("SELECT t.* ,(SELECT IFNULL(SUM(roomCreateNum),0) FROM log_promoter_report WHERE clubId = ? AND statDate <= t.statDate) totalNums " +
                " FROM " +
                " (SELECT roomCreateNum,statDate FROM log_promoter_report WHERE clubId = ? AND statDate BETWEEN ? AND ? ORDER BY statDate DESC)t;");
        db.addParameter(clubId);
        db.addParameter(clubId);
        db.addParameter(startDate);
        db.addParameter(endDate);

        return db.executeQuery();
    }


    //流水统计
    public DataTable getDiamondStatis(long clubId, Date startDate, Date endDate) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("SELECT t.* ,(SELECT IFNULL(SUM(gameCardConsume),0) FROM log_promoter_report WHERE clubId = ? AND statDate <= t.statDate) totalNums " +
                " FROM " +
                " (SELECT gameCardConsume,statDate FROM log_promoter_report WHERE clubId = ? AND statDate BETWEEN ? AND ? ORDER BY statDate DESC)t;");
        db.addParameter(clubId);
        db.addParameter(clubId);
        db.addParameter(startDate);
        db.addParameter(endDate);

        return db.executeQuery();
    }


}