package com.at.spring_boot.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        log.info("first main2");
        SpringApplication.run(Application.class, args);
    }
}
