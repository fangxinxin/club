package db_admin.impl;

import dsqp.common_const.club_admin.PowerTBName;
import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;

import java.util.Set;

/**
 * 管理员 权限分配
 * Created by ds on 2017/7/17.
 */
public class SysRolePermissionDaoImpl {

    private static final String CONNECTION = "admin";

    public int addArea(int roleId, Set<String> set) {
        DBHelper db = new DBHelper(CONNECTION);

        int result = 0;
        if (set.size() > 0) {
            StringBuilder sb = new StringBuilder(128);
            sb.append("INSERT IGNORE INTO sys_role_area (roleId,areaId) VALUES ");
            for (String id: set) {
                sb.append("(?,?)").append(",");
                db.addParameter(roleId);
                db.addParameter(id);
            }

            db.createCommand(sb.substring(0, sb.length() - 1));
            result = db.executeNonQuery();
        }

        return result;
    }

    public int addMenu(int roleId, Set<String> set) {
        DBHelper db = new DBHelper(CONNECTION);

        int result = 0;
        if (set.size() > 0) {
            StringBuilder sb = new StringBuilder(128);
            sb.append("INSERT IGNORE INTO sys_role_menu (roleId,menuId) VALUES ");
            for (String id: set) {
                sb.append("(?,?)").append(",");
                db.addParameter(roleId);
                db.addParameter(id);
            }

            db.createCommand(sb.substring(0, sb.length() - 1));
            result = db.executeNonQuery();
        }

        return result;
    }

    public int addPermission(int roleId, Set<String> set) {
        DBHelper db = new DBHelper(CONNECTION);

        int result = 0;
        if (set.size() > 0) {
            StringBuilder sb = new StringBuilder(128);
            sb.append("INSERT IGNORE INTO sys_role_permission_menu (roleId,permissionMenuId) VALUES ");
            for (String id: set) {
                sb.append("(?,?)").append(",");
                db.addParameter(roleId);
                db.addParameter(id);
            }

            db.createCommand(sb.substring(0, sb.length() - 1));
            result = db.executeNonQuery();
        }

        return result;
    }

    public Set<String> getAreaIds(int roleId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT areaId FROM sys_role_area WHERE roleId = ?;");
        db.addParameter(roleId);
        DataTable dt = db.executeQuery();

        return DBUtils.split(dt, "areaId");
    }

    public Set<String> getMenuIds(int roleId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT menuId FROM sys_role_menu WHERE roleId = ?;");
        db.addParameter(roleId);
        DataTable dt = db.executeQuery();

        return DBUtils.split(dt, "menuId");
    }

    public Set<String> getPermissionMenuIds(int roleId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT permissionMenuId FROM sys_role_permission_menu WHERE roleId = ?;");
        db.addParameter(roleId);
        DataTable dt = db.executeQuery();

        return DBUtils.split(dt, "permissionMenuId");
    }

    public int delete(int roleId, String tableName) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("DELETE FROM "+ tableName +" WHERE roleId = ?;");
        db.addParameter(roleId);

        return db.executeNonQuery();
    }

    public boolean isExist(int id, int roleId, String tableName) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sql = new StringBuilder(64);
        sql.append("SELECT id FROM "+ tableName);
        if (tableName.equals(PowerTBName.TB_AREA)) {
            sql.append(" WHERE areaId = ? AND roleId = ?;");
        }
        else if (tableName.equals(PowerTBName.TB_MENU)) {
            sql.append(" WHERE menuId = ? AND roleId = ?;");
        }
        else if(tableName.equals(PowerTBName.TB_PERMISSION)) {
            sql.append(" WHERE permissionMenuId = ? AND roleId = ?;");
        }

        db.createCommand(sql.toString());
        db.addParameter(id);
        db.addParameter(roleId);
        DataTable dt = db.executeQuery();

        return dt.rows.length>0;
    }

/*    public int update(int roleId, String areaIds, String menuIds, String permissionMenuIds) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sb = new StringBuilder(64);
        sb.append("UPDATE sys_role_permission_menu SET ");
        if (!Strings.isNullOrEmpty(areaIds)) {
            sb.append("areaIds = ? ");
            db.addParameter(areaIds);
        }
        if (!Strings.isNullOrEmpty(menuIds)) {
            if(!Strings.isNullOrEmpty(areaIds)) {
                sb.append(", ");
            }
            sb.append("menuIds = ? ");
            db.addParameter(menuIds);
        }
        if (!Strings.isNullOrEmpty(permissionMenuIds)) {
            if(!Strings.isNullOrEmpty(areaIds) || !Strings.isNullOrEmpty(menuIds)) {
                sb.append(", ");
            }
            sb.append("permissionMenuIds = ? ");
            db.addParameter(permissionMenuIds);
        }


        sb.append("WHERE roleId = ?;");
        db.addParameter(roleId);
        db.createCommand(sb.toString());

        return db.executeNonQuery();
    }*/

}
