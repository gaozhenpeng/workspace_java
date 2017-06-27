package com.at.spring.annotation;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.at.spring.annotation.MessagePrinter;

public class MessagePrinterAnnotationTest extends AnnotationTestBase {

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
