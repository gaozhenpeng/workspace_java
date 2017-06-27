package com.at.spring.spring_quartz;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
// search current package if no basePackages are defined
@ComponentScan(basePackages = "com.at.spring.spring_quartz")
public class SpringQuartzMain {
    private static final Logger logger = LoggerFactory.getLogger(SpringQuartzMain.class);

    @SuppressWarnings("static-access")
    public static void main(String[] args) throws SchedulerException, InterruptedException {
        logger.debug("Enterring Main.");

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                SpringQuartzMain.class);

        Thread.currentThread().sleep(11000);

        context.close();

        logger.debug("Exiting Main.");
    }

}
