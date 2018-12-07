package com.sdk4.boot.api.user;

import com.sdk4.boot.apiengine.ApiResponse;
import com.sdk4.boot.apiengine.ApiService;
import com.sdk4.boot.apiengine.RequestContent;
import com.sdk4.boot.bo.LoginUser;
import com.sdk4.boot.bo.Token;
import com.sdk4.boot.bo.apimodel.UserLoginRequestModel;
import com.sdk4.boot.bo.apimodel.UserLoginResponseModel;
import com.sdk4.boot.common.BaseResponse;
import com.sdk4.boot.domain.User;
import com.sdk4.boot.enums.UserTypeEnum;
import com.sdk4.boot.service.AuthService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户登录
 */
@Service("BootUserLogin")
public class UserLogin implements ApiService {

    @Autowired
    AuthService authService;

    @Override
    public String method() {
        return "user.login";
    }

    @Override
    public boolean requiredLogin() {
        return false;
    }

    @Override
    public ApiResponse call(RequestContent rc) {
        ApiResponse ret;

        UserLoginRequestModel params = rc.toJavaObject(UserLoginRequestModel.class);
        if (StringUtils.isEmpty(params.getMobile())) {
            ret = new ApiResponse(4, "手机号码不能空");
        } else if (StringUtils.isEmpty(params.getPassword())) {
            ret = new ApiResponse(4, "登录密码不能空");
        } else {
            BaseResponse<LoginUser> callResult = authService.loginByMobile(UserTypeEnum.COMMON_USER,
                    params.getMobile(), params.getPassword());

            ret = new ApiResponse(callResult.getCode(), callResult.getMessage());

            if (callResult.isSuccess()) {
                UserLoginResponseModel rspData = new UserLoginResponseModel();

                LoginUser loginUser = callResult.getData();

                try {
                    User user = loginUser.toUserObject();
                    Token token = loginUser.getToken();

                    rspData.setId(user.getId());
                    rspData.setMobile(user.getMobile());
                    rspData.setToken(token.tokenString());

                    ret.setRsp_content(rspData);
                } catch (Exception e) {
                    ret = new ApiResponse(4, "生成 Token 失败");
                }
            }
        }

        return ret;
    }
}
