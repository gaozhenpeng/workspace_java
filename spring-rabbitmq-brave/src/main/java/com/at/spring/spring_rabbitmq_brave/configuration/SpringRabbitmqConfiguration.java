package com.at.spring.spring_rabbitmq_brave.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
// search current package if no basePackages are defined
@ComponentScan(basePackages = "com.at.spring.spring_rabbitmq_brave")
public class SpringRabbitmqConfiguration {

}
