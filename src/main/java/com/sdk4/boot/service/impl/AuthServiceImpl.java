package com.sdk4.boot.service.impl;

import com.alibaba.fastjson.JSON;
import com.sdk4.boot.CallResult;
import com.sdk4.boot.bo.LoginUser;
import com.sdk4.boot.bo.Token;
import com.sdk4.boot.domain.AdminUser;
import com.sdk4.boot.domain.LoginToken;
import com.sdk4.boot.domain.User;
import com.sdk4.boot.enums.UserTypeEnum;
import com.sdk4.boot.repository.LoginTokenRepository;
import com.sdk4.boot.service.AdminUserService;
import com.sdk4.boot.service.AuthService;
import com.sdk4.boot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

/**
 * 用户权限
 *
 * @author sh
 */
@Slf4j
@Service("BootAuthService")
public class AuthServiceImpl implements AuthService {
    @Autowired
    LoginTokenRepository loginTokenRepository;

    @Autowired
    UserService userService;

    @Autowired
    AdminUserService adminUserService;

    @Override
    public CallResult<LoginUser> loginByMobile(UserTypeEnum type, String mobile, String password) {
        CallResult<LoginUser> result = new CallResult<>();

        LoginToken loginToken = null;
        Object userObject = null;

        if (UserTypeEnum.COMMON_USER == type) {
            CallResult<User> callResult = userService.loginByMobile(mobile, password);
            if (callResult.success()) {
                loginToken = LoginToken.by(callResult.getData());
                userObject = callResult.getData();
            } else {
                result.setError(callResult.getCode(), callResult.getMessage());
            }
        } else if (UserTypeEnum.ADMIN_USER == type) {
            CallResult<AdminUser> callResult = adminUserService.loginByMobile(mobile, password);
            if (callResult.success()) {
                loginToken = LoginToken.by(callResult.getData());
                userObject = callResult.getData();
            } else {
                result.setError(callResult.getCode(), callResult.getMessage());
            }
        }

        if (loginToken != null) {
            try {
                loginTokenRepository.deleteUserToken(loginToken.getType(), loginToken.getUserId());

                loginTokenRepository.save(loginToken);

                LoginUser loginUser = LoginUser.by(userObject, loginToken);

                result.setError(0, "登录成功", loginUser);
            } catch (Exception e) {
                log.error("生成登录 Token 异常: {}", JSON.toJSONString(loginToken), e);
            }
        }

        return result;
    }

    @Override
    public LoginUser getLoginUserByToken(String tokenString) {
        LoginUser loginUser = null;

        Token token = Token.parseToken(tokenString);
        if (token != null) {
            if (token.expire()) {
                // 已过期
                loginTokenRepository.deleteById(token.getLogin_id());
            } else {
                LoginToken loginToken = loginTokenRepository.findById(token.getLogin_id()).orElse(null);
                if (loginToken != null) {
                    if (UserTypeEnum.COMMON_USER == loginToken.getType()) {
                        User user = userService.getUser(loginToken.getUserId());

                        try {
                            loginUser = LoginUser.by(user, loginToken);
                        } catch (UnsupportedEncodingException e) {
                            log.error("获取用户时创建 Token 失败:{}", JSON.toJSONString(loginToken), e);
                        }
                    } else if (UserTypeEnum.ADMIN_USER == loginToken.getType()) {
                        AdminUser adminUser = adminUserService.getAdminUser(loginToken.getUserId());

                        try {
                            loginUser = LoginUser.by(adminUser, loginToken);
                        } catch (UnsupportedEncodingException e) {
                            log.error("获取用户时创建 Token 失败:{}", JSON.toJSONString(loginToken), e);
                        }
                    }
                }
            }
        }

        return loginUser;
    }

    @Override
    public LoginUser getLoginUserFromSession() {
        LoginUser loginUser = null;

        Subject subject = SecurityUtils.getSubject();
        if (subject != null && subject.isAuthenticated()) {
            PrincipalCollection pc = subject.getPrincipals();
            if (pc != null && !pc.getRealmNames().isEmpty()) {
                String tokenString = pc.getRealmNames().iterator().next();
                loginUser = getLoginUserByToken(tokenString);
            }
        }

        return loginUser;
    }

    @Override
    public void logoutByLoginId(String loginId) {
        if (StringUtils.isNotEmpty(loginId)) {
            try {
                loginTokenRepository.deleteById(loginId);
            } catch (Exception e) {
                log.error("删除登录token失败:{}", loginId, e);
            }
        }
    }
}
