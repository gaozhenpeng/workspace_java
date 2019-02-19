package com.at.spring.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MessagePrinter {
	private static final Logger logger = LoggerFactory.getLogger(MessagePrinter.class);

	private MessageServiceI messageService;

	@Autowired
	@Qualifier("ms_helloworld")
	public void setMessageService(MessageServiceI messageService) {
		this.messageService = messageService;
	}

	/**
	 * required for injection
	 */
	public MessagePrinter() {

	}

	public MessagePrinter(MessageServiceI messageService) {
		this.messageService = messageService;
	}

	public void printMessage() {
		logger.info(this.messageService.getMessage());
	}
}
