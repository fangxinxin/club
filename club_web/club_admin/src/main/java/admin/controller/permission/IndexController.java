package admin.controller.permission;

import admin.service.SysCfgPowerService;
import admin.vo.JsTreeVO;
import admin.vo.State;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import db_admin.dao.*;
import db_admin.model.SysRoleModel;
import db_admin.model.SysUserModel;
import dsqp.common_const.club_admin.Permission;
import dsqp.common_const.club_admin.PowerTBName;
import dsqp.db.model.DataRow;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.util.CommonUtils;
import dsqp.util.JsonUtils;
import dsqp.util.RequestUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by ds on 2017/9/12.
 */
@Controller("permission")
@RequestMapping("permission")
public class IndexController {
    private static final String PATH = "permission/";
    @Autowired
    private SysCfgPowerService sysCfgPowerService;

    @RequiresPermissions(Permission.OPERATE_MANAGE_ADMIN_ACTION)
    @RequestMapping("index")
    public String index() {
        return PATH+"index";
    }

    /**
     * 用户组
     * @return
     */
    @RequiresPermissions(Permission.OPERATE_MANAGE_ADMIN_ACTION)
    @RequestMapping("showGroup")
    public ModelAndView showGroup() {

        ModelAndView mv = new ModelAndView(PATH+"ajax/table");

        DataTable dt = SysGroupDao.listGroup(true);
        mv.addObject("isGroup", true);

        if (dt.rows.length > 0) {
            mv.addObject("groups", dt.rows);
        }

        return mv;
    }

    @RequiresPermissions(Permission.OPERATE_MANAGE_ADMIN_ACTION)
    @RequestMapping("addGroup")
    @ResponseBody
    public void addGroup(HttpServletResponse response
            , @RequestParam(value = "name", defaultValue = "") String name
            , @RequestParam(value = "priority", defaultValue = "") int priority ) {

        int result = 0;

        if (!Strings.isNullOrEmpty(name) && priority != 0) {
            result = SysGroupDao.add(name, priority);
        }

        RequestUtils.write(response, CommonUtils.getStringValue(result));
    }

    @RequiresPermissions(Permission.OPERATE_MANAGE_ADMIN_ACTION)
    @RequestMapping("editGroup")
    @ResponseBody
    public void editGroup(HttpServletResponse response
            , @RequestParam(value = "id", defaultValue = "0") int id
            , @RequestParam(value = "name", defaultValue = "") String name
            , @RequestParam(value = "priority", defaultValue = "0") int priority) {

        int result = 0;

        if (id != 0 && !Strings.isNullOrEmpty(name) || priority != 0) {
            result = SysGroupDao.update(id, name, priority);
        }

        RequestUtils.write(response, CommonUtils.getStringValue(result));
    }


//    @RequestMapping("removeGroup")
//    @ResponseBody
//    public void removeGroup(HttpServletResponse response
//            , @RequestParam(value = "id", defaultValue = "") long id ) {
//
//        int result = 0;
//
//        if (id != 0) {
//            result = SysGroupDao.delete(id);
//        }
//
//        RequestUtils.write(response, CommonUtils.getStringValue(result));
//    }


    /** 角色配置 **/
    @RequiresPermissions(Permission.OPERATE_MANAGE_ADMIN_ACTION)
    @RequestMapping("roleGroup")
    @ResponseBody
    public ModelAndView roleGroup(
            @RequestParam(value = "groupId", defaultValue = "0") int groupId) {
        ModelAndView mv = new ModelAndView(PATH + "ajax/table");

        DataTable dt = SysRoleDao.listRoles(groupId, true);
        mv.addObject("isRoleGroup", true);
        mv.addObject("groupId", groupId);
        mv.addObject("url", "permission/roleGroup?groupId="+groupId);

        if (dt.rows.length > 0) {
            mv.addObject("roleGroup", dt.rows);
        }

        return mv;
    }

