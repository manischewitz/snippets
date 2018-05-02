package com.restful.shop.test;

import java.math.BigDecimal;
import java.net.URI;
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

import com.restful.shop.handlers.JacksonXMLMarshaller;
import com.restful.shop.handlers.JacksonXMLResolver;
import com.restful.shop.handlers.JacksonXMLUnmarshaller;
import com.restful.shop.persistense.CustomerEntity;
import com.restful.shop.persistense.CustomersEntity;
import com.restful.shop.persistense.LineItemEntity;
import com.restful.shop.persistense.OrderEntity;
import com.restful.shop.persistense.ProductEntity;
import com.restful.shop.persistense.ProductsEntity;

public class DatabaseBasedResourcesTest {
	/* private static Client client;

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
	   
	   

	   protected void populateDB() throws Exception {
	      Response response = client.target(JacksonCustomerResourceTest.BASE_URI + 
	    		  "dbShop").request().head();
	      Link products = response.getLink("products");
	      response.close();

	      System.out.println("** Populate Products");

	      ProductEntity product = new ProductEntity();
	      product.setName("iPhone");
	      product.setCost(new BigDecimal(199.99));
	      response = client.target(products).request().post(Entity.xml(product));
	      Assert.assertEquals(201, response.getStatus());
	      response.close();

	      product = new ProductEntity();
	      product.setName("MacBook Pro");
	      product.setCost(new BigDecimal(3299.99));
	      response = client.target(products).request().post(Entity.xml(product));
	      Assert.assertEquals(201, response.getStatus());
	      response.close();

	      product = new ProductEntity();
	      product.setName("iPod");
	      product.setCost(new BigDecimal(49.99));
	      response = client.target(products).request().post(Entity.xml(product));
	      Assert.assertEquals(201, response.getStatus());
	      response.close();
	   }

	   @Test
	   public void testCreateOrder() throws Exception {
	      populateDB();
	      Response response = client.target(JacksonCustomerResourceTest.BASE_URI  + "dbShop")
	    		  .request().head();
	      Link customers = response.getLink("customers");
	      Link products = response.getLink("products");
	      Link orders = response.getLink("orders");
	      response.close();

	      System.out.println("** Buy an iPhone for Bill Burke\n");
	      System.out.println("** First see if Bill Burke exists as a customer");
	      CustomersEntity custs = client.target(customers)
	                                  .queryParam("firstName", "Bill")
	                                  .queryParam("lastName", "Burke")
	                                  .request().get(CustomersEntity.class);
	      CustomerEntity customer = null;
	      if (custs.getList().size() > 0) {
	         System.out.println("- Found a Bill Burke in the database, using that");
	         customer = custs.getList().iterator().next();
	      } else {
	         System.out.println("- Cound not find a Bill Burke in the database, creating one.");
	         customer = new CustomerEntity();
	         customer.setFirstName("Bill");
	         customer.setLastName("Burke");
	         customer.setStreet("222 Dartmouth Street");
	         customer.setCity("Boston");
	         customer.setState("MA");
	         customer.setZip("02115");
	         customer.setCountry("USA");
	         response = client.target(customers).request().post(Entity.xml(customer));
	         Assert.assertEquals(201, response.getStatus());
	         URI uri = response.getLocation();
	         response.close();
	         
	         customer = client.target(uri).request().get(CustomerEntity.class);
	      }

	      System.out.println();
	      System.out.println("Search for iPhone in the Product database");
	      ProductsEntity prods = client.target(products)
	                             .queryParam("name", "iPhone")
	                             .request()
	                             .get(ProductsEntity.class);
	      ProductEntity product = null;
	      if (prods.getList().size() > 0)
	      {
	         System.out.println("- Found iPhone in the database.");
	         product = prods.getList().iterator().next();
	      } else {
	         throw new RuntimeException("Failed to find an iPhone in the database!");
	      }

	      System.out.println();
	      System.out.println("** Create Order for iPhone");
	      LineItemEntity item = new LineItemEntity();
	      item.setProduct(product);
	      item.setQuantity(1);
	      OrderEntity order = new OrderEntity();
	      order.setTotal(product.getCost());
	      order.setCustomer(customer);
	      order.setDate(new Date().getTime());
	      order.getLineItems().add(item);
	      response = client.target(orders).request().post(Entity.xml(order));
	      Assert.assertEquals(201, response.getStatus());
	      response.close();

	      System.out.println();
	      System.out.println("** Show all orders.");
	      String xml = client.target(orders).request().get(String.class);
	      System.out.println(xml);


	   }*/
}
