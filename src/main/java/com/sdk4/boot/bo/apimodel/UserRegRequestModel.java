package com.sdk4.boot.bo.apimodel;

import lombok.Data;

/**
 * 用户注册请求
 *
 * @author sh
 */
@Data
public class UserRegRequestModel {
    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 短信验证码
     */
    private String sms_code;

    /**
     * 密码
     */
    private String password;

    /**
     * 确认密码
     */
    private String re_password;

    /**
     * 邀请码
     */
    private String invite_code;
}
