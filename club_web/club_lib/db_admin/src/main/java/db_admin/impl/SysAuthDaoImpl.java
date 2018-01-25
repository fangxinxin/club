package db_admin.impl;

import com.google.common.collect.Sets;
import db_admin.model.SysAuthModel;
import dsqp.db.db.DBHelper;
import dsqp.db.model.DataRow;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.util.CommonUtils;

import java.util.Set;

/**
 * 用户授权
 * Created by ds on 2017/7/17.
 */
public class SysAuthDaoImpl {

    private static final String CONNECTION = "admin";

    public SysAuthModel getOne(long userId){
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM sys_auth WHERE userId = ?;");
        db.addParameter(userId);

        DataTable dt = db.executeQuery();

        return dt.rows.length > 0 ? DBUtils.convert2Model(SysAuthModel.class, dt.rows[0]) : null;
    }


    public int add(long userId, int groupId) {
        DBHelper db = new DBHelper(CONNECTION);

        SysAuthModel model = new SysAuthModel();
        model.setGroupId(groupId);
        model.setUserId(userId);
        model.setRoleId(0);
        model.setPlatformId(0);

        db.createCommand("INSERT INTO sys_auth VALUES(null,?,?,?,?)");
        DBUtils.addSqlParameters(db, model);

        return db.executeNonQuery();
    }

    public int delete(long userId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("delete from sys_auth where userId = ?;");
        db.addParameter(userId);

        return db.executeNonQuery();
    }

    public int getRoleId(long userId){
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT roleId FROM sys_auth WHERE userId = ?;");
        db.addParameter(userId);

        DataTable dt = db.executeQuery();

        return dt.rows.length > 0 ? CommonUtils.getIntValue(dt.rows[0].getColumnValue("roleId")) : 0;
    }


    public Set getRoleIds(long userId){
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT roleId FROM sys_auth WHERE userId = ?;");
        db.addParameter(userId);
        Set set = Sets.newHashSet();

        DataTable dt = db.executeQuery();
        if (dt.rows.length > 0) {
            for (DataRow row: dt.rows) {
                set.add(row.getColumnValue("roleId"));
            }
        }

        return set;
    }

    public DataTable listByGroupId(int groupId){
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM sys_auth WHERE groupId = ?;");
        db.addParameter(groupId);

        return db.executeQuery();
    }

    public int update(long userId, int roleId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("UPDATE sys_auth SET roleId = ? WHERE userId = ?;");
        db.addParameter(roleId);
        db.addParameter(userId);

        return db.executeNonQuery();
    }
}
