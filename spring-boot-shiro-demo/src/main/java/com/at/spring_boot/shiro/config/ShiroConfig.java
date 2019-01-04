package com.at.spring_boot.shiro.config;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfig {
    @Value("#{'${server.servlet.context-path}'.trim()}")
    private String contextPath;
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        
//        // logged in users with the 'admin' role
//        chainDefinition.addPathDefinition("/admin/**", "authc, roles[admin]");
//        
//        // logged in users with the 'document:read' permission
//        chainDefinition.addPathDefinition("/docs/**", "authc, perms[document:read]");
//        
//        // all other paths require a logged in user
//        chainDefinition.addPathDefinition("/**", "authc");
        
        chainDefinition.addPathDefinition(contextPath + "/shiro/login", "anon");
        chainDefinition.addPathDefinition(contextPath + "/shiro/dologin", "anon");

        chainDefinition.addPathDefinition(contextPath + "/shiro/admin/home", "authc");
        
        // ',' in perms[] and roles[] means logical 'AND'
        chainDefinition.addPathDefinition(contextPath + "/shiro/admin/withperm", "authc,perms[user:create]");
        chainDefinition.addPathDefinition(contextPath + "/shiro/admin/withrole", "authc,roles[user]");
        
        // everything else in /api requires authentication:
        chainDefinition.addPathDefinition(contextPath + "/api/**", "authc");
        
        // everything NOT in /api can be accessed anonymously:
        chainDefinition.addPathDefinition("/**", "anon");

        return chainDefinition;
    }
    
    @Bean
    public Realm shiroRealm() {
        return new IniRealm("classpath:shiro.ini");
    }
}
