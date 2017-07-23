package com.at.zipkin.brave.noreport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zipkin.Span;
import zipkin.reporter.Reporter;

public class NoopReporter implements Reporter<zipkin.Span> {
	private static final Logger logger = LoggerFactory.getLogger(NoopReporter.class);

	@Override
	public void report(Span span) {
		if(logger.isDebugEnabled()){
			logger.debug("span: '" + span + "'");
		}
	}

}
