package com.at.spring.brave.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.at.spring.brave.logproperty.Slf4jMDCCurrentTraceContext;
import com.at.spring.brave.reporter.NoopReporter;

import brave.Tracer;
import brave.Tracing;
import brave.http.HttpTracing;
import brave.sampler.Sampler;
import brave.spring.webmvc.TracingHandlerInterceptor;
import zipkin.reporter.AsyncReporter;
import zipkin.reporter.Reporter;
import zipkin.reporter.okhttp3.OkHttpSender;

/**
 * This adds tracing configuration to any web mvc controllers or rest template clients. This should
 * be configured last.
 */
@Configuration
@EnableWebMvc
public class BraveSpringConfiguration  extends WebMvcConfigurerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(BraveSpringConfiguration.class);



	@Value("#{'${zipkin.tracing.localservicename}'.trim()}")
	private String zipkinTracingLocalServiceName;

	@Value("#{'${zipkin.sender.endpoint}'.trim()}")
	private String zipkinSenderEndpoint;
	
	/** Controls aspects of tracing such as the name that shows up in the UI */
	@Bean
	public Tracing tracing() {
		logger.trace("start tracing()");

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
		logger.trace("start tracer()");
		return tracing().tracer();
	}
	

	// decides how to name and tag spans. By default they are named the same as
	// the http method.
	@Bean
	public HttpTracing httpTracing() {
		logger.trace("start httpTracing()");
		return HttpTracing.create(tracing());
	}

	@Bean
	public AsyncHandlerInterceptor asyncHandlerInterceptor(){
		logger.trace("start asyncHandlerInterceptor()");
		AsyncHandlerInterceptor asyncHandlerInterceptor = TracingHandlerInterceptor.create(httpTracing());
		return asyncHandlerInterceptor;
	}

	/**
	 * adds tracing to the web controllers
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		logger.trace("start addInterceptors(InterceptorRegistry)");
		registry.addInterceptor(asyncHandlerInterceptor());
	}
}
