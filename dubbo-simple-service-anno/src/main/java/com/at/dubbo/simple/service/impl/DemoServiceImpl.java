package com.at.dubbo.simple.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.at.dubbo.simple.service.DemoService;

@Service(version="1.0.0")
public class DemoServiceImpl implements DemoService {
	public String sayHello(String name) {
		return "Hello " + name;
	}
}
