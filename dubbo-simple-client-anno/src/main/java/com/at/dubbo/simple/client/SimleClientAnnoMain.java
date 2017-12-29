package com.at.dubbo.simple.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.at.dubbo.simple.service.DemoService;
import com.at.dubbo.simple.service.dto.Request;
import com.at.dubbo.simple.service.dto.Response;

@Component
public class SimleClientAnnoMain {
	private static final Logger logger = LoggerFactory.getLogger(SimleClientAnnoMain.class);
	
	
	
	@Reference(version="1.0.0", url="dubbo://127.0.0.1:20880?serialization=compactedjava")
	private DemoService demoService;
	
	public Response sayHello(String name){
	    Request req = new Request();
	    req.setName(name);
		return demoService.sayHello(req);
	}
	
	
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "classpath*:/applicationContext-dubbo-client.xml" });
		context.start();
		SimleClientAnnoMain main = context.getBean(SimleClientAnnoMain.class); // 获取远程服务代理
		Response res = main.sayHello("world"); // 执行远程方法
		logger.info("Result: '{}'", res.getName()); // 显示调用结果
	}
}
