package com.sdk4.boot.exception;

import lombok.Getter;

/**
 * 数据库操作异常
 */
@Getter
public class DaoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private int errorCode;
    private String errorMessage;

    public DaoException() {
        super();
    }

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoException(Throwable cause) {
        super(cause);
    }

    public DaoException(int errorCode, String errorMessage, Throwable cause) {
        super(errorMessage, cause);

        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public DaoException(int errorCode, String errorMessage) {
        super(errorMessage);

        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
