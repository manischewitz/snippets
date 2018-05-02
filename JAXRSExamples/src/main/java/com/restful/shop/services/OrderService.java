package com.restful.shop.services;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.restful.shop.domain.Order;
import com.restful.shop.domain.Orders;

@Path("/orders")
public class OrderService {

	   private Map<Long, Order> orderDB = new ConcurrentHashMap<>();
	   private AtomicLong idCounter = new AtomicLong();
	   
	   @POST
	   @Consumes("application/xml")
	   public Response createOrder(@Context UriInfo uriInfo, Order order) {
		   long id = idCounter.incrementAndGet();
		   Order newOrder = new Order(id, order.isCancelled(), order.getDate(), order.getCustomer(), order.getLineItems());
		   orderDB.put(id, newOrder);
		   System.out.println("Created order " + order);
		   UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		   builder.path(Long.toString(id));
		   return Response.created(builder.build()).build();
	   }
	   
	   @GET
	   @Path("{id}")
	   @Produces("application/xml")
	   public Response getOrder(@PathParam("id") long id, @Context UriInfo uriInfo) {
		   Order order = orderDB.get(id);
		   if (order == null) {
			   throw new NotFoundException();
		   }
		   Response.ResponseBuilder builder = Response.ok(order);
		   if (!order.isCancelled()) {
			   addCancelHeader(uriInfo, builder);
		   }
		   return builder.build();
		}

	private void addCancelHeader(UriInfo uriInfo, ResponseBuilder builder) {
		 UriBuilder absolute = uriInfo.getAbsolutePathBuilder();
	     URI cancelUrl = absolute.clone().path("cancel").build();
	     builder.links(Link.fromUri(cancelUrl).rel("cancel").build());
	}
	   
	 @POST
	 @Path("{id}/cancel")
	 public void cancelOrder(@PathParam("id") long id) {
	      Order order = orderDB.get(id);
	      if (order == null) {
	         throw new NotFoundException();
	      }
	      orderDB.put(id, order.cancel());
	   }


	   @HEAD
	   @Path("{id}")
	   @Produces("application/xml")
	   public Response getOrderHeaders(@PathParam("id") long id, @Context UriInfo uriInfo) {
	      Order order = orderDB.get(id);
	      if (order == null) {
		         throw new NotFoundException();
		   }
	      Response.ResponseBuilder builder = Response.ok();
	      builder.type("application/xml");
	      if (!order.isCancelled()) {
	    	  	addCancelHeader(uriInfo, builder);
	      }
	      return builder.build();
	   }

	   @GET
	   @Produces("application/xml")
	   public Response getOrders(@QueryParam("start") int start, @QueryParam("size") @DefaultValue("2") int size, @Context UriInfo uriInfo) {
	      UriBuilder builder = uriInfo.getAbsolutePathBuilder();
	      builder.queryParam("start", "{start}");
	      builder.queryParam("size", "{size}");
	      
	      Orders orders = new Orders();
	      Response.ResponseBuilder responseBuilder;
	      synchronized (orderDB) {
	         int i = 0;
	         for (Order order : orderDB.values()) {
	            if (i >= start && i < start + size) {
	            		orders.add(order);
	            }
	            i++;
	         }
	         //next link
	         responseBuilder = Response.ok(orders);
	         if (start + size < orderDB.size()) {
	            int next = start + size;
	            URI nextUri = builder.clone().build(next, size);
	            Link nextLink = Link.fromUri(nextUri).rel("next").type("application/xml").build();
	            responseBuilder.links(nextLink);
	         }
	         // previous link
	         if (start > 0) {
	            int previous = start - size;
	            if (previous < 0) previous = 0;
	            URI previousUri = builder.clone().build(previous, size);
	            Link previousLink = Link.fromUri(previousUri).rel("previous").type("application/xml").build();
	            responseBuilder.links(previousLink);
	         }
	          addPurgeLinkHeader(uriInfo, responseBuilder);
	          return responseBuilder.build();
	      }
	   }

	   private void addPurgeLinkHeader(UriInfo uriInfo, Response.ResponseBuilder builder) {
	      UriBuilder absolute = uriInfo.getAbsolutePathBuilder();
	      URI purgeUri = absolute.clone().path("purge").build();
	      builder.links(Link.fromUri(purgeUri).rel("purge").build());
	   }

	   @POST
	   @Path("purge")
	   public void purgeOrders() {
	      synchronized (orderDB) {
	    	  	 List<Order> orders = new ArrayList<Order>();
	         orders.addAll(orderDB.values());
	         for (Order order : orders)	{
	            if (order.isCancelled()) {
	               orderDB.remove(order.getId());
	            }
	         }
	      }
	   }

	   @HEAD
	   @Produces("application/xml")
	   public Response getOrdersHeaders(@QueryParam("start") int start, @QueryParam("size") @DefaultValue("2") int size, @Context UriInfo uriInfo) {
	      Response.ResponseBuilder responseBuilder = Response.ok();
	      responseBuilder.type("application/xml");
	      
	      UriBuilder builder = uriInfo.getAbsolutePathBuilder();
	      builder.queryParam("start", "{start}");
	      builder.queryParam("size", "{size}");
	      
	      synchronized (orderDB) {
		         //next link
		         if (start + size < orderDB.size()) {
		            int next = start + size;
		            URI nextUri = builder.clone().build(next, size);
		            Link nextLink = Link.fromUri(nextUri).rel("next").type("application/xml").build();
		            responseBuilder.links(nextLink);
		         }
		         // previous link
		         if (start > 0) {
		            int previous = start - size;
		            if (previous < 0) previous = 0;
		            URI previousUri = builder.clone().build(previous, size);
		            Link previousLink = Link.fromUri(previousUri).rel("previous").type("application/xml").build();
		            responseBuilder.links(previousLink);
		         }
	      }
	      addPurgeLinkHeader(uriInfo, responseBuilder);
	      return responseBuilder.build();
	   }
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	
}
