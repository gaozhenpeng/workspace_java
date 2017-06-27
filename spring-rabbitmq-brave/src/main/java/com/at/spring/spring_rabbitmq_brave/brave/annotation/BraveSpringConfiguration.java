package com.at.spring.spring_rabbitmq_brave.brave.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import brave.Tracer;
import brave.Tracing;
import brave.context.slf4j.MDCCurrentTraceContext;
import brave.sampler.Sampler;
import zipkin.Span;
import zipkin.reporter.AsyncReporter;
import zipkin.reporter.Reporter;
import zipkin.reporter.Sender;
import zipkin.reporter.okhttp3.OkHttpSender;

/**
 * This adds tracing configuration to any web mvc controllers or rest template clients. This should
 * be configured last.
 */
@Configuration
public class BraveSpringConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(BraveSpringConfiguration.class);

	/** Configuration for how to send spans to Zipkin */
	@Bean
	public Sender sender() {
		logger.debug("okhttpsender");
		return OkHttpSender.create("http://127.0.0.1:9411/api/v1/spans");
	}

	/** Configuration for how to buffer spans into messages for Zipkin */
	@Bean
	public Reporter<Span> reporter() {
		logger.debug("asyncreporter");
		return AsyncReporter.builder(sender()).build();
	}

	/** Controls aspects of tracing such as the name that shows up in the UI */
	@Bean
	public Tracing tracing() {
		logger.debug("one tracing");
		return Tracing.newBuilder()
			.traceId128Bit(true) // use 128b traceID, 32 hex char
			.localServiceName("spring-rabbitmq-brave")
			//// log4j2, import brave.context.log4j2.ThreadContextCurrentTraceContext;
			//.currentTraceContext(ThreadContextCurrentTraceContext.create()) // puts trace IDs into logs
			.currentTraceContext(MDCCurrentTraceContext.create()) // puts trace IDs into logs
			.sampler(Sampler.ALWAYS_SAMPLE) // always sampler
			.reporter(reporter())
			.build();
	}

	/** Controls aspects of tracing such as the name that shows up in the UI */
	@Bean
	public Tracer tracer() {
		logger.debug("one tracer");
		return tracing().tracer();
	}
}
