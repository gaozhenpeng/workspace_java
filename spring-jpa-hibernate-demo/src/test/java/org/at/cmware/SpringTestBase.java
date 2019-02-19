package org.at.cmware;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringTestBase {
	protected static ClassPathXmlApplicationContext ctx = null;

	@BeforeClass
	public static void initSpring() {
		ctx = new ClassPathXmlApplicationContext("classpath*:/ac-res-test.xml");
	}

	@AfterClass
	public static void tearoffSpring() {
		ctx = null;
	}
}
