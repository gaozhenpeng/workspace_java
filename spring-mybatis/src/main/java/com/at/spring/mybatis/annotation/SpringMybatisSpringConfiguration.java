package com.at.spring.mybatis.annotation;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
// search current package if no basePackages are defined
@ComponentScan(basePackages = "com.at.spring.mybatis.annotation")
public class SpringMybatisSpringConfiguration {

}
