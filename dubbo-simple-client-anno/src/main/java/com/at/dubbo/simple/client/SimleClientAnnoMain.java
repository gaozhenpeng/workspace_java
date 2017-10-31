package com.at.dubbo.simple.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.at.dubbo.simple.service.DemoService;

@Component
public class SimleClientAnnoMain {
	private static final Logger logger = LoggerFactory.getLogger(SimleClientAnnoMain.class);
	
	
	
	@Reference(version="1.0.0", url="dubbo://127.0.0.1:20880")
	private DemoService demoService;
	
	public String sayHello(String name){
		return demoService.sayHello(name);
	}
	
	
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "classpath*:/applicationContext-dubbo-client.xml" });
		context.start();
		SimleClientAnnoMain main = context.getBean(SimleClientAnnoMain.class); // 获取远程服务代理
		String hello = main.sayHello("world"); // 执行远程方法
		logger.info("Result: '{}'", hello); // 显示调用结果
	}
}
