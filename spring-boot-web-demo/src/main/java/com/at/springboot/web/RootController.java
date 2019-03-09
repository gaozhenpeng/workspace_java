package com.at.springboot.web;

import org.apache.logging.log4j.message.FormattedMessage;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
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

    @RequestMapping(value="/upload")
    public ModelAndView upload(
            @RequestParam(value = "myfile", required=false) MultipartFile myfile
            ,Model model) {
        log.debug("Entering upload");

        String msg = "";
        if(myfile != null) {
            log.info("isEmpty: '{}', name: '{}', originalFilename: '{}', size: '{}'", myfile.isEmpty(), myfile.getName(), myfile.getOriginalFilename(), myfile.getSize());

            msg = new FormattedMessage(
                        "isEmpty: '{}', name: '{}', originalFilename: '{}', size: '{}'"
                        , myfile.isEmpty()
                        , myfile.getName()
                        , myfile.getOriginalFilename()
                        , myfile.getSize()
                    ).getFormattedMessage();
        }
        ModelAndView mav = new ModelAndView();
        mav.setViewName("upload");
        mav.addObject("message", msg);

        log.debug("Exiting upload");
        return mav;
    }
}
