package admin.util;

import admin.vo.MenuVO;
import com.google.common.collect.Lists;
import db_admin.model.SysMenuModel;
import dsqp.db.model.DataRow;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;

import java.util.List;

/**
 * Created by ds on 2017/7/27.
 * 菜单工具类
 */
public class MenuUtil {

    /**
     * 拼装菜单列表
     * @param dt 输入菜单列表
     */
    public static List<MenuVO> assembly(DataTable dt) {
        List<MenuVO> menuVOList = Lists.newArrayList();
        for (DataRow row: dt.rows) {
            SysMenuModel m = DBUtils.convert2Model(SysMenuModel.class, row);
            MenuVO vo = new MenuVO();
            vo.setId(m.getId());
            vo.setMenu(m.getMenu());
            vo.setParentId(m.getParentId());
            vo.setUrl(m.getUrl());
            vo.setPriority(m.getPriority());
            vo.setAvailable(m.getAvailable());
            vo.setIcon(m.getIcon());
            vo.setSequence(m.getSequence());
            vo.setName(m.getName());
            menuVOList.add(vo);
        }
        return assembly(menuVOList);
    }
    public static List<MenuVO> assembly(List<MenuVO> rootMenu) {
        // 拼装列表
        List<MenuVO> menuList = Lists.newArrayList();
        // 查找所有 :: 根菜单(一级菜单)
        for (int i = 0; i < rootMenu.size(); i++) {
            // 一级菜单没有父节点 :: parentId == 0
            if (rootMenu.get(i).getParentId() == 0) {
                menuList.add(rootMenu.get(i));
            }
        }
        // 为菜单 :: 设置子菜单，getChild是递归调用
        for (MenuVO menu : menuList) {
            menu.setChildMenus(getChild(menu.getId(), rootMenu));
        }

        return menuList;
    }

    /**
     * 递归查找子菜单
     * @param id  当前菜单id
     * @param rootMenu 要查找的列表
     */
    public static List<MenuVO> getChild(int id, List<MenuVO> rootMenu) {
        // 子菜单
        List<MenuVO> childList = Lists.newArrayList();
        for (MenuVO menu : rootMenu) {
            // 遍历所有节点，将父菜单id与传过来的id比较
            if (menu.getParentId() != 0) {
                if (menu.getParentId() == id) {
                    childList.add(menu);
                }
            }
        }
        // 把子菜单的子菜单再循环一遍
        for (MenuVO menu : childList) {// url = javascript:;  :: 子菜单还有子菜单
            if (menu.getUrl().equals("javascript:;")) {
                // 递归
                menu.setChildMenus(getChild(menu.getId(), rootMenu));
            }
        } // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }

}
