package com.at.springboot.quartz.job;

import org.quartz.CronScheduleBuilder;
import org.quartz.DateBuilder;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
// Note that
//     It would disable concurrent execution ONLY in ONE scheduler.
//     If multiple schedulers have been defined, the same job would 
//     also be executed concurrently.
//     This is implemented by using lock table 'qrtz_locks' in database
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class SpringHelloworldJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        final JobDetail jobDetail = jobExecutionContext.getJobDetail();
        final Trigger trigger = jobExecutionContext.getTrigger();
        log.info("Helloworld!, jobDetail: '{}', trigger: '{}'", jobDetail, trigger);
    }

    
    @Bean
    public JobDetail springHelloworldJobDetail() {
        JobDetail jobDetail = 
                JobBuilder.newJob(this.getClass())
                    .storeDurably(true)
                    .requestRecovery(true)
                    .withIdentity("springhellworldjob", "springhellworldjob_group")
                    .build()
                    ;
        return jobDetail;
    }

    @Bean
    public Trigger dailySummarytrigger() throws SchedulerException {
        Trigger trigger = 
                TriggerBuilder.newTrigger()
                    .withIdentity("springhellworldjob_crontrigger", "springhellworldjob_group")
                    .forJob(springHelloworldJobDetail())
                    .startAt(DateBuilder.futureDate(5, DateBuilder.IntervalUnit.SECOND))
                    .withSchedule(
                            //                                sec min hou day mon wee year
                            CronScheduleBuilder.cronSchedule("0/3   *   *  *   *   ?   *"))
                    .build()
                    ;
        return trigger;
    }

    @Bean
    public Trigger simpleTrigger() throws SchedulerException {
        Trigger trigger = 
                TriggerBuilder.newTrigger()
                    .withIdentity("springhellworldjob_simpletrigger", "springhellworldjob_group")
                    .forJob(springHelloworldJobDetail())
                    .withSchedule(
                            SimpleScheduleBuilder.repeatSecondlyForTotalCount(20, 2)) // total count
                    .build()
                    ;
        return trigger;
    }

}
