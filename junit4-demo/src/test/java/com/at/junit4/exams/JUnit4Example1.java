package com.at.junit4.exams;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class JUnit4Example1 {
	private Stack<Integer> stack;
	private final Integer number;

	public JUnit4Example1(final int number) {
		this.number = number;
	}

    /**
     * Annotation <code>@Parameters</code>  for a method which provides parameters to be injected into the
     * test class constructor by <code>Parameterized</code>. The method has to
     * be public and static.
     */
	@Parameters
	public static List<Integer[]> data() {
	    // run THIS test 4 times, each with one parameter, like JUnit4Example1(1),JUnit4Example1(2),...
		final Integer[][] data = new Integer[][] { { 1 }, { 2 }, { 3 }, { 4 } };
		return Arrays.asList(data);
	}

	@Before
	public void noSetup() {
		stack = new Stack<Integer>();
	}

	@After
	public void noTearDown() {
		stack = null;
	}

	@Test
	public void pushTest() {
		stack.push(number);
		assertEquals("stack.peek() ne number", stack.peek(), number);

	}

	@Test
	public void popTest() {
	}

	@Test(expected = EmptyStackException.class)
	public void peekTest() {
		stack = new Stack<Integer>();
		stack.peek();
	}

	@Test
	public void emptyTest() {
		stack = new Stack<Integer>();
		assertTrue("stack.isEmpty() is false", stack.isEmpty());
	}

	@Test
	public void searchTest() {
	}
}
