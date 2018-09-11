package com.at.spring_boot.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class TaskExecutorConfig {

    /**
     * <p>Best practice: <strong>always set queueCapacity to 0</strong></p>
     * 
     * <p>Thread strategy:</p>
     * <ol>
     *  <li>create new thread if <strong>threads</strong> &lt; <strong>corePoolSize</strong></li>
     *  <li>put new task to queue if <strong>threads</strong> &gt; <strong>corePoolSize</strong> and <strong>threads</strong> &lt; <strong>queueCapacity</strong></li>
     *  <li>create new thread if <strong>threads</strong> &gt; <strong>queueCapacity</strong> and <strong>threads</strong> &lt; <strong>maxPoolSize</strong></li>
     * </ol>
     */
    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // min
        executor.setCorePoolSize(10);
        // max
        executor.setMaxPoolSize(100);
        // queue
        executor.setQueueCapacity(0);
        // keepalive
        executor.setKeepAliveSeconds(180);
        // Policy when threads grows over maxPoolSize:
        //   CallerRunsPolicy: run in caller's thread
        //   DiscardOldestPolicy: discard the oldest task
        //   AbortPolicy: abort and throw RejectedExecutionException
        //   DiscardPolicy: discard quietly
        executor.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
