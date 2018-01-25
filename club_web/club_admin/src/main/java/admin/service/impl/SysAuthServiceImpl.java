package admin.service.impl;

import admin.service.SysAuthService;
import com.google.common.base.Strings;
import db_admin.dao.*;
import db_admin.model.SysMenuModel;
import db_admin.model.SysRoleModel;
import db_admin.model.SysUserModel;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static db_admin.dao.SysAuthDao.getRoleId;
import static db_admin.dao.SysRolePermissionDao.getAreaIds;
import static db_admin.dao.SysRolePermissionDao.getPermissionMenuIds;


@Service
public class SysAuthServiceImpl implements SysAuthService {

    @Override
    public DataTable getGames(int roleId) {
        Subject subject = SecurityUtils.getSubject();
        SysUserModel user = (SysUserModel) subject.getPrincipal();
        String userName = user.getUserName();

        if (userName.equalsIgnoreCase("admin")) {
            DataTable gameList = SysAreaDao.listAreas(true);

            return gameList;
        } else {
            Set<String> areaIds = SysRolePermissionDao.getAreaIds(roleId);
            DataTable gameList = SysAreaDao.listAreas(areaIds, true);

            return gameList;
        }

    }


    @Override
    public String getRole(long userId) {
        SysRoleModel roleModel = SysRoleDao.getOne(getRoleId(userId), true);
        return roleModel != null ? roleModel.getRole() : "";
    }

    @Override
    public List<String> listPermissions(int roleId) {
        List<String> list = null;
        Set<String> set = getPermissionMenuIds(roleId);
        if (set != null) {
            DataTable permissionMenuDt = SysPermissionMenuDao.listPermissionMenus(set, true);
            set = DBUtils.split(permissionMenuDt, "permissionId");
            DataTable permissionDt = SysPermissionDao.listPermissions(set);
            list = DBUtils.convert2List(String.class, "permission", permissionDt);
        }
        return list;
    }

    @Override
    public List<String> listAreas(int roleId) {
        List<String> list = null;
        Set<String> set = getAreaIds(roleId);
        if (set != null) {
            DataTable gameDt = SysAreaDao.listAreas(set, true);
            DBUtils.convert2List(String.class, "area", gameDt);
        }
        return list;
    }

    @Override
    public DataTable listRootMenus(int roleId) {
        Subject subject = SecurityUtils.getSubject();
        SysUserModel user = (SysUserModel) subject.getPrincipal();
        String userName = user.getUserName();

        if (userName.equalsIgnoreCase("admin")) {
            return SysMenuDao.listRootMenus(0, true);
        } else {
            return SysMenuDao.listRootMenus(roleId, 0, true);
        }

    }

    @Override
    public DataTable listCurrentMenus(int roleId, String menu) {
        Subject subject = SecurityUtils.getSubject();
        SysUserModel user = (SysUserModel) subject.getPrincipal();
        String userName = user.getUserName();

        if (userName.equalsIgnoreCase("admin")) {

            if (Strings.isNullOrEmpty(menu)) {
                DataTable menuList = SysMenuDao.listRootMenus(0, true);
                menu = menuList.rows[0].getColumnValue("menu");
            }

            SysMenuModel model = SysMenuDao.getCurrentMenu(menu, true);

            return SysMenuDao.listCurrentMenus(model.getPriority(), true);
        } else {

            if (Strings.isNullOrEmpty(menu)) {
                DataTable menuList = SysMenuDao.listRootMenus(roleId, 0, true);
                menu = menuList.rows[0].getColumnValue("menu");
            }

            SysMenuModel model = SysMenuDao.getCurrentMenu(roleId, menu, true);

            return SysMenuDao.listCurrentMenus(roleId, model.getPriority(), true);
        }

    }

}
