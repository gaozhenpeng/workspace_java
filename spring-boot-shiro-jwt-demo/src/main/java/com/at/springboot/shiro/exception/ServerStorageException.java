package com.at.springboot.shiro.exception;

/**
 * <p>Server side account related exception
 * </p>
 */
@SuppressWarnings("serial")
public class ServerStorageException extends ServerException{
    
    private void initProperties(String msg) {
        errorCode = "9202";
        errorMessage = msg; 
    }

    public ServerStorageException() {
        super();
        initProperties("Server Side Storage related Exception");
    }

    public ServerStorageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        initProperties(message);
    }

    public ServerStorageException(String message, Throwable cause) {
        super(message, cause);
        initProperties(message);
    }

    public ServerStorageException(String message) {
        super(message);
        initProperties(message);
    }

    public ServerStorageException(Throwable cause) {
        super(cause);
        initProperties(cause.getMessage());
    }

}
