package com.sdk4.boot.api.comm;

import com.sdk4.boot.CallResult;
import com.sdk4.boot.apiengine.ApiResponse;
import com.sdk4.boot.apiengine.ApiService;
import com.sdk4.boot.apiengine.RequestContent;
import com.sdk4.boot.bo.apimodel.SmsCodeRequestModel;
import com.sdk4.boot.enums.ServiceNameEnum;
import com.sdk4.boot.service.SmsService;
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

        if (StringUtils.isEmpty(params.getMobile())) {
            ret = new ApiResponse(4, "手机号码不能空");
        } else if (StringUtils.isEmpty(params.getType())) {
            ret = new ApiResponse(4, "验证码类型不能空");
        } else {

            CallResult<com.sdk4.boot.domain.SmsCode> callResult = smsService.sendCheckCode(params.getType(), params.getMobile());

            ret = new ApiResponse(callResult.getCode(), callResult.getMessage());

            if (callResult.success()) {
                com.sdk4.boot.domain.SmsCode smsCode = callResult.getData();
                ret.put("msg_id", smsCode.getMsgId());
            }
        }

        return ret;
    }
}
