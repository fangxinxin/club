package admin.service.impl;

import admin.service.SysCfgPowerService;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import db_admin.dao.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static dsqp.common_const.club_admin.PowerTBName.*;


@Service
public class SysCfgPowerServiceImpl implements SysCfgPowerService {

    /**
     * 配置角色权限：
     * 角色
     *  1）不存在，添加并配置设置
     *  2）已存在，修改已有配置
     * @param roleId
     * @param areaIds
     * @param menuIds
     * @param permissionIds
     * @return
     */
    @Override
    public int cfgPower(int roleId, String areaIds, String menuIds, String permissionIds) {
        int result = 0;

        SysRolePermissionDao.delete(roleId, TB_AREA);
        SysRolePermissionDao.delete(roleId, TB_MENU);
        SysRolePermissionDao.delete(roleId, TB_PERMISSION);

        if (!Strings.isNullOrEmpty(areaIds)) {
            Set areaIdsTr = getTree(areaIds);
//            Set areaIdsTr = getTree(areaIds, SysAreaDao.getAllIds(areaIds));

            result = SysRolePermissionDao.addArea(roleId, areaIdsTr);
        }
        if (!Strings.isNullOrEmpty(menuIds)) {
            Set menuIdsTr = getTree(menuIds, SysMenuDao.getAllIds(menuIds));
            result = SysRolePermissionDao.addMenu(roleId, menuIdsTr);
        }
        if (!Strings.isNullOrEmpty(permissionIds)) {
            Set permissionIdsTr = getTree(permissionIds);
            permissionIdsTr.remove("1");
            permissionIdsTr.remove("2");

            result = SysRolePermissionDao.addPermission(roleId, permissionIdsTr);
        }

        return result;
    }

    @Override
    public int delete(int roleId) {
        int result = 0;
        if (roleId != 0) {
            result = SysRoleDao.delete(roleId);
            if (result > 0) {
                SysRolePermissionDao.delete(roleId, TB_AREA);
                SysRolePermissionDao.delete(roleId, TB_MENU);
                SysRolePermissionDao.delete(roleId, TB_PERMISSION);
            }
        }
        return result;
    }


    public static Set<String> getTree(String str, List<String> list) {

        StringBuilder sb = new StringBuilder(128);
        String listStr = list.toString();

        sb.append(str).append(",").append(listStr.substring(1, listStr.length() - 1));

        Set<String> set = Sets.newHashSet(sb.toString().replaceAll("\\s*", "").split(","));
        set.remove("0");

        return set;
    }

    public static Set<String> getTree(String str) {

        StringBuilder sb = new StringBuilder(128);

        Set<String> set = Sets.newHashSet(sb.append(str).toString().replaceAll("\\s*", "").split(","));
        set.remove("0");

        return set;
    }

}
