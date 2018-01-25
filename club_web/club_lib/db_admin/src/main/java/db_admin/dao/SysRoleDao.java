package db_admin.dao;

import db_admin.impl.SysRoleDaoImpl;
import db_admin.model.SysRoleModel;
import dsqp.db.model.DataTable;

import java.util.Set;

/**
 * Created by ds on 2017/9/19.
 */
public class SysRoleDao {
    private static final SysRoleDaoImpl impl = new SysRoleDaoImpl();


    public static SysRoleModel getOne(int id, boolean available){
        return impl.getOne(id, available);
    }

    public static int add(SysRoleModel model){
        return impl.add(model);
    }

    public static int update(int id, String role, String name, int priority){
        return impl.update(id, role, name, priority);
    }

    public static int delete(int id){
        return impl.delete(id);
    }

    public static DataTable listRoles(int groupId, boolean available){
        return impl.listRoles(groupId, available);
    }

    public static DataTable listRoles(Set<String> roleIds, boolean available){
        return impl.listRoles(roleIds, available);
    }

}
