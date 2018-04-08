package com.at.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@ComponentScan("com.at.spring")
public class Application {

    public static void main(String[] args) {
        log.info("consumer main");
        SpringApplication.run(Application.class, args);
    }
}
