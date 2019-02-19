package com.at.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.at.quartz.job.HelloworldJob;

public class QuartzHelloworldMain {
	public static void main(String[] args) throws SchedulerException, InterruptedException{
        JobDetail jobDetail= JobBuilder.newJob(HelloworldJob.class)
                .withIdentity("hellworldjob","hellworldjob_group")
                .build();

        Trigger simpleTrigger= TriggerBuilder
                .newTrigger()
                .withIdentity("hellworldjob_trigger","hellworldjob_group")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(2)  //时间间隔
                        .withRepeatCount(2)        //重复次数(共执行 repeatcount + 1)
                        )
                .build();
        
        JobDetail jobDetailCron= JobBuilder.newJob(HelloworldJob.class)
                .withIdentity("hellworldjobcron","hellworldjob_group")
                .build();

        Trigger cronTrigger= TriggerBuilder
                .newTrigger()
                .withIdentity("hellworldjob_crontrigger","hellworldjob_group")
                .startNow()
                .withSchedule(
                    //                                sec min hou day mon wee year
//                    CronScheduleBuilder.cronSchedule("0   0   13  *   *   ?   *")
                    CronScheduleBuilder.cronSchedule("*/2   *   *  *   *   ?   *")
                )
                .build();
        
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        sched.scheduleJob(jobDetail,simpleTrigger);
        sched.scheduleJob(jobDetailCron,cronTrigger);

        sched.start();
        Thread.currentThread().sleep(11000);
        boolean isWaitJobToComplete = true;
        sched.shutdown(isWaitJobToComplete);
	}
	
}
