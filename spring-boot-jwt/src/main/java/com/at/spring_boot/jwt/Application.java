package com.at.spring_boot.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication(scanBasePackages="com.at.spring_boot")
public class Application {

    public static void main(String[] args) {
        log.info("spring application main");
        SpringApplication.run(Application.class, args);
    }
}
