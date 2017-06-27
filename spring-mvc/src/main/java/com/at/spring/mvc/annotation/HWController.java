package com.at.spring.mvc.annotation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HWController {
	private static final Logger logger = LoggerFactory.getLogger(HWController.class);

	@RequestMapping(value = "/helloworld/i__-r_mav", method = RequestMethod.GET)
	public ModelAndView helloWorld() {
		logger.debug("Entering helloworld, i__-r_mav.");

		ModelAndView mav = new ModelAndView();
		mav.setViewName("helloworld");
		mav.addObject("message", "i__-r_mav");

		logger.debug("Exiting helloworld, i__-r_mav.");
		return mav;
	}

	@RequestMapping(value = "/helloworld/i_m-r_str", method = RequestMethod.GET)
	public String helloWorld(Model m) {
		logger.debug("Entering helloworld, i_m-r_str.");

		m.addAttribute("message", "i_m-r_str");

		logger.debug("Exiting helloworld, i_m-r_str.");
		return "helloworld";
	}

	@RequestMapping(path = "/raw/**")
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