    @RequiresPermissions(Permission.OPERATE_MANAGE_ADMIN_ACTION)
    @RequestMapping("addRole")
    @ResponseBody
    public void addRole(HttpServletResponse response
            , @RequestParam(value = "role", defaultValue = "") String role
            , @RequestParam(value = "name", defaultValue = "") String name
            , @RequestParam(value = "groupId", defaultValue = "") int groupId
            , @RequestParam(value = "priority", defaultValue = "") int priority ) {

        int result = 0;

        if (!Strings.isNullOrEmpty(role) && !Strings.isNullOrEmpty(name) && priority != 0 && groupId != 0) {
            SysRoleModel model = new SysRoleModel();
            model.setRole(role);
            model.setName(name);
            model.setGroupId(groupId);
            model.setPriority(priority);
            model.setAvailable(true);

            result = SysRoleDao.add(model);
        }

        RequestUtils.write(response, CommonUtils.getStringValue(result));
    }

    @RequiresPermissions(Permission.OPERATE_MANAGE_ADMIN_ACTION)
    @RequestMapping("editRole")
    @ResponseBody
    public void editRole(HttpServletResponse response
            , @RequestParam(value = "id", defaultValue = "0") int id
            , @RequestParam(value = "role", defaultValue = "") String role
            , @RequestParam(value = "name", defaultValue = "") String name
            , @RequestParam(value = "priority", defaultValue = "0") int priority) {

        int result = 0;

        if (id != 0) {
            result = SysRoleDao.update(id, role, name, priority);
        }

        RequestUtils.write(response, CommonUtils.getStringValue(result));
    }

    @RequiresPermissions(Permission.OPERATE_MANAGE_ADMIN_ACTION)
    @RequestMapping("deleteRole")
    @ResponseBody
    public void deleteRole(HttpServletResponse response
            , @RequestParam(value = "id", defaultValue = "0") int id) {

        int result = sysCfgPowerService.delete(id);

        RequestUtils.write(response, CommonUtils.getStringValue(result));
    }

    /**
     * 权限
     * @param roleId
     * @return
     */
    @RequiresPermissions(Permission.OPERATE_MANAGE_ADMIN_ACTION)
    @RequestMapping("powerGroup")
    public ModelAndView powerGroup(
            @RequestParam(value = "roleId", defaultValue = "0") int roleId) {
        ModelAndView mv = new ModelAndView(PATH+"cfg_power");

        mv.addObject("role", SysRoleDao.getOne(roleId, true));
        mv.addObject("roleId", roleId);
        mv.addObject("url", "permission/powerGroup?roleId="+roleId);

        return mv;
    }

    @RequiresPermissions(Permission.OPERATE_MANAGE_ADMIN_ACTION)
    @RequestMapping("cfg_power")
    public void cfg_power(HttpServletResponse response
            , @RequestParam(value = "roleId", defaultValue = "0") int roleId
            , @RequestParam(value = "areaIds", defaultValue = "") String areaIds
            , @RequestParam(value = "menuIds", defaultValue = "") String menuIds
            , @RequestParam(value = "permissionIds", defaultValue = "") String permissionIds) {
        int result = 0;
        if (roleId != 0) {

            boolean r = Strings.isNullOrEmpty(areaIds) && Strings.isNullOrEmpty(menuIds) && Strings.isNullOrEmpty(permissionIds);
            if (!r) {
                result = sysCfgPowerService.cfgPower(roleId, areaIds, menuIds, permissionIds);
            }

        }

        RequestUtils.write(response, String.valueOf(result));
    }


    /**
     * 用户组操作：成员管理、角色管理
     * @param groupId
     */
    @RequiresPermissions(Permission.OPERATE_MANAGE_ADMIN_ACTION)
    @RequestMapping("userGroup")
    @ResponseBody
    public ModelAndView userGroup(
            @RequestParam(value = "groupId", defaultValue = "0") int groupId) {
        ModelAndView mv = new ModelAndView(PATH + "ajax/table");

        DataTable dt = SysAuthDao.listByGroupId(groupId);
        mv.addObject("isUserGroup", true);
        mv.addObject("groupId", groupId);
        mv.addObject("url", "permission/userGroup?groupId="+groupId);

        if (dt.rows.length > 0) {
            List<Long> userIds = DBUtils.convert2List(Long.class, "userId", dt);
            DataTable adminInfo = SysUserDao.listByUserIds(userIds);

            if (adminInfo.rows.length > 0) {
                mv.addObject("adminInfo", adminInfo.rows);
                DataTable roleDt = SysRoleDao.listRoles(groupId, true);
                if (roleDt.rows.length > 0) {
                    mv.addObject("roleDt", roleDt.rows);
                }
            }
        }

        return mv;
    }

