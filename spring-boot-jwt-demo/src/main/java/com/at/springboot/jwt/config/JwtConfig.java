package com.at.springboot.jwt.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.at.springboot.jwt.filter.JwtFilter;

@Configuration
public class JwtConfig {
    @Bean
    public FilterRegistrationBean<JwtFilter> loggingFilter(){
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
             
        registrationBean.setFilter(new JwtFilter());
//        registrationBean.addUrlPatterns(
//                "/jwt/dologout" 
//                ,"/jwt/userhome"
//                //, "/jwt/user*"  // invalid
//                //, "/jwt/*" // valid, but match all methods in /jwt/
//                //, "/*"  // default url pattern
//                );
        
        return registrationBean;
    }
}
