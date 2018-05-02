package com.restful.shop.test;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.restful.shop.domain.Customer;
import com.restful.shop.domain.LineItem;
import com.restful.shop.domain.Order;
import com.restful.shop.handlers.JacksonXMLMarshaller;
import com.restful.shop.handlers.JacksonXMLResolver;
import com.restful.shop.handlers.JacksonXMLUnmarshaller;

public class OrderServiceTest {

		private static Client client;

	   @BeforeClass
	   public static void initClient() {
	      client = ClientBuilder.newClient();
	      client.register(new JacksonXMLResolver());
	      client.register(new JacksonXMLUnmarshaller());
	      client.register(new JacksonXMLMarshaller() );
	   }

	   @AfterClass
	   public static void closeClient() {
	      client.close();
	   }
	   
	   @Test
	   public void testCreateCancelPurge() throws Exception {
		   Response response = client.target(JacksonCustomerResourceTest.BASE_URI+ "shop").request().head();
		   Link customers = response.getLink("customers");
		   Link orders = response.getLink("orders");
		   response.close();
		   
		   System.out.println("** Create a customer through this URL: " + customers.getUri().toString());
		   Customer customer = new Customer(1, "John", "Doe", "Main street", "Moscow", "Russia", "64128", "RU");
		   response = client.target(customers).request().post(Entity.xml(customer));
		   Assert.assertEquals(201, response.getStatus());
		   response.close();
		   
		   LineItem item = new LineItem(-1, "Soap", new BigDecimal(200.0));
		   Order order = new Order(-1, false, new Date().toString(), customer, new ArrayList<LineItem>() { { add(item); } });
		   
		   System.out.println("** Create an order through this URL: " + orders.getUri().toString());
		   response = client.target(orders).request().post(Entity.xml(order));
		   Assert.assertEquals(201, response.getStatus());
		   URI createdOrderUrl = response.getLocation();
		   response.close();
		   
		   System.out.println("** New list of orders");
		   response = client.target(orders).request().get();
		   String orderList = response.readEntity(String.class);
		   System.out.println(orderList);
		   Link purge = response.getLink("purge");
		   response.close();

		   response = client.target(createdOrderUrl).request().head();
		   Link cancel = response.getLink("cancel");
		   response.close();
		    if (cancel != null) {
		         System.out.println("** Canceling the order at URL: " + cancel.getUri().toString());
		         response = client.target(cancel).request().post(null);
		         Assert.assertEquals(204, response.getStatus());
		         response.close();
		     }

		     System.out.println("** New list of orders after cancel: ");
		     orderList = client.target(orders).request().get(String.class);
		     System.out.println(orderList);

		      System.out.println("** Purge cancelled orders at URL: " + purge.getUri().toString());
		      response = client.target(purge).request().post(null);
		      Assert.assertEquals(204, response.getStatus());
		      response.close();

		      System.out.println("** New list of orders after purge: ");
		      orderList = client.target(orders).request().get(String.class);
		      System.out.println(orderList);
		   
	   }
	
	   
	   
}
