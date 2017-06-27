package com.at.jpos.helper;

import org.jpos.util.LogEvent;
import org.jpos.util.LogListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A sufficient LogbackListener.(The log() method.)
 *
 */
public class LogbackListener implements LogListener {
	private static final Logger logger = LoggerFactory.getLogger(LogbackListener.class);

	@Override
	public synchronized LogEvent log(LogEvent ev) {
		logger.info(ev.toString());
		return ev;
	}
}
