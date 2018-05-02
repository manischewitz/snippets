package com.restful.shop.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class CustomerServiceTest {

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
	public void testCustomerResource() throws Exception {
		Client client = ClientBuilder.newClient();
		try {
	         System.out.println("*** Create a new Customer ***");

	         String xml = "<customer>"
	                 + "<first-name>Bill</first-name>"
	                 + "<last-name>Burke</last-name>"
	                 + "<street>256 Clarendon Street</street>"
	                 + "<city>Boston</city>"
	                 + "<state>MA</state>"
	                 + "<zip>02115</zip>"
	                 + "<country>USA</country>"
	                 + "</customer>";
	         Response response = client.target("http://localhost:8080/JAXRSExamples/api/customers")
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
	         
	         String updateCustomer = "<customer>"
	                 + "<first-name>William</first-name>"
	                 + "<last-name>Burke</last-name>"
	                 + "<street>256 Clarendon Street</street>"
	                 + "<city>Boston</city>"
	                 + "<state>MA</state>"
	                 + "<zip>02115</zip>"
	                 + "<country>USA</country>"
	                 + "</customer>";
	         response = client.target(location).request().put(Entity.xml(updateCustomer));
	         if (response.getStatus() != 204) {
	        	 	throw new RuntimeException("Failed to create");
	         }
	         response.close();
	         System.out.println("**** After Update ***");
	         customer = client.target(location).request().get(String.class);
	         System.out.println(customer);
	         
	         System.out.println("**** Patch Update ***");
	         String patchCustomer = "<customer>"
	                 + "<first-name>Walt</first-name>"
	                 + "</customer>";
	         response = client.target(location).request().method("PATH", Entity.xml(patchCustomer));
	         if (response.getStatus() != 204) {
	        	 	throw new RuntimeException("Failed to patch");
	         }
	         response.close();
	         
	         customer = client.target(location).request().get(String.class);
	         System.out.println(customer);
	         
	         System.out.println("**** Use first-name ***");
	         customer = client.target("http://localhost:8080/JAXRSExamples/api/customers/Walt-Burke").request().get(String.class);
	         System.out.println(customer);
		} finally {
			client.close();
		}
	} 
	
}
