package admin.vo;

import db_admin.model.SysMenuModel;

import java.util.List;

/**
 * Created by ds on 2017/7/27.
 */
public class MenuVO extends SysMenuModel {
    private List<MenuVO> childMenus;

    public List<MenuVO> getChildMenus() {
        return childMenus;
    }

    public void setChildMenus(List<MenuVO> childMenus) {
        this.childMenus = childMenus;
    }
}
