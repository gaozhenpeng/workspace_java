package com.at.spring.spring_rabbitmq.postprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.stereotype.Component;

@Component
public class AfterReceivePostProcessor implements MessagePostProcessor{
	private static final Logger logger = LoggerFactory.getLogger(AfterReceivePostProcessor.class);

	@Override
	public Message postProcessMessage(Message message) throws AmqpException {
		logger.debug("afterReceive");
		
		MessageProperties messageProperties = message.getMessageProperties();
		
		messageProperties.setHeader("afterReceive", "true");
		
		return new Message(message.getBody(), messageProperties);
	}
}
