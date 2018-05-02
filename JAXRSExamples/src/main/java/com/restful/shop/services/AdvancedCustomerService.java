package com.restful.shop.services;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

import com.restful.shop.domain.Customer;

@Path("paramCustomers")
public class AdvancedCustomerService {

	private Map<Long, Customer> customerDB = new ConcurrentHashMap<>();
	
	public AdvancedCustomerService() {
		long id = 1;
		customerDB.put(id, new Customer(id, "Bill", "Burke", "263 Clarendon Street", 
				"Boston", "MA", "02115", "USA"));
		customerDB.put(++id, new Customer(id, "Joe", "Burke", "263 Clarendon Street", 
				"Boston", "MA", "02115", "USA"));
		customerDB.put(++id, new Customer(id, "Monica", "Burke", "263 Clarendon Street", 
				"Boston", "MA", "02115", "USA"));
		customerDB.put(++id, new Customer(id, "Steve", "Burke", "263 Clarendon Street", 
				"Boston", "MA", "02115", "USA"));
	}
	
	@GET
	@Produces("application/xml")
	public StreamingOutput getCustomers(final @QueryParam("start") int start, 
							           final @QueryParam("size") @DefaultValue("2") int size) {
		return (OutputStream outputStream) -> {
			PrintStream writer = new PrintStream(outputStream, false);
            writer.println("<customers>");
            synchronized (customerDB) {
            		int i = 0;
            		for (Customer value : customerDB.values()) {
            			if (i >= start && i < start + size) {
            				outputCustomer("   ", writer, value);
            			}
            			i++;
            		}
            		
            }
            writer.println("</customers>");
            writer.flush();
		};
	}
	
	@GET
	@Produces("application/xml")
	@Path("uriinfo")
	public StreamingOutput getCustomers(@Context UriInfo info) {
	      int start = 0;
	      int size = 2;
	      if (info.getQueryParameters().containsKey("start")) {
	         start = Integer.valueOf(info.getQueryParameters().getFirst("start"));
	      }
	      if (info.getQueryParameters().containsKey("size")) {
	         size = Integer.valueOf(info.getQueryParameters().getFirst("size"));
	      }
	      return getCustomers(start, size);
	}
	
	   protected void outputCustomer(String indent, PrintStream writer, Customer cust) throws IOException
	   {
	      writer.println(indent + "<customer id=\"" + cust.getId() + "\">");
	      writer.println(indent + "   <first-name>" + cust.getFirstName() + "</first-name>");
	      writer.println(indent + "   <last-name>" + cust.getLastName() + "</last-name>");
	      writer.println(indent + "   <street>" + cust.getStreet() + "</street>");
	      writer.println(indent + "   <city>" + cust.getCity() + "</city>");
	      writer.println(indent + "   <state>" + cust.getState() + "</state>");
	      writer.println(indent + "   <zip>" + cust.getZip() + "</zip>");
	      writer.println(indent + "   <country>" + cust.getCountry() + "</country>");
	      writer.println(indent + "</customer>");
	   }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
