package com.restful.shop.test;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.specimpl.LinkBuilderImpl;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.restful.shop.domain.Customer;
import com.restful.shop.domain.Customers;
import com.restful.shop.domain.LastView;
import com.restful.shop.domain.LastViewed;
import com.restful.shop.handlers.JacksonXMLMarshaller;
import com.restful.shop.handlers.JacksonXMLResolver;
import com.restful.shop.handlers.JacksonXMLUnmarshaller;
import com.restful.shop.handlers.JavaMarshaller;

public class JacksonCustomerResourceTest {

	public static final String BASE_URI = "http://localhost:8080/JAXRSExamples/api/";
	
	private static Client client;

	   @BeforeClass
	   public static void initClient() {
	      client = ClientBuilder.newClient();
	      client.register(new JacksonXMLResolver());
	      client.register(new JacksonXMLUnmarshaller());
	      client.register(new JacksonXMLMarshaller() );
	      client.register(JavaMarshaller.class);
	      
	   }

	   @AfterClass
	   public static void closeClient() {
	      client.close();
	   }
	  
	@Test
	public void testJacksonCustomerResource() {
		System.out.println("*** Create a new Customer ***");
		Customer customer = new Customer(1, "John", "Doe", "Main street", "Moscow", "Russia", "64128", "RU");
		Response response = client.target(BASE_URI + "jacksonCustomers").request().post(Entity.xml(customer));
		System.out.println(response.readEntity(String.class));
		System.out.println(response.getStatus());
		if (response.getStatus() != 201) {
			throw new RuntimeException("Failed to create");
		}
		String location = response.getLocation().toString();
		System.out.println("Location: " + location);
	    response.close();
	    
	    System.out.println("*** GET Created Customer **");
	    Customer entity = client.target(location).request().get(Customer.class);
	    System.out.println(customer);
	    
	    Customer updated = new Customer(entity.getId(), "Anna", "Lee", "Main street", "Moscow", "Russia", 
	    		"128256", "N/A");
	    response = client.target(location).request().put(Entity.xml(updated));
	    if (response.getStatus() != 204) {
	    		throw new RuntimeException("Failed to update");
	    }
	    
	    System.out.println("**** After Update ***");
	    customer = client.target(location).request().get(Customer.class);
	    System.out.println(customer);

	}
	@Test
	public void testSerializableCustomerResource() {
	     System.out.println("*** Create a new Customer ***");
		Customer customer = new Customer(1, "John", "Doe", "Serialized street", "Moscow", "Russia", "64128", "RU");
		Response response = client.target(BASE_URI + "jacksonCustomers").request().post(Entity.entity(customer, "application/example-java"));
		System.out.println(response.readEntity(String.class));
		System.out.println(response.getStatus());
		if (response.getStatus() != 201) {
			throw new RuntimeException("Failed to create");
		}
		String location = response.getLocation().toString();
		System.out.println("Location: " + location);
	    response.close();
	    
	    System.out.println("*** GET Created Customer **");
	    Customer entity = client.target(location).request().get(Customer.class);
	    System.out.println(entity);
	    
	    Customer updated = new Customer(entity.getId(), "Anna", "Lee", "Serialized avenue", "Moscow", "Russia", 
	    		"128256", "N/A");
	    response = client.target(location).request().put(Entity.entity(updated, "application/example-java"));
	    if (response.getStatus() != 204) {
	    		throw new RuntimeException("Failed to update");
	    }
	    
	    System.out.println("**** After Update ***");
	    customer = client.target(location).request().get(Customer.class);
	    System.out.println(customer);
		
	}
	
	@Test
	public void malformedRequest() {
	     try {
	         client.target(BASE_URI + "jacksonCustomers/-256").request().get(Customer.class);
	         System.out.println("Should never get here!");
	      }  catch (NotFoundException e) {
	         System.out.println("Caught error!");
	         String error = e.getResponse().readEntity(String.class);
	         System.out.println(error);
	      }
	}
	
