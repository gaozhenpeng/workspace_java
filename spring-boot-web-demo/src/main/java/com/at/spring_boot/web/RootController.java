package com.at.spring_boot.web;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class RootController {

    @RequestMapping("/")
    public String home() {
        log.info("hello home.");
        return "hello world!";
    }
    

    @RequestMapping(value="/hwmv")
    public ModelAndView hwmv(Model model) {
        log.debug("Entering hwmv");

        ModelAndView mav = new ModelAndView();
        mav.setViewName("helloworld");
        mav.addObject("message", "hwmv 你好吗？？？");

        log.debug("Exiting hwmv");
        return mav;
    }
}
