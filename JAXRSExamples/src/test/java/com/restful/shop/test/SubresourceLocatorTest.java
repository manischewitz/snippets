package com.restful.shop.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class SubresourceLocatorTest {

	
	 private static Client client;

	   @BeforeClass
	   public static void initClient()
	   {
	      client = ClientBuilder.newClient();
	   }

	   @AfterClass
	   public static void closeClient()
	   {
	      client.close();
	   }

	   @Test
	   public void testCustomerResource() throws Exception
	   {
	      System.out.println("*** Create a new Message ***");

	      String xml = "<string>"
	              + "<message>HEllo Europe !!!(!)</message>"
	              + "</string>";

	      Response response = client.target("http://localhost:8080/JAXRSExamples/api/localDatabase/EUROPE-db")
	              .request().post(Entity.xml(xml));
	      if (response.getStatus() != 201) {
	    	  	throw new RuntimeException("Failed to create");
	      }
	      String location = response.getLocation().toString();
	      System.out.println("Location: " + location);
	      response.close();

	      System.out.println("*** GET Created Customer **");
	      String customer = client.target(location).request().get(String.class);
	      System.out.println(customer);
	   }

	   @Test
	   public void testFirstLastCustomerResource() throws Exception
	   {
	      System.out.println("*** Create a new message ***");

	      String xml = "<string by=\"John-Doe\">"
	              + "<message>Hello north AmErIcA!!(!!)</message>"
	              + "</string>";

	      Response response = 
	    		  client.target("http://localhost:8080/JAXRSExamples/api/localDatabase/NORTH_AMERICA-db")
	              .request().post(Entity.xml(xml));
	      if (response.getStatus() != 201) {
	    	  	throw new RuntimeException("Failed to create");
	      }
	      String location = response.getLocation().toString();
	      System.out.println("Location: " + location);
	      response.close();

	      System.out.println("*** GET Created Customer **");
	      String customer = 
	    		  client.target("http://localhost:8080/JAXRSExamples/api/localDatabase/NORTH_AMERICA-db/John-Doe")
	    		  .request().get(String.class);
	      System.out.println(customer);
	   }
}
