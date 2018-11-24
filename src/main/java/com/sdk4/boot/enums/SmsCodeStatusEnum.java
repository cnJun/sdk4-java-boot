package com.sdk4.boot.enums;

import lombok.Getter;

@Getter
public enum SmsCodeStatusEnum {

    /**
     * 已发送待验证
     */
    PENDING(10, "待验证"),

    /**
     * 验证成功
     */
    SUCCESS(20, "验证成功"),

    /**
     * 发送或验证失败
     */
    FAIL(40, "发送或验证失败");

	private int code;
	private String text;

	SmsCodeStatusEnum(int code, String text) {
		this.code = code;
		this.text = text;
	}
}
