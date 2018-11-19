package com.at.log4j2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4j2NativeMain {
    private final static Logger logger = LogManager.getLogger(Log4j2Main.class); // log4j2

    public static void main(String[] args) {
        logger.trace("trace");
        logger.debug("debug");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
        logger.fatal("fatal"); // log4j-specific
    }
}
