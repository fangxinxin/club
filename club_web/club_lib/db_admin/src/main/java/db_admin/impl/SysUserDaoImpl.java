package db_admin.impl;

import db_admin.model.SysUserModel;
import dsqp.db.db.DBHelper;
import dsqp.db.model.DBCommandType;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db.util.SqlUtils;

import java.util.List;

public class SysUserDaoImpl {

    private static final String CONNECTION = "admin";


    public SysUserModel getOne(long id) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from sys_user where id = ? and locked = 0");

        db.addParameter(id);
        DataTable dt = db.executeQuery();
        if (dt.rows.length > 0) {
            return DBUtils.convert2Model(SysUserModel.class, dt.rows[0]);
        } else {
            return null;
        }
    }

    public SysUserModel getByUserName(String userName) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from sys_user where userName = ?");
        db.addParameter(userName);

        DataTable dt = db.executeQuery();
        return dt.rows.length>0 ? DBUtils.convert2Model(SysUserModel.class, dt.rows[0]) : null;
    }

    public SysUserModel getByRealName(String realName) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from sys_user where realName = ?");
        db.addParameter(realName);

        DataTable dt = db.executeQuery();
        return dt.rows.length>0 ? (DBUtils.convert2Model(SysUserModel.class, dt.rows[0])) : null;
    }

    public DataTable getByLogin(String userName,String loginPassword) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from sys_user where userName = ? and loginPassword = ?");
        db.addParameter(userName);
        db.addParameter(loginPassword);

        return db.executeQuery();
    }

    public int add(SysUserModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("INSERT INTO sys_user " +
                "(userName, realName, loginPassword, cellphone, locked, email, createTime, remark) VALUES (?, ?, ?, ?, 0, ?, now(), ?);");
        db.addParameter(model.getUserName());
        db.addParameter(model.getRealName());
        db.addParameter(model.getLoginPassword());
        db.addParameter(model.getCellphone());
        db.addParameter(model.getEmail());
        db.addParameter("--");

        return db.executeNonQuery();
    }

    public int update(SysUserModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("CP_sys_user_update", DBCommandType.Procedure);
        //id要填进去就设置为true
        DBUtils.addSpParameters(db, model, true);

        return db.executeNonQuery();
    }

    public int updatePassById(long id, String password) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("update sys_user set loginPassword = ? where id = ?;");
        db.addParameter(password);
        db.addParameter(id);
        return db.executeNonQuery();
    }

    public int updateRemark(long userId, String remark) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("update sys_user set remark = ? where id = ?;");
        db.addParameter(remark);
        db.addParameter(userId);
        return db.executeNonQuery();
    }

    public DataTable listByUserIds(List<Long> userIds) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuffer sb = new StringBuffer(128);
        sb.append("select * from sys_user where id in ")
                .append(SqlUtils.buildInConditions(userIds.size()));

        for (long id : userIds) {
            db.addParameter(id);
        }
        db.createCommand(sb.toString());
        return db.executeQuery();
    }
}
