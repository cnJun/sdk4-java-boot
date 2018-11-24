package com.sdk4.boot.enums;

import lombok.Getter;

/**
 * 服务方法
 *
 * @author sh
 */
@Getter
public enum ServiceNameEnum {
    /**
     * 用户注册
     */
    UserReg("user.reg", "用户注册"),

    /**
     * 获取服务器当前时间
     */
    ServerTime("server.time", "服务器时间"),

    /**
     * 请求发送短信验证码
     */
    SmsCode("sms.code", "请求发送短信验证码"),

    ;

    private String method;
    private String text;

    ServiceNameEnum(String method, String text) {
        this.method = method;
        this.text = text;
    }
}
