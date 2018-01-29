package com.at.maven_assembly;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Log4j2Main {
	public static void main(String[] args) {
		log.trace("trace");
		log.debug("debug");
		log.info("info");
		log.warn("warn");
		log.error("error");
//		log.fatal("fatal"); // log4j-specific
	}
}
