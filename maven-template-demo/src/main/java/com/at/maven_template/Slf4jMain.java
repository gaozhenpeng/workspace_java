package com.at.maven_template;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Slf4jMain {
	public static void main(String[] args) {
		log.trace("trace");
		log.debug("debug");
		log.info("info");
		log.warn("warn");
		log.error("error");
//		log.fatal("fatal"); // log4j-specific
	}
}
