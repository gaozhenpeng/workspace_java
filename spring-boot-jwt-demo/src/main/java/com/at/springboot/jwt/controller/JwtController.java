package com.at.springboot.jwt.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.at.springboot.jwt.utils.JwtUtils;
import com.at.springboot.jwt.vo.LoginResponse;
import com.at.springboot.jwt.vo.LogoutResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/jwt")
public class JwtController {
    private static final String USERNAME = "myusername";
    private static final String PASSWORD = "mypassword";
    private static final String UID = "myuid";
    private static String currentJti = "mycurrentjti";

    @RequestMapping("/login")
    public ModelAndView login() {
        log.info("Entering login...");
        
        ModelAndView mav = new ModelAndView("/login");
        mav.addObject("message", "the login page");

        log.info("Leaving login...");
        return mav;
    }
    

    @RequestMapping(path = "/dologin")
    public LoginResponse doLogin(String username, String password) {
        log.info("Entering login...");
        
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setOk(false);
        loginResponse.setMsg("unknown error");
        if(!USERNAME.equals(username) || !PASSWORD.equals(password)) {
//            throw new RuntimeException("login failed");
            loginResponse.setOk(false);
            loginResponse.setMsg("login failed");
            log.error("login failed, username: '{}', password: '{}'", username, password);
            return loginResponse;
        }
        // logined
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", UID);
        
        // gen jwt
        String jwt = JwtUtils.genJws(claims);
        if(jwt == null) {
            loginResponse.setOk(false);
            loginResponse.setMsg("gen jws failed");
            log.error("gen jws failed, username: '{}', password: '{}'", username, password);
            return loginResponse;
            
        }
        loginResponse.setJws(jwt);
        
        try {
            Jws<Claims> jwsClaims = JwtUtils.verifyJws(jwt);
            String jti = jwsClaims.getBody().getId();
            currentJti = jti;
            log.info("the new generated jti: '{}'", currentJti);
        }catch(Exception e) {
            log.error("Passing the new generated jws failed.", e);
        }
        

        loginResponse.setOk(true);
        loginResponse.setMsg("generated");
        log.info("Leaving login...");
        return loginResponse;
    }

    @RequestMapping(path = "/dologout")
    public LogoutResponse doLogout(
            @RequestAttribute(required = true) boolean isAuthenticated
            ,@RequestAttribute(required = false) String uid
            ,@RequestAttribute(required = false) String jti
            , HttpServletRequest request) {
        log.info("Entering logout...");
        LogoutResponse logoutResponse = new LogoutResponse();
        logoutResponse.setOk(false);
        logoutResponse.setMsg("unknown error");
        
//        boolean isAuthenticated = (boolean)request.getAttribute("isAuthenticated");
        if(!isAuthenticated) {
            logoutResponse.setOk(false);
            logoutResponse.setMsg("current token is invalid.");
            log.error("current token is invalid.");
            return logoutResponse;
        }
        

        // update user set jti = 'logout' where uid = 'uid' and jti = 'jti'
        // ;
        // -- ensure affected rows = 1 
        
//        String jti = (String)request.getAttribute("jti");
        if(!currentJti.equals(jti)) {
            logoutResponse.setOk(false);
            logoutResponse.setMsg("current token is out of date.");
            log.error("current token is out of date.");
            return logoutResponse;
        }
        currentJti = "logut";
        log.info("loged out.");

        logoutResponse.setOk(true);
        logoutResponse.setMsg("logedout");
        log.info("Leaving logout...");
        return logoutResponse;
    }

    @RequestMapping("/userhome")
    public ModelAndView userhome(
            @RequestAttribute(required = true) boolean isAuthenticated
            ,@RequestAttribute(required = false) String uid
            ,@RequestAttribute(required = false) String jti
            , HttpServletRequest request) {
        log.info("Entering userhome...");
        
//        boolean isAuthenticated = (boolean)request.getAttribute("isAuthenticated");
        if(!isAuthenticated) {
            log.error("current token is invalid.");
            throw new RuntimeException("current token is invalid.");
        }
        

//        String jti = (String)request.getAttribute("jti");
        if(!currentJti.equals(jti)) {
            log.error("current token is out of date.");
            throw new RuntimeException("current token is out of date.");
        }

        ModelAndView mav = new ModelAndView("/userhome");
        Map<String, String> user = new HashMap<>();
        user.put("username", uid);
        mav.addObject("user", user);

        log.info("Leaving userhome...");
        return mav;
    }
    
}
