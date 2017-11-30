package com.at.dubbo.simple.service.impl;

import org.springframework.stereotype.Service;

import com.at.dubbo.simple.service.DemoService;

@Service
public class DemoServiceImpl implements DemoService {
	public String sayHello(String name) {
		return "Hello " + name;
	}
}
