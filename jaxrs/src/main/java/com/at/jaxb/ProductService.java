package com.at.jaxb;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

@Path("/product")
public class ProductService {
	private static final Logger logger = Logger.getLogger(ProductService.class);

	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_XML)
	public Product getProduct() {
		final Product prod = new Product();
		prod.setId(1);
		prod.setName("Mattress");
		prod.setDescription("Queen size mattress");
		prod.setPrice(500);
		logger.info("Sending: " + prod);
		return prod;
	}

	@POST
	@Path("/create")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response createProduct(final Product prod) {
		logger.info("Received: " + prod);
		return Response.ok().build();
	}
}
