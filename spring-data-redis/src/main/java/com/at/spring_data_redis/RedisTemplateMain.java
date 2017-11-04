package com.at.spring_data_redis;

import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
// search current package if no basePackages are defined
@ComponentScan(basePackages = "com.at.spring_data_redis")
public class RedisTemplateMain {
	private static final Logger logger = LoggerFactory.getLogger(RedisTemplateMain.class);

	// inject the actual template
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	// inject the template as ListOperations
	// can also inject as Value, Set, ZSet, and HashOperations
	@Resource(name = "redisTemplate")
	private ListOperations<String, String> listOps;

	public void addLink(String userId, URL url) {
		listOps.leftPush(userId, url.toExternalForm());
		// or use template directly
//		redisTemplate.boundListOps(userId).leftPush(url.toExternalForm());
	}

	public String getLink(String userId) {
		String url = listOps.leftPop(userId);
		// or use template directly
//		String url = redisTemplate.boundListOps(userId).leftPop();
		return url;
	}
	
	public static void main(String[] args) throws MalformedURLException {
		logger.debug("Enterring Main.");

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RedisTemplateMain.class);

		RedisTemplateMain main = context.getBean(RedisTemplateMain.class);
		
		String orgLink = "file:///myurl";
		main.addLink("myuserid", new URL(orgLink));
		String link = main.getLink("myuserid");
		logger.info("orgLink: '{}', link: '{}' ;", orgLink, link);
		
		context.close();
		logger.debug("Exiting Main.");
	}
}
