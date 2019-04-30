package com.at.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;

@Configuration
public class MariaDB4JConfiguration {
    @Bean
    public MariaDB4jSpringService mariaDB4j() {
        MariaDB4jSpringService mariaDB4jSpringService = new MariaDB4jSpringService();
        mariaDB4jSpringService
            .getConfiguration()
                .addArg("--character-set-server=utf8mb4")
                .addArg("--collation-server=utf8mb4_unicode_520_cs")
                ;
        
        
        return mariaDB4jSpringService;
    }
}
