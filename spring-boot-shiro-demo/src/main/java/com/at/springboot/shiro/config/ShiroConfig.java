package com.at.springboot.shiro.config;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;

@Configuration
public class ShiroConfig {
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
        
        chainDefinition.addPathDefinition("/shiro/login", "anon");
        chainDefinition.addPathDefinition("/shiro/dologin", "anon");

        chainDefinition.addPathDefinition("/shiro/admin/home", "authc");
        
        // ',' in perms[] and roles[] means logical 'AND'
        chainDefinition.addPathDefinition("/shiro/admin/withperm", "authc,perms[user:create]");
        chainDefinition.addPathDefinition("/shiro/admin/withrole", "authc,roles[user]");
        
        // everything else can be accessed anonymously:
        //   you can still combine annotations with this config as default
        //     anon : @RequiresGuest
        //     authc : @RequiresAuthentication
        //     perms[resource1:op1,resource1:op2,resource2:op1] : @RequiresPermissions(value = {"resource1:op1", "resource1:op2", "resource2:op1"})
        //     roles[role1,role2] : @RequiresRoles(value = {"role1", "role2"})
        //   annotation examples: {@link com.at.springboot.shiro.controller.ShiroController#admin(String)}
        chainDefinition.addPathDefinition("/**", "anon");

        return chainDefinition;
    }
    
    @Bean
    public Realm shiroRealm() {
        return new IniRealm("classpath:shiro.ini");
    }
    

    /** shiro tags for thymeleaf */
    @Bean
    public ShiroDialect shiroDialect(){
        return new ShiroDialect();
    }
}
