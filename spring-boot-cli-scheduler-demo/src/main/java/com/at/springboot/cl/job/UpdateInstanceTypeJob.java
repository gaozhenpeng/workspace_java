package com.at.springboot.cl.job;

import java.security.SecureRandom;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UpdateInstanceTypeJob {
    private static final SecureRandom secureRandom = new SecureRandom();

    private Long var = -1l;

    //                 sec min hou day mon wee
    @Scheduled(cron = "*/5   *   *   *   *   *")
    public void doUpdate() throws InterruptedException{
        log.info("start doUpdate.");
        Date start = new Date();

        long tid = Thread.currentThread().getId();

        log.debug("I'm a job. TID:" + tid);


        long sleepMS = secureRandom.nextInt(5000);
        var = sleepMS;
        log.debug("I'm going to sleep " + sleepMS + "(ms)");

        Thread.currentThread().sleep(sleepMS);

        log.debug("I'm leaving. TID:" + tid);

        Date end = new Date();
        Long interval = end.getTime() - start.getTime();
        end = null;
        start = null;
        log.info("end doUpdate." + interval);
    }

    //                 sec min hou day mon wee
    @Scheduled(cron = "${com.at.cli.job.crontab}")
    public void doUpdate_parameterized() throws InterruptedException{
        log.info("start doUpdate_parameterized.");
        log.info("end doUpdate_parameterized.");
    }


    public Long getVar(){
        return var;
    }
}
