package com.restful.shop.services;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.restful.shop.annotations.PATH;
import com.restful.shop.domain.Customer;

@Path("/customers")
public class CustomerResource {

	private Map<Long, Customer> customerDB = new ConcurrentHashMap<>();
	private AtomicLong idGenerator = new AtomicLong();
	
	public CustomerResource() { }
	
	@POST
	@Consumes("application/xml")
	public Response createCustomer(InputStream is) {
		Customer customer = readCustomer(is, null);
		customerDB.put(customer.getId(), customer);
		Logger.getGlobal().info("Created customer: "+customer);
		return Response.created(URI.create("customers/" + customer.getId())).build();
	}
	
	@GET
	@Path("{id}")
	@Produces("application/xml")
	public StreamingOutput getCustomer(@PathParam("id") long id) {
		final Customer customer = customerDB.get(id);
		System.out.println(id);
		if (customer == null) {
			throw new NotFoundException();
		}
		return (OutputStream outputStream) ->  {
			outputCustomer(outputStream, customer);
		};
	}
	
	@GET
	@Path("{first : [a-zA-Z]+}-{last:[a-zA-Z]+}")
	@Produces("application/xml")
	public StreamingOutput getCustomerFilstLast(@PathParam("first") String first, 
			@PathParam("last") String last) {
		for (Customer value : customerDB.values()) {
			if (value.getFirstName().equals(first) && value.getLastName().equals(last)) {
				return (OutputStream outputStream) ->  {
					outputCustomer(outputStream, value);
				};
			}
		}
		throw new NotFoundException();
	}
	
	@PUT
	@Path("{id}")
	@Consumes("application/xml")
	public void updateCustomer(@PathParam("id") long id, InputStream is) {
		final Customer current = customerDB.get(id);
		if (current == null) {
			throw new NotFoundException();
		}
		final Customer toUpdate = readCustomer(is, current);
		customerDB.put(id, toUpdate);
	}
	
	@PATH
	@Path("{id}")
	@Consumes("application/xml")
	public void pathCustomer(@PathParam("id") long id, InputStream is) {
		System.out.println("IN PATCH method");
		this.updateCustomer(id, is);
	}

	private void outputCustomer(OutputStream outputStream, Customer cust) {
		PrintStream writer = new PrintStream(outputStream, false);
	    writer.println("<customer id=\"" + cust.getId() + "\">");
	    writer.println("   <first-name>" + cust.getFirstName() + "</first-name>");
	    writer.println("   <last-name>" + cust.getLastName() + "</last-name>");
	    writer.println("   <street>" + cust.getStreet() + "</street>");
	    writer.println("   <city>" + cust.getCity() + "</city>");
	    writer.println("   <state>" + cust.getState() + "</state>");
	    writer.println("   <zip>" + cust.getZip() + "</zip>");
	    writer.println("   <country>" + cust.getCountry() + "</country>");
	    writer.println("</customer>");
	    writer.flush();
	}
	
	

	private Customer readCustomer(InputStream is, Customer old) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();                
			Document doc = builder.parse(is);
			Element root = doc.getDocumentElement();
			
			NodeList nodes = root.getChildNodes();
			Element element = null;
			long id = old != null ? old.getId() : idGenerator.incrementAndGet();
			String firstName = old != null ? old.getFirstName() : "";
			String lastName = old != null ? old.getLastName() : "";
			String street = old != null ? old.getStreet() : "";
			String city = old != null ? old.getCity() : "";
			String state = old != null ? old.getState() : "";
			String zip = old != null ? old.getZip() : "";
			String country = old != null ? old.getCountry() : "";
			
			for (int i = 0; i < nodes.getLength(); i++) {
				element = (Element) nodes.item(i);
	            if (element.getTagName().equals("first-name")) {
	            		firstName = element.getTextContent();
	            } else if (element.getTagName().equals("last-name")) {
	            		lastName = element.getTextContent();
	            } else if (element.getTagName().equals("street")) {
	            		street = element.getTextContent();
	            } else if (element.getTagName().equals("city")) {
	            		city = element.getTextContent();
	            } else if (element.getTagName().equals("state")) {
	            		state = element.getTextContent();
	            } else if (element.getTagName().equals("zip")) {
	            		zip = element.getTextContent();
	            } else if (element.getTagName().equals("country")) {
	            		country = element.getTextContent();
	            }
			}
			Customer customer = new Customer(id, firstName, lastName, street, city, state, zip, country);
			System.out.println(customer);
			return customer;
		} catch (Exception e) {
			throw new BadRequestException();
		}
	}

}
