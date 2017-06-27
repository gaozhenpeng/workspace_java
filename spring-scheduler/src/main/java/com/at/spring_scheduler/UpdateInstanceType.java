package com.at.spring_scheduler;

import java.security.SecureRandom;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UpdateInstanceType {
    private static final Logger logger = LoggerFactory.getLogger(UpdateInstanceType.class);
    private static final SecureRandom secureRandom = new SecureRandom();

    private Long var = -1l;

    //                 sec min hou day mon wee
    @Scheduled(cron = "*/5   *   *   *   *   *")
    public void doUpdate() throws InterruptedException{
        logger.info("start doUpdate.");
        Date start = new Date();

        long tid = Thread.currentThread().getId();

        logger.debug("I'm a job. TID:" + tid);


        long sleepMS = secureRandom.nextInt(5000);
        var = sleepMS;
        logger.debug("I'm going to sleep " + sleepMS + "(ms)");

        Thread.currentThread().sleep(sleepMS);

        logger.debug("I'm leaving. TID:" + tid);

        Date end = new Date();
        Long interval = end.getTime() - start.getTime();
        end = null;
        start = null;
        logger.info("end doUpdate." + interval);
    }

    //                 sec min hou day mon wee
    @Scheduled(cron = "${spring.configuration.scheduled.crontab}")
    public void doUpdate_parameterized() throws InterruptedException{
        logger.info("start doUpdate_parameterized.");
        logger.info("end doUpdate_parameterized.");
    }


    public Long getVar(){
        return var;
    }
}