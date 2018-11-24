package com.sdk4.boot.enums;

import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Map;

/**
 * 管理用户状态
 */
@Getter
public enum AdminUserStatusEnum {

    /**
     * 正常
     */
    NORMAL(20, "正常"),

    /**
     * 禁用
     */
    DISABLED(30, "禁用");

    private int code;
    private String text;

    AdminUserStatusEnum(int code, String text) {
        this.code = code;
        this.text = text;
    }

    static Map<Integer, AdminUserStatusEnum> _map = Maps.newConcurrentMap();

    static {
        AdminUserStatusEnum[] values = AdminUserStatusEnum.values();
        for (AdminUserStatusEnum item : values) {
            _map.put(item.code, item);
        }
    }

    public static AdminUserStatusEnum of(int code) {
        return _map.get(code);
    }

}
