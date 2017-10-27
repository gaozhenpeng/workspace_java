package com.at.spring.mvc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.at.spring.mybatis.dao.BlogDao;
import com.at.spring.mybatis.vo.Blog;

@Controller
@RequestMapping(value = "/helloworld")
public class HWController {
	private static final Logger logger = LoggerFactory.getLogger(HWController.class);

	@Autowired
	private BlogDao blogDao = null;
	
	@RequestMapping(value = "daoPage", method = RequestMethod.GET)
	public ModelAndView daoPage() {
		logger.debug("Entering daoPage.");

		StringBuilder sb = new StringBuilder();
		
		
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(1);
		ids.add(2);
		ids.add(3);
		ids.add(4);
		ids.add(5);
		List<Integer> not_ids = new ArrayList<Integer>();
		not_ids.add(5);
		not_ids.add(6);
		not_ids.add(7);
		not_ids.add(8);

		Map params = new HashMap();
		params.put("not_name", "notmyname");
		params.put("ids", ids);
		params.put("not_ids", not_ids);

		List<Blog> blogs = blogDao.listPage(params, "selectBlogs");
		if(blogs != null && blogs.size() > 0){
			for(Blog blog : blogs){
				sb.append("blog: '" + blog.getName() + "'");
			}
		}
		
		
		
		
		
		
		
		
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("helloworld");
		mav.addObject("message", sb.toString());

		logger.debug("Exiting daoPage.");
		return mav;
	}
	@RequestMapping(value = "daoOne", method = RequestMethod.GET)
	public ModelAndView daoOne() {
		logger.debug("Entering daoOne.");

		StringBuilder sb = new StringBuilder();
		
				Map params = new HashMap();;
		params.put("blog_id", 3);

		Blog blog = blogDao.listOne(params, "selectBlog");
		if(blog != null){
			sb.append("blog: '" + blog.getName() + "'");
		}
		
		
		
		
		
		
		
		
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("helloworld");
		mav.addObject("message", sb.toString());

		logger.debug("Exiting daoOne.");
		return mav;
	}
	
	@RequestMapping(value = "1", method = RequestMethod.GET)
	public ModelAndView helloWorld() {
		logger.debug("Entering helloworld, 1.");

		ModelAndView mav = new ModelAndView();
		mav.setViewName("helloworld");
		mav.addObject("message", "1");

		logger.debug("Exiting helloworld, 1.");
		return mav;
	}

	@RequestMapping(value = "2", method = RequestMethod.GET)
	public String helloWorld(Model m) {
		logger.debug("Entering helloworld, 2.");

		m.addAttribute("message", "2");

		logger.debug("Exiting helloworld, 2.");
		return "helloworld";
	}
	@RequestMapping(value = "params", method = {RequestMethod.GET,RequestMethod.POST})
	public String helloWorld(@RequestParam Map<String, String> params, Model m) {
		logger.debug("Entering helloworld, params.");

		String par_name = params.get("name");
		if(par_name != null){
			m.addAttribute("message", "name = '" + par_name + "'");
		}
		
		String viewname = "helloworld";
		String par_url = params.get("url");
		if(par_url != null){
			viewname = par_url;
		}

		logger.debug("Exiting helloworld, params.");
		return viewname;
	}

	@RequestMapping(path = "/raw/**") // !!! /helloworld/raw/**
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// method
		String reqMethod = request.getMethod();
		logger.debug("Request method: '" + reqMethod + "'");
		response.addHeader("VC3-Request-Method", reqMethod);

		// pathInfo
		String pathInfo = request.getPathInfo();
		logger.debug("Request pathInfo: '" + pathInfo + "'");
		response.addHeader("VC3-Request-PathInfo", pathInfo);

		// url
		logger.debug("Request parameters: ");
		Map<String, String[]> parameters = request.getParameterMap();
		if (parameters != null) {
			StringBuilder reqParams = new StringBuilder();
			boolean isLeadingAnd = true;
			for (String key : parameters.keySet()) {
				if (isLeadingAnd) {
					isLeadingAnd = false;
				} else {
					reqParams.append("&");
				}

				reqParams.append(key + "=" + URLEncoder.encode(String.join(",", parameters.get(key)), "UTF-8"));
				logger.debug("\t" + key + ": " + String.join(",", parameters.get(key)));
			}
			response.addHeader("VC3-Request-Parameters", reqParams.toString());
		}
		logger.debug(";");

		// header
		logger.debug("Request headers: ");
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames != null && headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			Enumeration<String> headerValues = request.getHeaders(headerName);
			while (headerValues != null && headerValues.hasMoreElements()) {
				String headerValue = headerValues.nextElement();
				logger.debug("\t" + WordUtils.capitalize(headerName, '-', '_') + ": " + headerValue);
				response.addHeader(WordUtils.capitalize(headerName, '-', '_'), headerValue);
			}
		}

		// content
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = null;
		OutputStream os = null;
		try {
			is = request.getInputStream();
			os = response.getOutputStream();
			byte[] buf = new byte[1024 * 8];
			while (is != null && is.read(buf) > 0) {
				baos.write(buf);
				os.write(buf);
			}
		} finally {
			if (is != null) {
				is.close();
			}
			if (os != null) {
				os.close();
			}
			if (baos != null) {
				baos.close();
			}
		}

		logger.debug("Request body: " + baos.toString("UTF-8"));

		response.addHeader("X-API-VERSION", "1.0");

		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpStatus.OK.value());

		return;
	}
}