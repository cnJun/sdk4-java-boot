package com.sdk4.boot.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sdk4.boot.config.FastJsonConfigration;
import com.sdk4.boot.exception.BaseError;
import com.sdk4.boot.exception.BusinessException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

/**
 * 基本响应
 *
 * @author sh
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BaseResponse<T> {
    protected Integer code;
    protected String message;
    protected T data;

    @JsonIgnore
    @JSONField(serialize=false)
    private Exception exception;

    public BaseResponse() {
    }

    public BaseResponse(BusinessException e) {
        this.code = e.getErrorCode();
        this.message = e.getErrorMessage();
    }

    public BaseResponse(BaseError e) {
        this.code = e.getCode();
        this.message = e.getMessage();
    }

    public BaseResponse(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            this.code = BaseError.INVALID_PARAMETER.getCode();
            ObjectError error = bindingResult.getAllErrors().get(0);
            this.message = error.getDefaultMessage();
        }
    }

    public BaseResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseResponse(int code, String message, Exception e) {
        this.code = code;
        this.message = message;
        this.exception = e;
    }

    public void put(BusinessException e) {
        this.code = e.getErrorCode();
        this.message = e.getErrorMessage();
    }

    public void put(BaseError e) {
        this.code = e.getCode();
        this.message = e.getMessage();
    }

    public void put(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            this.code = BaseError.INVALID_PARAMETER.getCode();
            ObjectError error = bindingResult.getAllErrors().get(0);
            this.message = error.getDefaultMessage();
        }
    }

    public void put(T data) {
        put(BaseError.SUCCESS);

        this.data = data;
    }

    public void put(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public void put(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public void put(int code, String message, Exception e) {
        this.code = code;
        this.message = message;
        this.exception = e;
    }

    @JsonIgnore
    @JSONField(serialize=false)
    public boolean isSuccess() {
        return code == null || code == 0;
    }

    @JsonIgnore
    @JSONField(serialize=false)
    public HttpStatus getHttpStatus() {
        HttpStatus httpStatus = HttpStatus.OK;

        if (code != null && code > 0) {
            if (code >= BaseError.LOGIN_FAIL.getCode() && code <= BaseError.TOKEN_EXPIRED.getCode()) {
                httpStatus = HttpStatus.UNAUTHORIZED;
            } else if (code == BaseError.UNAUTHORIZED.getCode()) {
                httpStatus = HttpStatus.FORBIDDEN;
            } else if (code == BaseError.INVALID_PARAMETER.getCode() || code == BaseError.INVALID_PARAMETER.getCode()) {
                httpStatus = HttpStatus.BAD_REQUEST;
            } else {
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }

        return httpStatus;
    }


    public String toJSONString() {
        if (code == null || StringUtils.isEmpty(message)) {
            this.code = BaseError.SUCCESS.getCode();
            this.message = BaseError.SUCCESS.getMessage();
        }

        return JSON.toJSONString(this,
                FastJsonConfigration.serializeConfig,
                SerializerFeature.WriteDateUseDateFormat);
    }
}
