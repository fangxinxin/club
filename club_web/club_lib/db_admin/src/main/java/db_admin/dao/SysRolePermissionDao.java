package db_admin.dao;

import db_admin.impl.SysRolePermissionDaoImpl;

import java.util.Set;

/**
 * Created by ds on 2017/7/17.
 */
public class SysRolePermissionDao {
    private static final SysRolePermissionDaoImpl impl = new SysRolePermissionDaoImpl();

    public static int addArea(int roleId, Set<String> set) {
        return impl.addArea(roleId, set);
    }

    public static int addMenu(int roleId, Set<String> set) {
        return impl.addMenu(roleId, set);
    }

    public static int addPermission(int roleId, Set<String> set) {
        return impl.addPermission(roleId, set);
    }

    public static Set<String> getAreaIds(int roleId) {
        return impl.getAreaIds(roleId);
    }

    public static Set<String> getMenuIds(int roleId) {
        return impl.getMenuIds(roleId);
    }

    public static Set<String> getPermissionMenuIds(int roleId) {
        return impl.getPermissionMenuIds(roleId);
    }

    public static int delete(int roleId, String tableName) {
        return impl.delete(roleId, tableName);
    }

    public static boolean isExist(int id, int roleId, String tableName) {
        return impl.isExist(id, roleId, tableName);
    }


/*    public static int update(int roleId, String areaIds, String menuIds, String permissionIds){
        return impl.update(roleId, areaIds, menuIds, permissionIds);
    }*/


}
