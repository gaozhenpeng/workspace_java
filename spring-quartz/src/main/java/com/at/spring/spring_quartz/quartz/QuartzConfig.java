package com.at.spring.spring_quartz.quartz;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import com.at.spring.spring_quartz.quartz.job.SpringHelloworldJob;

@Configuration
@ComponentScan(basePackages = "com.lokesh.tracker.integrations.jobs")
public class QuartzConfig {

	@Bean
	public JobDetailFactoryBean springHelloworld() {
		JobDetailFactoryBean bean = new JobDetailFactoryBean();
		bean.setJobClass(SpringHelloworldJob.class);
		bean.setName("springhellworldjob");
		bean.setGroup("springhellworldjob_group");
		return bean;
	}

	@Bean
	public CronTriggerFactoryBean dailySummarytrigger() {
		CronTriggerFactoryBean bean = new CronTriggerFactoryBean();
		bean.setJobDetail(springHelloworld().getObject());
		bean.setStartDelay(5000);
		bean.setBeanName("springhellworldjob_trigger");
		bean.setGroup("springhellworldjob_group");
		//                      sec min hou day mon wee year
		bean.setCronExpression("0   0   13  *   *   ?   *");
		return bean;
	}

	@Bean
	public SimpleTriggerFactoryBean simpleTrigger() {
		SimpleTriggerFactoryBean bean = new SimpleTriggerFactoryBean();
		bean.setJobDetail(springHelloworld().getObject());
		bean.setBeanName("springhellworldjob_trigger");
		bean.setGroup("springhellworldjob_group");
		bean.setRepeatInterval(2000); // repeat interval, in miliseconds
		bean.setRepeatCount(2);
		return bean;
	}

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
		scheduler.setTriggers(simpleTrigger().getObject());
		return scheduler;
	}
}
