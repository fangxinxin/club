package dsqp.db_club.impl;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DBCommandType;
import dsqp.db.model.DataTable;
import dsqp.db.service.BaseDao;
import dsqp.db.util.DBUtils;
import dsqp.db.util.SqlUtils;
import dsqp.db_club.model.NoticeModel;
import dsqp.util.CommonUtils;

import java.util.List;

/**
 * Created by jeremy on 2017/7/21.
 */
public class NoticeDaoImpl implements BaseDao<NoticeModel> {

    private static final String CONNECTION = "club";

    public int add(NoticeModel model) {

        DBHelper db = new DBHelper(CONNECTION);

        String sql = "INSERT INTO notice " +
                "(gameId, noticeType, title, content, createTime) " +
                "VALUES(?,?,?,?,?)";

        db.createCommand(sql, DBCommandType.Text, true);
        DBUtils.addSqlParameters(db, model);

        int result = db.executeNonQuery();
        model.setId(db.generatedValue);
        return result;
    }

    public int update(NoticeModel model) {
        return 0;
    }

    public NoticeModel getOne(long id) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from notice where id = ?");

        db.addParameter(id);
        DataTable dt = db.executeQuery();

        return dt.rows.length > 0 ? (DBUtils.convert2Model(NoticeModel.class, dt.rows[0])): null;
    }

    public DataTable getList() {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from notice");
        return db.executeQuery();
    }

    public DataTable findNoticeListByGameIdAndPromoterId(long gameId,long promoterId){
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT t1.*,t2.promoterId,t2.isRead,t2.id AS dId\n" +
                "FROM notice        t1\n" +
                "JOIN notice_detail t2\n" +
                "ON t1.id = t2.noticeId AND t1.gameId=? AND t2.promoterId=?\n" +
                "ORDER BY t2.isRead,t2.createTime DESC");

        db.addParameter(gameId);
        db.addParameter(promoterId);

        return db.executeQuery();
    }

    public int deleteById(long id) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("delete from notice where id = ?");

        db.addParameter(id);

        return db.executeNonQuery();
    }

    public int deleteNoticeByListId(List listId){
        DBHelper db = new DBHelper(CONNECTION);
        StringBuffer sb = new StringBuffer(128);
        if(listId != null && listId.size() > 0) {
            sb.append("DELETE FROM notice WHERE id in ").append(SqlUtils.buildInConditions(listId.size()));

            for(int i = 0; i < listId.size(); ++i) {
                db.addParameter(listId.get(i));
            }

            db.createCommand(sb.toString());

            return db.executeNonQuery();
        }
        return 0;
    }

    public boolean hasNewNotice(long promoterId, boolean isRead) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand(
                "SELECT EXISTS(" +
                        "SELECT * FROM notice t1 JOIN notice_detail t2 ON t1.id = t2.noticeId " +
                        "WHERE t2.promoterId = ? AND t2.isRead = ?" +
                        ") AS hasNewNotice;"
        );

        db.addParameter(promoterId);
        db.addParameter(isRead);

        DataTable dt = db.executeQuery();

        return CommonUtils.getIntValue(dt.rows[0].getColumnValue("hasNewNotice")) > 0 ? true : false;
    }
}
