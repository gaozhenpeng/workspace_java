package com.at.spring.mvc.config;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.at.spring.mvc.annotation.SpringMVCAnnotationMain;

public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	public void onStartup(ServletContext servletContext) {

		// **** global servlets
		
		// spring annotation config
		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.register(SpringMVCAnnotationMain.class);
		ctx.setServletContext(servletContext);
		
		ServletRegistration.Dynamic registration = servletContext.addServlet("dispatcher", new DispatcherServlet(ctx));
		registration.setLoadOnStartup(1);
		registration.addMapping("/api/*"); // url-pattern
		registration.setAsyncSupported(true);
		
		
		
		// Druid StatViewServlet
		StatViewServlet druidStatViewServlet = new StatViewServlet();
		ServletRegistration.Dynamic druidStatViewServletDynamic = servletContext.addServlet("druidStatViewServlet", druidStatViewServlet);
		Map<String, String> druidStatViewServletInitParameters = new HashMap<String, String>();
		druidStatViewServletInitParameters.put("loginUsername", "druidadmin");
		druidStatViewServletInitParameters.put("loginPassword", "druidadmin");
		druidStatViewServletDynamic.setInitParameters(druidStatViewServletInitParameters);
		druidStatViewServletDynamic.addMapping("/druid/*");
		
		
		// **** global filters
		
//		// add spring characterEncoding filter
//		// to always have encoding on all requests
//		EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ERROR);

		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);

		FilterRegistration.Dynamic characterEncoding = servletContext.addFilter("characterEncoding", characterEncodingFilter);
		//characterEncoding.addMappingForUrlPatterns(dispatcherTypes, true, "/*");
		characterEncoding.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
		characterEncoding.setAsyncSupported(true);
		
		
		
		
		// Druid WebStatFilter
		WebStatFilter druidWebStatFilter = new WebStatFilter();
		druidWebStatFilter.setProfileEnable(true);
		druidWebStatFilter.setSessionStatEnable(true);
		
//		WebAppStat webAppStat = new WebAppStat();
//		webAppStat.setMaxStatSessionCount(10000);
//		webAppStat.setMaxStatUriCount(10000);
//		druidWebStatFilter.setWebAppStat(webAppStat);
		
		FilterRegistration.Dynamic druidWebStatFilterDynamic = servletContext.addFilter("druidWebStatFilter", druidWebStatFilter);
		Map<String, String> druidWebStatFilterInitParameters = new HashMap<String, String>();
		druidWebStatFilterInitParameters.put("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
//		druidWebStatFilterInitParameters.put("principalCookieName", "zs_uid");
//		druidWebStatFilterInitParameters.put("principalSessionName", "da_ss_ud");
//		druidWebStatFilterInitParameters.put("principalSessionName", "zs_re_aa");
		druidWebStatFilterDynamic.setInitParameters(druidWebStatFilterInitParameters);
		druidWebStatFilterDynamic.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");

		
	}

	@Override
	protected String[] getServletMappings() {
//		return new String[] { "*.do" };
		return new String[] { "/" };
	}

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return null;
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}

}
