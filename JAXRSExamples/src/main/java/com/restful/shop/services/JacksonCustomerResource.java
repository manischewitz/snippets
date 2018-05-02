package com.restful.shop.services;

import java.net.URI;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.jboss.resteasy.specimpl.RequestImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.restful.shop.annotations.MaxAge;
import com.restful.shop.domain.Customer;
import com.restful.shop.domain.Customers;
import com.restful.shop.domain.LastView;
import com.restful.shop.domain.LastViewed;
import com.restful.shop.exceptions.NegativeIdException;
import com.restful.shop.util.HttpUtil;
import com.restful.shop.util.HttpUtil.Algorithm;

@Path("/jacksonCustomers")
public class JacksonCustomerResource {

	   private Map<Long, Customer> customerDB = new ConcurrentHashMap<>();
	   private AtomicLong idCounter = new AtomicLong();
	   
	   public JacksonCustomerResource() {
		   customerDB.put(idCounter.incrementAndGet(), new Customer(1, "John", "Doe", "Main street", "Moscow", "Russia", "64128", "RU"));
		   customerDB.put(idCounter.incrementAndGet(), new Customer(2, "Ann", "Lee", "Main street", "Moscow", "Russia", "64128", "RU"));
		   customerDB.put(idCounter.incrementAndGet(), new Customer(3, "Bill", "Foo", "Main street", "Moscow", "Russia", "64128", "RU"));
		   customerDB.put(idCounter.incrementAndGet(), new Customer(4, "Lisa", "Doe", "Main street", "Moscow", "Russia", "64128", "RU"));
		   customerDB.put(idCounter.incrementAndGet(), new Customer(5, "Bar", "Foo", "Main street", "Moscow", "Russia", "64128", "RU"));
	   }
	   
	   @POST
	   @Consumes({"application/xml", "application/example-java", MediaType.APPLICATION_JSON})
	   public Response createCustomer(Customer in) {
		   long id = idCounter.incrementAndGet();
		   Customer customer = new Customer(id, in.getFirstName(), in.getLastName(), in.getStreet(), 
				   in.getCity(), in.getState(), in.getZip(), in.getCountry());
		   customerDB.put(customer.getId(), customer);
		   System.out.println("Created customer " + customer);
		   return Response.created(URI.create("jacksonCustomers/" + id)).build();
	   }
	   
	   @GET
	   @Path("{id}")
	   @MaxAge(9000)
	   @Produces({"application/xml", "application/example-java", "application/json"})
	   public Customer getCustomer(@PathParam("id") long id) {
		   if (id < 0) {
			   throw new NegativeIdException("ID value must be 1 or heiger.");
		   }
		   Customer customer = customerDB.get(id);
	       if (customer == null) {
	         throw new NotFoundException();
	       }
	      return customer;
	   }
	   
	   @GET
	   @Path("last/{id}")
	   @Produces("application/xml")
	   public Response getLastViewed (@PathParam("id") long id, @HeaderParam("If-None-Match") String ifNoneMath,  @Context Request request, 
               @HeaderParam("If-Modified-Since") String ifModifiedSince) {
		   if (id < 0) {
			   throw new NegativeIdException("ID value must be 1 or heiger.");
		   }
		   if (ifNoneMath == null && ifModifiedSince == null) {
			   System.out.println("No If-None-Match nor If-Modified-Since sent by client");
		   }
		   Customer customer = customerDB.get(id);
		      if (customer == null) {
		         throw new NotFoundException();
		      }
		      EntityTag tag = new EntityTag(HttpUtil.hash(Algorithm.MD5, customer));
		      CacheControl cc = new CacheControl();
		      cc.setMaxAge(5);

		      Response.ResponseBuilder builder = request.evaluatePreconditions(customer.getLastModifiedDate(), tag);
		      if (builder != null) {
		    	  	System.out.println("** revalidation on the server was successful");
		    	  	builder.cacheControl(cc);
		    	  	return builder.build();
		      }
		      LastView lv = new LastView(Instant.now().toString(), customer);
		      List<LastView> list = new ArrayList<>();
		      list.add(lv);
		      LastViewed<Customer> viewed = new LastViewed<>(customer, list);
		      builder = Response.ok(viewed, "application/xml");
		      builder.cacheControl(cc);
		      builder.tag(tag);
		      builder.lastModified(customer.getLastModifiedDate());
		      return builder.build();
	   }
	   
