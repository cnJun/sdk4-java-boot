package com.sdk4.boot.enums;

import lombok.Getter;

/**
 * 短信验证码类型
 *
 * @author sh
 */
@Getter
public enum SmsCodeTypeEnum {

    /**
     * 用户注册
     */
    user_reg("用户注册", false),

    /**
     * 忘记/重置密码
     */
    forget_password("忘记密码", false),

    ;

    private String text;
    private boolean requireLogined;

    SmsCodeTypeEnum(String text, boolean requireLogined) {
        this.text = text;
        this.requireLogined = requireLogined;
    }
}
