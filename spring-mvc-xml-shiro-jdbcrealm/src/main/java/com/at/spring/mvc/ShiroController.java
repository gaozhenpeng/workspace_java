package com.at.spring.mvc;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/shiro")
public class ShiroController {
	private static final Logger logger = LoggerFactory.getLogger(ShiroController.class);

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

	
	
	
	
	
	
	@RequestMapping(value = {"/{page_path:index|login|unauthorized}"}, method = {RequestMethod.GET,RequestMethod.POST})
	public String login(@PathVariable("page_path") String page_path) {
		logger.debug("Entering login()");
		
		logger.info("page_path: '{}'", page_path);
		
		logger.debug("Exiting login()");
		return "login";
	}

	@RequestMapping(value = "/dologout", method = {RequestMethod.GET,RequestMethod.POST})
	public String doLogout(HttpServletRequest request, Model model) {
		logger.debug("Entering doLogout(HttpServletRequest, Model)");
		
		logger.info("logout: '" + request.getSession().getAttribute("user") + "'");
		
		SecurityUtils.getSubject().logout();

		logger.debug("Exiting doLogout(HttpServletRequest, Model)");
		return "redirect:login";
	}

	@RequestMapping(value = "/dologin", method = RequestMethod.POST)
	public String doLogin(@RequestParam Map<String,String> params, HttpServletRequest request, Model model) {
		logger.debug("Entering doLogin(@RequestParam Map<String,String>, HttpServletRequest, Model)");
		String msg;
		UsernamePasswordToken token = new UsernamePasswordToken(params.get("username"), params.get("password"));
		token.setRememberMe(true);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
			if (subject.isAuthenticated()) {
				request.getSession().setAttribute("user", params);
				SavedRequest savedRequest = WebUtils.getSavedRequest(request);
				// saved request url
				if (savedRequest == null || savedRequest.getRequestUrl() == null) {
					return "redirect:admin/home";
				} else {
					// String url = savedRequest.getRequestUrl().substring(12,
					// savedRequest.getRequestUrl().length());
//					return "forward:" + savedRequest.getRequestUrl();
					return "redirect:" + savedRequest.getRequestUrl();
				}
			} else {
				return "login";
			}
		} catch (IncorrectCredentialsException e) {
			msg = "Incorrect Credential. Password for account " + token.getPrincipal() + " was incorrect.";
			model.addAttribute("message", msg);
			logger.warn(msg);
		} catch (ExcessiveAttemptsException e) {
			msg = "Excessive Attempts";
			model.addAttribute("message", msg);
			logger.warn(msg);
		} catch (LockedAccountException e) {
			msg = "Locked Account. The account for username " + token.getPrincipal() + " was locked.";
			model.addAttribute("message", msg);
			logger.warn(msg);
		} catch (DisabledAccountException e) {
			msg = "Disabled Account. The account for username " + token.getPrincipal() + " was disabled.";
			model.addAttribute("message", msg);
			logger.warn(msg);
		} catch (ExpiredCredentialsException e) {
			msg = "Expired Credential. the account for username " + token.getPrincipal() + "  was expired.";
			model.addAttribute("message", msg);
			logger.warn(msg);
		} catch (UnknownAccountException e) {
			msg = "Unknown Acount. There is no user with username of " + token.getPrincipal();
			model.addAttribute("message", msg);
			logger.warn(msg);
		} catch (UnauthorizedException e) {
			msg = "Unauthorized! " + e.getMessage();
			model.addAttribute("message", msg);
			logger.warn(msg);
		}

		logger.debug("Exiting doLogin(@RequestParam Map<String,String>, HttpServletRequest, Model)");
		return "login";
	}
	
	
	
	

	@RequestMapping(value = "/admin/{admin_path}", method = {RequestMethod.GET,RequestMethod.POST})
	public String admin(@PathVariable("admin_path") String admin_path) {
		logger.debug("Exiting admin((@PathVariable(\"admin_path\") String )");

		logger.info("admin_path: '{}'", admin_path);
		
		logger.debug("Exiting admin((@PathVariable(\"admin_path\") String )");
		return "/admin/" + admin_path;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}