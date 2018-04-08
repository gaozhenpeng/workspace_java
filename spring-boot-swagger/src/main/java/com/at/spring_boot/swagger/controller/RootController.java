package com.at.spring_boot.swagger.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.at.spring_boot.swagger.model.Greeting;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value = "user", tags = {"User API", "Root Controller"})
@Slf4j
@RestController
public class RootController {
    @ApiOperation(value = "say hello")
    @RequestMapping(
            value = "/"
            , method = RequestMethod.GET
            , produces = "application/json; charset=utf-8")
    public Greeting home(@RequestParam(name="name", required=true) String name) {
        log.info("Entering home.");
        
        Greeting greeting = new Greeting();
        greeting.setName(name);
        greeting.setMessage("hello "+name+"!");
        return greeting;
    }
}
