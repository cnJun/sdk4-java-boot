package com.sdk4.boot.api.comm;

import com.sdk4.boot.apiengine.ApiResponse;
import com.sdk4.boot.apiengine.ApiService;
import com.sdk4.boot.apiengine.RequestContent;
import com.sdk4.boot.bo.apimodel.SmsCodeRequestModel;
import com.sdk4.boot.common.BaseResponse;
import com.sdk4.boot.domain.User;
import com.sdk4.boot.enums.ServiceNameEnum;
import com.sdk4.boot.enums.SmsCodeTypeEnum;
import com.sdk4.boot.service.SmsService;
import com.sdk4.boot.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 请求发送短信验证码
 *
 * @author sh
 */
@Service
public class SmsCode implements ApiService {
    @Autowired
    SmsService smsService;

    @Autowired
    UserService userService;

    @Override
    public String method() {
        return ServiceNameEnum.SmsCode.getMethod();
    }

    @Override
    public boolean requiredLogin() {
        return false;
    }

    @Override
    public ApiResponse call(RequestContent rc) {
        ApiResponse ret;

        SmsCodeRequestModel params = rc.toJavaObject(SmsCodeRequestModel.class);

        boolean mobileExist = false;
        if (StringUtils.isNotEmpty(params.getMobile())
                && StringUtils.equals(SmsCodeTypeEnum.user_reg.name(), params.getType())) {
            User user = userService.getUserByMobile(params.getMobile());
            if (user != null) {
                mobileExist = true;
            }
        }

        if (StringUtils.isEmpty(params.getMobile())) {
            ret = new ApiResponse(4, "手机号码不能空");
        } else if (mobileExist) {
            ret = new ApiResponse(4, "该手机号码已注册");
        } else if (StringUtils.isEmpty(params.getType())) {
            ret = new ApiResponse(4, "验证码类型不能空");
        } else {

            BaseResponse<com.sdk4.boot.domain.SmsCode> callResult = smsService.sendCheckCode(params.getType(), params.getMobile());

            ret = new ApiResponse(callResult.getCode(), callResult.getMessage());

            if (callResult.isSuccess()) {
                com.sdk4.boot.domain.SmsCode smsCode = callResult.getData();
                ret.put("msg_id", smsCode.getMsgId());
            }
        }

        return ret;
    }
}
