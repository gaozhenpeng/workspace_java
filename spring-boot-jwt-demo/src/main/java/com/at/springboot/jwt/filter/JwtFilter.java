package com.at.springboot.jwt.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.at.springboot.jwt.utils.JwtUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtFilter  implements Filter  {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.debug("Entering JwtFilter...");
        
        HttpServletRequest req = (HttpServletRequest) request;
        log.info("requestUri: '{}'", req.getRequestURI());
        
        String jwt = req.getHeader("jwt");
        if(jwt != null) {
            Jws<Claims> jws = JwtUtils.verifyJws(jwt);
            if(jws == null) {
                ServletException e = new ServletException("jwt is not verified.");
                log.error("jwt is presented, but can not be verified. '{}'", jwt);
                throw e;
            }
            String uid = jws.getBody().get("uid", String.class);
            String jti = jws.getBody().getId();
            // assembly the jws body into an vo
            
            
            
            req.setAttribute("isAuthenticated", true);
            req.setAttribute("uid", uid);
            req.setAttribute("jti", jti);

            
        }else {

            req.setAttribute("isAuthenticated", false);
////            ServletException e = new ServletException("jwt is required.");
////            log.error("jwt is required.");
////            throw e;
//            HttpServletResponse res = (HttpServletResponse) response;
//            res.sendRedirect(req.getContextPath() + "/jwt/login");
//            res.flushBuffer();
        }
        
        log.debug("Leaving JwtFilter...");
        // chaining
        chain.doFilter(request, response);
    }

}
