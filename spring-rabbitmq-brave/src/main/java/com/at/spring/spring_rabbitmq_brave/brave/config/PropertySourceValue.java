package com.at.spring.spring_rabbitmq_brave.brave.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource(
		/**
		 * It's INVALID to use wildcard in the path. see the doc of
		 * {@link org.springframework.context.annotation.PropertySource#value()}
		 * for details
		 */
		value = {"classpath:/brave.properties"}, ignoreResourceNotFound = false)
public class PropertySourceValue {
	/**
	 * This Bean is used to resolve the expression ${} .
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}