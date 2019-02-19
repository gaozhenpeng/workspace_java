package com.at.dubbo.simple.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.at.dubbo.simple.service.DemoService;
import com.at.dubbo.simple.service.dto.Request;
import com.at.dubbo.simple.service.dto.Response;

@Service
public class DemoServiceImpl implements DemoService {
    private static final Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);
    @Override
    public Response sayHello(Request req) {
        logger.info(req.getName());
        Response res = new Response();
        res.setName("res: the req name is '" + req.getName() + "'");
        return res;
    }
}
