package com.sdk4.boot.api.user;

import com.sdk4.boot.apiengine.ApiResponse;
import com.sdk4.boot.apiengine.ApiService;
import com.sdk4.boot.apiengine.RequestContent;
import com.sdk4.boot.bo.apimodel.UserRegRequestModel;
import com.sdk4.boot.common.BaseResponse;
import com.sdk4.boot.domain.User;
import com.sdk4.boot.enums.ServiceNameEnum;
import com.sdk4.boot.enums.SmsCodeTypeEnum;
import com.sdk4.boot.service.SmsService;
import com.sdk4.boot.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户注册
 */
@Service("BootUserReg")
public class UserReg implements ApiService {

    @Autowired
    SmsService smsService;

    @Autowired
    UserService userService;

    @Override
    public String method() {
        return ServiceNameEnum.UserReg.getMethod();
    }

    @Override
    public boolean requiredLogin() {
        return false;
    }

    @Override
    public ApiResponse call(RequestContent rc) {
        ApiResponse ret;

        UserRegRequestModel params = rc.toJavaObject(UserRegRequestModel.class);

        if (StringUtils.isEmpty(params.getMobile())) {
            ret = new ApiResponse(4, "手机号码不能空");
        } else if (StringUtils.isEmpty(params.getPassword())) {
            ret = new ApiResponse(4, "登录密码不能空");
        } else if (!StringUtils.equals(params.getPassword(), params.getRe_password())) {
            ret = new ApiResponse(4, "两次输入密码不一致");
        } else if (!smsService.verifyCheckCode(SmsCodeTypeEnum.user_reg.name(), params.getMobile(), params.getSms_code()).isSuccess()) {
            ret = new ApiResponse(4, "短信验证码不正确或已过期");
        } else {
            BaseResponse<User> callResult = userService.registerByMobile(params.getMobile(), params.getPassword());

            ret = new ApiResponse(callResult.getCode(), callResult.getMessage());

            if (callResult.isSuccess()) {
                User user = callResult.getData();

                ret.put("id", user.getId());
                ret.put("mobile", user.getMobile());
                ret.put("reg_time", new DateTime(user.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss"));
            }
        }

        return ret;
    }
}
