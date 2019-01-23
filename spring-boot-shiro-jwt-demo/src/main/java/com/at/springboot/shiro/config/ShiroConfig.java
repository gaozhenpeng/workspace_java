package com.at.springboot.shiro.config;

import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.at.springboot.shiro.filter.JwtFilter;
import com.at.springboot.shiro.realm.JwtRealm;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ShiroConfig {
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        log.info("Entering shiroFilterChainDefinition...");
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        
//        // logged in users with the 'admin' role
//        chainDefinition.addPathDefinition("/admin/**", "authc, roles[admin]");
//        
//        // logged in users with the 'document:read' permission
//        chainDefinition.addPathDefinition("/docs/**", "authc, perms[document:read]");
//        
//        // all other paths require a logged in user
//        chainDefinition.addPathDefinition("/**", "authc");
        
        
        
//        // authc, it only supports well only for 1 realm in the whole domain 
//        chainDefinition.addPathDefinition("/shiro/login", "anon");
//        chainDefinition.addPathDefinition("/shiro/dologin", "anon");
//
//        chainDefinition.addPathDefinition("/shiro/admin/home", "authc");
//        
//        // ',' in perms[] and roles[] means logical 'AND'
//        chainDefinition.addPathDefinition("/shiro/admin/withperm", "authc,perms[user:create]");
//        chainDefinition.addPathDefinition("/shiro/admin/withrole", "authc,roles[user]");

        // jwt
        chainDefinition.addPathDefinition("/jwt/login", "anon");
        chainDefinition.addPathDefinition("/jwt/dologin", "anon");

        chainDefinition.addPathDefinition("/jwt/userhome", "jwt,roles[user]");
        
//        // ',' in perms[] and roles[] means logical 'AND'
//        chainDefinition.addPathDefinition("/jwt/admin/withperm", "jwt,perms[user:create]");
//        chainDefinition.addPathDefinition("/jwt/admin/withrole", "jwt,roles[user]");
        
        // everything else can be accessed anonymously:
        //   you can still combine annotations with this config as default
        //     anon : @RequiresGuest
        //     authc : @RequiresAuthentication
        //     perms[resource1:op1,resource1:op2,resource2:op1] : @RequiresPermissions(value = {"resource1:op1", "resource1:op2", "resource2:op1"})
        //     roles[role1,role2] : @RequiresRoles(value = {"role1", "role2"})
        //   annotation examples: {@link com.at.springboot.shiro.controller.ShiroController#admin(String)}
        chainDefinition.addPathDefinition("/**", "anon");

        log.info("Leaving shiroFilterChainDefinition...");
        return chainDefinition;
    }
