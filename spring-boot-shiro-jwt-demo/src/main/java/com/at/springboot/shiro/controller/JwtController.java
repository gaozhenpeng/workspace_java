package com.at.springboot.shiro.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.at.springboot.shiro.utils.JwtUtils;
import com.at.springboot.shiro.vo.LoginResponse;
import com.at.springboot.shiro.vo.LogoutResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/jwt")
public class JwtController {
    private static final String JSP_PREFIX_JWT = "/jwt";
    private static final String JTI_INVALID = "INVALIDATED JTI";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("/login")
    public ModelAndView login() {
        log.info("Entering login...");
        
        ModelAndView mav = new ModelAndView(JSP_PREFIX_JWT + "/login");
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
        
        // login
        Map<String, Object> queryResult = null;
        try {
            queryResult = jdbcTemplate.queryForMap(
                    "select user_id,user_name,password from sec_user where user_name = ? and password = ?"
                    , username
                    , password);
        }catch(DataAccessException e) {
            loginResponse.setOk(false);
            loginResponse.setMsg("username/password is invalid.");
            log.error("username/password is invalid. username: '{}', password: '{}'", username, password, e);
            return loginResponse;
        }
        // get uid
        Long uid = (Long)queryResult.get("user_id");
        
        // gen jwt
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", uid.toString());
        String jwt = JwtUtils.genJws(claims);
        if(jwt == null) {
            loginResponse.setOk(false);
            loginResponse.setMsg("gen jws failed");
            log.error("gen jws failed, username: '{}', password: '{}'", username, password);
            return loginResponse;
        }
        
        // extract jti
        String jti = null;
        try {
            Jws<Claims> jwsClaims = JwtUtils.verifyJws(jwt);
            jti = jwsClaims.getBody().getId();
            log.info("the new generated jti: '{}'", jti);
        }catch(Exception e) {
            log.error("Passing the new generated jws failed.", e);
        }
        
        // update jti
        int affectedRows = jdbcTemplate.update("update sec_user set jti = ? where user_id = ?", jti, uid);
        if(affectedRows != 1) {
            loginResponse.setOk(false);
            loginResponse.setMsg("update jti failed");
            log.error("update jti failed, jti: '{}', user_id: '{}'", jti, uid);
            return loginResponse;
        }
        loginResponse.setJws(jwt);
        loginResponse.setOk(true);
        loginResponse.setMsg("generated");
        log.info("Leaving login...");
        return loginResponse;
    }

    @RequestMapping(path = "/dologout")
    public LogoutResponse doLogout(@RequestHeader(name="jwt", defaultValue="") String jws) {
        log.info("Entering logout...");
        LogoutResponse logoutResponse = new LogoutResponse();
        logoutResponse.setOk(false);
        logoutResponse.setMsg("unknown error");
        
        // empty jws
        if("".equals(jws.trim())) {
            log.info("empty jws");
            
            logoutResponse.setOk(true);
            logoutResponse.setMsg("logedout");
            return logoutResponse;
        }
        
        // parse jws
        Jws<Claims> jwsClaims = null;
        try{
            jwsClaims = JwtUtils.verifyJws(jws);
        }catch(Exception e) {
            log.error("Incoming jws is invalid: '{}'", jws, e);

            logoutResponse.setOk(false);
            logoutResponse.setMsg("Incoming jws is invalid");
            return logoutResponse;
        }
        
        // extract jti, uid
        String jti = jwsClaims.getBody().getId();
        String uid = jwsClaims.getBody().get("uid", String.class);
        
        // check jti, ensure the jti and uid matches
        try {
            jdbcTemplate.queryForMap(
                    "select user_id from sec_user where user_id = ? and jti = ?"
                    , uid
                    , jti);
        }catch(DataAccessException e) {
            logoutResponse.setOk(false);
            logoutResponse.setMsg("jws is out of date.");
            log.error("jws is out of date, user_id: '{}', jti: '{}'", uid, jti, e);
            return logoutResponse;
        }
        
        // invalid the jti
        int affectedRows = jdbcTemplate.update("update sec_user set jti = ? where user_id = ?", JTI_INVALID, uid);
        if(affectedRows != 1) {
            logoutResponse.setOk(false);
            logoutResponse.setMsg("invalidating jti failed");
            log.error("invalidating jti failed, user_id: '{}'", uid);
            return logoutResponse;
        }

        logoutResponse.setOk(true);
        logoutResponse.setMsg("logedout");
        log.info("Leaving logout...");
        return logoutResponse;
    }

    @RequestMapping("/userhome")
    public ModelAndView userhome(@RequestHeader(name="jwt", required=true) String jws) {
        log.info("Entering userhome...");
        
        log.debug("incoming jws: '{}'", jws);

        // parse jws
        Jws<Claims> jwsClaims = JwtUtils.verifyJws(jws);
        
        // extract jti, uid
        String jti = jwsClaims.getBody().getId();
        String uid = jwsClaims.getBody().get("uid", String.class);
        log.debug("jti: '{}', uid: '{}'", jti, uid);
        
        ModelAndView mav = new ModelAndView(JSP_PREFIX_JWT + "/userhome");
        Map<String, String> user = new HashMap<>();
        user.put("username", uid);
        mav.addObject("user", user);

        log.info("Leaving userhome...");
        return mav;
    }
    
}
