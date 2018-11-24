package com.sdk4.boot.enums;

/**
 * 用户类型
 */
public enum UserTypeEnum {

    /**
     * 注册用户
     */
    COMMON_USER("注册用户"),

    /**
     * 管理用户
     */
    ADMIN_USER("管理用户"),

    ;

    private String text;

    UserTypeEnum(String text) {
        this.text = text;
    }

}
