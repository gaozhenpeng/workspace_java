package com.at.java_assert;

public class JavaAssertMain {
	public static void main(String[] args){
		String a = "a";
		String b = "b";
		assert "a".equals(a) : a + " not equals to 'a'";
		assert a.equals(b) : a + " not equals to " + b;
	}
}
