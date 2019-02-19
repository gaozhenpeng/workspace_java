package com.at.disconf.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ValueInjection {
	@Value("#{'${jdbc.driverClassName}'.trim()}")
	private String jdbc_driverClassName = null;
	@Value("#{'${jdbc.url}'.trim()}")
	private String jdbc_url = null;
	@Value("#{'${jdbc.username}'.trim()}")
	private String jdbc_username = null;
	@Value("#{'${jdbc.password}'.trim()}")
	private String jdbc_password = null;
	
	public void setJdbc_driverClassName(String jdbc_driverClassName) {
		this.jdbc_driverClassName = jdbc_driverClassName;
	}

	public void setJdbc_url(String jdbc_url) {
		this.jdbc_url = jdbc_url;
	}

	public void setJdbc_username(String jdbc_username) {
		this.jdbc_username = jdbc_username;
	}

	public void setJdbc_password(String jdbc_password) {
		this.jdbc_password = jdbc_password;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("jdbc.driverClassName: '").append(jdbc_driverClassName).append("'");
		sb.append(", jdbc.url: '").append(jdbc_url).append("'");
		sb.append(", jdbc.username: '").append(jdbc_username).append("'");
		sb.append(", jdbc.password: '").append(jdbc_password).append("'");
		return sb.toString();
	}

}
