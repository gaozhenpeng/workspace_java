package com.at.spring.spring_quartz.quartz.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.quartz.Trigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

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

    
    
    
    
    @Bean(name="springhellworldjob")
    public JobDetailFactoryBean springHelloworld() {
        JobDetailFactoryBean bean = new JobDetailFactoryBean();
        bean.setJobClass(SpringHelloworldJob.class);
        bean.setDurability(true);
        bean.setRequestsRecovery(true);
        bean.setName("springhellworldjob");
        bean.setGroup("springhellworldjob_group");
        return bean;
    }

    @Bean(name="springhellworldjob_crontrigger")
    public CronTriggerFactoryBean dailySummarytrigger() {
        CronTriggerFactoryBean bean = new CronTriggerFactoryBean();
        bean.setJobDetail(springHelloworld().getObject());
        bean.setStartDelay(5000);
        bean.setBeanName("springhellworldjob_crontrigger");
        bean.setGroup("springhellworldjob_group");
        //                      sec min hou day mon wee year
        bean.setCronExpression("0/3   *   *  *   *   ?   *");
        return bean;
    }

    @Bean(name="springhellworldjob_simpletrigger")
    public SimpleTriggerFactoryBean simpleTrigger() {
        SimpleTriggerFactoryBean bean = new SimpleTriggerFactoryBean();
        bean.setJobDetail(springHelloworld().getObject());
        bean.setBeanName("springhellworldjob_simpletrigger");
        bean.setGroup("springhellworldjob_group");
        bean.setRepeatInterval(2000); // repeat interval, in miliseconds
        bean.setRepeatCount(19); // total execution = repeat count + 1
        return bean;
    }

}
