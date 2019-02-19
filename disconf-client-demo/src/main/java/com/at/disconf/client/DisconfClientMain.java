package com.at.disconf.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DisconfClientMain {
	private static final Logger logger = LoggerFactory.getLogger(DisconfClientMain.class);
	
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "classpath*:/applicationContext-disconf-client.xml" });
		context.start();
		ValueInjection vi = context.getBean(ValueInjection.class);
		logger.info(vi.toString());

	}
}
