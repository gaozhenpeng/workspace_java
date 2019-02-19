package com.at.spring.spring_mvc_servlet3.controller;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloworldController {
	private static final Logger logger = LoggerFactory.getLogger(HelloworldController.class);

	@RequestMapping(value="/helloworld")
	public String helloworld(Model model) {
		model.addAttribute("message", "helloworld 你好吗？？？");
		return "helloworld";
	}

	@RequestMapping(value="/hwmv")
	public ModelAndView hwmv(Model model) {
		logger.debug("Entering hwmv");

		ModelAndView mav = new ModelAndView();
		mav.setViewName("helloworld");
		mav.addObject("message", "hwmv 你好吗？？？");

		logger.debug("Exiting hwmv");
		return mav;
	}
	@RequestMapping(path = "/raw/**")
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ServletOutputStream sos = response.getOutputStream();
		sos.println("{\"result\" : \"OK\"}");
		response.addHeader("X-API-VERSION", "1.0");

		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpStatus.OK.value());

		sos.close();
		return;
	}
}

