package db_admin.dao;

import db_admin.impl.SysPermissionMenuDaoImpl;
import dsqp.db.model.DataTable;

import java.util.List;
import java.util.Set;

/**
 * 授权
 * Created by ds on 2017/7/17.
 */
public class SysPermissionMenuDao {

    private static final SysPermissionMenuDaoImpl impl = new SysPermissionMenuDaoImpl();

    public static DataTable listPermissionMenus(boolean available) {
        return impl.listPermissionMenus(available);
    }

    //列出当前账户，权限
    public static DataTable listPermissionMenus(Set<String> permissionIds, boolean available){
        return impl.listPermissionMenus(permissionIds, available);
    }

    //获取父节点
    public static List<String> getAllIds(String permissionIds) {
        return impl.getAllIds(permissionIds);
    }

}
