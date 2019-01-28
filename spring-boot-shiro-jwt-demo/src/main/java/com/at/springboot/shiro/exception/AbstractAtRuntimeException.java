package com.at.springboot.shiro.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/** 
 * <p>The base runtime exception for this application.
 * </p>
 * <p>All other exceptions should extends this one.
 * </p>
 */
@Getter
@SuppressWarnings("serial")
public abstract class AbstractAtRuntimeException extends RuntimeException{
    HttpStatus httpStatus;
    String errorCode;
    String errorMessage;

    private void initProperties(String msg) {
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        errorCode = "9200";
        errorMessage = msg;
    }
    public AbstractAtRuntimeException() {
        super();
        initProperties("Unknown Exception");
    }

    public AbstractAtRuntimeException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        initProperties(message);
    }

    public AbstractAtRuntimeException(String message, Throwable cause) {
        super(message, cause);
        initProperties(message);
    }

    public AbstractAtRuntimeException(String message) {
        super(message);
        initProperties(message);
    }

    public AbstractAtRuntimeException(Throwable cause) {
        super(cause);
        initProperties(cause.getMessage());
    }
}
