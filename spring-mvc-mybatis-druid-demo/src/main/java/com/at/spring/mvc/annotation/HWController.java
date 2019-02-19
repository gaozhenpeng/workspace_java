package com.at.spring.mvc.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HWController {
	private static final Logger logger = LoggerFactory.getLogger(HWController.class);

	@RequestMapping(value = "/helloworld/i__-r_mav", method = RequestMethod.GET)
	public ModelAndView helloWorld() {
		logger.debug("Entering helloworld, i__-r_mav.");

		ModelAndView mav = new ModelAndView();
		mav.setViewName("helloworld");
		mav.addObject("message", "i__-r_mav");

		logger.debug("Exiting helloworld, i__-r_mav.");
		return mav;
	}
}
