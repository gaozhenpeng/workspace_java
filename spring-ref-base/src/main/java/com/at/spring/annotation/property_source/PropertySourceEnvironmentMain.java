package com.at.spring.annotation.property_source;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.at.spring.annotation.property_source")
public class PropertySourceEnvironmentMain {
	private static final Logger logger = LoggerFactory.getLogger(PropertySourceEnvironmentMain.class);
	

	@Resource(name = "COMPUTE_TARGETURL")
	private String compute_targeturl = null;

	@Bean(name = "getCompute_targeturl")
	public String getCompute_targeturl() {
		return compute_targeturl;
	}

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext context = new AnnotationConfigApplicationContext(PropertySourceEnvironmentMain.class);
		String getCompute_targeturl = context.getBean("getCompute_targeturl", String.class);
		logger.info(getCompute_targeturl);

	}

}
