package com.at.shopping;

import org.apache.log4j.Logger;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.at.svr.AService;
import com.at.svr.BService;
import com.at.svr.ServiceFactory;

public class ProductListing {
	private static final Logger logger = Logger.getLogger(ProductListing.class);
	private ServiceFactory serviceFactory = null;

	public long getPriceA(long quantity) {
		AService s = (AService) serviceFactory.getService("aService");
		return s.getA() * quantity;
	}

	public long getPriceAX(long quantity) {
		AService s = (AService) serviceFactory.getService("axService");
		return s.getA() * quantity;
	}

	public long getPriceB(long quantity) {
		BService s = (BService) serviceFactory.getService("bService");
		return s.getB() * quantity;
	}

	public long getPriceBX(long quantity) {
		BService s = (BService) serviceFactory.getService("bxService");
		return s.getB() * quantity;
	}

	public void setServiceFactory(ServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	public static void main(String[] args) {
		AbstractApplicationContext ctx = new ClassPathXmlApplicationContext(
				"classpath*:/applicationContext-resources.xml");

		ctx.registerShutdownHook();
		ctx.start();

		ProductListing pl = (ProductListing) ctx.getBean("prodListing");
		logger.info("A 1: " + pl.getPriceA(10));
		logger.info("A 2: " + pl.getPriceA(10));
		logger.info("A 3: " + pl.getPriceA(10));

		logger.info("Ax 1: " + pl.getPriceAX(10));
		logger.info("Ax 2: " + pl.getPriceAX(10));
		logger.info("Ax 3: " + pl.getPriceAX(10));

		logger.info("B 1: " + pl.getPriceB(10));
		logger.info("B 2: " + pl.getPriceB(10));
		logger.info("B 3: " + pl.getPriceB(10));

		logger.info("Bx 1: " + pl.getPriceBX(10));
		logger.info("Bx 2: " + pl.getPriceBX(10));
		logger.info("Bx 3: " + pl.getPriceBX(10));

		logger.info("A 1: " + pl.getPriceA(10));
		logger.info("A 2: " + pl.getPriceA(10));
		logger.info("A 3: " + pl.getPriceA(10));

		logger.info("Ax 1: " + pl.getPriceAX(10));
		logger.info("Ax 2: " + pl.getPriceAX(10));
		logger.info("Ax 3: " + pl.getPriceAX(10));

		logger.info("B 1: " + pl.getPriceB(10));
		logger.info("B 2: " + pl.getPriceB(10));
		logger.info("B 3: " + pl.getPriceB(10));

		logger.info("Bx 1: " + pl.getPriceBX(10));
		logger.info("Bx 2: " + pl.getPriceBX(10));
		logger.info("Bx 3: " + pl.getPriceBX(10));

		ctx.stop();

	}

}
