package dsqp.db_club_log.dao;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DBCommandType;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db_club_log.model.LogRecordModel;

/**
 * Created by jeremy on 2017/8/15.
 */
public class LogRecordDao {
    private static final LogRecordDaoImpl impl = new LogRecordDaoImpl();

    public static int add(LogRecordModel model) {
        return impl.add(model);
    }

    public static int update(LogRecordModel model) {
        return impl.update(model);
    }

    public static DataTable getListByGameId(int gameId,String menuItem) { return impl.getListByGameId(gameId,menuItem);}

    public static DataTable getListByGameIdAndDate(int gameId,String menuItem,String startDate,String endDate){
        return impl.getListByGameIdAndDate(gameId, menuItem, startDate, endDate);
    }

    public static DataTable getListByRecordTypeAndDate(int gameId,int recordType,String startDate,String endDate){
        return impl.getListByRecordTypeAndDate(gameId, recordType, startDate, endDate);
    }

}

class LogRecordDaoImpl implements dsqp.db.service.BaseDao<LogRecordModel> {

    private static final String CONNECTION = "club_log";

    public int add(LogRecordModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "INSERT INTO log_record (gameId,menuItem,menuName,recordType,typeName,content,requestIp,editAdminId,editAdmin,createDate,createTime) VALUES(?,?,?,?,?,?,?,?,?,?,?)";

        db.createCommand(sql, DBCommandType.Text, true);
        DBUtils.addSqlParameters(db, model);
        int result = db.executeNonQuery();
        model.setId(db.generatedValue);
        return result;
    }

    public int update(LogRecordModel logRecordModel) {
        return 0;
    }

    public LogRecordModel getOne(long id) {
        return null;
    }

    public DataTable getList() {
        return null;
    }

    public DataTable getListByGameId(int gameId,String menuItem) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("select * from log_record where gameId= ? AND menuItem= ? GROUP BY createTime DESC");
        db.addParameter(gameId);
        db.addParameter(menuItem);
        return db.executeQuery();
    }

    public DataTable getListByGameIdAndDate(int gameId,String menuItem,String startDate,String endDate){
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("SELECT * FROM  log_record WHERE gameId = ? AND menuItem = ? AND createDate BETWEEN ? AND ? GROUP BY createTime  DESC;");
        db.addParameter(gameId);
        db.addParameter(menuItem);
        db.addParameter(startDate);
        db.addParameter(endDate);
        return db.executeQuery();
    }

    public DataTable getListByRecordTypeAndDate(int gameId,int recordType,String startDate,String endDate){
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("SELECT * FROM  log_record WHERE gameId = ? AND recordType = ? AND createDate BETWEEN ? AND ? GROUP BY createTime  DESC;");
        db.addParameter(gameId);
        db.addParameter(recordType);
        db.addParameter(startDate);
        db.addParameter(endDate);
        return db.executeQuery();
    }
}
