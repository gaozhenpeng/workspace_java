package com.at.spring.annotation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.at.spring.annotation.MessagePrinter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/META-INF/spring/**/*.xml",
		"classpath*:/META-INF/web/**/spring-*.xml" })
public class MessagePrinterXMLTest {

	private MessagePrinter messagePrinter = null;

	@Autowired
	public void setMessagePrinter(MessagePrinter messagePrinter) {
		this.messagePrinter = messagePrinter;
	}

	@Test
	public void testPrintMessage() {
		messagePrinter.printMessage();
	}
}
