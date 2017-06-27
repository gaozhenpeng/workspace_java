package com.at.spring.brave.annotation;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import brave.Tracing;
import brave.context.slf4j.MDCCurrentTraceContext;
import brave.http.HttpTracing;
import brave.sampler.Sampler;
import brave.spring.web.TracingClientHttpRequestInterceptor;
import brave.spring.webmvc.TracingHandlerInterceptor;
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
@EnableWebMvc
// Importing these classes is effectively the same as declaring bean methods
@Import({TracingClientHttpRequestInterceptor.class, TracingHandlerInterceptor.class})
public class BraveSpringConfiguration extends WebMvcConfigurerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(BraveSpringConfiguration.class);

	@Autowired
	private TracingHandlerInterceptor serverInterceptor;

	@Autowired
	private TracingClientHttpRequestInterceptor clientInterceptor;

	@Autowired
	private RestTemplate restTemplate;


	/** Configuration for how to send spans to Zipkin */
	@Bean
	Sender sender() {
		return OkHttpSender.create("http://127.0.0.1:9411/api/v1/spans");
	}

	/** Configuration for how to buffer spans into messages for Zipkin */
	@Bean
	Reporter<Span> reporter() {
		return AsyncReporter.builder(sender()).build();
	}

	/** Controls aspects of tracing such as the name that shows up in the UI */
	@Bean
	Tracing tracing() {
		return Tracing.newBuilder()
			.traceId128Bit(true) // use 128b traceID, 32 hex char
			.localServiceName("spring-mvc-brave-mybatis-druid-example")
			//// log4j2, import brave.context.log4j2.ThreadContextCurrentTraceContext;
			//.currentTraceContext(ThreadContextCurrentTraceContext.create()) // puts trace IDs into logs
			.currentTraceContext(MDCCurrentTraceContext.create()) // puts trace IDs into logs
			.sampler(Sampler.ALWAYS_SAMPLE) // always sampler
			.reporter(reporter())
			.build();
	}

	// decides how to name and tag spans. By default they are named the same as
	// the http method.
	@Bean
	HttpTracing httpTracing() {
		return HttpTracing.create(tracing());
	}

	@Bean
	RestTemplate template() {
		return new RestTemplate();
	}

	/**
	 * adds tracing to the spring rest template
	 */
	@PostConstruct
	public void init() {
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>(restTemplate.getInterceptors());
		interceptors.add(clientInterceptor);
		restTemplate.setInterceptors(interceptors);
	}

	/**
	 * adds tracing to the web controllers
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(serverInterceptor);
	}
}
