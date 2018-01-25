package db_admin.dao;

import db_admin.impl.SysPermissionDaoImpl;
import dsqp.db.model.DataTable;

import java.util.Set;

/**
 * 授权
 * Created by ds on 2017/7/17.
 */
public class SysPermissionDao {

    private static final SysPermissionDaoImpl impl = new SysPermissionDaoImpl();

    //列出当前账户，权限
    public static DataTable listPermissions(Set<String> permissionIds){
        return impl.listPermissions(permissionIds);
    }


}
