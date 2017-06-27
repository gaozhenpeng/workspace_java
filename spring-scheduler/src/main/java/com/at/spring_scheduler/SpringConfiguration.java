package com.at.spring_scheduler;

import java.security.SecureRandom;
import java.util.concurrent.Executor; // don't remove
import java.util.concurrent.Executors; // don't remove

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled; // don't remove
import org.springframework.scheduling.annotation.SchedulingConfigurer; // don't remove
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar; // don't remove

@Configuration
// search current package if no basePackages are defined
@ComponentScan(basePackages = "com.at.spring_scheduler")
@PropertySource(value = "classpath:/spring_configuration.properties")
@EnableScheduling // @Scheduled
public class SpringConfiguration /* implements SchedulingConfigurer */{
    private static final Logger logger = LoggerFactory.getLogger(SpringConfiguration.class);
    private static final SecureRandom secureRandom = new SecureRandom();

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
        return new PropertySourcesPlaceholderConfigurer();
    }


//  /**
//   * implements SchedulingConfigurer
//   */
//  @Override
//  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
//      taskRegistrar.setScheduler(taskExecutor());
//  }
//
//  @Bean(destroyMethod = "shutdown")
//  public Executor taskExecutor() {
//      return Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
//  }


    /**
     * used by @Schduled as a default TaskScheduler.
     * @return
     */
    @Bean
    public TaskScheduler getTaskScheduler(){
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(Runtime.getRuntime().availableProcessors()); // pool size
        return scheduler;
    }

    public static void main(String[] args) throws InterruptedException {
        AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        UpdateInstanceType upInsTypBean = ctx.getBean(UpdateInstanceType.class);

        Long tid = Thread.currentThread().getId();

        logger.info("doing other work.....");

        long mainJobTimes = secureRandom.nextInt(10);
        for(int i = 0 ; i < mainJobTimes ; i++){
            long sleepMS = secureRandom.nextInt(5000);

            logger.debug("Doing a job for " + sleepMS + "(ms), "+i+" tid: " + tid);

            logger.info("upInsTypBean.getVar(): " + upInsTypBean.getVar());

            Thread.currentThread().sleep(sleepMS);
        }

        logger.info("main done.");
    }
}
