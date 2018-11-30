package com.sdk4.boot.api.user;

import com.sdk4.boot.apiengine.ApiResponse;
import com.sdk4.boot.apiengine.ApiService;
import com.sdk4.boot.apiengine.RequestContent;
import com.sdk4.boot.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户信息
 */
@Service("BootUserInfo")
public class UserInfo implements ApiService {

    @Autowired
    AuthService authService;

    @Override
    public String method() {
        return "user.info";
    }

    @Override
    public boolean requiredLogin() {
        return true;
    }

    @Override
    public ApiResponse call(RequestContent rc) {
        return new ApiResponse();
    }
}
