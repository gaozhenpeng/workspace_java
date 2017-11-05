package com.at.spring.helloworld;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringSimplestMain {
	private static Logger logger = LoggerFactory.getLogger(SpringSimplestMain.class);
	
	public static void main(String[] args){
		AbstractApplicationContext ctx = new ClassPathXmlApplicationContext(
				"classpath*:/spring/applicationContext-resources.xml");

		ctx.registerShutdownHook();
		ctx.start();
		
		Helloworld hw = ctx.getBean(Helloworld.class);
		logger.info("hw: '{}' ;", hw.say());
		
		
		ctx.close();
	}
}
