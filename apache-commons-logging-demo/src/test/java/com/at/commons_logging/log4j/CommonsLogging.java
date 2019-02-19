package com.at.commons_logging.log4j;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class CommonsLogging {
	private final Log log = LogFactory.getLog(CommonsLogging.class);

	@Test
	public void testLogging() throws InterruptedException {
		log.info("message1");
	}
}
