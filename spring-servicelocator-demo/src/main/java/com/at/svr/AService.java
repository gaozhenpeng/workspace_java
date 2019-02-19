package com.at.svr;

public class AService implements Service {
	private Integer price = 20;

	public Integer getA() {
		Integer oldprice = price;
		price++;
		return oldprice;
	}
}
