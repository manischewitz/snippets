package com.restful.shop.resouces;

import java.net.URI;

import javax.ws.rs.HEAD;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

@Path("/dbShop")
public class StoreResource {

	@HEAD
	   public Response head(@Context UriInfo uriInfo) {
	      UriBuilder absolute = uriInfo.getBaseUriBuilder();
	      URI customerUrl = absolute.clone().path("dbCustomers").build();
	      URI orderUrl = absolute.clone().path("dbOrders").build();
	      URI productUrl = absolute.clone().path("dbProducts").build();
	      Link customers = Link.fromUri(customerUrl).rel("customers").type("application/xml").build();
	      Link orders = Link.fromUri(orderUrl).rel("orders").type("application/xml").build();
	      Link products = Link.fromUri(productUrl).rel("products").type("application/xml").build();

	      Response.ResponseBuilder builder = Response.ok().links(customers, orders, products);
	      return builder.build();
	   }
	
}
