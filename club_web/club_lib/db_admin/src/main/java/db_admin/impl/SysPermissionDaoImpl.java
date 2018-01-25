package db_admin.impl;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db.util.SqlUtils;

import java.util.Set;

/**
 * 授权
 * Created by ds on 2017/7/17.
 */
public class SysPermissionDaoImpl {

    private static final String CONNECTION = "admin";

    //列出当前账户，权限
    public DataTable listPermissions(Set<String> permissionIds){
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sb = new StringBuilder(128);

        sb.append("SELECT * FROM sys_permission WHERE id in ").append(SqlUtils.buildInConditions(permissionIds.size()));

        for (String id : permissionIds) {
            db.addParameter(id);
        }

        db.createCommand(sb.toString());

        return db.executeQuery();
    }

}
