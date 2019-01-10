package com.at.springboot.shiro.config;

import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.IniRealm;
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
        
        chainDefinition.addPathDefinition("/shiro/login", "anon");
        chainDefinition.addPathDefinition("/shiro/dologin", "anon");

        chainDefinition.addPathDefinition("/shiro/admin/home", "authc");
        
        // ',' in perms[] and roles[] means logical 'AND'
        chainDefinition.addPathDefinition("/shiro/admin/withperm", "authc,perms[user:create]");
        chainDefinition.addPathDefinition("/shiro/admin/withrole", "authc,roles[user]");
        

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

    @Bean
    public Realm shiroIniRealm() {
        log.info("Entering shiroIniRealm...");
        
        IniRealm iniRealm = new IniRealm("classpath:shiro.ini");

        log.info("Leaving shiroIniRealm...");
        return iniRealm;
    }

    @Bean
    public Realm shiroJwtRealm(@Autowired JdbcTemplate jdbcTemplate) {
        log.info("Entering shiroJwtRealm...");
        
        JwtRealm jwtRealm = new JwtRealm(jdbcTemplate);
        
        log.info("Leaving shiroJwtRealm...");
        return jwtRealm;
    }
    
}