	   @PUT
	   @Path("{id}")
	   @Consumes({"application/xml", "application/example-java", "application/json"})
	   public void updateCustomer(@PathParam("id") long id, Customer customer) {
		   if (id < 0) {
			   throw new NegativeIdException("ID value must be 1 or heiger.");
		   }
		   Customer current = customerDB.get(id);
		   if (null == current) {
			   throw new NotFoundException();
		   }
		  Customer updated = new Customer(current.getId(), customer.getFirstName(), customer.getLastName(),
				   customer.getStreet(), customer.getCity(), customer.getState(), customer.getZip(), 
				   customer.getCountry());
		   customerDB.put(id, updated);
		 }
	   
	   @PUT
	   @Path("last/{id}")
	   @Consumes({"application/xml", "application/example-java", "application/json"})
	   public Response updateCustomer(@PathParam("id") long id, LastViewed<Customer> view, @Context Request request, 
			   @HeaderParam("If-Match") String ifNoneMath, @HeaderParam("If-Modified-Since") String ifModifiedSince) {
		   if (id < 0) {
			   throw new NegativeIdException("ID value must be 1 or heiger.");
		   }
		   if (ifNoneMath == null && ifModifiedSince == null) {
			   System.out.println("No If-Match nor If-Modified-Since sent by client");
			   //throw new 
		   }
		   Customer current = customerDB.get(id);
		   Customer customer = view.getTarget();
		   if (null == current) {
			   throw new NotFoundException();
		   }
		   EntityTag tag = new EntityTag(HttpUtil.hash(Algorithm.MD5, current));
		   Response.ResponseBuilder builder = request.evaluatePreconditions(current.getLastModifiedDate(), tag);

		   if (builder  == null) {
			   Customer updated = new Customer(current.getId(), customer.getFirstName(), customer.getLastName(),
				   customer.getStreet(), customer.getCity(), customer.getState(), customer.getZip(), 
				   customer.getCountry());
		   customerDB.put(id, updated);
		   return Response.noContent().build();
		   }
		   System.out.println("Preconditions not met!");
			 return builder.build();
		 }
	   
	   @GET
	   @Path("{id}")
	   @Produces("text/plain")
	   public String getCustomerString(@PathParam("id") long id) {
		   Customer current = customerDB.get(id);
		   if (null == current) {
			   throw new NotFoundException();
		   }
	      return current.toString();
	   }
	   
	   @GET
	   @Produces({"application/xml", "application/json"})
	   public Response getCustomers(@QueryParam("start") int start, @QueryParam("size") @DefaultValue("2") int size, @Context UriInfo uriInfo) {
	      UriBuilder builder = uriInfo.getAbsolutePathBuilder();
	      builder.queryParam("start", "{start}");
	      builder.queryParam("size", "{size}");

	      Response.ResponseBuilder responseBuilder;
	      Customers wrapper = new Customers();
	     synchronized (customerDB) {
	         int i = 0;
	         for (Customer customer : customerDB.values()) {
	            if (i >= start && i < start + size) {
	            	wrapper.list.add(customer);
	            }
	            i++;
	         }
	         responseBuilder = Response.ok(wrapper);
	         // next link
	         if (start + size < customerDB.size()) {
	            int next = start + size;
	            URI nextUri = builder.clone().build(next, size);
	            Link nextXMLLink = Link.fromUri(nextUri).rel("next").type("application/xml").build();
	            Link nextJSONLink = Link.fromUri(nextUri).rel("next").type("application/json").build();
	            responseBuilder.links(nextXMLLink, nextJSONLink);
	          }
	         // previous link
	         if (start > 0) {
	            int previous = start - size;
	            if (previous < 0) {
	            		previous = 0;
	            }
	            URI previousUri = builder.clone().build(previous, size);
	            Link XMLLink = Link.fromUri(previousUri).rel("previous").type("application/xml").build();
	            Link JSONLink = Link.fromUri(previousUri).rel("previous").type("application/json").build();
	            responseBuilder.links(XMLLink, JSONLink);
	         }
	      }
	     return responseBuilder.build();
	   }
	   
	   
	   
	   
	   
	   
	   
	
}
