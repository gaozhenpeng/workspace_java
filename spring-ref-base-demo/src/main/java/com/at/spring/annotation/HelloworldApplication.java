package com.at.spring.annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
// search current package if no basePackages are defined
@ComponentScan(basePackages = "com.at.spring.annotation")
public class HelloworldApplication {
	private static final Logger logger = LoggerFactory.getLogger(HelloworldApplication.class);

	@Autowired
	private ApplicationContext appCtx = null;

	@Bean
	@Qualifier("ms_helloworld")
	MessageServiceI helloworldMessageService() {
		return new MessageServiceI() {
			public String getMessage() {
				return "Hello World!";
			}
		};
	}

	@Bean
	@Qualifier("ms_hijack")
	MessageServiceI hijackMessageService() {
		return new MessageServiceI() {
			public String getMessage() {
				return "Hijack!";
			}
		};
	}

	@Bean
	public String helloworldApplicationName() {
		return "My name is HelloworldApplicationName.";
	}

	@Bean
	public String helloWorldName() {
		return "My name is HelloWorldName.";
	}

	@Bean(name = "azureName")
	public String AzureName() {
		return "My name is AzureName.";
	}

	@Bean(name = "aliyunName")
	public String AliyunName() {
		return "My name is AliyunName.";
	}

	public String callBeanName(String namePrefix) {
		String pre = namePrefix.toLowerCase(Locale.US);
		String nameString = null;
		try {
			nameString = appCtx.getBean("" + pre + "Name", String.class);
		} catch (BeansException be) {
			logger.error("bean not found.", be);
			nameString = "Bean not found for '" + namePrefix + "'";
		}
		return nameString;
	}

	public static void main(String[] args) {
		logger.debug("Enterring Main.");

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				HelloworldApplication.class);

		// 默认 bean 的名字
		String name1 = context.getBean("helloworldApplicationName", String.class);
		logger.info("name: '" + name1 + "'");

		String name2 = context.getBean("helloWorldName", String.class);
		logger.info("name: '" + name2 + "'");

		// 动态 bean 名, (@Bean @Component @Service @Controller @Repository)
		HelloworldApplication helloworldApplicationBean = context.getBean(HelloworldApplication.class);

		List<String> beanNames = new ArrayList<String>();
		beanNames.add("AzuRe");
		beanNames.add("AzUre");
		beanNames.add("azur");
		beanNames.add("AlIYuN");
		beanNames.add("AliYUN");
		beanNames.add("liyun");

		for (String bn : beanNames) {
			String rtStr = helloworldApplicationBean.callBeanName(bn);
			logger.info(rtStr);
		}

		// 按加载 service 加载
		MessagePrinter printer = context.getBean(MessagePrinter.class);
		printer.printMessage();

		context.close();
		logger.debug("Exiting Main.");
	}
}
