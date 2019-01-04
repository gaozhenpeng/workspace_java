package com.at.spring_boot.shiro.controller;

import java.util.HashMap;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/shiro")
public class ShiroController {
    @RequestMapping(value = "params", method = { RequestMethod.GET, RequestMethod.POST })
    public String helloWorld(@RequestParam Map<String, String> params, Model m) {
        log.debug("Entering helloworld, params.");

        String par_name = params.get("name");
        if (par_name != null) {
            m.addAttribute("message", "name = '" + par_name + "'");
        }

        String viewname = "helloworld";
        String par_url = params.get("url");
        if (par_url != null) {
            viewname = par_url;
        }

        log.debug("Exiting helloworld, params.");
        return viewname;
    }

    @RequestMapping(value = { "/{page_path:index|login|unauthorized}" }, method = { RequestMethod.GET,
            RequestMethod.POST })
    public String login(@PathVariable("page_path") String page_path) {
        log.debug("Entering login()");

        log.info("page_path: '{}'", page_path);

        log.debug("Exiting login()");
        return "/login";
    }

    @RequestMapping(value = "/dologout", method = { RequestMethod.GET, RequestMethod.POST })
    public String doLogout(HttpServletRequest request, Model model) {
        log.debug("Entering doLogout(HttpServletRequest, Model)");


        Subject shiroSubject = SecurityUtils.getSubject();
        if(shiroSubject.isAuthenticated()) {
            String username = (String)shiroSubject.getSession().getAttribute("username");
            log.info("logout: '{}'", username);
            shiroSubject.logout();
        }

        log.debug("Exiting doLogout(HttpServletRequest, Model)");
        return "redirect:login";
    }

    @RequestMapping(value = "/dologin", method = RequestMethod.POST)
    public String doLogin(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request, Model model) {
        log.debug("Entering doLogin(@RequestParam Map<String,String>, HttpServletRequest, Model)");
        String msg;
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        token.setRememberMe(true);
        Subject shiroSubject = SecurityUtils.getSubject();
        try {
            shiroSubject.login(token);
            if (shiroSubject.isAuthenticated()) {
                log.debug("subject is authenticated.");
                shiroSubject.getSession().setAttribute("username", username);
                SavedRequest savedRequest = WebUtils.getSavedRequest(request);
                // saved request url
                if (savedRequest == null || savedRequest.getRequestUrl() == null) {
                    log.debug("redirect:admin/home");
                    return "redirect:admin/home";
                } else {
                    String urlWithContextPath = savedRequest.getRequestUrl();
                    String urlWithoutContextPath = urlWithContextPath.replaceFirst(request.getContextPath(), "");
                    log.debug("urlWithContextPath: '{}'", urlWithContextPath);
                    log.debug("urlWithoutContextPath: '{}'", urlWithoutContextPath);
                    //return "forward:" + savedRequest.getRequestUrl();
                    return "redirect:" + urlWithoutContextPath;
                }
            } else {
                log.debug("subject is unauthenticated.");
                return "/login";
            }
        } catch (IncorrectCredentialsException e) {
            msg = "Incorrect Credential. Password for account " + token.getPrincipal() + " was incorrect.";
            model.addAttribute("message", msg);
            log.warn(msg);
        } catch (ExcessiveAttemptsException e) {
            msg = "Excessive Attempts";
            model.addAttribute("message", msg);
            log.warn(msg);
        } catch (LockedAccountException e) {
            msg = "Locked Account. The account for username " + token.getPrincipal() + " was locked.";
            model.addAttribute("message", msg);
            log.warn(msg);
        } catch (DisabledAccountException e) {
            msg = "Disabled Account. The account for username " + token.getPrincipal() + " was disabled.";
            model.addAttribute("message", msg);
            log.warn(msg);
        } catch (ExpiredCredentialsException e) {
            msg = "Expired Credential. the account for username " + token.getPrincipal() + "  was expired.";
            model.addAttribute("message", msg);
            log.warn(msg);
        } catch (UnknownAccountException e) {
            msg = "Unknown Acount. There is no user with username of " + token.getPrincipal();
            model.addAttribute("message", msg);
            log.warn(msg);
        } catch (UnauthorizedException e) {
            msg = "Unauthorized! " + e.getMessage();
            model.addAttribute("message", msg);
            log.warn(msg);
        }

        log.debug("Exiting doLogin(@RequestParam Map<String,String>, HttpServletRequest, Model)");
        return "/login";
    }

//    @RequiresGuest
//    @RequiresAuthentication
//    @RequiresPermissions(value = {"resource1:op1", "resource1:op2", "resource2:op1"})
//    @RequiresPermissions(value = {"resource1:op1", "resource1:op2", "resource2:op1"}, logical = Logical.OR)
//    @RequiresRoles(value = {"admin"})
//    @RequiresRoles(value = {"user", "admin"}, logical = Logical.OR)
    @RequestMapping(value = "/admin/{admin_path}", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView admin(@PathVariable("admin_path") String admin_path) {
        log.debug("Exiting admin((@PathVariable(\"admin_path\") String )");

        log.info("admin_path: '{}'", admin_path);
        

        Subject shiroSubject = SecurityUtils.getSubject();
        String username = "";
        if(shiroSubject.isAuthenticated()) {
            username = (String)shiroSubject.getSession().getAttribute("username");
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/admin/" + admin_path);
        Map<String, String> user = new HashMap<>();
        user.put("username", username);
        mav.addObject("user", user);


        log.debug("Exiting admin((@PathVariable(\"admin_path\") String )");
        return mav;
    }

}