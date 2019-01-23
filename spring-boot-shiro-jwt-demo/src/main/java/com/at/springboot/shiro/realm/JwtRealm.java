package com.at.springboot.shiro.realm;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.message.FormattedMessage;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationException;
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
        
        boolean isSupported = (token != null && JwtToken.class.isAssignableFrom(token.getClass()));
        log.debug("isSupported: '{}'", isSupported);
        
        log.info("Leaving supports...");
        return isSupported;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("Entering doGetAuthorizationInfo...");
        Assert.notNull(principals, "PrincipalCollection principals should not be null");
        Assert.isTrue(!principals.isEmpty(), "PrincipalCollection principals should not be empty");

        @SuppressWarnings("rawtypes")
        Collection realmPrincipals = principals.fromRealm(getName());
        if(CollectionUtils.isEmpty(realmPrincipals)) {
            log.info("No principals for this realm, '{}'", getName());
            return null;
        }
        
        // parsing jws
        String jws = (String) realmPrincipals.iterator().next();
        log.debug("jws: '{}'", jws);
        Jws<Claims> jwsClaims = null;
        try {
            jwsClaims = JwtUtils.verifyJws(jws);
        }catch(Exception e) {
//            String msg = new FormattedMessage("jws could not be verified. jws: '{}'", jws).toString();
//            log.error(msg, e);
//            throw new AuthenticationException(msg, e);
            //// should be authorized outside this method
            String msg = new FormattedMessage("the jws is invalid, '{}'", jws).toString();
            log.error(msg, e);
            throw new AuthorizationException(msg, e);
        }
        Claims jwsBody = jwsClaims.getBody();
        String jti = jwsBody.getId();
        String uid = jwsBody.get("uid", String.class);
        if(jti == null || uid == null) {
            //// should be authorized outside this method
            String msg = new FormattedMessage("both of jti and uid are required to be not null, jti: '{}', uid: '{}'", jti, uid).toString();
            log.error(msg);
            throw new AuthorizationException(msg);
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

        log.info("uid: '{}', roles: '{}', permissions: '{}'", uid, simpleAuthorizationInfo.getRoles(), simpleAuthorizationInfo.getStringPermissions());

        log.info("Leaving doGetAuthorizationInfo...");
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.info("Entering doGetAuthenticationInfo...");
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

        Map<String, Object> queryResult = jdbcTemplate.queryForMap("select user_id,jti from sec_user where user_id = ? ", uid);
        if(queryResult == null || queryResult.isEmpty() || queryResult.get("user_id") == null) {
            String msg = new FormattedMessage("uid is invalid. uid: '{}'", uid).toString();
            log.error(msg);
            throw new AuthenticationException(msg);
        }else if(queryResult.get("jti") == null || !queryResult.get("jti").equals(jti)) {
            String msg = new FormattedMessage("jti is out of date. jti: '{}', uid: '{}'", jti, uid).toString();
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
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(jws, jws, getName());

        log.info("Leaving doGetAuthenticationInfo...");
        return simpleAuthenticationInfo;
    }

}
