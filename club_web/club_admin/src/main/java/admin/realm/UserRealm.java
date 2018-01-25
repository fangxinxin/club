package admin.realm;

import admin.service.SysAuthService;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import db_admin.dao.SysAuthDao;
import db_admin.dao.SysUserDao;
import db_admin.model.SysUserModel;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasAuthenticationException;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.cas.CasToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UserRealm extends CasRealm {

    @Autowired
    private SysAuthService rolePermissionService;

    protected final Map<String, SimpleAuthorizationInfo> roles = new ConcurrentHashMap<String, SimpleAuthorizationInfo>();

    /**
     * 设置角色和权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUserModel user = (SysUserModel) principals.getPrimaryPrincipal();
        long userId = user.getId();
        String userName = user.getUserName();
        int roleId = SysAuthDao.getRoleId(userId);

        if (!userName.equalsIgnoreCase("admin") && roleId == 0) {
            throw new UnknownAccountException();//登录失败
        }

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        String role = rolePermissionService.getRole(userId);
        if (!Strings.isNullOrEmpty(role)) {
            authorizationInfo.addRole(role);    //增加角色
        }

        if (userName.equalsIgnoreCase("admin")){
            Set<String> power = Sets.newHashSet();
            power.add("sys");
            power.add("operate");
            authorizationInfo.setStringPermissions(power);
        }else {
            List<String> permissionList = rolePermissionService.listPermissions(roleId);
            List<String> gameList = rolePermissionService.listAreas(roleId);
            if (null != permissionList && permissionList.size() > 0) {
                authorizationInfo.addStringPermissions(permissionList);
            }
            if (null != gameList && gameList.size() > 0) {
                authorizationInfo.addStringPermissions(gameList);    //增加权限
            }
        }

        roles.put(userName, authorizationInfo);

        return authorizationInfo;
    }

    /**
     * 1、CAS认证 ,验证用户身份
     * 2、将用户基本信息设置到会话中
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        Subject subject = SecurityUtils.getSubject();
        CasToken casToken = (CasToken) token;
        if (token == null) {
            return null;
        }

        String ticket = (String)casToken.getCredentials();
        if (!StringUtils.hasText(ticket)) {
            return null;
        }

        TicketValidator ticketValidator = ensureTicketValidator();

        try {

            Assertion casAssertion = ticketValidator.validate(ticket, getCasService());

            AttributePrincipal casPrincipal = casAssertion.getPrincipal();
            String userName = casPrincipal.getName();

            Map<String, Object> attributes = casPrincipal.getAttributes();
            // refresh authentication token (user id + remember me)
            String rememberMeAttributeName = getRememberMeAttributeName();
            String rememberMeStringValue = (String)attributes.get(rememberMeAttributeName);
            boolean isRemembered = rememberMeStringValue != null && Boolean.parseBoolean(rememberMeStringValue);
            if (isRemembered) {
                casToken.setRememberMe(true);
            }

            // 根据用户名获取账号信息
            SysUserModel user = SysUserDao.getByUserName(userName);

            if (user != null) {
                casToken.setUserId(String.valueOf(user.getId()));
            } else {
                throw new UnknownAccountException();//登录失败
            }

            return new SimpleAuthenticationInfo(user, ticket, getName());
        } catch (TicketValidationException e) {
            throw new CasAuthenticationException("Unable to validate ticket [" + ticket + "]", e);
        }
    }


/*    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
        clearAllCache();
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }*/

}
