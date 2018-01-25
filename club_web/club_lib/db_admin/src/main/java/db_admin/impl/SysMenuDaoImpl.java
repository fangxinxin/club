package db_admin.impl;

import com.google.common.collect.Lists;
import db_admin.model.SysMenuModel;
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
public class SysMenuDaoImpl {

    private static final String CONNECTION = "admin";

    //根据父级列出菜单
    public DataTable listRootMenus(int parentId, boolean available) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql =
                "SELECT * FROM sys_menu t1 WHERE parentId = ? AND available = ? ORDER BY sequence;";
        db.addParameter(parentId);
        db.addParameter(available);

        db.createCommand(sql);

        return db.executeQuery();
    }
    public DataTable listRootMenus(int roleId, int parentId, boolean available) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql =
                "SELECT * FROM sys_menu t1 WHERE parentId = ? AND available = ? AND id in " +
                        "(SELECT menuId FROM sys_role_menu t2 WHERE roleId = ? AND t1.id = t2.menuId) " +
                "ORDER BY sequence;";
        db.addParameter(parentId);
        db.addParameter(available);
        db.addParameter(roleId);

        db.createCommand(sql);

        return db.executeQuery();
    }

    //获取当前菜单
    public SysMenuModel getCurrentMenu(String menu, boolean available) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "SELECT * FROM sys_menu WHERE menu = ? AND available = ? ORDER BY sequence;";
        db.addParameter(menu);
        db.addParameter(available);

        db.createCommand(sql);

        DataTable dt = db.executeQuery();
        return dt.rows.length > 0 ? DBUtils.convert2Model(SysMenuModel.class, dt.rows[0]) : null;

    }
    public SysMenuModel getCurrentMenu(int roleId, String menu, boolean available) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql =
                "SELECT * FROM sys_menu t1 WHERE menu = ? AND available = ? AND id in " +
                        "(SELECT menuId FROM sys_role_menu t2 WHERE roleId = ? AND t1.id = t2.menuId) " +
                "ORDER BY sequence;";
        db.addParameter(menu);
        db.addParameter(available);
        db.addParameter(roleId);

        db.createCommand(sql);

        DataTable dt = db.executeQuery();
        return dt.rows.length > 0 ? DBUtils.convert2Model(SysMenuModel.class, dt.rows[0]) : null;

    }

    //列出当前菜单
    public DataTable listCurrentMenus(int priority, boolean available) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql =
                "SELECT * FROM sys_menu t1 WHERE priority = ? AND available = ? ORDER BY sequence;";
        db.addParameter(priority);
        db.addParameter(available);

        db.createCommand(sql);

        return db.executeQuery();
    }
    public DataTable listCurrentMenus(int roleId, int priority, boolean available) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql =
                "SELECT * FROM sys_menu t1 WHERE priority = ? AND available = ? AND id in " +
                        "(SELECT menuId FROM sys_role_menu t2 WHERE roleId = ? AND t1.id = t2.menuId) " +
                        "ORDER BY sequence;";
        db.addParameter(priority);
        db.addParameter(available);
        db.addParameter(roleId);

        db.createCommand(sql);

        return db.executeQuery();
    }

    //列出所有菜单
    public DataTable listMenus(Set<String> menuIds, boolean available) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sb = new StringBuilder(128);
        sb.append("SELECT * FROM sys_menu WHERE available = ? ")
                .append("AND id in ").append(SqlUtils.buildInConditions(menuIds.size()))
                .append(" ORDER BY sequence;");

        db.addParameter(available);
        for (String id : menuIds) {
            db.addParameter(id);
        }

        db.createCommand(sb.toString());

        return db.executeQuery();
    }

    public int add(SysMenuModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "INSERT INTO sys_menu VALUES (null, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        db.createCommand(sql);
        DBUtils.addSpParameters(db, model);

        return db.executeNonQuery();
    }


    public DataTable listMenus(boolean available) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM sys_menu WHERE available = ? ORDER BY sequence;");
        db.addParameter(available);

        return db.executeQuery();
    }


    public List<String> getAllIds(String menuIds) {
        DBHelper db = new DBHelper(CONNECTION);

        List<String> s = Lists.newArrayList(menuIds.split(","));

        StringBuilder sb = new StringBuilder(128);
        sb.append("SELECT DISTINCT parentIds FROM sys_menu WHERE id in ")
                .append(SqlUtils.buildInConditions(s.size()));

        for (String id : s) {
            db.addParameter(id);
        }

        db.createCommand(sb.toString());
        DataTable dt = db.executeQuery();

        return dt.rows.length>0 ? DBUtils.convert2List(String.class, "parentIds", dt) : null;
    }
}
