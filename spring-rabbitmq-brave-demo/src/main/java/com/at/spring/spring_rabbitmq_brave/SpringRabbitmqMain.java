package com.at.spring.spring_rabbitmq_brave;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.at.spring.spring_rabbitmq_brave.configuration.SpringRabbitmqConfiguration;

public class SpringRabbitmqMain {
	private static final Logger logger = LoggerFactory.getLogger(SpringRabbitmqMain.class);

	public static void main(String[] args) throws UnsupportedEncodingException, InterruptedException {
		logger.debug("Enterring Main.");

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				SpringRabbitmqConfiguration.class);
		AmqpTemplate template = context.getBean(AmqpTemplate.class);
		
//		// direct
//		logger.info("direct template.convertAndSend ing");
//		template.convertAndSend("mydirectexchange", "mydirectqueuekey", "directfoo");
//		logger.info("direct template.receiveAndConvert ing");
//		String foo = (String) template.receiveAndConvert("mydirectqueue", 1000);
//		logger.info("direct recv: " + foo);
//		
//		
//		// headers
//		MessageProperties messageProperties = new MessageProperties();
//		messageProperties.setHeader("name", "test");
//		messageProperties.setHeader("sex", "male");
//		Message message = new Message("headersfoo".getBytes("UTF-8"), messageProperties);
//		logger.info("headers template.send ing");
//		template.send("myheadersexchange", "someroutingkey", message);
//
//		logger.info("headers template.receive ing");
//		Message recvMsg = template.receive("myheadersqueue", 1000);
//		logger.info("headers recv: " + new String(recvMsg.getBody(), "UTF-8"));
//		
//		
		// directforlistener
		logger.info("directforlistener template.convertAndSend ing");
		template.convertAndSend("mydirectexchange", "mydirectqueueforlistenerkey", "directfooforlistener1111");
//		String fooforlistener = (String) template.receiveAndConvert("mydirectqueueforlistener", 1000);
//		logger.info("direct recv: " + foo);
		// directforlistener2
		logger.info("directforlistener template.convertAndSend ing");
		template.convertAndSend("mydirectexchange", "mydirectqueueforlistenerkey", "directfooforlistener2222");
//		String fooforlistener = (String) template.receiveAndConvert("mydirectqueueforlistener", 1000);
//		logger.info("direct recv: " + foo);
//		
//		
//		// headersforlistener
//		MessageProperties messagePropertiesForListener = new MessageProperties();
//		messagePropertiesForListener.setHeader("name", "test");
//		messagePropertiesForListener.setHeader("sex", "female");
//		Message messageForListener = new Message("headersfooforlistener".getBytes("UTF-8"), messagePropertiesForListener);
//
//		logger.info("headersforlistener template.send ing");
//		template.send("myheadersexchange", "someroutingkey", messageForListener);
//		
////		Message recvMsg = template.receive("myheadersqueue", 1000);
////		logger.info("headers recv: " + new String(recvMsg.getBody(), "UTF-8"));
		
		
		
		
		
		Thread.sleep(15000);
		context.close();
		logger.debug("Exiting Main.");
	}
}