//    /** */
//    @Bean
//    public AuthenticationStrategy authenticationStrategy() {
//        return new FirstSuccessfulStrategy();
//    }
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(
            @Value("#{ @environment['shiro.loginUrl'] ?: '/login.jsp' }") String loginUrl
            ,@Value("#{ @environment['shiro.successUrl'] ?: '/' }") String successUrl
            ,@Value("#{ @environment['shiro.unauthorizedUrl'] ?: null }") String unauthorizedUrl
            ,org.apache.shiro.mgt.SecurityManager securityManager
            ,ShiroFilterChainDefinition shiroFilterChainDefinition
            ,Map<String, Filter> filterMap) {
        log.info("Entering shiroFilterFactoryBean...");
        
        
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();

        filterFactoryBean.setLoginUrl(loginUrl);
        filterFactoryBean.setSuccessUrl(successUrl);
        filterFactoryBean.setUnauthorizedUrl(unauthorizedUrl);
        filterFactoryBean.setSecurityManager(securityManager);
        filterFactoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition.getFilterChainMap());
        
        log.debug("filterMap: '{}'", filterMap);
        // add jwt filter
        //    the filter name 'jwt' which is used in the ShiroFilterChainDefinition
        //    e.g. chainDefinition.addPathDefinition("/jwt/userhome", "jwt");
        filterMap.put("jwt", new JwtFilter());
        log.debug("filterMap: '{}'", filterMap);
        filterFactoryBean.setFilters(filterMap);

        log.info("Leaving shiroFilterFactoryBean...");
        return filterFactoryBean;
    }

    /**
     * <pre><code>
         Used by the SecurityManager to access security data (users, roles, etc).
         Many other realm implementations can be used too (PropertiesRealm,
         LdapRealm, etc.
         Shiro native JdbcRealm
         
            DROP TABLE IF EXISTS `sec_role_permission`;
            DROP TABLE IF EXISTS `sec_user_role`;
            DROP TABLE IF EXISTS `sec_user`;
            DROP TABLE IF EXISTS `sec_role`;
            DROP TABLE IF EXISTS `sec_permission`;
            
            CREATE TABLE `sec_permission` (
              `permission_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
              `permission_name` varchar(64) COLLATE utf8mb4_unicode_520_ci DEFAULT NULL,
              `created_datetime` datetime DEFAULT NULL,
              `update_datetime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
              PRIMARY KEY (`permission_id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;
            
            insert into `sec_permission` (permission_id, permission_name, created_datetime) values(1, '*', now()), (2, 'user:*', now()), (3, 'page:view', now()), (4, 'doc:view', now())
            ;
            
            CREATE TABLE `sec_role` (
              `role_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
              `role_name` varchar(64) COLLATE utf8_bin DEFAULT NULL,
              `created_datetime` datetime DEFAULT NULL,
              `update_datetime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
              PRIMARY KEY (`role_id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;
            
            insert into `sec_role` (role_id, role_name, created_datetime) values(1, 'superadmin', now()), (2, 'admin', now()), (3, 'user', now())
            ;
            
            
            CREATE TABLE `sec_role_permission` (
              `role_permission_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
              `permission_id` int(10) unsigned NOT NULL,
              `role_id` int(10) unsigned NOT NULL,
              PRIMARY KEY (`role_permission_id`),
              KEY `FK_SRP_PERMISSION_ID` (`permission_id`),
              KEY `FK_SRP_ROLE_ID` (`role_id`),
              CONSTRAINT `FK_SRP_PERMISSION_ID` FOREIGN KEY (`permission_id`) REFERENCES `sec_permission` (`permission_id`) ON DELETE CASCADE ON UPDATE CASCADE,
              CONSTRAINT `FK_SRP_ROLE_ID` FOREIGN KEY (`role_id`) REFERENCES `sec_role` (`role_id`) ON DELETE CASCADE ON UPDATE CASCADE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;
            
            insert into `sec_role_permission` (role_permission_id, role_id, permission_id) values(1, 1, 1), (2, 2, 2), (3, 3, 3), (4, 3, 4)
            ;
            
            
            CREATE TABLE `sec_user` (
              `user_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
              `user_name` varchar(64) COLLATE utf8mb4_unicode_520_ci DEFAULT NULL,
              `password` varchar(128) COLLATE utf8mb4_unicode_520_ci DEFAULT NULL,
              `created_datetime` datetime DEFAULT NULL,
              `update_datetime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
              `jti` varchar(32)  COLLATE utf8mb4_unicode_520_ci DEFAULT NULL,
              PRIMARY KEY (`user_id`)
            ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;
            
            insert into `sec_user` (user_id, user_name, password, created_datetime) values(1, 'rt', 'rtpw', now()), (2, 'adm', 'admpw', now()), (3, 'at', 'atpw', now())
            ;
            
            
            CREATE TABLE `sec_user_role` (
              `user_role_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
              `user_id` int(10) unsigned DEFAULT NULL,
              `role_id` int(10) unsigned DEFAULT NULL,
              PRIMARY KEY (`user_role_id`),
              KEY `FK_SUR_USER_ID` (`user_id`),
              KEY `FK_SUR_ROLE_ID` (`role_id`),
              CONSTRAINT `FK_SUR_ROLE_ID` FOREIGN KEY (`role_id`) REFERENCES `sec_role` (`role_id`) ON DELETE CASCADE ON UPDATE CASCADE,
              CONSTRAINT `FK_SUR_USER_ID` FOREIGN KEY (`user_id`) REFERENCES `sec_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
            ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;
            
            insert into `sec_user_role` (user_role_id, user_id, role_id) values (1, 1, 1), (2, 1, 2), (3, 1, 3), (4, 2, 2), (5,3,3)
            ;
       </code></pre>
     *
     */
    @Bean
    public Realm shiroJwtRealm(@Autowired JdbcTemplate jdbcTemplate) {
        log.info("Entering shiroJwtRealm...");
        
        JwtRealm jwtRealm = new JwtRealm(jdbcTemplate);
        
        log.info("Leaving shiroJwtRealm...");
        return jwtRealm;
    }
    

    /** shiro tags for thymeleaf */
    @Bean
    public ShiroDialect shiroDialect(){
        return new ShiroDialect();
    }
}
