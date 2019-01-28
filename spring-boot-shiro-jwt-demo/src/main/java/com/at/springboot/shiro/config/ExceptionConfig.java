package com.at.springboot.shiro.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.at.springboot.shiro.exception.AbstractAtRuntimeException;
import com.at.springboot.shiro.exception.ServerException;
import com.at.springboot.shiro.vo.ExceptionResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ExceptionConfig {
    private static final ServerException SERVER_EXCEPTION = new ServerException();
    
    @ControllerAdvice(basePackages = "com.at.springboot.shiro")
    public static class AtRuntimeExceptionAdvice extends ResponseEntityExceptionHandler {

        @ExceptionHandler(RuntimeException.class)
        @ResponseBody
        public ResponseEntity<?> handleRuntimeException(HttpServletRequest request, RuntimeException ex) {
            log.info("Entering handleRuntimeException...");
            
            log.info("RuntimeException catched: '{}'", ex.getMessage());
            
            if(log.isDebugEnabled()) {
                log.debug("RuntimeException catched in details.", ex);
            }
            
            
            HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            Object statusCodeObj = request.getAttribute("javax.servlet.error.status_code");
            if (statusCodeObj != null) {
                httpStatus = HttpStatus.valueOf((Integer)statusCodeObj);
            }
            ResponseEntity<?> responseEntity = 
                    new ResponseEntity<>(
                        new ExceptionResponse(
                                SERVER_EXCEPTION.getErrorCode()
                                , SERVER_EXCEPTION.getErrorMessage() + ", " + ex.getMessage())
                        , httpStatus);
            log.debug("RuntimeException responseEntity: '{}'", responseEntity);
            
            
            log.info("Leaving handleRuntimeException...");
            return responseEntity;
        }
        

        @ExceptionHandler(AbstractAtRuntimeException.class)
        @ResponseBody
        public ResponseEntity<?> handleAbstractAtRuntimeException(HttpServletRequest request, AbstractAtRuntimeException ex) {
            log.info("Entering handleAbstractAtRuntimeException...");
            
            log.info("AbstractAtRuntimeException catched: '{}'", ex.getMessage());
            
            if(log.isDebugEnabled()) {
                log.debug("AbstractAtRuntimeException catched in details.", ex);
            }
            
            ResponseEntity<?> responseEntity = 
                    new ResponseEntity<>(
                        new ExceptionResponse(
                                ex.getErrorCode()
                                , ex.getErrorMessage())
                        , ex.getHttpStatus());
            log.debug("AbstractAtRuntimeException responseEntity: '{}'", responseEntity);
            
            
            log.info("Leaving handleAbstractAtRuntimeException...");
            return responseEntity;
        }
    }
}
