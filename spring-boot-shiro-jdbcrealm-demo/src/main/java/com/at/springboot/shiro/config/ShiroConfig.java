package com.at.springboot.shiro.config;

import javax.sql.DataSource;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.beans.factory.annotation.Autowired;
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
              `permission_name` varchar(64) COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
              `created_datetime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
              `updated_datetime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
              PRIMARY KEY (`permission_id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            
            insert into `sec_permission` (permission_id, permission_name) values(1, '*'), (2, 'user:*'), (3, 'page:view'), (4, 'doc:view')
            ;
            
            CREATE TABLE `sec_role` (
              `role_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
              `role_name` varchar(64) COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
              `created_datetime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
              `updated_datetime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
              PRIMARY KEY (`role_id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            
            insert into `sec_role` (role_id, role_name) values(1, 'superadmin'), (2, 'admin'), (3, 'user')
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
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            
            insert into `sec_role_permission` (role_permission_id, role_id, permission_id) values(1, 1, 1), (2, 2, 2), (3, 3, 3), (4, 3, 4)
            ;
            
            
            CREATE TABLE `sec_user` (
              `user_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
              `user_name` varchar(64) COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
              `password` varchar(128) COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
              `created_datetime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
              `updated_datetime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
              PRIMARY KEY (`user_id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            
            insert into `sec_user` (user_id, user_name, password) values(1, 'rt', 'rtpw'), (2, 'adm', 'admpw'), (3, 'at', 'atpw')
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
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            
            insert into `sec_user_role` (user_role_id, user_id, role_id) values (1, 1, 1), (2, 1, 2), (3, 1, 3), (4, 2, 2), (5,3,3)
            ;
       </code></pre>
     *
     * @return
     */
    @Bean
    public Realm shiroRealm(@Autowired DataSource ds) {
        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(ds);
        jdbcRealm.setAuthenticationQuery("SELECT password FROM sec_user WHERE user_name = ?");
        jdbcRealm.setUserRolesQuery("select sr.role_name from sec_role sr,sec_user_role sur,sec_user su where 1 = 1 and su.user_name = ? and sur.role_id = sr.role_id and su.user_id = sur.user_id");
        jdbcRealm.setPermissionsLookupEnabled(true);
        jdbcRealm.setPermissionsQuery("select sp.permission_name from sec_permission sp,sec_role_permission srp,sec_role sr where 1 = 1 and sr.role_name = ? and srp.role_id = sr.role_id and sp.permission_id = srp.permission_id");
        return jdbcRealm;
    }
    

    /** shiro tags for thymeleaf */
    @Bean
    public ShiroDialect shiroDialect(){
        return new ShiroDialect();
    }
}
