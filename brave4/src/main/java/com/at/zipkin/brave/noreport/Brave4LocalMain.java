package com.at.zipkin.brave.noreport;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import brave.Span;
import brave.Span.Kind;
import brave.Tracer;
import brave.Tracing;
import brave.context.slf4j.MDCCurrentTraceContext;
import brave.sampler.Sampler;
import zipkin.reporter.AsyncReporter;
import zipkin.reporter.Sender;
import zipkin.reporter.okhttp3.OkHttpSender;

public class Brave4LocalMain {

	private static final Logger logger = LoggerFactory.getLogger(Brave4LocalMain.class);
	
	public static void main(String[] args) throws IOException {
		
//		Sender zkSender1 = OkHttpSender.create("http://127.0.0.1:9411/api/v1/spans");
//		AsyncReporter<zipkin.Span> reporter1 = AsyncReporter.builder(zkSender1).build();
		// Create a tracing component with the service name you want to see in Zipkin.
		Tracing tracing1 = Tracing.newBuilder()
							.traceId128Bit(true) // use 128b traceID, 32 hex char
							.localServiceName("Brave4LocalMain-1")
							.currentTraceContext(MDCCurrentTraceContext.create()) // puts trace IDs into logs
//							.reporter(reporter1)
							.reporter(new LocalReporter())
							.sampler(Sampler.ALWAYS_SAMPLE) // or any other Sampler
							.build();
		
		// simulate to be the 2nd component
//		Sender zkSender2 = OkHttpSender.create("http://127.0.0.1:9411/api/v1/spans");
//		AsyncReporter<zipkin.Span> reporter2 = AsyncReporter.builder(zkSender2).build();
		Tracing tracing2 = Tracing.newBuilder()
							.traceId128Bit(true) // use 128b traceID, 32 hex char
							.localServiceName("Brave4LocalMain-2")
							.currentTraceContext(MDCCurrentTraceContext.create()) // puts trace IDs into logs
							.reporter(new LocalReporter())
							.sampler(Sampler.ALWAYS_SAMPLE) // or any other Sampler
							.build();

		
		// Tracing exposes objects you might need, most importantly the tracer
		Tracer tracer1 = tracing1.tracer();
		// simulate to be the 2nd component
		Tracer tracer2 = tracing2.tracer();
		
		Span rootSpan = null;
		Span childSpan1 = null;
		Span childChildSpan1 = null;
		Span childSpan2 = null;
		Span childChildSpan2 = null;
		Span childChildChildSpan2 = null;
		try {
			rootSpan = tracer1.newTrace().name("rootSpan").kind(Kind.SERVER).start();
			logger.info("rootSpan started");
			Thread.sleep(2000);
			childSpan1 = tracer1.newChild(rootSpan.context()).name("childSpan1").kind(Kind.CLIENT).start();
			logger.info("childSpan1 started");
			Thread.sleep(2000);
			
			childChildSpan1 = tracer2.newChild(childSpan1.context()).name("childChildSpan1").kind(Kind.SERVER).start();
			logger.info("childChildSpan1 started");
			Thread.sleep(2000);
			
			logger.info("do something expensive 1");
			Thread.sleep(2000);
			
			
			
			childSpan2 = tracer1.newChild(rootSpan.context()).name("childSpan2").kind(Kind.CLIENT).start();
			logger.info("childSpan2 started");
			Thread.sleep(2000);
			
			childChildSpan2 = tracer1.newChild(childSpan2.context()).name("childChildSpan2").kind(Kind.CLIENT).start();
			logger.info("childChildSpan2 started");
			Thread.sleep(2000);
			
			childChildChildSpan2 = tracer1.newChild(childChildSpan2.context()).name("childChildChildSpan2").kind(Kind.CLIENT).start();
			logger.info("childChildChildSpan2 started");
			Thread.sleep(2000);
			

			logger.info("do something expensive 2");
			Thread.sleep(2000);
			
		} catch (InterruptedException e) {
			logger.info("Thread interruptted", e);
		}finally{
			if(childChildSpan1 != null){
				childChildSpan1.finish();
				logger.info("childChildSpan1 finished");
			}
			if(childSpan1 != null){
				childSpan1.finish();
				logger.info("childSpan1 finished");
			}
			if(childChildChildSpan2 != null){
				childChildChildSpan2.finish();
				logger.info("childChildChildSpan2 finished");
			}
			if(childChildSpan2 != null){
				childChildSpan2.finish();
				logger.info("childChildSpan2 finished");
			}
			if(childSpan2 != null){
				childSpan2.finish();
				logger.info("childSpan2 finished");
			}
			if(rootSpan != null){
				rootSpan.finish();
				logger.info("rootSpan finished");
			}
		}
		
		logger.info("End!");
		
		// When all tracing tasks are complete, close the tracing component and reporter
		// This might be a shutdown hook for some users
		tracing1.close();
		tracing2.close();
//		reporter1.close();
//		reporter2.close();
//		zkSender1.close();
//		zkSender2.close();
		logger.info("closed");
	}

}
