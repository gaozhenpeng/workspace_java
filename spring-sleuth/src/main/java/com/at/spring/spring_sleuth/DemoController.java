package com.at.spring.spring_sleuth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoController {
	private static Logger logger = LoggerFactory.getLogger(DemoController.class);

	@RequestMapping("/")
	public String home() {
		logger.info("Handling home");
		return "Hello World";
	}
	@Bean
	public Sampler defaultSampler() {
		return new AlwaysSampler();
	}
	public static void main(String[] args) {
		SpringApplication.run(DemoController.class, args);
	}
}
