package com.at.zipkin.brave;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.ClientRequestAdapter;
import com.github.kristofa.brave.ClientRequestInterceptor;
import com.github.kristofa.brave.ClientResponseAdapter;
import com.github.kristofa.brave.ClientResponseInterceptor;
import com.github.kristofa.brave.EmptySpanCollectorMetricsHandler;
import com.github.kristofa.brave.KeyValueAnnotation;
import com.github.kristofa.brave.ServerRequestAdapter;
import com.github.kristofa.brave.ServerRequestInterceptor;
import com.github.kristofa.brave.ServerResponseAdapter;
import com.github.kristofa.brave.ServerResponseInterceptor;
import com.github.kristofa.brave.SpanId;
import com.github.kristofa.brave.TraceData;
import com.github.kristofa.brave.http.HttpSpanCollector;
import com.twitter.zipkin.gen.Endpoint;

public class Brave390Main {

	private static final Logger logger = LoggerFactory.getLogger(Brave390Main.class);
	
	private static HttpSpanCollector collector = null;
	private static Brave brave1 = null;
	private static Brave brave2 = null;
	
	private static void braveInit(){
		collector = HttpSpanCollector.create("http://localhost:9411/", new EmptySpanCollectorMetricsHandler());

		brave1 = new Brave.Builder("appserver").spanCollector(collector).build();
		brave2 = new Brave.Builder("datacenter").spanCollector(collector).build();
	}
	
