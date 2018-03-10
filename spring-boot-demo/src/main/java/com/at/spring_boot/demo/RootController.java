package com.at.spring_boot.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class RootController {

    @RequestMapping("/")
    public String home() {
        log.info("Inside.");
        return "hello world!";
    }
}
