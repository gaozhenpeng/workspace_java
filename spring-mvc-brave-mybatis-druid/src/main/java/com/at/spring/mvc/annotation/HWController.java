package com.at.spring.mvc.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HWController {
	private static final Logger logger = LoggerFactory.getLogger(HWController.class);

	@RequestMapping(value = "/helloworld/1", method = RequestMethod.GET)
	public ModelAndView helloWorld() {
		logger.debug("Entering helloworld, 1.");

		ModelAndView mav = new ModelAndView();
		mav.setViewName("helloworld");
		mav.addObject("message", "1");

		logger.debug("Exiting helloworld, 1.");
		return mav;
	}
	@RequestMapping(value = "/helloworld/2", method = RequestMethod.GET)
	public String name(Model model) {
		logger.debug("Entering name");
		
		model.addAttribute("message", "2");
		
		logger.debug("Exiting name");
		return "helloworld";
	}
}
