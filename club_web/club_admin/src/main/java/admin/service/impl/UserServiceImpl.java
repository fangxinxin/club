package admin.service.impl;

import admin.service.UserService;
import db_admin.dao.SysUserDao;
import db_admin.model.SysUserModel;
import dsqp.db.model.DataTable;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Override
    public int updatePassword(String userName, String md5Password) {
        SysUserModel model = SysUserDao.getByUserName(userName);
        model.setLoginPassword(md5Password);

        return SysUserDao.update(model);
    }

    @Override
    public boolean login(String userName, String password) {
        DataTable dt = SysUserDao.getByLogin(userName, DigestUtils.md5Hex(password));
        if(dt.rows.length>0){
            return true;
        }
        return false;
    }
}
