package dsqp.db_club.impl;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DBCommandType;
import dsqp.db.model.DataTable;
import dsqp.db.service.BaseDao;
import dsqp.db.util.DBUtils;
import dsqp.db.util.SqlUtils;
import dsqp.db_club.model.NoticeDetailModel;

import java.util.List;

/**
 * Created by jeremy on 2017/7/21.
 */
public class NoticeDetailDaoImpl implements BaseDao<NoticeDetailModel> {

    private static final String CONNECTION = "club";

    public int add(NoticeDetailModel model) {

        DBHelper db = new DBHelper(CONNECTION);

        String sql = "INSERT INTO notice_detail " +
                "(gameId, noticeId, promoterId, isRead, createTime) " +
                "VALUES(?,?,?,?,?)";

        db.createCommand(sql, DBCommandType.Text, true);
        DBUtils.addSqlParameters(db, model);

        int result = db.executeNonQuery();
        model.setId(db.generatedValue);
        return result;
//
//        DBHelper db = new DBHelper(CONNECTION);
//
//        db.createCommand("CP_notice_detail_add", DBCommandType.Procedure);
//        DBUtils.addSpParameters(db, model);
//        //int参数在方法里面添加,out参数在外面添加
//        db.addParameter("OUT_lastId", ParameterDirection.Output);
//
//        int num = db.executeNonQuery();
//        model.setId(Integer.parseInt(db.getParamValue("OUT_lastId")));
//        return num;
    }

    public int update(NoticeDetailModel model) {
        return 0;
    }

    public NoticeDetailModel getOne(long id) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from notice_detail where id = ?");

        db.addParameter(id);
        DataTable dt = db.executeQuery();

        return  dt.rows.length > 0 ? (DBUtils.convert2Model(NoticeDetailModel.class, dt.rows[0])) : null;
    }

    public DataTable getList() {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from notice_detail");

        return db.executeQuery();
    }

    public int deleteById(long id) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("delete from notice_detail where id = ?");

        db.addParameter(id);

        return db.executeNonQuery();
    }

    public int updateIsReadById(long id) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("update notice_detail set isRead = ? where id = ?");

        db.addParameter(1);

        db.addParameter(id);

        return db.executeNonQuery();
    }

    public DataTable getNoticeIsReadInfo(long promoterId,int isRead){
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM notice WHERE id IN (" +
                "SELECT noticeId FROM notice_detail WHERE promoterId = ? AND isRead = ?)");

        db.addParameter(promoterId);

        db.addParameter(isRead);

        return db.executeQuery();
    }

    public DataTable getNoticeNumsById(long promoterId){
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT COUNT(*) AS nums FROM notice_detail WHERE  promoterId = ?");

        db.addParameter(promoterId);

        return db.executeQuery();
    }

    public DataTable getNoticeList2Delete(long promoterId,int index,int nums){
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT n1.id,n2.id AS dId  FROM " +
                "(SELECT * FROM notice WHERE id IN (" +
                "SELECT noticeId FROM notice_detail WHERE promoterId = ?)) n1" +
                " " +
                "LEFT JOIN " +
                "(SELECT * FROM notice_detail WHERE promoterId = ?) n2" +
                " " +
                "ON n1.id = n2.noticeId ORDER BY n2.isRead,n2.createTime DESC LIMIT ?,?");

        db.addParameter(promoterId);
        db.addParameter(promoterId);
        db.addParameter(index);
        db.addParameter(nums);

        return db.executeQuery();
    }

    public int deleteNoticeDetailByListId(List listId){
        DBHelper db = new DBHelper(CONNECTION);
        StringBuffer sb = new StringBuffer(128);
        if(listId != null && listId.size() > 0) {
            sb.append("DELETE FROM notice_detail WHERE id in ").append(SqlUtils.buildInConditions(listId.size()));

            for(int i = 0; i < listId.size(); ++i) {
                db.addParameter(listId.get(i));
            }

            db.createCommand(sb.toString());

            return db.executeNonQuery();
        }
        return 0;
    }

}
