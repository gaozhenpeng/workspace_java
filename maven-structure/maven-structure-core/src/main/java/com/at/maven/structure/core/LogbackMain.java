package com.at.maven.structure.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogbackMain {
	private final static Logger logger = LoggerFactory.getLogger(LogbackMain.class);

	public static void main(String[] args) throws InterruptedException {
		logger.trace("trace message");
		logger.debug("debug message");
		logger.info("info message");
		logger.warn("warn message");
		logger.error("error message");
		// logger.fatal("fatal message"); // there's no .fatal() method.
	}
}