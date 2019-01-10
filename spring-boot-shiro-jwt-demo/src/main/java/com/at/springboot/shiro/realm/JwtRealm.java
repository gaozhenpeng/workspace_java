package com.at.springboot.shiro.realm;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.message.FormattedMessage;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.Assert;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.at.springboot.shiro.controller.jwt.JwtToken;
import com.at.springboot.shiro.utils.JwtUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtRealm extends AuthorizingRealm {
    
    
    private JdbcTemplate jdbcTemplate;
    
    public JwtRealm(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public boolean supports(AuthenticationToken token) {
        log.info("Entering supports...");
        
        boolean isSupported = token instanceof JwtToken;
        
        log.info("Leaving supports...");
        return isSupported;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("Entering doGetAuthorizationInfo...");
        Assert.notNull(principals, "PrincipalCollection principals should not be null");
        Assert.isTrue(!principals.isEmpty(), "PrincipalCollection principals should not be empty");

        // parsing jws
        String jws = (String) principals.getPrimaryPrincipal();
        Jws<Claims> jwsClaims = null;
        try {
            jwsClaims = JwtUtils.verifyJws(jws);
        }catch(Exception e) {
//            String msg = new FormattedMessage("jws could not be verified. jws: '{}'", jws).toString();
//            log.error(msg, e);
//            throw new AuthenticationException(msg, e);
            //// should be authorized outside this method
            return null;
        }
        Claims jwsBody = jwsClaims.getBody();
        String jti = jwsBody.getId();
        String uid = jwsBody.get("uid", String.class);
        if(jti == null || uid == null) {
//            String msg = new FormattedMessage("both of uid and jti are required in jws. jti: '{}', uid: '{}'", jti, uid).toString();
//            log.error(msg);
//            throw new AuthenticationException(msg);
            //// should be authorized outside this method
            return null;
        }
        // result
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        
        
        // roles
        List<String> queryRolesResult = 
                jdbcTemplate.query(
                        "select sr.role_name from sec_role sr,sec_user_role sur where  sur.role_id = sr.role_id and sur.user_id = ? "
                        , new Object[] {uid}
                        , (rs, rowNum) -> rs.getString("role_name"));
        if(!CollectionUtils.isEmpty(queryRolesResult)) {
            simpleAuthorizationInfo.addRoles(queryRolesResult);
        }
        
        // permissions
        List<String> queryPermsResult = 
                jdbcTemplate.query(
                        "SELECT sp.permission_name FROM sec_permission sp, sec_role_permission srp WHERE 1 = 1  AND srp.role_id = ? AND sp.permission_id = srp.permission_id"
                        , new Object[] {uid}
                        , (rs, rowNum) -> rs.getString("permission_name"));
        if(!CollectionUtils.isEmpty(queryPermsResult)) {
            simpleAuthorizationInfo.addStringPermissions(queryPermsResult);
        }

        log.info("Leaving doGetAuthorizationInfo...");
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.info("Entering doGetAuthorizationInfo...");
        Assert.notNull(token, "AuthenticationToken token should not be null");
        Assert.isTrue(token instanceof JwtToken, "The token should be JwtToken");
        
        JwtToken jwtToken = (JwtToken) token;
        String jws = jwtToken.getJws();
        
        Jws<Claims> jwsClaims = null;
        try {
            jwsClaims = JwtUtils.verifyJws(jws);
        }catch(Exception e) {
            String msg = new FormattedMessage("jws could not be verified. jws: '{}'", jws).toString();
            log.error(msg, e);
            throw new AuthenticationException(msg, e);
        }
        
        Claims jwsBody = jwsClaims.getBody();
        String jti = jwsBody.getId();
        String uid = jwsBody.get("uid", String.class);
        if(jti == null || uid == null) {
            String msg = new FormattedMessage("both of uid and jti are required in jws. jti: '{}', uid: '{}'", jti, uid).toString();
            log.error(msg);
            throw new AuthenticationException(msg);
        }

        /**
         * <pre><code>
            ALTER TABLE `test`.`sec_user` 
                ADD COLUMN `jti` VARCHAR(45) NULL DEFAULT NULL AFTER `update_datetime`;
                
                
            CREATE TABLE `sec_user` (
              `user_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
              `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci DEFAULT NULL,
              `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci DEFAULT NULL,
              `created_datetime` datetime DEFAULT NULL,
              `update_datetime` datetime DEFAULT CURRENT_TIMESTAMP,
              `jti` varchar(45) COLLATE utf8mb4_unicode_520_ci DEFAULT NULL,
              PRIMARY KEY (`user_id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci
           </code></pre>
         */
        Map<String, Object> queryResult = jdbcTemplate.queryForMap("select user_id,jti from sec_user where user_id = ? ", uid);
        if(queryResult == null || queryResult.isEmpty() || queryResult.get("user_id") == null) {
            String msg = new FormattedMessage("uid is invalid. uid: '{}'", uid).toString();
            log.error(msg);
            throw new AuthenticationException(msg);
        }else if(queryResult.get("jti") == null || !queryResult.get("jti").equals(jti)) {
            String msg = new FormattedMessage("jti is out of date. jti: '{}'", jti).toString();
            log.error(msg);
            throw new AuthenticationException(msg);
        }
        
        // principal: username
        // credentials: password
        // 
        //    The principal and credentials will be compared with the incoming token's credentails
        //    They should be defined in the same way.
        //    
        //    The best practice is to set them both to the jws string, unique and quick(you don't need to parse them first)
        SimpleAuthenticationInfo simpleAuthenticationInfo = 
                new SimpleAuthenticationInfo(
                    jws
                    , jws
                    , this.getClass().getSimpleName().toLowerCase(Locale.ENGLISH));

        log.info("Leaving doGetAuthenticationInfo...");
        return simpleAuthenticationInfo;
    }

}
