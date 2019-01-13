package com.at.springboot.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import com.at.springboot.shiro.controller.jwt.JwtToken;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtFilter extends FormAuthenticationFilter  {
    /** http header name, jwt */
    public static final String JWT_HTTP_HEADER = "jwt";
    
    /** if false then onAccessDenied */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        log.debug("Entering isAccessAllowed...");
        boolean isAccessAllowed = (isLoginRequest(request, response) && loginOrThrowException(request, response));
        
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
    
    /** do login or throw exception if any, no redirect after executing */
    private boolean loginOrThrowException(ServletRequest request, ServletResponse response) {
        log.debug("Entering loginOrThrowException...");
        
        AuthenticationToken token = createToken(request, response);
        // delegate the login operation to realm.
        // an exception will be thrown if failed
        getSubject(request, response).login(token);
        
        log.debug("Leaving loginOrThrowException...");
        return true;
    }
    
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
        return super.preHandle(request, response);
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
        
        boolean onAccessDenied = false;
        
        log.debug("Leaving onAccessDenied...");
        return onAccessDenied;
    }
}
