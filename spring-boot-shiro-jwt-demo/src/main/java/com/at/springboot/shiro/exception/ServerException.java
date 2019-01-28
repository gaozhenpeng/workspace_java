package com.at.springboot.shiro.exception;

import org.springframework.http.HttpStatus;

/**
 * <p>The base class for all the server related conditions (server side)
 * </p>
 */
@SuppressWarnings("serial")
public class ServerException extends AbstractAtRuntimeException{

    private void initProperties(String msg) {
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        errorCode = httpStatus.value() + "-00";
        errorMessage = msg;
    }
    
    public ServerException() {
        super();
        initProperties("Server Side Exception");
    }

    public ServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        initProperties(message);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
        initProperties(message);
    }

    public ServerException(String message) {
        super(message);
        initProperties(message);
    }

    public ServerException(Throwable cause) {
        super(cause);
        initProperties(cause.getMessage());
    }
}
