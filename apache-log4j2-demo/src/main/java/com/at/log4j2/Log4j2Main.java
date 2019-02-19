package com.at.log4j2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log4j2Main {
//    private final static Logger logger = LogManager.getLogger(Log4j2Main.class); // log4j2
    private final static Logger logger = LoggerFactory.getLogger(Log4j2Main.class); // slf4j

    public static void main(String[] args) {
        logger.trace("trace");
        logger.debug("debug");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
//        logger.fatal("fatal"); // log4j-specific
    }
}
