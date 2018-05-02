package com.restful.shop.resouces;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.springframework.transaction.annotation.Transactional;

import com.restful.shop.persistense.OrderEntity;
import com.restful.shop.persistense.OrdersEntity;

@Path("/dbOrders")
@Transactional
public class OrderResourse {

	@PersistenceContext
	   private EntityManager em;

	public static void addPurgeLinkHeader(UriInfo uriInfo, Response.ResponseBuilder builder) {
	      UriBuilder absolute = uriInfo.getAbsolutePathBuilder();
	      URI purgeUri = absolute.clone().path("purge").build();
	      builder.link(purgeUri, "purge");
	   }

	@POST
	   @Consumes("application/xml")
	   public Response createOrder(OrderEntity order, @Context UriInfo uriInfo) {
	      OrderEntity entity = new OrderEntity();
	      em.persist(entity);
	      em.flush();
	      System.out.println("Created order " + entity);
	      UriBuilder builder = uriInfo.getAbsolutePathBuilder();
	      builder.path(Integer.toString(entity.getId()));
	      return Response.created(builder.build()).build();
	}

	@POST
	   @Path("purge")
	   public void purgeOrders() {
	      System.out.println(em.createQuery("delete from PurchaseOrder o where o.cancelled = true")
	    		  .executeUpdate() + " - orders are purged");
	   }

	@HEAD
	   @Produces("application/xml")
	   public Response getOrdersHeaders(@Context UriInfo uriInfo) {
	      Response.ResponseBuilder builder = Response.ok();
	      builder.type("application/xml");
	      addPurgeLinkHeader(uriInfo, builder);
	      return builder.build();
	   }

		@GET
	   @Produces("application/xml")
	   public Response getOrders(@QueryParam("start") int start,
			   @QueryParam("size") @DefaultValue("2") int size,
			   @Context UriInfo uriInfo) {
	      UriBuilder builder = uriInfo.getAbsolutePathBuilder();
	      builder.queryParam("start", "{start}");
	      builder.queryParam("size", "{size}");
	      Response.ResponseBuilder responseBuilder;
	      List orderEntities = em.createQuery("select p from PurchaseOrder p")
	              .setFirstResult(start)
	              .setMaxResults(size)
	              .getResultList();
OrdersEntity orders = new OrdersEntity();
	      orders.setList(orderEntities);
	      responseBuilder = Response.ok(orders);
	      
	      for (Object obj : orderEntities) {
	         OrderEntity order = (OrderEntity) obj;
	         URI self = uriInfo.getAbsolutePathBuilder().path(Integer.toString(order.getId())).build();
	         Link selfLink = Link.fromUri(self).rel("self").type("application/xml").build();
	         responseBuilder.links(selfLink);
	         if (!order.isCancelled()) {
	            URI cancel = uriInfo.getAbsolutePathBuilder().path(Integer.toString(order.getId())).path("cancel").build();
	            Link cancelLink = Link.fromUri(cancel).rel("cancel").type("application/xml").build();
	            responseBuilder.links(cancelLink);
	         }
	       }
	      
	    //next link
	         
	         if (start + size < orderEntities.size()) {
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

	   public static void addCancelHeader(UriInfo uriInfo, Response.ResponseBuilder builder)
	   {
	      UriBuilder absolute = uriInfo.getAbsolutePathBuilder();
	      URI cancelUri = absolute.clone().path("cancel").build();
	      builder.link(cancelUri, "cancel");
	   }

	   @POST
	   @Path("{id}/cancel")
	   public void cancelOrder(@PathParam("id") int id) {
	      OrderEntity order = em.getReference(OrderEntity.class, id);
	      order.cancel(true);
	   }

	   @GET
	   @Path("{id}")
	   @Produces("application/xml")
	   public Response getOrder(@PathParam("id") int id,  @Context UriInfo uriInfo) {
	      OrderEntity order = em.getReference(OrderEntity.class, id);
	      Response.ResponseBuilder builder = Response.ok(order);
	      URI self = uriInfo.getAbsolutePathBuilder().build();
	      Link selfLink = Link.fromUri(self).rel("self").type("application/xml").build();
	      builder.links(selfLink);
	      
	      if (!order.isCancelled())  {
	    	  	addCancelHeader(uriInfo, builder);
	      }
	      return builder.build();
	   }
	   
	   @HEAD
	   @Path("{id}")
	   @Produces("application/xml")
	   public Response getOrderHeaders(@PathParam("id") int id, @Context UriInfo uriInfo) {
	      OrderEntity order = em.getReference(OrderEntity.class, id);
	      Response.ResponseBuilder builder = Response.ok();
	      builder.type("application/xml");
	      if (!order.isCancelled()) {
	    	  	addCancelHeader(uriInfo, builder);
	      } 
	      return builder.build();
	   }
	
}