    @RequiresPermissions(Permission.OPERATE_MANAGE_ADMIN_ACTION)
    @RequestMapping("roleSelect")
    @ResponseBody
    public ModelAndView roleSelect(
            @RequestParam(value = "groupId", defaultValue = "0") int groupId,
            @RequestParam(value = "userId", defaultValue = "0") long userId) {
        ModelAndView mv = new ModelAndView(PATH + "ajax/select");

        DataTable roleDt = SysRoleDao.listRoles(groupId, true);
        if (roleDt.rows.length > 0) {
            mv.addObject("roleDt", roleDt.rows);
            mv.addObject("userId", userId);
        }

        return mv;
    }

    @RequiresPermissions(Permission.OPERATE_MANAGE_ADMIN_ACTION)
    @RequestMapping("savePower")
    @ResponseBody
    public void savePower(HttpServletResponse response
            , @RequestParam(value = "userId", defaultValue = "0") long userId
            , @RequestParam(value = "roleId", defaultValue = "0") int roleId ) {

        int result = 0;

        if (userId != 0 && roleId != 0) {
            result = SysAuthDao.update(userId, roleId);
            if (result > 0) {
                SysRoleModel role = SysRoleDao.getOne(roleId, true);
                result = SysUserDao.updateRemark(userId, role.getName());
            }
        }

        RequestUtils.write(response, CommonUtils.getStringValue(result));
    }


    @RequiresPermissions(Permission.OPERATE_MANAGE_ADMIN_ACTION)
    @RequestMapping("setUserPower")
    @ResponseBody
    public ModelAndView setUserPower(HttpServletResponse response
            , @RequestParam(value = "groupId", defaultValue = "0") int groupId) {
        ModelAndView mv = new ModelAndView(PATH + "ajax/table");

        DataTable dt = SysAuthDao.listByGroupId(groupId);
        if (dt.rows.length > 0) {
            List<Long> userIds = DBUtils.convert2List(Long.class, "userId", dt);
            DataTable adminInfo = SysUserDao.listByUserIds(userIds);

            if (adminInfo.rows.length > 0) {
                mv.addObject("adminInfo", adminInfo.rows);
            }
        }

        return mv;
    }


    /**
     * 添加用户
     * @param response
     * @param userName
     * @param groupId
     */
    @RequiresPermissions(Permission.OPERATE_MANAGE_ADMIN_ACTION)
    @RequestMapping("addUser")
    @ResponseBody
    public void addUser(HttpServletResponse response
            , @RequestParam(value = "userName", defaultValue = "") String userName
            , @RequestParam(value = "groupId", defaultValue = "0") int groupId ) {

        int result = 0;

        if (!Strings.isNullOrEmpty(userName) && groupId != 0) {
            SysUserModel userInfo = SysUserDao.getByUserName(userName);

            if (userInfo != null && SysAuthDao.getOne(userInfo.getId()) == null) {
                result = SysAuthDao.add(userInfo.getId(), groupId);
            }
        }

        RequestUtils.write(response, CommonUtils.getStringValue(result));
    }

    @RequiresPermissions(Permission.OPERATE_MANAGE_ADMIN_ACTION)
    @RequestMapping("removeUser")
    @ResponseBody
    public void removeUser(HttpServletResponse response
            , @RequestParam(value = "userId", defaultValue = "") long userId ) {

        int result = 0;
        if (userId != 0) {
            result = SysAuthDao.delete(userId);
        }

        RequestUtils.write(response, CommonUtils.getStringValue(result));
    }


//    @RequestMapping("queryAdmin")
//    @ResponseBody
//    public ModelAndView queryAdmin(HttpServletResponse response
//            , @RequestParam(value = "userName", defaultValue = "") String userName) {
//        ModelAndView mv = new ModelAndView(PATH + "ajax/table");
//
//        mv.addObject("adminInfo", SysUserDao.getByUserName(userName));
//        return mv;
//    }



