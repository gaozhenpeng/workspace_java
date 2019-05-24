package com.at.springboot.cl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling // @Scheduled
public class SchedulerConfig {

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

}
