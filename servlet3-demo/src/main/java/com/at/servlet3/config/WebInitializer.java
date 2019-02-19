package com.at.servlet3.config;

import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>This class is <strong>unnecessary</strong> for most of time.
 * So is the file META-INF/services/javax.servlet.ServletContainerInitializer
 * </p>
 * <p>Use @WebInitParam, @WebFilter, @WebListener, @WebServlet instead.
 * </p>
 */
public class WebInitializer implements ServletContainerInitializer  {
	private static final Logger logger = LoggerFactory.getLogger(WebInitializer.class);
	@Override
	public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
		logger.info("Container initializing");
	}

}
