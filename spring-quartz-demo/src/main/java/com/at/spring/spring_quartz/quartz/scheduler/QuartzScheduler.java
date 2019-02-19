package com.at.spring.spring_quartz.quartz.scheduler;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzScheduler {
    private static final String QUARTZ_PROPERTIES_IN_CLASSPATH = "/quartz.properties";

//    @Autowired
//    @Qualifier("springhellworldjob_simpletrigger")
//    private SimpleTriggerFactoryBean springhellworldjob_simpletrigger;
//    @Autowired
//    @Qualifier("springhellworldjob_crontrigger")
//    private CronTriggerFactoryBean springhellworldjob_crontrigger;

    @Autowired
    private List<JobDetail> jobDetails;
    @Autowired
    private List<Trigger> triggers;
        
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setOverwriteExistingJobs(true);
        scheduler.setAutoStartup(true);
        //// Ref: http://www.quartz-scheduler.org/documentation/quartz-2.2.x/configuration/ConfigJobStoreCMT.html
        ////    You need to define org.quartz.jobStore.nonManagedTXDataSource
        ////    
        ////    JobStoreCMT requires a (second) datasource that contains connections that
        ////    will not be part of container-managed transactions. The value of this property
        ////    must be the name of one the DataSources defined in the configuration properties
        ////    file. This datasource must contain non-CMT connections, or in other words, 
        ////    connections for which it is legal for Quartz to directly call commit() and rollback() on.
//        scheduler.setDataSource(dataSource);
        scheduler.setQuartzProperties(buildQuartzProperties());
        
//        scheduler.setTriggers(
//                springhellworldjob_crontrigger.getObject()
//                , springhellworldjob_simpletrigger.getObject());

        scheduler.setJobDetails(jobDetails.toArray(new JobDetail[0]));
        scheduler.setTriggers(triggers.toArray(new Trigger[0]));
        return scheduler;
    }
    
    @Bean
    public Properties buildQuartzProperties() {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource(QUARTZ_PROPERTIES_IN_CLASSPATH));
        try {
            propertiesFactoryBean.afterPropertiesSet();
            return propertiesFactoryBean.getObject();
        } catch (IOException e) {
            throw new RuntimeException("Not able to build quartz properties", e);
        }
    }
}
