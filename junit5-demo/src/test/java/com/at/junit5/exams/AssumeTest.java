package com.at.junit5.exams;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * Assumptions should have been replaced by 
 *       @EnabledIfEnvironmentVariable
 *     , @EnabledOnJre
 *     , @EnabledOnOs
 *     , @EnabledIf
 *     , @DisabledIfSystemProperty
 */
@Slf4j
public class AssumeTest {
    
    @BeforeAll
    static void b4Test() {
        System.setProperty("ENV", "DEV");
    }
    @Test
    void testOnDev() {
        // assume some condition  
        assumeTrue("DEV".equalsIgnoreCase(System.getProperty("ENV")), () -> "only for 'DEV' environment.");
        // true and continue
        log.info("testOnDev after assumeTrue");
    }

    @Test
    void testOnProd() {
        // assume some condition  
        // false and return
        assumeTrue("PROD".equalsIgnoreCase(System.getProperty("ENV")), () -> "only for 'PROD' environment.");
        
        log.info("testOnProd after assumeTrue");
    }
}
