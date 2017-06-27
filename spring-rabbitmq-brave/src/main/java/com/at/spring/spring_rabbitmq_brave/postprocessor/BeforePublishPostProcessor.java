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
import brave.Tracing;

@Component
public class BeforePublishPostProcessor implements MessagePostProcessor {
	private static final Logger logger = LoggerFactory.getLogger(BeforePublishPostProcessor.class);

	@Autowired
	private Tracing tracing;
	@Autowired
	private Tracer tracer;
	@Autowired
	private PropagationSetter propagationSetter;
	
	@Override
	public Message postProcessMessage(Message message) throws AmqpException {
		logger.debug("beforeSend");
		MessageProperties messageProperties = message.getMessageProperties();
		
		try{
			Span currentSpan = tracer.currentSpan();
			Span newSpan = null;
			if(currentSpan == null){
				newSpan = tracer.newTrace();
			}else{
				newSpan = tracer.newChild(currentSpan.context());
			}
			
			/**
			 * you don't need to put the outgoing span into scope.
			 * there's only one current span in usual
			*/
//			tracer.withSpanInScope(newSpan);
//			logger.debug("tracer.withSpanInScope(newSpan)");
			
			Map<String, String> strHeaders = new HashMap<String, String>();
			// JDK 1.8 required
//			tracing.propagation().injector(Map<String, String>::put).inject(newSpan.context(),strHeaders);
			tracing.propagation().injector(propagationSetter).inject(newSpan.context(),strHeaders);
			messageProperties.setHeader("zipkin.brave.tracing.headers", strHeaders);
			
			
			newSpan
//				.name("name_me_from_business") // DON'T call .name() in client side for oneway tracing
				.kind(Kind.CLIENT) // client cs
				.start().flush();
		}catch(Exception e){
			logger.warn("zipkin problem", e);
		}
		
		messageProperties.setHeader("beforeSend", "true");
		return new Message(message.getBody(), messageProperties);
	}
}
