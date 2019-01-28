package com.at.springboot.shiro.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.at.springboot.shiro.exception.ClientAccountException;
import com.at.springboot.shiro.exception.ClientException;
import com.at.springboot.shiro.exception.ServerAccountException;
import com.at.springboot.shiro.exception.ServerException;
import com.at.springboot.shiro.exception.ServerStorageException;
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
    private static final String TEMPLATES_PREFIX_JWT = "/jwt";
    private static final String JTI_INVALID = "INVALIDATED JTI";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("/login")
    public ModelAndView login() {
        log.info("Entering login...");
        
        ModelAndView mav = new ModelAndView(TEMPLATES_PREFIX_JWT + "/login");
        mav.addObject("message", "the login page");

        log.info("Leaving login...");
        return mav;
    }

    @RequestMapping(path = "/exthrower")
    @ResponseBody
    public String exceptionThrower(
            @RequestParam(name="exName", defaultValue="RuntimeException", required=true) String exName
            ) {
        log.info("Entering exceptionThrower...");
        
        if("RuntimeException".equals(exName)) {
            throw new RuntimeException("Runtime Exception");
        }else if("ClientException".equals(exName)) {
            throw new ClientException("Client Exception");
        }else if("ClientAccountException".equals(exName)) {
            throw new ClientAccountException("Client Account Exception");
        }else if("ServerException".equals(exName)) {
            throw new ServerException("Server Exception");
        }else if("ServerAccountException".equals(exName)) {
            throw new ServerAccountException("Server Account Exception");
        }else if("ServerStorageException".equals(exName)) {
            throw new ServerStorageException("Sever Storage Exception");
        }

        log.info("Leaving exceptionThrower...");
        return exName;
    }

    @RequestMapping(path = "/dologin")
    public LoginResponse doLogin(String username, String password) {
        log.info("Entering login...");
        
        // login
        Map<String, Object> queryResult = null;
        try {
            queryResult = jdbcTemplate.queryForMap(
                    "select user_id,user_name,password from sec_user where user_name = ? and password = ?"
                    , username
                    , password);
        }catch(DataAccessException e) {
            log.error("username/password is invalid. username: '{}', password: '{}'", username, password, e);
            throw new ClientAccountException("username/password is invalid.", e);
        }
        // get uid
        Long uid = (Long)queryResult.get("user_id");
        
        // gen jwt
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", uid.toString());
        String jwt = JwtUtils.genJws(claims);
        if(jwt == null) {
            log.error("gen jws failed, username: '{}', password: '{}'", username, password);
            throw new ServerAccountException("gen jws failed.");
        }
        
        // extract jti
        String jti = null;
        try {
            Jws<Claims> jwsClaims = JwtUtils.verifyJws(jwt);
            jti = jwsClaims.getBody().getId();
            log.info("the new generated jti: '{}'", jti);
        }catch(Exception e) {
            log.error("passing the new generated jws failed.", e);
            throw new ServerAccountException("passing the new generated jws failed.", e);
        }
        
        // update jti
        int affectedRows = jdbcTemplate.update("update sec_user set jti = ? where user_id = ?", jti, uid);
        if(affectedRows != 1) {
            log.error("update jti failed, jti: '{}', user_id: '{}'", jti, uid);
            throw new ServerStorageException("update jti failed");
        }

//        // shiro login
//        Subject shiroSubject = SecurityUtils.getSubject();
//        try {
//            shiroSubject.login(new JwtToken(jwt));
//        }catch(AuthenticationException e) {
//            log.error("shiro login failed. user_id: '{}', jti: '{}'", uid, jti, e);
//            throw new ServerAccountException("shiro login failed.", e);
//        }
        
        
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setJws(jwt);
        log.info("Leaving login...");
        return loginResponse;
    }

    @RequestMapping(path = "/dologout")
    public LogoutResponse doLogout(@RequestHeader(name="jwt", defaultValue="") String jws) {
        log.info("Entering logout...");

        
        // empty jws
        if("".equals(jws.trim())) {
            log.info("empty jws");

            LogoutResponse logoutResponse = new LogoutResponse();
            logoutResponse.setMsg("logedout");
            return logoutResponse;
        }
        
        // parse jws
        Jws<Claims> jwsClaims = null;
        try{
            jwsClaims = JwtUtils.verifyJws(jws);
        }catch(Exception e) {
            log.error("Incoming jws is invalid: '{}'", jws, e);
            
            throw new ClientAccountException("Incoming jws is invalid");
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
            log.error("jws is out of date, user_id: '{}', jti: '{}'", uid, jti, e);
            throw new ClientAccountException("jws is out of date.");
        }
        
//        // shiro logout
//        SecurityUtils.getSubject().logout();
        
        // invalid the jti
        int affectedRows = jdbcTemplate.update("update sec_user set jti = ? where user_id = ?", JTI_INVALID, uid);
        if(affectedRows != 1) {
            log.error("invalidating jti failed, user_id: '{}'", uid);
            throw new ServerStorageException("invalidating jti failed");
        }

        LogoutResponse logoutResponse = new LogoutResponse();
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
        
        ModelAndView mav = new ModelAndView(TEMPLATES_PREFIX_JWT + "/userhome");
        Map<String, String> user = new HashMap<>();
        user.put("username", uid);
        mav.addObject("user", user);

        log.info("Leaving userhome...");
        return mav;
    }
    
}
