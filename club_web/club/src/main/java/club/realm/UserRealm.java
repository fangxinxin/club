package club.realm;


import dsqp.db.model.DataTable;
import dsqp.db_club.dao.PromoterDao;
import dsqp.util.CommonUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

@Component
public class UserRealm extends AuthorizingRealm {


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return new SimpleAuthorizationInfo();
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        long cellPhone = CommonUtils.getLongValue(authenticationToken.getPrincipal());

        DataTable dt = PromoterDao.queryByCellPhone(cellPhone);
        if (dt.rows.length == 0) {
            throw new UnknownAccountException();//账户未知
        }
        SimpleAuthenticationInfo authorizationInfo = new SimpleAuthenticationInfo(
                dt.rows[0].getColumnValue("cellPhone"),
                dt.rows[0].getColumnValue("loginPassWord"),
                getName()
        );
        return authorizationInfo;
    }

}
