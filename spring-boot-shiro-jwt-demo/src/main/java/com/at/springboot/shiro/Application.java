package com.at.springboot.shiro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication(scanBasePackages="com.at.springboot")
public class Application {

    public static void main(String[] args) {
        log.info("spring application main");
        SpringApplication.run(Application.class, args);
    }
}
