package com.at.spring.spring_rabbitmq.postprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.stereotype.Component;

@Component
public class BeforePublishPostProcessor implements MessagePostProcessor {
	private static final Logger logger = LoggerFactory.getLogger(BeforePublishPostProcessor.class);


	@Override
	public Message postProcessMessage(Message message) throws AmqpException {
		logger.debug("beforeSend");
		
		MessageProperties messageProperties = message.getMessageProperties();
		
		messageProperties.setHeader("beforeSend", "true");
		
		return new Message(message.getBody(), messageProperties);
	}
}
