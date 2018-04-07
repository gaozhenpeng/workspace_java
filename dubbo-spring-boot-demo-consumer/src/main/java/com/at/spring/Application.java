package com.at.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        log.info("consumer main");
        SpringApplication.run(Application.class, args);
    }
}