	@Test
	public void testJsonRequests() throws Exception {
		System.out.println("*** Create a new Customer ***");
		Customer customer = new Customer(1, "John", "Doe", "Main street", "Moscow", "Russia", "64128", "RU");
		ObjectMapper m = new ObjectMapper();
		
		System.out.println(m.writeValueAsString(customer));		
		Response response = client.target(BASE_URI + "jacksonCustomers").request().post(Entity.entity(m.writeValueAsString(customer), MediaType.APPLICATION_JSON));
		
		System.out.println(response.readEntity(String.class));
		System.out.println(response.getStatus());
		if (response.getStatus() != 201) {
			throw new RuntimeException("Failed to create");
		}
		String location = response.getLocation().toString();
		System.out.println("Location: " + location);
	    response.close();
	    
	    System.out.println("*** GET XML Created Customer **");
	    String xml = client.target(location).request().accept(MediaType.APPLICATION_XML_TYPE).get(String.class);
	    System.out.println(xml);

	     System.out.println("*** GET JSON Created Customer **");
	     String json = client.target(location).request().accept(MediaType.APPLICATION_JSON_TYPE) .get(String.class);
	      System.out.println(json);
	      
	      System.out.println("*** GET TEXT Created Customer **");
		  String plainText = client.target(location).request().accept(MediaType.TEXT_PLAIN) .get(String.class);
		  System.out.println(plainText);
	}
	
	   @Test
	   public void testQueryCustomers() throws Exception {
	      URI uri = new URI(BASE_URI+"jacksonCustomers");
	      
	      while (uri != null) {
	         Response response = client.target(uri).request().accept(MediaType.APPLICATION_XML).get();
	         String output = response.readEntity( String.class);
	         System.out.println("** XML from " + uri.toString());
	         System.out.println(output);
	         System.out.println("HEADERS:\n" + response.getStringHeaders().toString());
	         
	         Customers customers = client.target(uri).request().get(Customers.class);
	         customers.list.forEach(System.out::println);
	         
	         Set<Link> links =  response.getLinks();
	         
	         for (Link l : links) {
	        	 		System.out.println(l.toString());
	        	 		if (l.getRel().equals("next") && l.getType().equals(MediaType.APPLICATION_XML)) {
	        	 			uri = l.getUri();
	        	 			break;
	        	 		}
	        	 		uri  = null;
	        	 	}
	         
	      }
	   }
	   
	   @Test
	   public void testConditionalGetAndConcurrentUpdates() {
		   WebTarget customerTarget = client.target(BASE_URI+"jacksonCustomers/last/1");
		      Response response = customerTarget.request().get();
		      Assert.assertEquals(200, response.getStatus());
		      LastViewed<Customer> cust = response.readEntity(new GenericType<LastViewed<Customer>>() { });
		      System.out.println(cust);

		      EntityTag etag = response.getEntityTag();
		      String lastModdified = response.getHeaderString("Last-Modified");
		      response.close();

		      System.out.println("Doing a conditional GET with ETag: " + etag.toString() + " and Last-Modified: "+lastModdified);
		      response = customerTarget.request().header("If-None-Match", etag).header("If-Modified-Since", lastModdified).get();
		      Assert.assertEquals(304, response.getStatus());
		      response.close();

		      // Update and send a bad etag with conditional PUT
			Customer customer = new Customer(1, "John", "Bedford", "Serialized street", "Moscow", "Russia", "64128", "RU");
			LastViewed<Customer> viewed = new LastViewed<>(customer, cust.getPeople());
		      response = customerTarget.request().header("If-Match", "\"OMG\"").header("If-Modified-Since", "LOL").put(Entity.xml(viewed));
		      Assert.assertEquals(412, response.getStatus());
		      response.close();
	   }
	
	   @Test
	   public void testMaxAgeAnnotation() {
		   System.out.println("*** Create a new Customer ***");
		   Customer customer = new Customer(9001, "Joe", "Cache", "Cache street", "Moscow", "Russia", "64128", "RU");
		   Response response = client.target(BASE_URI+"jacksonCustomers").request().post(Entity.xml(customer));
		      if (response.getStatus() != 201) {
		    	  	throw new RuntimeException("Failed to create");
		      }
		      String location = response.getLocation().toString();
		      System.out.println("Location: " + location);
		      response.close();

		      System.out.println("*** GET Created Customer **");
		      response = client.target(location).request().get();
		      CacheControl cc = CacheControl.valueOf(response.getHeaderString(HttpHeaders.CACHE_CONTROL));
		      System.out.println("Max age: " + cc.getMaxAge());
		      String md5 = response.getHeaderString("Content-MD5");
		      System.out.println("Content-MD5: " + md5);
	   }
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
}
