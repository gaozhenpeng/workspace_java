package com.at.spring.spring_mvc_servlet3.web;
//
//import javax.servlet.ServletContext;
//import javax.servlet.ServletRegistration;
//
//import org.springframework.web.WebApplicationInitializer;
//import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
//import org.springframework.web.servlet.DispatcherServlet;
//
//import com.at.spring.spring_mvc_servlet3.config.SpringMVCConfig;
//
//public class MyWebApplicationInitializer implements WebApplicationInitializer {
//
//	@Override
//	public void onStartup(ServletContext container) {
////
////		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
////		ctx.register(SpringMVCConfig.class);
////		ctx.setServletContext(container);
//
//		//// look for <servlet-name>-servlet.xml, e.g. example-servlet.xml
//		ServletRegistration.Dynamic registration = container.addServlet("dispatcher", new DispatcherServlet());
//		registration.setLoadOnStartup(1);
//		registration.addMapping("/api/*");
//	}
//
//}
