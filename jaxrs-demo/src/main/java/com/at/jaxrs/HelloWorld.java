package com.at.jaxrs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/helloworld")
public class HelloWorld {
	private String name = null;

	@GET
	@Produces("text/html")
	public String getHtml() {
		return "<html lang=\"en\"><body><h1>Hello, " + name + "!!</body></h1></html>";
	}

	public void setName(final String name) {
		this.name = name;
	}
}
