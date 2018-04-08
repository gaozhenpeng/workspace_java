package com.at.spring;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.at.spring.service.DemoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class RootController {

    @Reference(version="1.0.0"
//            // direct link
//            , url = "dubbo://localhost:12345"
            )
    private DemoService demoService;

    @RequestMapping("/")
    public String home(@RequestParam(name="name", required=true) String name) {
        log.info("hello home.");
        
        
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("demoService.sayHello(\"").append(name).append("\"): ");
        stringBuilder.append(demoService.sayHello(name));
        log.info(stringBuilder.toString());
        return stringBuilder.toString();
    }
}
