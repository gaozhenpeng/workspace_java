package com.at.springboot.shiro.exception;

import org.springframework.http.HttpStatus;

/**
 * <p>The base class for all the client related conditions (client side)
 * </p>
 */
@SuppressWarnings("serial")
public class ClientException extends AbstractAtRuntimeException{

    private void initProperties(String msg) {
        httpStatus = HttpStatus.BAD_REQUEST;
        errorCode = "9100";
        errorMessage = msg;
    }
    
    public ClientException() {
        super();
        initProperties("Client Side Exception");
    }

    public ClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        initProperties(message);
    }

    public ClientException(String message, Throwable cause) {
        super(message, cause);
        initProperties(message);
    }

    public ClientException(String message) {
        super(message);
        initProperties(message);
    }

    public ClientException(Throwable cause) {
        super(cause);
        initProperties(cause.getMessage());
    }
}
