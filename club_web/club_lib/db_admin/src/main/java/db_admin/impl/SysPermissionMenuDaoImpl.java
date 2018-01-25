package db_admin.impl;

import com.google.common.collect.Lists;
import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db.util.SqlUtils;

import java.util.List;
import java.util.Set;

/**
 * 授权
 * Created by ds on 2017/7/17.
 */
public class SysPermissionMenuDaoImpl {

    private static final String CONNECTION = "admin";

    //列出所有权限
    public DataTable listPermissionMenus(boolean available) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM sys_permission_menu WHERE available = ? ORDER BY priority;");
        db.addParameter(available);

        return db.executeQuery();
    }

    //列出当前账户，权限
    public DataTable listPermissionMenus(Set<String> permissionIds, boolean available){
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sb = new StringBuilder(128);

        sb.append("SELECT * FROM sys_permission_menu WHERE available = ? ")
                .append("AND id in ").append(SqlUtils.buildInConditions(permissionIds.size()))
                .append(" ORDER BY priority;");

        db.addParameter(available);
        for (String id : permissionIds) {
            db.addParameter(id);
        }

        db.createCommand(sb.toString());

        return db.executeQuery();
    }

    public List<String> getAllIds(String permissionIds) {
        DBHelper db = new DBHelper(CONNECTION);

        List<String> s = Lists.newArrayList(permissionIds.split(","));

        StringBuilder sb = new StringBuilder(128);
        sb.append("SELECT DISTINCT parentIds FROM sys_permission_menu WHERE id in ")
                .append(SqlUtils.buildInConditions(s.size()));

        for (String id : s) {
            db.addParameter(id);
        }

        db.createCommand(sb.toString());
        DataTable dt = db.executeQuery();

        return dt.rows.length>0 ? DBUtils.convert2List(String.class, "parentIds", dt) : null;
    }
}
