package com.sdk4.boot.exception;

import lombok.Getter;

/**
 * 业务异常
 *
 * @author sh
 */
@Getter
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private int errorCode;
    private String errorMessage;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(int errorCode, String errorMessage, Throwable cause) {
        super(errorMessage, cause);

        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public BusinessException(int errorCode, String errorMessage) {
        super(errorMessage);

        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
