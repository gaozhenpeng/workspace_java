package com.at.springboot.cl;

import java.util.Arrays;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(100)
public class ApplicationJob implements ApplicationRunner{
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        log.info("Entering ApplicationJob run, applicationArguments: '{}'", applicationArguments);
        

        
        log.info("applicationArguments.getSourceArgs(): '{}'", Arrays.toString(applicationArguments.getSourceArgs()));
        log.info("applicationArguments.getOptionNames(): '{}'", applicationArguments.getOptionNames());
        log.info("applicationArguments.getOptionValues(\"optname\"): '{}'", applicationArguments.getOptionValues("optname"));
        log.info("applicationArguments.getNonOptionArgs(): '{}'", applicationArguments.getNonOptionArgs());
        

        
        
        log.info("Leaving ApplicationJob run.");
    }

}
