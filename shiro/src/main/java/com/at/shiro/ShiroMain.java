package com.at.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ShiroMain {
	private static final Logger log = LoggerFactory.getLogger(ShiroMain.class);

	public static void main(String[] args) {

		// All of realms, users, roles and permissions are defined in shiro.ini.
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
		SecurityManager securityManager = factory.getInstance(); // getInstance() is the sole method of Factory  

		// SecurityUtils are the main class of getting Subject.
		SecurityUtils.setSecurityManager(securityManager); // statically holding the instance of SecurityManager
		
		
		// get the current user (subject = user)
		Subject currentUser = SecurityUtils.getSubject();

		// shiro session
		Session session = currentUser.getSession();
		session.setAttribute("someKey", "aValue"); // note that the user has not logged in!
		String value = (String) session.getAttribute("someKey");
		if (value.equals("aValue")) {
			log.info("Retrieved the correct value! [" + value + "]");
		}

		// if current user has not logged in.
		if (!currentUser.isAuthenticated()) {
			UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");
			token.setRememberMe(true); // remember the user across different sessions
			try {
				currentUser.login(token); // login with token
			} catch (UnknownAccountException uae) {
				log.warn("There is no user with username of " + token.getPrincipal());
			} catch (IncorrectCredentialsException ice) {
				log.warn("Password for account " + token.getPrincipal() + " was incorrect!");
			} catch (LockedAccountException lae) {
				log.warn("The account for username " + token.getPrincipal() + " is locked.	" +
						"Please contact your administrator to unlock it.");
			}
			// ... catch more exceptions here (maybe custom ones specific to your application?
			catch (AuthenticationException ae) {
				//unexpected condition?	 error?
				log.warn("Unexpected exception.", ae);
			}
		}

		// print their identifying principal (in this case, a username):
		log.info("User [" + currentUser.getPrincipal() + "] logged in successfully.");

		// check role
		if (currentUser.hasRole("schwartz")) {
			log.info("May the Schwartz be with you!");
		} else {
			log.info("Hello, mere mortal.");
		}

		// check permission (not instance-level)
		if (currentUser.isPermitted("lightsaber:weild")) {
			log.info("You may use a lightsaber ring.  Use it wisely.");
		} else {
			log.info("Sorry, lightsaber rings are for schwartz masters only.");
		}

		// check permission (instance level)
		if (currentUser.isPermitted("winnebago:drive:eagle5")) {
			log.info("You are permitted to 'drive' the winnebago with license plate (id) 'eagle5'.	" +
					"Here are the keys - have fun!");
		} else {
			log.info("Sorry, you aren't allowed to drive the 'eagle5' winnebago!");
		}

		// destroy the authenticated session
		currentUser.logout();
	}
}

