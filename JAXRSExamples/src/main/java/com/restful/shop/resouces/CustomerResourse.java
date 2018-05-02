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

import com.restful.shop.domain.Customers;
import com.restful.shop.persistense.CustomerEntity;
import com.restful.shop.persistense.CustomersEntity;

@Path("/dbcustomers")
@Transactional
public class CustomerResourse {

	@PersistenceContext
	 private EntityManager em;
	
	@POST
	@Consumes("application/xml")
	 public Response createCustomer(CustomerEntity customer, @Context UriInfo uriInfo) {
	      em.persist(customer);
	      em.flush();

	      System.out.println("Created customer " + customer);
	      UriBuilder builder = uriInfo.getAbsolutePathBuilder();
	      builder.path(Long.toString(customer.getId()));
	      return Response.created(builder.build()).build();
	}

	@GET
	@Path("{id}")
	@Produces("application/xml")
	   public CustomerEntity getCustomer(@PathParam("id") int id) {
	      return em.getReference(CustomerEntity.class, id);
	   }

	@GET
	   @Produces("application/xml")
	public Response getCustomers(@QueryParam("start") int start,
			@QueryParam("size") @DefaultValue("2") int size,
			@QueryParam("firstName") String firstName,
			@QueryParam("lastName") String lastName,
			@Context UriInfo uriInfo) {
	      UriBuilder builder = uriInfo.getAbsolutePathBuilder();
	      builder.queryParam("start", "{start}");
	      builder.queryParam("size", "{size}");

	      //ArrayList<CustomerEntity> list = new ArrayList<>();
	      Response.ResponseBuilder responseBuilder;
	      Query query = null;
	      if (firstName != null && lastName != null) {
	         query = em.createQuery(
	        		 "select c from Customer c where c.firstName=:first and c.lastName=:last");
	         query.setParameter("first", firstName);
	         query.setParameter("last", lastName);

	      } else if (lastName != null) {
	         query = em.createQuery("select c from Customer c where c.lastName=:last");
	         query.setParameter("last", lastName);
	      } else {
	         query = em.createQuery("select c from Customer c");
	      }

	      @SuppressWarnings("unchecked")
		List<CustomerEntity> customerEntities = query.setFirstResult(start)
	              .setMaxResults(size)
	              .getResultList();
	      CustomersEntity ce = new CustomersEntity();
	      ce.setList(customerEntities);
	      responseBuilder = Response.ok(ce);
	      // next link
	         if (start + size < customerEntities.size()) {
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
	      
	      return responseBuilder.build();
	   }

}
