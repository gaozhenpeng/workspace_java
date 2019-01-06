package com.at.spring_boot.jwt.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.at.spring_boot.jwt.filter.JwtFilter;

@Configuration
public class JwtConfig {
    @Bean
    public FilterRegistrationBean<JwtFilter> loggingFilter(){
        FilterRegistrationBean<JwtFilter> registrationBean 
          = new FilterRegistrationBean<>();
             
        registrationBean.setFilter(new JwtFilter());
        registrationBean.addUrlPatterns("/jwt/dologout", "/jwt/user*");
        
        return registrationBean;
    }
}
