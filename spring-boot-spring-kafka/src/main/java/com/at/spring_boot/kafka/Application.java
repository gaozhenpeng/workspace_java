package com.at.spring_boot.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.at.spring_boot.kafka.config.KafkaCommProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication(scanBasePackages="com.at.spring_boot")
@EnableConfigurationProperties(KafkaCommProperties.class)
public class Application {

    public static void main(String[] args) {
        log.info("spring application main");
        SpringApplication.run(Application.class, args);
    }
}
