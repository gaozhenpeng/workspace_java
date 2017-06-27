package com.at.spark;

import org.apache.log4j.Logger;

public class Log4jMain {
    private final static Logger logger = Logger.getLogger(Log4jMain.class);
    
    public static void main(String[] args){
        logger.trace("trace");
        logger.debug("debug");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
        logger.fatal("fatal");
    }
}
