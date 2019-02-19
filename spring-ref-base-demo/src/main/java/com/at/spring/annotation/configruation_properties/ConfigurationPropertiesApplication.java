package com.at.spring.annotation.configruation_properties;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * The spring-boot package is required for the @EnableConfigurationProperties annotation
 * <dependency>
 *  <groupId>org.springframework.boot</groupId>
 *  <artifactId>spring-boot</artifactId>
 *  <!-- @EnableConfigurationProperties -->
 *  <version>1.2.6.RELEASE</version>
 * </dependency>
 */
@Configuration
//@EnableConfigurationProperties({OpenStackProperties.class})
@ComponentScan(basePackages="com.at.spring.annotation.configuration_properties")
public class ConfigurationPropertiesApplication {
	private static final Logger logger = LoggerFactory.getLogger(ConfigurationPropertiesApplication.class);
	
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ConfigurationPropertiesApplication.class);
        OpenstackProperties openStackProperties = context.getBean(OpenstackProperties.class);
        logger.info(openStackProperties.getCompute().getService().getTargeturl());
    }
}