    /** 权限配置 **/
    @RequiresPermissions(Permission.OPERATE_MANAGE_ADMIN_ACTION)
    @RequestMapping("getGameList")
    @ResponseBody
    public void getGameList(HttpServletResponse response
            , @RequestParam(value = "roleId", defaultValue = "0") int roleId) {
        //游戏列表
        DataTable gameList = SysAreaDao.listAreas(true);
        if (gameList.rows.length > 0) {
            List<JsTreeVO> jsTreeVOs = Lists.newLinkedList();
            JsTreeVO vo = null;
            State state = null;
            for (DataRow row: gameList.rows) {
                vo = new JsTreeVO();
                state = new State();
                int id = CommonUtils.getIntValue(row.getColumnValue("id"));
                int parentId = CommonUtils.getIntValue(row.getColumnValue("parentId"));

                vo.setId(String.valueOf(id));
                if (parentId != 0) {
                    vo.setParent(row.getColumnValue("parentId"));

                    if (SysRolePermissionDao.isExist(id, roleId, PowerTBName.TB_AREA)) {
                        state.setSelected(true);
                        state.setOpened(true);
                        vo.setState(state);
                    }

                    if (CommonUtils.getBooleanValue(row.getColumnValue("disabled"))) {
//                    state.setDisabled(true);
                    }

                } else {
                    vo.setParent("#");
                }

                vo.setIcon(row.getColumnValue("icon"));
                vo.setText(row.getColumnValue("name"));
                jsTreeVOs.add(vo);
            }
            String json = JsonUtils.getJson(jsTreeVOs);

            RequestUtils.write(response, json);
        } else {
            throw new UnknownAccountException();//登录失败
        }

    }

    @RequiresPermissions(Permission.OPERATE_MANAGE_ADMIN_ACTION)
    @RequestMapping("getMenuList")
    @ResponseBody
    public void getMenuList(HttpServletResponse response
            , @RequestParam(value = "roleId", defaultValue = "0") int roleId) {
        //菜单列表
        DataTable menuList = SysMenuDao.listMenus(true);
        if (menuList.rows.length > 0) {
            List<JsTreeVO> jsTreeVOs = Lists.newLinkedList();
            JsTreeVO vo = null;
            State state = null;
            for (DataRow row: menuList.rows) {
                vo = new JsTreeVO();
                state = new State();
                int id = CommonUtils.getIntValue(row.getColumnValue("id"));
                int parentId = CommonUtils.getIntValue(row.getColumnValue("parentId"));

                vo.setId(String.valueOf(id));
                if (parentId != 0) {
                    vo.setParent(String.valueOf(parentId));

                    if (SysRolePermissionDao.isExist(id, roleId, PowerTBName.TB_MENU) && parentId != 1) {
                        state.setSelected(true);
                        state.setOpened(true);
                        vo.setState(state);
                    }

                } else {
                    vo.setParent("#");
                }

                vo.setIcon(row.getColumnValue("icon"));
                vo.setText(row.getColumnValue("name"));
                jsTreeVOs.add(vo);
            }
            String json = JsonUtils.getJson(jsTreeVOs);

            RequestUtils.write(response, json);
        } else {
            throw new UnknownAccountException();//登录失败
        }

    }

    @RequiresPermissions(Permission.OPERATE_MANAGE_ADMIN_ACTION)
    @RequestMapping("getPermissionList")
    @ResponseBody
    public void getPermissionList(HttpServletResponse response
            , @RequestParam(value = "roleId", defaultValue = "0") int roleId) {
        //权限列表
        DataTable permissions = SysPermissionMenuDao.listPermissionMenus(true);
        if (permissions.rows.length > 0) {
            List<JsTreeVO> jsTreeVOs = Lists.newLinkedList();
            JsTreeVO vo = null;
            State state = null;
            for (DataRow row: permissions.rows) {
                vo = new JsTreeVO();
                state = new State();
                int id = CommonUtils.getIntValue(row.getColumnValue("id"));
                int parentId = CommonUtils.getIntValue(row.getColumnValue("parentId"));

                vo.setId(String.valueOf(id));
                if (parentId != 0) {
                    vo.setParent(row.getColumnValue("parentId"));

                    if (SysRolePermissionDao.isExist(id, roleId, PowerTBName.TB_PERMISSION)) {
                        state.setSelected(true);
                        state.setOpened(true);
                        vo.setState(state);
                    }

                    if (CommonUtils.getBooleanValue(row.getColumnValue("disabled"))) {
//                    state.setDisabled(true);
                    }

                } else {
                    vo.setParent("#");
                }

                vo.setIcon(row.getColumnValue("icon"));
                vo.setText(row.getColumnValue("name"));
                jsTreeVOs.add(vo);
            }

            String json = JsonUtils.getJson(jsTreeVOs);
            RequestUtils.write(response, json);
        } else {
            throw new UnknownAccountException();//登录失败
        }

    }



