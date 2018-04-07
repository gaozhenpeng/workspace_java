package com.at.spring;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.at.spring.service.DemoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class RootController {

    @Reference(version="1.0.0"
//            , application = "${dubbo.application.id}"
//            , url = "dubbo://localhost:12345"
            )
    private DemoService demoService;

    @RequestMapping("/")
    public String home() {
        log.info("hello home.");
        log.info("demoService.sayHello(\"whoami\"): '{}'", demoService.sayHello("whoami"));
        return "hello world!";
    }
}
