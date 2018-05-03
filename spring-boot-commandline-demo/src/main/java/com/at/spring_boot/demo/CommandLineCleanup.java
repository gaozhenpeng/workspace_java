package com.at.spring_boot.demo;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(999)
public class CommandLineCleanup implements CommandLineRunner{

    @Override
    public void run(String... args) throws Exception {
        log.info("Entering CommandLineCleanup run, args: '{}'", Arrays.toString(args));
        
        
        log.info("I'm running at the end of processing, ensured by '@Order(999)' ");
        
        
        
        log.info("Leaving CommandLineCleanup run.");
    }

}
