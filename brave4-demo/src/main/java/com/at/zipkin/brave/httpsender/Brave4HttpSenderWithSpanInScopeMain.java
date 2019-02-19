package com.at.zipkin.brave.httpsender;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import brave.Span;
import brave.Span.Kind;
import brave.Tracer;
import brave.Tracer.SpanInScope;
import brave.Tracing;
import brave.context.slf4j.MDCCurrentTraceContext;
import brave.sampler.Sampler;
import zipkin.reporter.AsyncReporter;
import zipkin.reporter.Sender;
import zipkin.reporter.okhttp3.OkHttpSender;

public class Brave4HttpSenderWithSpanInScopeMain {

	private static final Logger logger = LoggerFactory.getLogger(Brave4HttpSenderWithSpanInScopeMain.class);
	
	public static void main(String[] args) throws IOException, InterruptedException {
		String zipkin_server_endpoint = "http://127.0.0.1:9411/api/v1/spans";
		
		Sender zkSender1 = OkHttpSender.create(zipkin_server_endpoint);
		AsyncReporter<zipkin.Span> reporter1 = AsyncReporter.builder(zkSender1).build();
		// Create a tracing component with the service name you want to see in Zipkin.
		Tracing tracing1 = Tracing.newBuilder()
							.traceId128Bit(true) // use 128b traceID, 32 hex char
							.localServiceName("Brave4HttpSenderWithSpanInScopeMain")
							.currentTraceContext(MDCCurrentTraceContext.create()) // puts trace IDs into logs
							.reporter(reporter1)
							.sampler(Sampler.ALWAYS_SAMPLE) // or any other Sampler
							.build();

		// simulate to be the 2nd component
		Sender zkSender2 = OkHttpSender.create(zipkin_server_endpoint);
		AsyncReporter<zipkin.Span> reporter2 = AsyncReporter.builder(zkSender2).build();
		Tracing tracing2 = Tracing.newBuilder()
							.traceId128Bit(true) // use 128b traceID, 32 hex char
							.localServiceName("Brave4LocalWithSpanInScopeMain2")
							.currentTraceContext(MDCCurrentTraceContext.create()) // puts trace IDs into logs
							.reporter(reporter2)
							.sampler(Sampler.ALWAYS_SAMPLE) // or any other Sampler
							.build();

		// Tracing exposes objects you might need, most importantly the tracer
		Tracer tracer1 = tracing1.tracer();
		// simulate to be the 2nd component
		Tracer tracer2 = tracing2.tracer();
		
		Span rootSpan = null;
		Span childSpan1 = null;
		Span childChildSpan1 = null;
		
		rootSpan = tracer1.newTrace().name("rootSpan").kind(Kind.SERVER).start();
		logger.info("rootSpan started");
		try(SpanInScope spanInScopeRootSpan = tracer1.withSpanInScope(rootSpan) ){// activate newScope()
			Thread.sleep(2000);
			childSpan1 = tracer1.newChild(rootSpan.context()).name("childSpan1").kind(Kind.CLIENT).start();
			logger.info("childSpan1 started");
			try(SpanInScope spanInScopeChildSpan1 = tracer1.withSpanInScope(childSpan1)){
			
				Thread.sleep(2000);
			
				childChildSpan1 = tracer2.newChild(childSpan1.context()).name("childChildSpan1").kind(Kind.SERVER).start();
				logger.info("childChildSpan1 started");
				try(SpanInScope spanInScopeChildChildSpan1 = tracer2.withSpanInScope(childChildSpan1)){
				
					Thread.sleep(2000);
				
					logger.info("do something expensive 1");
					Thread.sleep(2000);
				}
				childChildSpan1.finish();
				logger.info("childChildSpan1 finished");
				
				tracing2.close();
				reporter2.close();
			}
			childSpan1.finish();
			logger.info("childSpan1 finished");
		}
		rootSpan.finish();
		logger.info("rootSpan finished");
		
		logger.info("End!");
		
		// When all tracing tasks are complete, close the tracing component and reporter
		// This might be a shutdown hook for some users
		tracing1.close();
		reporter1.close();
		logger.info("closed");
	}

}
