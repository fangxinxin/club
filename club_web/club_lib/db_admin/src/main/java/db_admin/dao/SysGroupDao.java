package db_admin.dao;

import db_admin.impl.SysGroupDaoImpl;
import db_admin.model.SysGroupModel;
import dsqp.db.model.DataTable;

/**
 * Created by ds on 2017/9/19.
 */
public class SysGroupDao {
    private static final SysGroupDaoImpl impl = new SysGroupDaoImpl();

    public static SysGroupModel getOne(int id, boolean available){
        return impl.getOne(id, available);
    }

    public static int add(String name, int priority){
        return impl.add(name, priority);
    }

    public static int update(int id, String name, int priority){
        return impl.update(id, name, priority);
    }

//    public static int delete(long groupId) {
//        return impl.delete(groupId);
//    }

    public static DataTable listGroup(boolean available){
        return impl.listGroup(available);
    }

}
