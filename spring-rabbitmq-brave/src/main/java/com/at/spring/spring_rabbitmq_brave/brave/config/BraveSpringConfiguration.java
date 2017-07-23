package com.at.spring.spring_rabbitmq_brave.brave.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.at.spring.spring_rabbitmq_brave.brave.logproperty.Slf4jMDCCurrentTraceContext;
import com.at.spring.spring_rabbitmq_brave.brave.reporter.NoopReporter;

import brave.Tracer;
import brave.Tracing;
import brave.sampler.Sampler;
import zipkin.reporter.AsyncReporter;
import zipkin.reporter.Reporter;
import zipkin.reporter.okhttp3.OkHttpSender;

/**
 * This adds tracing configuration to any web mvc controllers or rest template clients. This should
 * be configured last.
 */
@Configuration
public class BraveSpringConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(BraveSpringConfiguration.class);


	@Value("#{'${zipkin.tracing.localservicename}'.trim()}")
	private String zipkinTracingLocalServiceName;

	@Value("#{'${zipkin.sender.endpoint}'.trim()}")
	private String zipkinSenderEndpoint;

	/** Controls aspects of tracing such as the name that shows up in the UI */
	@Bean
	public Tracing tracing() {
		if(logger.isDebugEnabled()){
			logger.debug("zipkinTracingLocalServiceName: '" + zipkinTracingLocalServiceName + "'");
			logger.debug("zipkinSenderEndpoint: '" + zipkinSenderEndpoint + "'");
		}
		
		Reporter<zipkin.Span> reporter = null;
		if("localreporter".equals(zipkinSenderEndpoint)){
			reporter = new NoopReporter();
		}else{
			reporter = AsyncReporter.builder(OkHttpSender.create(zipkinSenderEndpoint)).build();
		}

		logger.debug("one tracing");
		return Tracing.newBuilder()
			.traceId128Bit(true) // use 128b traceID, 32 hex char
			.localServiceName(zipkinTracingLocalServiceName)
			.currentTraceContext(Slf4jMDCCurrentTraceContext.create()) // puts trace IDs into logs
			.sampler(Sampler.ALWAYS_SAMPLE) // always sampler
			.reporter(reporter)
			.build();
	}

	/** Controls aspects of tracing such as the name that shows up in the UI */
	@Bean
	public Tracer tracer() {
		logger.debug("one tracer");
		return tracing().tracer();
	}
}