    /**
     * 创建管理员
     */
    @RequiresPermissions(Permission.OPERATE_CREATE_ADMIN_ADD)
    @RequestMapping("createAdmin")
    public String createAdmin() {
        return PATH + "createAdmin";
    }

    @RequestMapping("save_createAdmin")
    @ResponseBody
    public void save_createAdmin(HttpServletResponse response
            , @RequestParam(value = "userName", defaultValue = "") String userName
            , @RequestParam(value = "realmName", defaultValue = "") String realmName
            , @RequestParam(value = "cellphone", defaultValue = "") String cellphone
            , @RequestParam(value = "loginPassword", defaultValue = "") String loginPassword
            , @RequestParam(value = "email", defaultValue = "") String email) {

        int result = 0;

        if (!Strings.isNullOrEmpty(userName) && !Strings.isNullOrEmpty(realmName)) {
            SysUserModel model = new SysUserModel();
            model.setUserName(userName);
            model.setRealName(realmName);
            model.setLoginPassword(DigestUtils.md5Hex(loginPassword));
            if (Strings.isNullOrEmpty(email)) {
                email = "--";
            }
            if (Strings.isNullOrEmpty(cellphone)) {
                cellphone = "0";
            }
            model.setCellphone(CommonUtils.getIntValue(cellphone));
            model.setEmail(email);

            if (SysUserDao.getByRealName(realmName) == null) {
                result = SysUserDao.add(model);
            }

        }

        RequestUtils.write(response, CommonUtils.getStringValue(result));
    }


    /**
     * 游戏菜单项
     * @return
     */
    @RequiresPermissions(Permission.OPERATE_MANAGE_ADMIN_ACTION)
    @RequestMapping("showArea")
    public ModelAndView showArea() {

        ModelAndView mv = new ModelAndView(PATH+"ajax/addArea_table");

        DataTable dt = SysAreaDao.listAreas(true);
        mv.addObject("isAreaGroup", true);

        if (dt.rows.length > 0) {
            mv.addObject("areas", dt.rows);
        }

        return mv;
    }

    @RequiresPermissions(Permission.OPERATE_CREATE_ADMIN_ADD)
    @RequestMapping("addArea")
    public String addArea() {
        return PATH + "addArea";
    }

    @RequestMapping("save_addArea")
    @ResponseBody
    public void save_addArea(HttpServletResponse response
            , @RequestParam(value = "area", defaultValue = "") String area
            , @RequestParam(value = "gameId", defaultValue = "0") int gameId
            , @RequestParam(value = "parentId", defaultValue = "0") int parentId
            , @RequestParam(value = "name", defaultValue = "") String name) {

        int result = 0;
        if (!Strings.isNullOrEmpty(area) && gameId != 0 && !Strings.isNullOrEmpty(name)) {
            result = SysAreaDao.add(area, gameId, parentId, name);
        }

        RequestUtils.write(response, CommonUtils.getStringValue(result));
    }


    /**
     * 移除游戏
     * @param response
     * @param gameId
     */
    @RequiresPermissions(Permission.OPERATE_MANAGE_ADMIN_ACTION)
    @RequestMapping("removeGame")
    @ResponseBody
    public void removeGame(HttpServletResponse response
            , @RequestParam(value = "gameId", defaultValue = "") int gameId ) {

        int result = 0;
        if (gameId != 0) {
            result = SysAreaDao.delete(gameId);
        }

        RequestUtils.write(response, CommonUtils.getStringValue(result));
    }


}
