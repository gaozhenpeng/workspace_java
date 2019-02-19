package com.at.dubbo.simple.service;

import com.at.dubbo.simple.service.dto.Request;
import com.at.dubbo.simple.service.dto.Response;

public interface DemoService {
	public Response sayHello(Request req);
}
