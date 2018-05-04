package com.at.spring_boot.cl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

/**
 * mvn clean package
 * java -jar target/spring-boot-commandline-demo-0.0.1-SNAPSHOT.jar com.at.spring_boot.cl.Application arg1 arg2 arg3 arg4 arg5 optname=myvalue optname=myvalue2 --optname=myvalue3 --optname=myvalue4
 */
@Slf4j
@SpringBootApplication(scanBasePackages="com.at.spring_boot")
public class Application {
    public static void main(String[] args) {
        log.info("spring application main");
        SpringApplication.run(Application.class, args);
    }
}
