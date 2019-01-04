package com.at.spring_boot.shiro.config;

import javax.sql.DataSource;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.beans.factory.annotation.Autowired;
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
     * @return
     */
    @Bean
    public Realm shiroRealm(@Autowired DataSource ds) {
        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(ds);
        jdbcRealm.setAuthenticationQuery("SELECT password FROM sec_user WHERE user_name = ?");
        jdbcRealm.setUserRolesQuery("select sr.role_name from sec_role sr,sec_user_role sur,sec_user su where 1 = 1 and su.user_name = ? and sur.role_id = sr.role_id and su.user_id = sur.user_id");
        jdbcRealm.setPermissionsLookupEnabled(true);
        jdbcRealm.setPermissionsQuery("select sp.permission_name from sec_permission sp,sec_role_permission srp,sec_role sr where 1 = 1 and sr.role_name = ? and srp.role_id = sr.role_id and sp.permission_id = srp.role_id");
        return jdbcRealm;
    }
}
