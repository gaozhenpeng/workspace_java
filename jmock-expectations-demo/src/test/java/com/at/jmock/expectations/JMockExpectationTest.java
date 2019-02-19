package com.at.jmock.expectations;

import static org.junit.Assert.assertEquals;

import org.hamcrest.Description;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.junit.Test;

public class JMockExpectationTest {
	@Test
	public void testExpectations() {
		final Mockery mockery = new Mockery();
		final AInterface a = mockery.mock(AInterface.class);
		mockery.checking(new Expectations() {
			{
				exactly(1).of(a).method1(with(any(String.class)), with(any(String.class)), with(any(String.class)));
				will(onConsecutiveCalls(new Action() {
					@Override
					public void describeTo(final Description desc) {
						desc.appendText("Mock Action 1");
					}

					@Override
					public Object invoke(final Invocation invoc) throws Throwable {
						return (String) invoc.getParameter(0) + (String) invoc.getParameter(1)
								+ (String) invoc.getParameter(2);
					}
				}));
			}
		});
		final String methodResult = a.method1("String1", "String2", "String3");
		assertEquals("Concatenated String is expected.", "String1String2String3", methodResult);
	}
}
