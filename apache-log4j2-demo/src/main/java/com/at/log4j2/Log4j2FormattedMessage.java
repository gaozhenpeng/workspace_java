package com.at.log4j2;

import org.apache.logging.log4j.message.FormattedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log4j2FormattedMessage {
    private final static Logger logger = LoggerFactory.getLogger(Log4j2FormattedMessage.class); // slf4j
    
    
    public static void main(String[] args) {
        Integer no = 10;
        String world = "world!";
        String msg = new FormattedMessage("hello {},  {}", world, no).getFormattedMessage();
        logger.info(msg);
    }
}
