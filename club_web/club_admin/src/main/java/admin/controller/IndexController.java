package admin.controller;

import admin.service.SysAuthService;
import admin.util.MenuUtil;
import admin.vo.MenuVO;
import db_admin.dao.SysAreaDao;
import db_admin.dao.SysRolePermissionDao;
import db_admin.model.SysAreaModel;
import db_admin.model.SysUserModel;
import dsqp.db.model.DataTable;
import dsqp.util.CommonUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

import static db_admin.dao.SysAuthDao.getRoleId;

@Controller
public class IndexController {

    @Resource
	SysAuthService authService;

	@RequestMapping("")
	public String loginSuccess(){
		return "redirect:/index";
	}


	@RequestMapping("loginSuccess")
	public String loginSuccess(final Model model){
		SysUserModel admin = getPrincipal();

		if (admin == null) {
			return "logout";
		}

		return "redirect:/index";
	}

	@RequestMapping("select")
	public ModelAndView select(){
		ModelAndView mv = new ModelAndView("select");

		Subject subject = SecurityUtils.getSubject();
		SysUserModel admin = getPrincipal();
		if (admin == null) {
			return new ModelAndView(new RedirectView("logout"));
		}
		long adminId = admin.getId();
		String userName = admin.getUserName();

		int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
		if (gameId > 0) {
			return index();
		}

		int roleId = getRoleId(adminId);
		Set<String> areaIds = SysRolePermissionDao.getAreaIds(roleId);
		Set<String> menuIds = SysRolePermissionDao.getMenuIds(roleId);
		if (!userName.equalsIgnoreCase("admin")) {
			if (areaIds == null || menuIds == null) {
				throw new UnknownAccountException();//登录失败
			}
		}

		//游戏列表
		DataTable gameList = authService.getGames(roleId);
		if (gameList.rows.length > 0) {
			mv.addObject("gameList", gameList.rows);
		} else {
			throw new UnknownAccountException();//登录失败
		}

		subject.getSession().setAttribute("remark", admin.getRemark());
		subject.getSession().setAttribute("realName", admin.getRealName());

		return mv;
	}

	@RequestMapping("index")
	public ModelAndView index(){
		ModelAndView mv = new ModelAndView("index");

		Subject subject = SecurityUtils.getSubject();
		SysUserModel admin = getPrincipal();
		if (admin == null) {
			return new ModelAndView(new RedirectView("logout"));
		}

		long adminId = admin.getId();
		int gameId = CommonUtils.getIntValue(subject.getSession().getAttribute("gameId"));
		if (gameId > 0) {
			SysAreaModel gameModel = SysAreaDao.getByGameId(gameId, true);
			subject.getSession().setAttribute("gameName", gameModel.getName());
		} else {
			return select();
		}

		int roleId = getRoleId(adminId);//角色ID

		//游戏列表
		DataTable gameList = authService.getGames(roleId);
		if (gameList.rows.length > 0) {
			mv.addObject("gameList", gameList.rows);
		} else {
			throw new UnknownAccountException();//登录失败
		}

		DataTable rootMenus = authService.listRootMenus(roleId);
		if (rootMenus.rows.length > 0) {
			mv.addObject("rootMenus", rootMenus.rows);
		} else {
			throw new UnknownAccountException();//登录失败
		}

		return mv;
	}

	@RequestMapping("changeGame")
	@ResponseBody
	public void changeGame(
			@RequestParam(value = "gameId", defaultValue = "0") int gameId) {
		if (gameId == 0) {
			return;
		}
		Session session = SecurityUtils.getSubject().getSession();
		session.setAttribute("gameId", gameId);
	}

	@RequestMapping("getSidebar")
	public ModelAndView getSidebar(
			@RequestParam(value = "menu", defaultValue = "") String menu){
		ModelAndView mv = new ModelAndView("sidebar");

		SysUserModel a = getPrincipal();
		String userName = a.getUserName();
		int roleId = getRoleId(a.getId());

		//当前选中菜单列表
		DataTable dt = authService.listCurrentMenus(roleId, menu);
		List<MenuVO> currentMenuList = MenuUtil.assembly(dt);

		mv.addObject("currentMenuList", currentMenuList);
		return mv;
	}


	//获取当前用户信息
	private static final SysUserModel getPrincipal() {
		Subject subject = SecurityUtils.getSubject();
		SysUserModel admin = (SysUserModel) subject.getPrincipal();
		return admin;
	}


}