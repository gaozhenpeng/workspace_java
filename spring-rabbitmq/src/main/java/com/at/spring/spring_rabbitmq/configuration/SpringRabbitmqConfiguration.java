package com.at.spring.spring_rabbitmq.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
// search current package if no basePackages are defined
@ComponentScan(basePackages = "com.at.spring.spring_rabbitmq")
public class SpringRabbitmqConfiguration {

}
