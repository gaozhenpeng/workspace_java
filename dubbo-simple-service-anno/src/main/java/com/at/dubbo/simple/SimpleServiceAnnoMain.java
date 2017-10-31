package com.at.dubbo.simple;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SimpleServiceAnnoMain {
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "classpath*:/applicationContext-dubbo-service.xml" });
		context.start();
		System.in.read(); // press any key to exit
	}
}
