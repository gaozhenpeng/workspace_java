package com.at.springboot.shiro.exception;

/**
 * <p>Account related exception
 * </p>
 */
@SuppressWarnings("serial")
public class ClientAccountException extends ClientException{

    private void initProperties(String msg) {
        errorCode = "9101";
        errorMessage = msg;
    }
    public ClientAccountException() {
        super();
        initProperties("Client Side Account related Exception");
    }

    public ClientAccountException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        initProperties(message);
    }

    public ClientAccountException(String message, Throwable cause) {
        super(message, cause);
        initProperties(message);
    }

    public ClientAccountException(String message) {
        super(message);
        initProperties(message);
    }

    public ClientAccountException(Throwable cause) {
        super(cause);
        initProperties(cause.getMessage());
    }
    
}
