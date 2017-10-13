package com.at.dubbo.simple.service.impl;

import com.at.dubbo.simple.service.DemoService;

public class DemoServiceImpl implements DemoService {
	public String sayHello(String name) {
		return "Hello " + name;
	}
}
