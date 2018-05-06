package com.at.spring.spring_quartz;

import org.quartz.SchedulerException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
// search current package if no basePackages are defined
@ComponentScan(basePackages = "com.at.spring.spring_quartz")
public class SpringQuartzMain {

    public static void main(String[] args) throws SchedulerException, InterruptedException {
        log.debug("Enterring Main.");

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                SpringQuartzMain.class);

        Thread.sleep(60000);

        context.close();

        log.debug("Exiting Main.");
    }

}
