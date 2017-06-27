package com.at.spring.mybatis.annotation.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(value = "classpath:/spring-mybatis.properties", ignoreResourceNotFound = false)
public class SpringMybatisProperties implements EnvironmentAware {
    private static final Logger logger = LoggerFactory.getLogger(SpringMybatisProperties.class);
    
    @Bean(name="DATABASE_DRIVERCLASS")
    public String DATABASE_DRIVERCLASS() {
        return readString("spring-mybatis.database.driverclass");
    }
    @Bean(name="DATABASE_URL")
    public String DATABASE_URL() {
        return readString("spring-mybatis.database.url");
    }
    @Bean(name="DATABASE_USERNAME")
    public String DATABASE_USERNAME() {
        return readString("spring-mybatis.database.username");
    }
    @Bean(name="DATABASE_PASSWORD")
    public String DATABASE_PASSWORD() {
        return readString("spring-mybatis.database.password");
    }
    
    private Environment env = null;
    
    @Override
    public void setEnvironment(Environment env) {
        logger.debug("Setting Environment.");
        this.env = env;
    }

    private String readString(String propName) {
        logger.debug("Reading '"+propName+"'.");
        return readString(propName, "");
    }
    private String readString(String propName, String defaultValue) {
        logger.debug("Reading '"+propName+"', default value: '"+defaultValue+"'.");
        return env.getProperty(propName, defaultValue).trim();
    }
    

}