	static class Task {
		String name;
		SpanId spanId;
		public Task(String name, SpanId spanId) {
			super();
			this.name = name;
			this.spanId = spanId;
		}
	}
	private static final AtomicBoolean isContinue = new AtomicBoolean(true);
	private static final BlockingQueue<Task> queue = new ArrayBlockingQueue<Task>(10);
	public static void main(String[] args) throws Exception {
		braveInit();
		
		Thread thread = new Thread(){
			
			public void run() {
				logger.info("enter run");
				while(isContinue.get()) {
					logger.info("enter for");
					try {
						Task task = queue.poll(1, TimeUnit.SECONDS);
						if(task == null){
							isContinue.compareAndSet(true, false);
							return;
						}
						dcHandle(task.name, task.spanId);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
				logger.info("exit run");
			}
		};
		thread.start();
		
		{
			ServerRequestInterceptor serverRequestInterceptor = brave1.serverRequestInterceptor();
			ServerResponseInterceptor serverResponseInterceptor = brave1.serverResponseInterceptor();
			ClientRequestInterceptor clientRequestInterceptor = brave1.clientRequestInterceptor();
			ClientResponseInterceptor clientResponseInterceptor = brave1.clientResponseInterceptor();
			
			// 1. brave1 as a server, received a request
			serverRequestInterceptor.handle(new ServerRequestAdapterImpl("group_data"));
			
			// 1.1.1. brave1 as a client, sent request to brave2, request 1
			ClientRequestAdapterImpl clientRequestAdapterImpl = new ClientRequestAdapterImpl("get_radio_list");
			clientRequestInterceptor.handle(clientRequestAdapterImpl);
			// simulate the package sending to other component (datacenter, brave2) 
			queue.offer(new Task("get_radio_list", clientRequestAdapterImpl.getSpanId()));
			Thread.sleep(10);
			// 1.1.2. brave1 as a client, received response from brave2 
			clientResponseInterceptor.handle(new ClientResponseAdapterImpl());
			
			// 1.2.1 brave1 as a client, sent request to brave2, request 2
			ClientRequestAdapterImpl clientRequestAdapterImpl2 = new ClientRequestAdapterImpl("get_user_list");
			clientRequestInterceptor.handle(clientRequestAdapterImpl2);
			// simulate the package sending to other component (datacenter, brave2) 
			queue.offer(new Task("get_user_list", clientRequestAdapterImpl2.getSpanId()));
			Thread.sleep(10);
			// 1.2.2. brave1 as a client, received response from brave2 
			clientResponseInterceptor.handle(new ClientResponseAdapterImpl());

			// 1.3.1 brave1 as a client, sent request to brave2, request 3
			ClientRequestAdapterImpl clientRequestAdapterImpl3 = new ClientRequestAdapterImpl("get_program_list");
			clientRequestInterceptor.handle(clientRequestAdapterImpl3);
			// simulate the package sending to other component (datacenter, brave2) 
			queue.offer(new Task("get_program_list", clientRequestAdapterImpl3.getSpanId()));
			Thread.sleep(10);
			// 1.3.2. brave1 as a client, received response from brave2 
			clientResponseInterceptor.handle(new ClientResponseAdapterImpl());

			// 2. brave1 as a server, sent response to the caller
			serverResponseInterceptor.handle(new ServerResponseAdapterImpl());
		}
		logger.info("thread.isAlive(): " + thread.isAlive());
		while(isContinue.get()){
			Thread.sleep(1000);
		}
		logger.info("thread.isAlive(): " + thread.isAlive());
		collector.close();
	}
	
	public static void dcHandle(String spanName, SpanId spanId){
		logger.info("enter dcHandle");
		ServerRequestInterceptor serverRequestInterceptor = brave2.serverRequestInterceptor();
		ServerResponseInterceptor serverResponseInterceptor = brave2.serverResponseInterceptor();
		
		// brave2 as a server, received a request
		// sr: server received
		serverRequestInterceptor.handle(new ServerRequestAdapterImpl(spanName, spanId));
		// brave2 as a server, sent a response
		// ss: sever sent
		serverResponseInterceptor.handle(new ServerResponseAdapterImpl());
		
		logger.info("exit dcHandle");
	}
	
	
	static class ServerRequestAdapterImpl implements ServerRequestAdapter {
		
		Random randomGenerator = new Random();
		SpanId spanId;
		String spanName;
		
		ServerRequestAdapterImpl(String spanName){
			this.spanName = spanName;
			long startId = randomGenerator.nextLong();
			SpanId spanId = SpanId.builder().spanId(startId).traceId(startId).parentId(startId).build();
			this.spanId = spanId;
		}
		
		ServerRequestAdapterImpl(String spanName, SpanId spanId){
			this.spanName = spanName;
			this.spanId = spanId;
		}

		@Override
		public TraceData getTraceData() {
			if (this.spanId != null) {
				return TraceData.builder().spanId(this.spanId).build();
			}
			long startId = randomGenerator.nextLong();
			SpanId spanId = SpanId.builder().spanId(startId).traceId(startId).parentId(startId).build();
			return TraceData.builder().spanId(spanId).build();
		}

		@Override
		public String getSpanName() {
			return spanName;
		}

		@Override
		public Collection<KeyValueAnnotation> requestAnnotations() {
			Collection<KeyValueAnnotation> collection = new ArrayList<KeyValueAnnotation>();
			KeyValueAnnotation kv = KeyValueAnnotation.create("radioid", "165646485468486364");
			collection.add(kv);
			return collection;
		}
		
	}
	
	static class ServerResponseAdapterImpl implements ServerResponseAdapter {

		@Override
		public Collection<KeyValueAnnotation> responseAnnotations() {
			Collection<KeyValueAnnotation> collection = new ArrayList<KeyValueAnnotation>();
			KeyValueAnnotation kv = KeyValueAnnotation.create("radioid", "165646485468486364");
			collection.add(kv);
			return collection;
		}
		
	}
	
	static class ClientRequestAdapterImpl implements ClientRequestAdapter {
		
		String spanName;
		SpanId spanId;
		
		ClientRequestAdapterImpl(String spanName){
			this.spanName = spanName;
		}
		
		public SpanId getSpanId() {
			return spanId;
		}

		@Override
		public String getSpanName() {
			return this.spanName;
		}

		@Override
		public void addSpanIdToRequest(SpanId spanId) {
			//记录传输到远程服务
			logger.info("spanId: "+spanId);
			if (spanId != null) {
				this.spanId = spanId;
				logger.info(String.format("trace_id=%s, parent_id=%s, span_id=%s", spanId.traceId, spanId.parentId, spanId.spanId));
			}
		}

		@Override
		public Collection<KeyValueAnnotation> requestAnnotations() {
			Collection<KeyValueAnnotation> collection = new ArrayList<KeyValueAnnotation>();
			KeyValueAnnotation kv = KeyValueAnnotation.create("radioid", "165646485468486364");
			collection.add(kv);
			return collection;
		}

		@Override
		public Endpoint serverAddress() {
			return null;
		}
		
	}

	static class ClientResponseAdapterImpl implements ClientResponseAdapter {

		@Override
		public Collection<KeyValueAnnotation> responseAnnotations() {
			Collection<KeyValueAnnotation> collection = new ArrayList<KeyValueAnnotation>();
			KeyValueAnnotation kv = KeyValueAnnotation.create("radioname", "火星人1");
			collection.add(kv);
			return collection;
		}
		
	}
}
