package com.at.junit4.spring;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = { "/ApplicationContext-junit4.xml" })
public class JUnit4SpringContextTestsTest extends AbstractJUnit4SpringContextTests {
	private String injectedString = null;

	@Resource(name = "injectedString")
	public void setInjectedString(final String injectedString) {
		this.injectedString = injectedString;
	}

	@Test
	public void testMethod() {
		Assert.assertNotNull(injectedString);
		System.out.println("injectedString: " + injectedString);
	}
}
