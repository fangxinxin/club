package dsqp.db_club_dict.impl;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DBCommandType;
import dsqp.db.model.DataTable;
import dsqp.db.service.BaseDao;
import dsqp.db.util.DBUtils;
import dsqp.db.util.SqlUtils;
import dsqp.db_club_dict.model.DictGameModel;
import dsqp.util.CommonUtils;

import java.util.List;

/**
 * Created by mj on 2017/7/22.
 */
public class DictGameDaoImpl implements BaseDao<DictGameModel> {
    private static final String CONNECTION = "club_dict";
    private static final String _CONNECTION = "henan_recharge_dev";
    private static final String CONNECTION_LOG = "henan_recharge_log_dev";


    public int add(DictGameModel model) {
        return 0;
    }

    public int update(DictGameModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("CP_dict_game_update", DBCommandType.Procedure);
        //id要填进去就设置为true
        DBUtils.addSpParameters(db, model, true);

        return db.executeNonQuery();
    }

    public DictGameModel getOne(long id) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("select * from dict_game where id = ?");
        db.addParameter(id);
        DataTable dt = db.executeQuery();
        return dt.rows.length>0 ? (DBUtils.convert2Model(DictGameModel.class, dt.rows[0])) : null;
    }

    public DataTable getList() {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("select *, (select remark from dict_game where id= t1.parentId) as preName from dict_game t1 where parentId != 0;");
        return db.executeQuery();
    }

    public DataTable listByGameIds(List<Long> gameIds) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuffer sb = new StringBuffer(128);
        sb.append("select * from dict_game where gameId in ")
                .append(SqlUtils.buildInConditions(gameIds.size()));

        for (long id : gameIds) {
            db.addParameter(id);
        }
        db.createCommand(sb.toString());
        return db.executeQuery();
    }

    public DataTable queryByGameId(int gameId) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("select * from dict_game where gameId= ?;");
        db.addParameter(gameId);
        return db.executeQuery();
    }

    public DataTable getByGameId(int gameId) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("select *, (select remark from dict_game where id= t1.parentId) as preName from dict_game t1 where gameId = ?;");
        db.addParameter(gameId);
        return db.executeQuery();
    }

    public String queryRemarkByGameId(int gameId) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("SELECT remark FROM dict_game WHERE id = (SELECT parentId FROM dict_game WHERE gameId= ? )");
        db.addParameter(gameId);

        DataTable dt = db.executeQuery();
        return dt.rows.length > 0 ? dt.rows[0].getColumnValue("remark") : "";
    }

    public DataTable getListByDepth(int depth, boolean isEnable) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("select * from dict_game where depth = ? and isEnable = ?");
        db.addParameter(Integer.valueOf(depth));
        db.addParameter(Boolean.valueOf(isEnable));
        return db.executeQuery();
    }

    public DataTable getUserInfoByUserId(long userId){
        DBHelper db = new DBHelper(_CONNECTION);
        db.createCommand("select * from u_user_info where userId = ? ");
        db.addParameter(userId);

        return db.executeQuery();
    }

    public DataTable getUserInfoByNickName(String nickName){
        DBHelper db = new DBHelper(_CONNECTION);
        db.createCommand("select * from u_user_info where nickName = ? ORDER BY userId");
        db.addParameter(nickName);

        return db.executeQuery();
    }

    //从合服的库读取钻石数据
    public int getPointByUserId(long userId){
        DBHelper db = new DBHelper(_CONNECTION);
        db.createCommand("select privateRoomDiamond from u_user_point where userId = ? ");
        db.addParameter(userId);

        DataTable dt = db.executeQuery();
        int result = dt.rows.length>0 ? CommonUtils.getIntValue(dt.rows[0].getColumnValue("privateRoomDiamond")) : 0;
        return result;
    }

    public DataTable getDTPointByUserId(long userId){
        DBHelper db = new DBHelper(_CONNECTION);
        db.createCommand("select privateRoomDiamond,userId from u_user_point where userId = ? ORDER BY userId");
        db.addParameter(userId);

        DataTable dt = db.executeQuery();
        return dt;
    }
    public int getPointByNickName(String nickName){
        DBHelper db = new DBHelper(_CONNECTION);
        db.createCommand("select privateRoomDiamond from u_user_point WHERE userId IN\n" +
                "(SELECT userId FROM  u_user_info  WHERE  nickName = ?) ORDER BY userId ");
        db.addParameter(nickName);

        DataTable dt = db.executeQuery();
        int result = dt.rows.length>0 ? CommonUtils.getIntValue(dt.rows[0].getColumnValue("privateRoomDiamond")) : 0;
        return result;
    }

    public DataTable getDTPointByNickName(String nickName){
        DBHelper db = new DBHelper(_CONNECTION);
        db.createCommand("select privateRoomDiamond,userId from u_user_point WHERE userId IN\n" +
                "(SELECT userId FROM  u_user_info  WHERE  nickName = ?) ORDER BY userId");
        db.addParameter(nickName);

        DataTable dt = db.executeQuery();
        return dt;
    }

    //从合服的库读取兑换券数据
    public int getPaperByUserId(long userId){
        DBHelper db = new DBHelper(_CONNECTION);
        db.createCommand("select paper from u_user_point where userId = ? ");
        db.addParameter(userId);

        DataTable dt = db.executeQuery();
        int result = dt.rows.length>0 ? CommonUtils.getIntValue(dt.rows[0].getColumnValue("paper")) : 0;
        return result;
    }

    public DataTable getDTPaperByUserId(long userId){
        DBHelper db = new DBHelper(_CONNECTION);
        db.createCommand("select paper,userId from u_user_point where userId = ? ORDER BY userId");
        db.addParameter(userId);

        DataTable dt = db.executeQuery();
        return dt;
    }

    public int getPaperByNickName(String nickName){
        DBHelper db = new DBHelper(_CONNECTION);
        db.createCommand("select paper from u_user_point WHERE userId IN\n" +
                "(SELECT userId FROM  u_user_info  WHERE  nickName = ?) ORDER BY userId ");
        db.addParameter(nickName);

        DataTable dt = db.executeQuery();
        int result = dt.rows.length>0 ? CommonUtils.getIntValue(dt.rows[0].getColumnValue("paper")) : 0;
        return result;
    }

    public DataTable getDTPaperByNickName(String nickName){
        DBHelper db = new DBHelper(_CONNECTION);
        db.createCommand("select paper,userId from u_user_point WHERE userId IN\n" +
                "(SELECT userId FROM  u_user_info  WHERE  nickName = ?) ORDER BY userId");
        db.addParameter(nickName);

        DataTable dt = db.executeQuery();
        return dt;
    }

    //获取钻石日志
    public DataTable getPropsLogByUserId(long userId){
        DBHelper db = new DBHelper(CONNECTION_LOG);
        db.createCommand("SELECT userId,nickName,propsBefore,propsAfter,propsNum,createTime FROM u_props_log WHERE userId = ? AND propsId = 10008 ORDER BY createTime DESC LIMIT 10");
        db.addParameter(userId);

        DataTable dt = db.executeQuery();
        return dt;
    }

    public DataTable getPropsLogByNickName(String nickName){
        DBHelper db = new DBHelper(CONNECTION_LOG);
        db.createCommand("SELECT userId,nickName,propsBefore,propsAfter,propsNum,createTime FROM u_props_log WHERE nickName = ? AND propsId = 10008 ORDER BY createTime DESC LIMIT 10");
        db.addParameter(nickName);

        DataTable dt = db.executeQuery();
        return dt;
    }

}
