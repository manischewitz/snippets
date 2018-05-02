package com.restful.shop.services;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletContext;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;


import com.restful.shop.domain.Customer;

@Path("prettyCustomer")
public class PrettyCustomerService {

	   private Map<Long, Customer> customerDB = new ConcurrentHashMap<>();
	   private AtomicInteger idCounter = new AtomicInteger();
	   
	   @Context
	   private ServletContext context;
	   
	   @GET
	   @Produces("text/html")
	   public String getWebFace() {
		   try {
			byte[] lines = Files.readAllBytes(Paths.get(context.getRealPath("WEB-INF/index.html")));
			return new String(lines);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(Response.serverError().build());
		}
	} 
	   
	   @GET
	   @Path("{id}")
	   @Produces("text/plain")
	   public Response getCustomer(@PathParam("id") long id,
			   @HeaderParam("User-Agent") String userAgent,
			   @CookieParam("last-visit") String date) {
		   final Customer customer = customerDB.get(id);
		   if (customer == null) {
			   throw new NotFoundException();
		   }
		   String output = "User-Agent: " + userAgent + "\r\n";
		   output += "Last visit: " + date + "\r\n\r\n";
		   output += "Customer: " + customer.getFirstName() + " " + customer.getLastName();
		   String lastVisit = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG).format(new Date());
		   return Response.ok(output).cookie(new NewCookie("last-visit", lastVisit)).build();
	   }
	   
	   @POST
	   @Produces("text/html")
	   public Response createCustomer(@FormParam("firstname") String first,
			   @FormParam("lastname") String last) {
		   long id = idCounter.incrementAndGet();
		   final Customer customer = new Customer(id, first, last, "somestreet",
				   "Moscow", "Russia", "25612864", "Russian Federation");
		   customerDB.put(id, customer);
		   System.out.println("Created customer " + customer);
		   
		   String output = "Created customer <a href=\"" + customer.getId() + "\">" 
		   + customer.getId() + "</a>";
		   String lastVisit = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG).format(new Date());
		   URI location = URI.create("prettyCustomer/"+customer.getId());
		   return Response.created(location).entity(output).cookie(new NewCookie("last-visit", lastVisit)).build();
	   }
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	
}
