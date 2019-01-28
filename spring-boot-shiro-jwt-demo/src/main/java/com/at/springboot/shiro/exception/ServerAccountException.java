package com.at.springboot.shiro.exception;

/**
 * <p>Server side account related exception
 * </p>
 */
@SuppressWarnings("serial")
public class ServerAccountException extends ServerException{

    private void initProperties(String msg) {
        errorCode = "9201";
        errorMessage = msg;
    }
    
    public ServerAccountException() {
        super();
        initProperties("Server Side Account related Exception");
    }

    public ServerAccountException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        initProperties(message);
    }

    public ServerAccountException(String message, Throwable cause) {
        super(message, cause);
        initProperties(message);
    }

    public ServerAccountException(String message) {
        super(message);
        initProperties(message);
    }

    public ServerAccountException(Throwable cause) {
        super(cause);
        initProperties(cause.getMessage());
    }
}
