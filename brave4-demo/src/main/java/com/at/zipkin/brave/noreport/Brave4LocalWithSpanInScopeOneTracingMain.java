package com.at.zipkin.brave.noreport;

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

public class Brave4LocalWithSpanInScopeOneTracingMain {
	private static final Logger logger = LoggerFactory.getLogger(Brave4LocalWithSpanInScopeOneTracingMain.class);
	
	public static void main(String[] args) throws IOException, InterruptedException {
//		Sender zkSender1 = OkHttpSender.create("http://127.0.0.1:9411/api/v1/spans");
//		AsyncReporter<zipkin.Span> reporter1 = AsyncReporter.builder(zkSender1).build();
		// Create a tracing component with the service name you want to see in Zipkin.
		Tracing tracing1 = Tracing.newBuilder()
							.traceId128Bit(true) // use 128b traceID, 32 hex char
							.localServiceName("Brave4HttpSenderWithSpanInScopeOneTracingMain")
							.currentTraceContext(MDCCurrentTraceContext.create()) // puts trace IDs into logs
							.reporter(new NoopReporter())
							.sampler(Sampler.ALWAYS_SAMPLE) // or any other Sampler
							.build();

		// Tracing exposes objects you might need, most importantly the tracer
		Tracer tracer1 = tracing1.tracer();
		
		Span rootSpan = null;
		Span childSpan1 = null;
		
		rootSpan = tracer1.newTrace().name("rootSpan").kind(Kind.SERVER).start();
		logger.info("rootSpan started");
		try(SpanInScope spanInScopeRootSpan = tracer1.withSpanInScope(rootSpan) ){
			Thread.sleep(2000);
			childSpan1 = tracer1.newChild(rootSpan.context()).name("childSpan1").kind(Kind.CLIENT).start();
			logger.info("childSpan1 started");
			try(SpanInScope spanInScopeChildSpan1 = tracer1.withSpanInScope(childSpan1)){
			
				// do something expensive
				Thread.sleep(3000);
				
				logger.info("expensive work done");
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
//		reporter1.close();
		logger.info("closed");
	}

}
