package com.at.dubbo.simple.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.at.dubbo.simple.service.DemoService;
import com.at.dubbo.simple.service.dto.Request;
import com.at.dubbo.simple.service.dto.Response;

public class SimleClientMain {
	private static final Logger logger = LoggerFactory.getLogger(SimleClientMain.class);
	
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "classpath*:/applicationContext-dubbo-client.xml" });
		context.start();
		DemoService demoService = (DemoService) context.getBean("demoService"); // 获取远程服务代理
		Request req = new Request();
		req.setName("a new request name");
		Response res = demoService.sayHello(req); // 执行远程方法
		logger.info("Result: '{}'", res.getName()); // 显示调用结果
	}
}
