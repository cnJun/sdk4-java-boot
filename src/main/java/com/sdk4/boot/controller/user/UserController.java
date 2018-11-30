package com.sdk4.boot.controller.user;

import com.google.common.collect.Lists;
import com.sdk4.boot.AjaxResponse;
import com.sdk4.boot.CommonErrorCode;
import com.sdk4.boot.bo.LoginUser;
import com.sdk4.boot.domain.AdminUser;
import com.sdk4.boot.enums.UserTypeEnum;
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

    @ResponseBody
    @PostMapping(value = "login", produces = "application/json;charset=utf-8")
    public String login(@RequestBody Map<String, String> reqMap, HttpServletRequest request, HttpServletResponse response) {
        AjaxResponse ret = new AjaxResponse();

        String type = reqMap.get("type");
        String username = reqMap.get("username");
        String password = reqMap.get("password");

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            ret.putError(CommonErrorCode.MISSING_PARAMETER.getCode(), "登录账号密码不能为空");
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
                    ret.putError(0, "登录成功");

                    ret.put("token", subject.getPrincipals().getRealmNames().iterator().next());
                } else {
                    ret.putError(CommonErrorCode.USERNAME_OR_PASSWORD_INCORRECT);
                }
            } catch (IncorrectCredentialsException e) {
                ret.putError(CommonErrorCode.USERNAME_OR_PASSWORD_INCORRECT);
            } catch (ExcessiveAttemptsException e) {
                ret.putError(CommonErrorCode.EXCESSIVE_ATTEMPT);
            } catch (LockedAccountException e) {
                ret.putError(CommonErrorCode.ACCOUNT_LOCKED);
            } catch (DisabledAccountException e) {
                ret.putError(CommonErrorCode.ACCOUNT_DISABLED);
            } catch (ExpiredCredentialsException e) {
                ret.putError(CommonErrorCode.ACCOUNT_EXPIRED);
            } catch (UnknownAccountException e) {
                ret.putError(CommonErrorCode.USERNAME_OR_PASSWORD_INCORRECT);
            } catch (UnauthorizedException e) {
                ret.putError(CommonErrorCode.UNAUTHORIZED);
            } catch (AuthenticationException e) {
                ret.putError(CommonErrorCode.USERNAME_OR_PASSWORD_INCORRECT);
            } catch (Exception e) {
                ret.putError(CommonErrorCode.USERNAME_OR_PASSWORD_INCORRECT);
            }
        }

        return ret.toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "info", produces = "application/json;charset=utf-8")
    public String info(HttpServletRequest request, HttpServletResponse response) {
        AjaxResponse ret = new AjaxResponse();

        List<String> roles = Lists.newArrayList();

        roles.add("default");
        roles.add("admin");

        Subject subject = SecurityUtils.getSubject();
        if (subject != null && subject.isAuthenticated()) {
            PrincipalCollection pc = subject.getPrincipals();
            if (pc != null && !pc.getRealmNames().isEmpty()) {
                String token = pc.getRealmNames().iterator().next();

                ret.putError(CommonErrorCode.SUCCESS);

                ret.put("token", token);

                LoginUser loginUser = authService.getLoginUserFromSession();

                if (loginUser != null && loginUser.getUserObject() != null) {
                    if (loginUser.getUserType() == UserTypeEnum.ADMIN_USER) {
                        AdminUser adminUser = loginUser.toUserObject();
                        ret.put("id", adminUser.getId());
                        ret.put("name", adminUser.getMobile());

                        String defaultAvatar = "https://secure.gravatar.com/avatar/default.jpg";
                        ret.put("avatar", defaultAvatar);
                        ret.put("roles", roles);

                        DruidUtils.setLoginedName(request, adminUser.getMobile());
                    }
                } else {
                    ret.putError(CommonErrorCode.NOT_LOGIN);
                }
            } else {
                ret.putError(CommonErrorCode.NOT_LOGIN);
            }
        } else {
            ret.putError(CommonErrorCode.NOT_LOGIN);
        }

        return ret.toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "logout", produces = "application/json;charset=utf-8")
    public String logout() {
        AjaxResponse ret = new AjaxResponse();

        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            subject.logout();
        }

        ret.putError(CommonErrorCode.SUCCESS);

        return ret.toJSONString();
    }
}
