package com.at.spring.mvc;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/helloworld")
public class HWController {
	private static final Logger logger = LoggerFactory.getLogger(HWController.class);

	@RequestMapping(value = "1", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView helloWorld() {
		logger.trace("Entering helloworld, 1.");

		ModelAndView mav = new ModelAndView();
		mav.setViewName("helloworld");
		mav.addObject("message", "1");

		logger.trace("Exiting helloworld, 1.");
		return mav;
	}
	@RequestMapping(value = "2", method = {RequestMethod.GET, RequestMethod.POST})
	public String name(Model model) {
		logger.trace("Entering name");
		
		model.addAttribute("message", "2");
		
		logger.trace("Exiting name");
		return "helloworld";
	}
	@RequestMapping(path = "raw")
	public void raw(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.trace("Entering raw");
		
		response.addHeader("X-API-VERSION", "1.0");
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpStatus.OK.value());
		PrintWriter printWriter = response.getWriter();
		printWriter.println("hello world!");
		printWriter.close();

		logger.trace("Exiting raw");
		return;
	}
}