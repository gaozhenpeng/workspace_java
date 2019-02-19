package com.at.spring.mvc.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
// search current package if no basePackages are defined
@ComponentScan(basePackages = {"com.at.spring"})
public class SpringMVCAnnotationMain {
	private static final Logger logger = LoggerFactory.getLogger(SpringMVCAnnotationMain.class);

	@Bean
	public InternalResourceViewResolver viewResolver() {
		logger.debug("Configuring InternalResourceViewResolver");
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/jsp/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

}
