package com.sdk4.boot.config;

import com.sdk4.boot.CallResult;
import com.sdk4.boot.bo.LoginUser;
import com.sdk4.boot.enums.UserTypeEnum;
import com.sdk4.boot.service.AuthService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

/**
 * @author sh
 */
public class MyShiroRealm extends AuthorizingRealm {

    @Resource
    AuthService authService;

    /**
     * 授权信息：获取角色/权限信息用于授权验证
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return new SimpleAuthorizationInfo();
    }

    /**
     * 身份验证
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        UsernamePasswordToken upt = (UsernamePasswordToken) token;

        String username = upt.getUsername();
        String password = new String(upt.getPassword());

        CallResult<LoginUser> callResult = authService.loginByMobile(UserTypeEnum.ADMIN_USER, username, password);
        if (callResult.success()) {
            try {
                return new SimpleAuthenticationInfo(username, password, callResult.getData().getToken().tokenString());
            } catch (UnsupportedEncodingException e) {
                throw new UnauthenticatedException();
            }
        } else {
            throw new UnauthenticatedException();
        }
    }
}
