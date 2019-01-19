package com.at.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;

@Configuration
public class MariaDB4JConfiguration {
    @Bean
    public MariaDB4jSpringService mariaDB4j() {
        return new MariaDB4jSpringService();
    }
}
