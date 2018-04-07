package com.at.spring;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class RootController {

    @RequestMapping("/")
    public String home() {
        log.info("hello home.");
        return "hello world!";
    }
}
