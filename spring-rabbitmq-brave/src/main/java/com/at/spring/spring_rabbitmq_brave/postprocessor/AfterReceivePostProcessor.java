package com.at.spring.spring_rabbitmq_brave.postprocessor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import brave.Span;
import brave.Span.Kind;
import brave.Tracer;
import brave.Tracer.SpanInScope;
import brave.Tracing;
import brave.propagation.TraceContextOrSamplingFlags;

@Component
public class AfterReceivePostProcessor implements MessagePostProcessor{
	private static final Logger logger = LoggerFactory.getLogger(AfterReceivePostProcessor.class);

	@Autowired
	private Tracing tracing;
	@Autowired
	private Tracer tracer;
	@Autowired
	private PropagationGetter propagationGetter;
	
	private static final Map<String, String> EMPTY_MAP = new HashMap<String, String>();
	
	@SuppressWarnings("unchecked")
	@Override
	public Message postProcessMessage(Message message) throws AmqpException {
		logger.debug("afterReceive");
		MessageProperties messageProperties = message.getMessageProperties();
		
		try{
			Map<String, Object> msgHeaders = messageProperties.getHeaders();
			Object headersObj = msgHeaders.get("zipkin.brave.tracing.headers");
			Map<String, String> strHeaders = null;
			if(headersObj != null){
				strHeaders = (HashMap<String, String>)headersObj;
			}else{
				strHeaders = EMPTY_MAP;
			}
			// JDK1.8 required
//			TraceContextOrSamplingFlags result = tracing.propagation().extractor(Map<String, String>::get).extract(strHeaders);
			TraceContextOrSamplingFlags result = tracing.propagation().extractor(propagationGetter).extract(strHeaders);

			Span clientSpan = null;
			if(result.context() != null){
				clientSpan = tracer.joinSpan(result.context()); // client span
			}else{
				clientSpan = tracer.newTrace(); // new span
			}
			if(clientSpan != null){
				@SuppressWarnings("unused")
				SpanInScope spanInScope = tracer.withSpanInScope(clientSpan);
				logger.debug("withSpanInScope");
				clientSpan
					.name("name_me_from_business") // named by business, it'd be calculated according to the receiving message
					.kind(Kind.SERVER)
					.start().flush();
			}
		}catch(Exception e){
			logger.warn("zipkin problem", e);
		}
		
		messageProperties.setHeader("afterReceive", "true");
		return new Message(message.getBody(), messageProperties);
	}
}
