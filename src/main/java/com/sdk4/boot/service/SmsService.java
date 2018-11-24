package com.sdk4.boot.service;

import com.sdk4.boot.CallResult;
import com.sdk4.boot.domain.SmsCode;

/**
 * 短信接口
 *
 * @author sh
 */
public interface SmsService {

    /**
     * 发短信验证码
     *
     * @param type 类型
     * @param mobile 手机号码
     * @return 下发短信id
     */
    CallResult<SmsCode> sendCheckCode(String type, String mobile);

    /**
     * 验证短信验证码
     *
     * @param type 类型
     * @param mobile 手机号码
     * @param code 用户输入的验证码
     * @return 验证结果
     */
    CallResult<SmsCode> verifyCheckCode(String type, String mobile, String code);

}
