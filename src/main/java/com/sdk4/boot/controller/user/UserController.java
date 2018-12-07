package com.sdk4.boot.controller.user;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sdk4.boot.bo.LoginUser;
import com.sdk4.boot.common.BaseResponse;
import com.sdk4.boot.domain.AdminUser;
import com.sdk4.boot.enums.UserTypeEnum;
import com.sdk4.boot.exception.BaseError;
import com.sdk4.boot.service.AuthService;
import com.sdk4.boot.util.DruidUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 用户登录验证及权限信息获取
 *
 * @author sh
 */
@RequestMapping("/user")
@RestController("BootUserController")
public class UserController {

    @Autowired
    AuthService authService;

    @PostMapping(value = "login", produces = "application/json;charset=utf-8")
    public BaseResponse<Map> login(@RequestBody Map<String, String> reqMap, HttpServletRequest request, HttpServletResponse response) {
        BaseResponse<Map> ret = new BaseResponse<>();

        String type = reqMap.get("type");
        String username = reqMap.get("username");
        String password = reqMap.get("password");

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            ret.put(BaseError.MISSING_PARAMETER.getCode(), "登录账号密码不能为空");
        } else {
            if (StringUtils.isEmpty(type)) {
                type = UserTypeEnum.ADMIN_USER.name();
            }
            // username = type + ":" + username;
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            // token.setRememberMe(true);

            try {
                Subject subject = SecurityUtils.getSubject();
                subject.login(token);

                if (subject.isAuthenticated()) {
                    ret.put(0, "登录成功");

                    Map data = Maps.newHashMap();
                    data.put("token", subject.getPrincipals().getRealmNames().iterator().next());

                    ret.setData(data);
                } else {
                    ret.put(BaseError.USERNAME_OR_PASSWORD_INCORRECT);
                }
            } catch (IncorrectCredentialsException e) {
                ret.put(BaseError.USERNAME_OR_PASSWORD_INCORRECT);
            } catch (ExcessiveAttemptsException e) {
                ret.put(BaseError.EXCESSIVE_ATTEMPT);
            } catch (LockedAccountException e) {
                ret.put(BaseError.ACCOUNT_LOCKED);
            } catch (DisabledAccountException e) {
                ret.put(BaseError.ACCOUNT_DISABLED);
            } catch (ExpiredCredentialsException e) {
                ret.put(BaseError.ACCOUNT_EXPIRED);
            } catch (UnknownAccountException e) {
                ret.put(BaseError.USERNAME_OR_PASSWORD_INCORRECT);
            } catch (UnauthorizedException e) {
                ret.put(BaseError.UNAUTHORIZED);
            } catch (AuthenticationException e) {
                ret.put(BaseError.USERNAME_OR_PASSWORD_INCORRECT);
            } catch (Exception e) {
                ret.put(BaseError.USERNAME_OR_PASSWORD_INCORRECT);
            }
        }

        return ret;
    }

    @RequestMapping(value = "info", produces = "application/json;charset=utf-8")
    public BaseResponse<Map> info(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse<Map> ret = new BaseResponse<>();

        List<String> roles = Lists.newArrayList();

        roles.add("default");
        roles.add("admin");

        Subject subject = SecurityUtils.getSubject();
        if (subject != null && subject.isAuthenticated()) {
            PrincipalCollection pc = subject.getPrincipals();
            if (pc != null && !pc.getRealmNames().isEmpty()) {
                ret.put(BaseError.SUCCESS);

                Map data = Maps.newHashMap();

                String token = pc.getRealmNames().iterator().next();
                data.put("token", token);

                LoginUser loginUser = authService.getLoginUserFromSession();

                if (loginUser != null && loginUser.getUserObject() != null) {
                    if (loginUser.getUserType() == UserTypeEnum.ADMIN_USER) {
                        AdminUser adminUser = loginUser.toUserObject();
                        data.put("id", adminUser.getId());
                        data.put("name", adminUser.getMobile());

                        String defaultAvatar = "https://secure.gravatar.com/avatar/default.jpg";
                        data.put("avatar", defaultAvatar);
                        data.put("roles", roles);

                        ret.setData(data);

                        DruidUtils.setLoginedName(request, adminUser.getMobile());
                    }
                } else {
                    ret.put(BaseError.NOT_LOGIN);
                }
            } else {
                ret.put(BaseError.NOT_LOGIN);
            }
        } else {
            ret.put(BaseError.NOT_LOGIN);
        }

        return ret;
    }

    @ResponseBody
    @RequestMapping(value = "logout", produces = "application/json;charset=utf-8")
    public BaseResponse logout() {
        BaseResponse ret = new BaseResponse();

        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            subject.logout();
        }

        ret.put(BaseError.SUCCESS);

        return ret;
    }
}
