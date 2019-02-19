package com.at.spring.brave.reporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zipkin.reporter.AsyncReporter;
import zipkin.reporter.Reporter;
import zipkin.reporter.okhttp3.OkHttpSender;

public class ReporterManager {
	private static final Logger logger = LoggerFactory.getLogger(ReporterManager.class);
	private String zipkinSenderEndpoint = null;
	
	public Reporter<zipkin.Span> build() {
		logger.trace("start build()");
		if(zipkinSenderEndpoint == null || "noopreporter".equals(zipkinSenderEndpoint)){
			logger.debug("NoopReporter");
			return new NoopReporter();
		}else{
			logger.debug("AsyncReporter with zipkinSenderEndpoint = '"+zipkinSenderEndpoint+"'");
			return AsyncReporter.builder(OkHttpSender.create(zipkinSenderEndpoint)).build();
		}
	}
	
	public Reporter<zipkin.Span> build(String zipkinSenderEndpoint){
		setZipkinSenderEndpoint(zipkinSenderEndpoint);
		return build();
	}

	public String getZipkinSenderEndpoint() {
		return zipkinSenderEndpoint;
	}

	public void setZipkinSenderEndpoint(String zipkinSenderEndpoint) {
		this.zipkinSenderEndpoint = zipkinSenderEndpoint;
	}

	
}
