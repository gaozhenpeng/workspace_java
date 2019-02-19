package com.at.svr;

public class BService implements Service {
	private Integer price = 10;

	public Integer getB() {
		Integer oldprice = price;
		price++;
		return oldprice;
	}
}
