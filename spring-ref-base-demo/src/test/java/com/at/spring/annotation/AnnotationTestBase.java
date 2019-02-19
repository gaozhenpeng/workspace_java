package com.at.spring.annotation;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AnnotationTestBase.class })
@Configuration
// search current package if no basePackages are defined
@ComponentScan(basePackages = "com.at.spring.annotation")
@PropertySource(value = "classpath:/openstack.properties")
@Ignore
public class AnnotationTestBase {

	// identity service
	@Value("#{'${openstack.identity.service.targeturl}'.trim()}")
	protected String identity_service_targetURL;
}
