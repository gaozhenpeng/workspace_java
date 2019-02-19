package com.at.spring.spring_rabbitmq_brave.listener;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DirectMessageListener implements MessageListener{
	private static final Logger logger = LoggerFactory.getLogger(DirectMessageListener.class);

	/**
	 * Injecting AmqpTemplate will bring in the problem of circular references
	 */
	@Autowired
	private ApplicationContext context ;

	/**
	 * processing business
	 */
	@Override
	public void onMessage(Message message) {
		logger.info("received: " + message);

		// dicing to stop message sending circle
		int dice = (int) ((Math.random() * 1000) % 10);
		try {
			Thread.sleep(dice*100);
		} catch (InterruptedException e1) {
			logger.warn("sleeping was interrupted.", e1);
		}
		if(dice == 6){
			logger.info("6666666666666666666666666666666666666");
			return;
		}
		
		logger.info("direct template.convertAndSend ing");
		AmqpTemplate template = context.getBean(AmqpTemplate.class);

		MessageProperties messagePropertiesForListener = new MessageProperties();
		messagePropertiesForListener.setHeader("name", "test");
		messagePropertiesForListener.setHeader("sex", "female");
		Message messageForListener;
		try {
			messageForListener = new Message("headersfooforlistener".getBytes("UTF-8"), messagePropertiesForListener);
		} catch (UnsupportedEncodingException e) {
			logger.warn("UTF-8",e);
			throw new RuntimeException(e);
		}

		logger.info("headersforlistener template.send ing");
		template.send("myheadersexchange", "someroutingkey", messageForListener);
		
		
	}

}
