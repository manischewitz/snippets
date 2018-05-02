package com.restful.shop.services;

import java.net.URI;

import javax.ws.rs.HEAD;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

@Path("/shop")
public class StoreResource {

	@HEAD
	public Response head(@Context UriInfo info) {
		UriBuilder absolute = info.getBaseUriBuilder();
		URI customersURL = absolute.clone().path(JacksonCustomerResource.class).build();
		URI orderURL = absolute.clone().path(OrderService.class).build();
		
		Response.ResponseBuilder builder = Response.ok();
		Link customersXML = Link.fromUri(customersURL).rel("customers").type("application/xml").build();
		Link customersJSON = Link.fromUri(customersURL).rel("customers").type("application/json").build();
		Link orders = Link.fromUri(orderURL).rel("orders").type("application/xml").build();
		return builder.links(customersXML, customersJSON, orders).build();
	}
	
}
