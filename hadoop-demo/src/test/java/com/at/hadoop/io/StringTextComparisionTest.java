package com.at.hadoop.io;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.UnsupportedEncodingException;

import org.apache.hadoop.io.Text;
import org.junit.Test;

public class StringTextComparisionTest {
	@Test
	public void testString() throws UnsupportedEncodingException{
		String s = "\u0041\u00DF\u6771\uD801\uDC00";
		assertThat(s.length(), is(5));
		assertThat(s.getBytes("UTF-8").length, is(10));
		
		assertThat(s.indexOf("\u0041"), is(0));
		assertThat(s.indexOf("\u00DF"), is(1));
		assertThat(s.indexOf("\u6771"), is(2));
		assertThat(s.indexOf("\uD801\uDC00"), is(3));

		assertThat(s.charAt(0), is('\u0041'));
		assertThat(s.charAt(1), is('\u00DF'));
		assertThat(s.charAt(2), is('\u6771'));
		assertThat(s.charAt(3), is('\uD801'));
		assertThat(s.charAt(4), is('\uDC00'));

		assertThat(s.codePointAt(0), is(0x0041));
		assertThat(s.codePointAt(1), is(0x00DF));
		assertThat(s.codePointAt(2), is(0x6771));
		assertThat(s.codePointAt(3), is(0x10400));
	}

	@Test
	public void testText() {
		Text t = new Text("\u0041\u00DF\u6771\uD801\uDC00");
		assertThat(t.getLength(), is(10));
		
		assertThat(t.find("\u0041"), is(0));
		assertThat(t.find("\u00DF"), is(1));
		assertThat(t.find("\u6771"), is(3));
		assertThat(t.find("\uD801\uDC00"), is(6));

		assertThat(t.charAt(0), is(0x0041));
		assertThat(t.charAt(1), is(0x00DF));
		assertThat(t.charAt(3), is(0x6771));
		assertThat(t.charAt(6), is(0x10400));
	}
}
