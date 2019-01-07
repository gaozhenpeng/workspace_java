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
//        // the url patterns should be filtered by this bean
//        registrationBean.addUrlPatterns("/jwt/dologout","/jwt/userhome");
//        // NOTE: "/jwt/user*" is invalid
//        registrationBean.addUrlPatterns("/jwt/dologout", "/jwt/*");
//        // default url patterns, org.springframework.boot.web.servlet.AbstractFilterRegistrationBean.DEFAULT_URL_MAPPINGS
//        registrationBean.addUrlPatterns("/*");
        
        return registrationBean;
    }
}
