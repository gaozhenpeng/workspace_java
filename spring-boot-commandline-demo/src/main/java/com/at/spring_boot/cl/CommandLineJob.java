package com.at.spring_boot.cl;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(100)
public class CommandLineJob implements CommandLineRunner{

    @Override
    public void run(String... args) throws Exception {
        log.info("Entering CommandLineJob run, args: '{}'", Arrays.toString(args));
        

        log.info("I'm doing a hard work!");
        
        
        
        log.info("Leaving CommandLineJob run.");
    }

}
