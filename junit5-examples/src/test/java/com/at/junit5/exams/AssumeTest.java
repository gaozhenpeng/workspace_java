package com.at.junit5.exams;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AssumeTest {
    @Test
    void testOnDev() {
        System.setProperty("ENV", "DEV");
        // assume some condition  
        assumeTrue("DEV".equalsIgnoreCase(System.getProperty("ENV")), AssumeTest::message);
        // true and continue
        log.info("testOnDev after assumeTrue");
    }

    @Test
    void testOnProd() {
        System.setProperty("ENV", "PROD");
        // assume some condition  
        // false and return
        assumeTrue("DEV".equalsIgnoreCase(System.getProperty("ENV")), AssumeTest::message);
        
        log.info("testOnProd after assumeTrue");
    }

    private static String message() {
        return "TEST Execution Failed :: ";
    }
}
