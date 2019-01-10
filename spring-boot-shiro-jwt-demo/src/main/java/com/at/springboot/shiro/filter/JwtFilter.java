package com.at.springboot.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import com.at.springboot.shiro.controller.jwt.JwtToken;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * extends <strong> BasicHttpAuthenticationFilter</strong> is better 
 * than <strong>FormAuthenticationFilter</strong> and <strong>AuthenticatingFilter</strong>
 * <ul>
 *   <li>FormAuthenticationFilter and FormAuthenticationFilter limits to username/password style</li>
 *   <li>BasicHttpAuthenticationFilter provides login attempt mechanism</li>
 * </ul>
 * </p>
 * 
 */
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter  {
    /** http header name, jwt */
    public static final String JWT_HTTP_HEADER = "jwt";
    
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {
            executeLogin(request, response);
        }
        return true;
    }
    
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        log.debug("Entering isLoginAttempt...");
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader(JWT_HTTP_HEADER);
        log.debug("Leaving isLoginAttempt...");
        return authorization != null;
    }
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        log.debug("Entering executeLogin...");
        AuthenticationToken token = createToken(request, response);
        // delegate the login opeartion to realm.
        // an exception will be thrown if failed
        getSubject(request, response).login(token);
        // login success if no exception
        log.debug("Leaving executeLogin...");
        return true;
    }
    
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

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        log.debug("Entering createToken...");
        // extract jwt header
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        String jwt = httpRequest.getHeader(JWT_HTTP_HEADER);
        log.debug("extracted header '{}' from http request: '{}'", JWT_HTTP_HEADER, jwt);
        
        // WARNING: jwt could be null.
        log.debug("Leaving createToken...");
        return new JwtToken(jwt);
    }
}
