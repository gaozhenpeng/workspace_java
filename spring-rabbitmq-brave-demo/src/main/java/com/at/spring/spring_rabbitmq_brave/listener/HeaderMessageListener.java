package com.at.spring.spring_rabbitmq_brave.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class HeaderMessageListener implements MessageListener{
	private static final Logger logger = LoggerFactory.getLogger(HeaderMessageListener.class);

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
		
		// directforlistener
		AmqpTemplate template = context.getBean(AmqpTemplate.class);

		logger.info("directforlistener template.convertAndSend ing");
		template.convertAndSend("mydirectexchange", "mydirectqueueforlistenerkey", "directfooforlistener");
//		String fooforlistener = (String) template.receiveAndConvert("mydirectqueueforlistener", 1000);
//		logger.info("direct recv: " + foo);
//		
	}

}
