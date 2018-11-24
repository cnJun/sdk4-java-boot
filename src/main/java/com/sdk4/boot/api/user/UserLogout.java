package com.sdk4.boot.api.user;

import com.sdk4.boot.apiengine.ApiResponse;
import com.sdk4.boot.apiengine.ApiService;
import com.sdk4.boot.apiengine.RequestContent;
import com.sdk4.boot.bo.LoginUser;
import com.sdk4.boot.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户登出
 *
 * Created by sh on 2018/6/20.
 */
@Service("BootUserLogout")
public class UserLogout implements ApiService {
    @Autowired
    AuthService authService;

    @Override
    public String method() {
        return "user.logout";
    }

    @Override
    public boolean requiredLogin() {
        return false;
    }

    @Override
    public ApiResponse call(RequestContent rc) {
        ApiResponse ret;

        LoginUser loginUser = rc.getLoginUser();

        if (rc.getLoginUser() != null) {
            authService.logoutByLoginId(loginUser.getLoginId());
        }

        // 清除 session
        /**
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            subject.logout();
        }*/

        ret = new ApiResponse(0, "退出登录成功");

        return ret;
    }
}
