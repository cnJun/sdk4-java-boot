package com.sdk4.boot;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 服务调用返回结果
 *
 * @author sh
 */
@Data
public class CallResult<T> {
    private int code;
    private String message;
    private T data;

    @JSONField(serialize=false)
    private Exception exception;

    public boolean success() {
        return code == 0;
    }

    public void setError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public void setError(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public void setError(int code, String message, Exception e) {
        this.code = code;
        this.message = message;
        this.exception = e;
    }
}
