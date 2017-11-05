package com.at.spring.helloworld;

public class Helloworld {
	private String name = null;
	public void setName(String name){
		this.name = name;
	}
	public String say(){
		return "Hello, " + name;
	}
}
