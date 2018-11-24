package com.sdk4.boot;

import lombok.Getter;

/**
 * API 接口错误代码
 */
@Getter
public enum CommonErrorCode {

    /**
     * 调用成功业务提示
     */
    SUCCESS(0, "SUCCESS"),

    /**
     * 系统错误: 系统异常
     */
    SYSTEM_ERROR(10000, "系统错误"),

    /**
     * 登录失败
     */
    LOGIN_FAIL                     (10100, "登录失败"),
    USERNAME_OR_PASSWORD_INCORRECT (10101, "用户名或密码错误"),
    EXCESSIVE_ATTEMPT              (10102, "登录失败次数过多"),
    ACCOUNT_LOCKED                 (10103, "帐号已被锁定"),
    ACCOUNT_DISABLED               (10104, "帐号已被禁用"),
    ACCOUNT_EXPIRED                (10105, "帐号已过期"),
    LOGIN_OTHER_CLIENT             (10010, "已在其他地方登录"),

    /**
     * 未登录
     */
    NOT_LOGIN(10200, "未登录"),

    /**
     * Token已失效
     */
    TOKEN_EXPIRED(10201, "Token已失效"),

    /**
     * 没有权限
     */
    UNAUTHORIZED(10300, "没有权限"),

    /**
     * 缺少必选参数
     */
    MISSING_PARAMETER(41000, "缺少必选参数"),

    /**
     * 非法的参数
     */
    INVALID_PARAMETER(42000, "非法的参数"),

    /**
     * 业务处理失败
     */
    BIZ_FAIL(50000, "业务处理失败"),

    ;

    private int code;
    private String msg;

    CommonErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
