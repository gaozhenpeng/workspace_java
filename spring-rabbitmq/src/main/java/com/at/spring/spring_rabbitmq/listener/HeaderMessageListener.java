package com.at.spring.spring_rabbitmq.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class HeaderMessageListener implements MessageListener {
	private static final Logger logger = LoggerFactory.getLogger(HeaderMessageListener.class);

	@Override
	public void onMessage(Message message) {
		logger.info("received: " + message);
	}

}
