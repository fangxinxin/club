package db_admin.dao;

import db_admin.impl.SysMenuDaoImpl;
import db_admin.model.SysMenuModel;
import dsqp.db.model.DataTable;

import java.util.List;
import java.util.Set;

/**
 * 授权
 * Created by ds on 2017/7/17.
 */
public class SysMenuDao {

    private static final SysMenuDaoImpl impl = new SysMenuDaoImpl();



    //根据父级列出菜单
    public static DataTable listRootMenus(int parentId, boolean available) {
        return impl.listRootMenus(parentId, available);
    }
    public static DataTable listRootMenus(int roleId, int parentId, boolean available) {
        return impl.listRootMenus(roleId, parentId, available);
    }

    //获取当前菜单
    public static SysMenuModel getCurrentMenu(String menu, boolean available) {
        return impl.getCurrentMenu(menu, available);
    }
    public static SysMenuModel getCurrentMenu(int roleId, String menu, boolean available) {
        return impl.getCurrentMenu(roleId, menu, available);
    }

    //列出当前菜单
    public static DataTable listCurrentMenus(int priority, boolean available) {
        return impl.listCurrentMenus(priority, available);
    }
    public static DataTable listCurrentMenus(int roleId, int priority, boolean available) {
        return impl.listCurrentMenus(roleId, priority, available);
    }

    //列出授权的所有菜单
    public static DataTable listMenus(Set<String> menuIds, boolean available) {
        return impl.listMenus(menuIds, available);
    }

    //获取父节点
    public static List<String> getAllIds(String menuIds) {
        return impl.getAllIds(menuIds);
    }

    //添加菜单
    public static int add(SysMenuModel model) {
        return impl.add(model);
    }

    //列出所有菜单
    public static DataTable listMenus(boolean available) {
        return impl.listMenus(available);
    }

}
