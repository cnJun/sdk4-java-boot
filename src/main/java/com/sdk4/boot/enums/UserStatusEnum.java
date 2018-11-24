package com.sdk4.boot.enums;

import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Map;

/**
 * 用户状态
 */
@Getter
public enum UserStatusEnum {

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

    UserStatusEnum(int code, String text) {
        this.code = code;
        this.text = text;
    }

    static Map<Integer, UserStatusEnum> _map = Maps.newConcurrentMap();

    static {
        UserStatusEnum[] values = UserStatusEnum.values();
        for (UserStatusEnum item : values) {
            _map.put(item.code, item);
        }
    }

    public static UserStatusEnum of(int code) {
        return _map.get(code);
    }

}
