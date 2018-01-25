package db_admin.dao;

import db_admin.impl.SysAuthDaoImpl;
import db_admin.model.SysAuthModel;
import dsqp.db.model.DataTable;

import java.util.Set;

/**
 * Created by ds on 2017/7/17.
 */
public class SysAuthDao {
    private static final SysAuthDaoImpl impl = new SysAuthDaoImpl();

    public static SysAuthModel getOne(long userId){
        return impl.getOne(userId);
    }

    public static int getRoleId(long userId){
        return impl.getRoleId(userId);
    }

    public static int add(long userId, int groupId) {
        return impl.add(userId, groupId);
    }

    public static int update(long userId, int roleId){
        return impl.update(userId, roleId);
    }

    public static int delete(long userId) {
        return impl.delete(userId);
    }

    public static Set getRoleIds(long userId){
        return impl.getRoleIds(userId);
    }

    public static DataTable listByGroupId(int groupId){
        return impl.listByGroupId(groupId);
    }

}
