package db_admin.impl;

import com.google.common.base.Strings;
import db_admin.model.SysRoleModel;
import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db.util.SqlUtils;

import java.util.Set;

/**
 * 授权
 * Created by ds on 2017/7/17.
 */
public class SysRoleDaoImpl {

    private static final String CONNECTION = "admin";

    public DataTable listRoles(int groupId, boolean available){
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM sys_role WHERE groupId = ? AND available = ? ORDER BY priority;");
        db.addParameter(groupId);
        db.addParameter(available);

        return db.executeQuery();
    }

    //列出当前账户，角色
    public DataTable listRoles(Set<String> roleIds, boolean available){
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sb = new StringBuilder(128);

        sb.append("SELECT * FROM sys_role WHERE available = ? ")
                .append("AND id in ").append(SqlUtils.buildInConditions(roleIds.size()));

        db.addParameter(available);
        for (String id : roleIds) {
            db.addParameter(id);
        }

        db.createCommand(sb.toString());

        return db.executeQuery();
    }

    public int add(SysRoleModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("INSERT INTO sys_role VALUES(null,?,?,?,?,?)");
        DBUtils.addSqlParameters(db, model);

        int result = db.executeNonQuery();
        model.setId(db.generatedValue);
        return result;
    }

    public int update(int id, String role, String name, int priority) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sb = new StringBuilder(64);
        sb.append("UPDATE sys_role ");
        if (!Strings.isNullOrEmpty(role)) {
            sb.append("SET role = ? ");
            db.addParameter(role);
        }
        if (!Strings.isNullOrEmpty(name)) {
            sb.append(", name = ? ");
            db.addParameter(name);
        }
        if (priority != 0) {
            sb.append(", priority = ? ");
            db.addParameter(priority);
        }

        sb.append("WHERE id = ?;");
        db.addParameter(id);
        db.createCommand(sb.toString());

        return db.executeNonQuery();
    }

    public SysRoleModel getOne(int id, boolean available) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM sys_role WHERE id = ? AND available = ?;");
        db.addParameter(id);
        db.addParameter(available);

        DataTable dt = db.executeQuery();

        return dt.rows.length>0 ? DBUtils.convert2Model(SysRoleModel.class, dt.rows[0]) :null;
    }

    public int delete(int id) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("DELETE FROM sys_role WHERE id = ?;");
        db.addParameter(id);
        return db.executeNonQuery();
    }
}
