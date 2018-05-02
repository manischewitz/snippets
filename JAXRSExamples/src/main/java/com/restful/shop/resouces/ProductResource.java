package com.restful.shop.resouces;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
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

import com.restful.shop.persistense.ProductEntity;
import com.restful.shop.persistense.ProductsEntity;

@Path("/dbProducts")
@Transactional
public class ProductResource {
	
		@PersistenceContext
	   private EntityManager em;

		 @POST
		   @Consumes("application/xml")
	   public Response createProduct(ProductEntity product, @Context UriInfo uriInfo) {
	      ProductEntity entity = new ProductEntity();
	      em.persist(entity);
	      em.flush();

	      System.out.println("Created product " + entity);
	      UriBuilder builder = uriInfo.getAbsolutePathBuilder();
	      builder.path(Integer.toString(entity.getId()));
	      return Response.created(builder.build()).build();

	   }
		 
		 @GET
		   @Produces("application/xml")
	   public Response getProducts(@QueryParam("start") int start,
               @QueryParam("size") @DefaultValue("2") int size,
               @QueryParam("name") String name,
               @Context UriInfo uriInfo) {
	      UriBuilder builder = uriInfo.getAbsolutePathBuilder();
	      builder.queryParam("start", "{start}");
	      builder.queryParam("size", "{size}");
	      Response.ResponseBuilder responseBuilder;
	      
	      Query query = null;
	      if (name != null) {
	         query = em.createQuery("select p from Product p where p.name=:name");
	         query.setParameter("name", name);

	      } else {
	         query = em.createQuery("select p from Product p");
	      }
	      List productEntities = query.setFirstResult(start)
	              .setMaxResults(size)
	              .getResultList();
	      ProductsEntity pe = new ProductsEntity();
	      pe.setList(productEntities);
	      responseBuilder = Response.ok(pe);
	      
	      // next link
	      // If the size returned is equal then assume there is a next
	      if (productEntities.size() == size) {
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
	      return  responseBuilder.build();
	   }

		 @GET
		   @Path("{id}")
		   @Produces("application/xml")
	   public ProductEntity getProduct(@PathParam("id") int id){
	      return em.getReference(ProductEntity.class, id);
	   }

}
