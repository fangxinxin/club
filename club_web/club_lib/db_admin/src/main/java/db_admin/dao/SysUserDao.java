package db_admin.dao;


import db_admin.impl.SysUserDaoImpl;
import db_admin.model.SysUserModel;
import dsqp.db.model.DataTable;

import java.util.List;

/**
 * Created by ds on 2017/6/21.
 */
public class SysUserDao {

    private static final SysUserDaoImpl impl = new SysUserDaoImpl();

    public static SysUserModel getByUserName(String userName) {
        return impl.getByUserName(userName);
    }

    public static SysUserModel getByRealName(String realName) {
        return impl.getByRealName(realName);
    }

    public static DataTable getByLogin(String userName,String loginPassword) {
        return impl.getByLogin(userName, loginPassword);
    }

    public static int add(SysUserModel model) {
        return impl.add(model);
    }

    public static int update(SysUserModel model) {
        return impl.update(model);
    }

    public static SysUserModel getOne(long id) {
        return impl.getOne(id);
    }

//    public static DataTable getList() {
//        return impl.getList();
//    }

    public static DataTable listByUserIds(List<Long> userIds) {
        return impl.listByUserIds(userIds);
    }

    /** 修改密码 **/
    public static int updatePassById(long id, String password) {
        return impl.updatePassById(id, password);
    }

    /** 修改角色 **/
    public static int updateRemark(long userId, String remark) {
        return impl.updateRemark(userId, remark);
    }

}
