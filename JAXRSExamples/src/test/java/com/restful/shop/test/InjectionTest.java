package com.restful.shop.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class InjectionTest {
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
	   public void testCarResource() throws Exception
	   {
	      System.out.println("**** CarResource Via @MatrixParam ***");
	      String car = client.target("http://localhost:8080/JAXRSExamples/api/cars/matrix/mercedes/e55;color=black/2006")
	    		  .request().get(String.class);
	      System.out.println(car);

	      System.out.println("**** CarResource Via PathSegment ***");
	      car = client.target("http://localhost:8080/JAXRSExamples/api/cars/segment/mercedes/e55;color=black/2006")
	    		  .request().get(String.class);
	      System.out.println(car);

	      System.out.println("**** CarResource Via PathSegments ***");
	      car = client.target("http://localhost:8080/JAXRSExamples/api/cars/segments/mercedes/e55/amg/year/2006")
	    		  .request().get(String.class);
	      System.out.println(car);

	      System.out.println("**** CarResource Via PathSegment ***");
	      car = client.target("http://localhost:8080/JAXRSExamples/api/cars/uriinfo/mercedes/e55;color=black/2006")
	    		  .request().get(String.class);
	      System.out.println(car);
	   }

	   @Test
	   public void testCustomerResource() throws Exception
	   {
	      System.out.println("**** CustomerResource No Query params ***");
	      String customer = client.target("http://localhost:8080/JAXRSExamples/api/paramCustomers").request().get(String.class);
	      System.out.println(customer);

	      System.out.println("**** CustomerResource With Query params ***");
	      String list = client.target("http://localhost:8080/JAXRSExamples/api/paramCustomers")
	                          .queryParam("start", "1")
	                          .queryParam("size", "3")
	                          .request().get(String.class);
	      System.out.println(list);

	      System.out.println("**** CustomerResource With UriInfo and Query params ***");
	      list = client.target("http://localhost:8080/JAXRSExamples/api/paramCustomers/uriinfo")
	                   .queryParam("start", "2")
	                   .queryParam("size", "2")
	                   .request().get(String.class);
	   }
}
