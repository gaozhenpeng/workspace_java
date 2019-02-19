package com.at.servlet3_brave.config;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import brave.Tracing;
import brave.context.slf4j.MDCCurrentTraceContext;
import brave.sampler.Sampler;
import brave.servlet.TracingFilter;
import zipkin.reporter.AsyncReporter;
import zipkin.reporter.okhttp3.OkHttpSender;

@WebListener
public class TracingServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		Tracing tracing = Tracing.newBuilder()
							.traceId128Bit(true) // use 128b traceID, 32 hex char
							.localServiceName("servlet3_brave")
							//// log4j2, import brave.context.log4j2.ThreadContextCurrentTraceContext;
							//.currentTraceContext(ThreadContextCurrentTraceContext.create()) // puts trace IDs into logs
							.currentTraceContext(MDCCurrentTraceContext.create()) // puts trace IDs into logs
							.sampler(Sampler.ALWAYS_SAMPLE) // always sampler
							.reporter(
									AsyncReporter.builder(
											OkHttpSender.create("http://127.0.0.1:9411/api/v1/spans")
									).build()
							)
							.build();
		servletContextEvent
			.getServletContext()
			.addFilter("TracingFilter", TracingFilter.create(tracing))
			.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
	}
}
