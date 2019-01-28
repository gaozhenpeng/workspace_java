package com.at.springboot.shiro.shiro.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;

import com.at.springboot.shiro.exception.AbstractAtRuntimeException;
import com.at.springboot.shiro.exception.ClientAccountException;
import com.at.springboot.shiro.shiro.token.JwtToken;
import com.at.springboot.shiro.vo.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtFilter extends FormAuthenticationFilter  {
    /** jackson object mapper */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    
    /** http header name, jwt */
    private static final String JWT_HTTP_HEADER = "jwt";

    
    /** allow cross site call */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        log.debug("Entering preHandle...");
        
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // origin-crossing request will send 'options' request first
        // so terminate the processing for 'options'
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        
        log.debug("Leaving preHandle...");
        return super.preHandle(request, response); // isAccessAllowed || onAccessDenied
    }

    /** if false then onAccessDenied */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        log.debug("Entering isAccessAllowed...");
        boolean isAccessAllowed = false;
        if(isLoginRequest(request, response)) {
            isAccessAllowed = loginOrWriteException(request, response);
        }else {
            writeException(response, new ClientAccountException("jwttoken is required"));
        }
        
        log.debug("Leaving isAccessAllowed...");
        return isAccessAllowed;
    }

    /** request with jwt header is loginRequest */
    @Override
    protected boolean isLoginRequest(ServletRequest request, ServletResponse response) {
        log.debug("Entering isLoginAttempt...");
        boolean isLoginAttempt = false;
        if(HttpServletRequest.class.isAssignableFrom(request.getClass())) {
            HttpServletRequest req = (HttpServletRequest) request;
            String authorization = req.getHeader(JWT_HTTP_HEADER);
            isLoginAttempt = (authorization != null);
        }else {
            isLoginAttempt = false;
        }
        log.debug("isLoginAttempt: '{}'", isLoginAttempt);
        
        log.debug("Leaving isLoginAttempt...");
        return isLoginAttempt;
    }
    
    /** do login or throw exception if any, no redirect after executing  */
    private boolean loginOrWriteException(ServletRequest request, ServletResponse response){
        log.debug("Entering loginOrThrowException...");
        
        boolean isOK = false;
        
        AuthenticationToken token = createToken(request, response);
        // delegate the login operation to realm.
        // an exception will be thrown if failed
        try {
            getSubject(request, response).login(token);
            isOK = true;
        }catch(Exception e) {
            log.warn("jwt shiro login failed.", e);
            
            writeException(response, new ClientAccountException(e));
            
            isOK = false;
        }
        
        log.debug("Leaving loginOrThrowException...");
        return isOK;
    }
    
    private void writeException(ServletResponse response, AbstractAtRuntimeException ex) {
        if(HttpServletResponse.class.isAssignableFrom(response.getClass())) {
            try {
                HttpServletResponse res = (HttpServletResponse) response;
                
                ExceptionResponse exceptionResponse = 
                        new ExceptionResponse(
                            ex.getErrorCode()
                            , ex.getErrorMessage());
                
                res.setStatus(ex.getHttpStatus().value());
                res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                PrintWriter printWriter = res.getWriter();
                printWriter.write(OBJECT_MAPPER.writeValueAsString(exceptionResponse));
                //// WARNING: Don't close the printWriter, or it will be short of some finishing operations
//                printWriter.flush();
//                printWriter.close();
            }catch(IOException e) {
                log.error("Not able to output the exception content.", e);
            }
        }
    }
    /** create JwtToken */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        log.debug("Entering createToken...");
        // extract jwt header
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        String jwt = httpRequest.getHeader(JWT_HTTP_HEADER);
        log.debug("extracted header '{}' from http request: '{}'", JWT_HTTP_HEADER, jwt);
        
        AuthenticationToken jwtToken =  new JwtToken(jwt);
        log.debug("jwtToken: '{}'", jwtToken);
        
        // WARNING: jwt could be null.
        log.debug("Leaving createToken...");
        return jwtToken;
    }
    
    /**
     * The super.onAccessDenied(ServletRequest, ServletResponse) would redirect to loginUrl
     * if isAccessAllowed returns false
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        log.debug("Entering onAccessDenied...");
        
        boolean isPassed = false;
        
        log.debug("Leaving onAccessDenied...");
        return isPassed;
    }
}
