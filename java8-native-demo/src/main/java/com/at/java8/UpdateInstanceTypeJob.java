package com.at.java8;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateInstanceTypeJob implements Callable<Long> {
    private static final Logger logger = LoggerFactory.getLogger(UpdateInstanceTypeJob.class);
    private SecureRandom secureRandom = new SecureRandom();

    @Override
    public Long call() throws Exception {
        Date start = new Date();

        long tid = Thread.currentThread().getId();

        logger.debug("I'm a job. TID:" + tid);


        long sleepMS = secureRandom.nextInt(5000);
        logger.debug("I'm going to sleep " + sleepMS + "(ms)");

        Thread.currentThread().sleep(sleepMS);

        logger.debug("I'm leaving. TID:" + tid);

        Date end = new Date();
        Long interval = end.getTime() - start.getTime();
        end = null;
        start = null;
        return interval;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException{
        String runTimesStr = args.length > 0 ? args[0] : "1";
        int runs = Integer.parseInt(runTimesStr, 10);

        int threadsNum = Runtime.getRuntime().availableProcessors();

        Thread.currentThread().setPriority(Thread.MAX_PRIORITY); // setting the thread priority, it will be inherited within Executors.privilegedThreadFactory().
        final ExecutorService execService = Executors.newFixedThreadPool(threadsNum,Executors.privilegedThreadFactory());

        final List<Future<Long>> futures = new ArrayList<Future<Long>>();

        long total_runtime = 0;

        try{
            logger.debug("before adding futures");
            for(int r = 0; r < runs; r++){
                futures.add(execService.submit(new UpdateInstanceTypeJob())); // add 'Callable' jobs
            }

            logger.debug("before getting results");
            for(int r = 0; r < runs; r++){
                total_runtime += futures.get(r).get(24, TimeUnit.HOURS); // the waiting time should be longer than the job itself. the control flow will stop here.
            }
            logger.debug("after getting results");
        }finally{
            execService.shutdown(); // 必须调用了 shutdown 方法才能在 awaitTermination 中当线程结束时返回
            execService.awaitTermination(3, TimeUnit.DAYS); // 等待在调用 shutdown之前加入的线程结束或是指定的时间到达
            execService.shutdownNow(); // 强制结束
        }

        logger.info("total_runtime: " + total_runtime);

    }
}
