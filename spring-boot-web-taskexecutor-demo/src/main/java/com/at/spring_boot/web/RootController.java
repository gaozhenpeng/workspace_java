package com.at.spring_boot.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class RootController {
    @Autowired
    private AsyncTaskExecutor asyncTaskExecutor;
    private Random random = new Random();

    @RequestMapping("/")
    public String home() throws InterruptedException, ExecutionException, TimeoutException {
        log.info("hello home.");
        List<Future<Integer>> results = new ArrayList<>();
        for(int i = 0 ; i < 20 ; i++) {
            results.add(
                asyncTaskExecutor.submit(
                        new Callable<Integer>() {
                            @Override
                            public Integer call() throws Exception {
                                Thread.sleep(1000);
                                return random.nextInt(1000);
                            }
                        })
                );
        }
        for(Future<Integer> f : results) {
            f.get(10, TimeUnit.SECONDS);
        }
        return "hello world!";
    }
}
