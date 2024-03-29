package com.at.spring.mvc.annotation;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.at.spring.mybatis.annotation.SpringMybatisMain;

@Controller
public class DBController {
	private static final Logger logger = LoggerFactory.getLogger(DBController.class);

	@RequestMapping(value = "/helloworld/spring_mybatis_main", method = RequestMethod.GET)
	public ModelAndView spring_mybatis_main() throws IOException {
		logger.debug("Entering spring_mybatis_main, SpringMybatisMain.main(null).");

		SpringMybatisMain.main(null);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("helloworld");
		mav.addObject("message", "spring_mybatis_main");

		logger.debug("Exiting spring_mybatis_main, SpringMybatisMain.main(null)");
		return mav;
	}
}
