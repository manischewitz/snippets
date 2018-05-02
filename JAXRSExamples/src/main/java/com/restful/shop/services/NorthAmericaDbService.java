package com.restful.shop.services;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class NorthAmericaDbService {

	private Map<String, String> europeDB = new ConcurrentHashMap<>();
	
	@POST
	@Consumes("application/xml")
	public Response createCustomer(InputStream is) {
		String[] message = readCustomer(is);
		europeDB.put(message[0], message[1]);
		Logger.getGlobal().info("Created message: " + Arrays.toString(message));
		return Response.created(URI.create("localDatabase/NORTH_AMERICA/" + message[0])).build();
	}
	
	private String[] readCustomer(InputStream is) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();                
			Document doc = builder.parse(is);
			Element root = doc.getDocumentElement();
			
			NodeList nodes = root.getChildNodes();
			Element element = null;
			String[] pair = new String[2];
			pair[0] = root.getAttribute("by");
			
			for (int i = 0; i < nodes.getLength(); i++) {
				element = (Element) nodes.item(i);
	            if (element.getTagName().equals("message")) {
	            		 pair[1] = element.getTextContent();
	            }
			}
			
			return pair;
		} catch (Exception e) {
			throw new BadRequestException();
		}
	}
	
	@GET
	@Path("{first : [a-zA-Z]+}-{last:[a-zA-Z]+}")
	@Produces("application/xml")
	public StreamingOutput getCustomer(@PathParam("first") String first, 
			@PathParam("last") String last) {
		String id = first + "-" + last;
		final String message = europeDB.get(id);
		System.out.println(id);
		if (message == null) {
			throw new NotFoundException();
		}
		return (OutputStream outputStream) ->  {
			PrintStream writer = new PrintStream(outputStream, false);
			writer.println("<string by=\"" + id + "\">");
		    writer.println("   <message>" + message + "</message>");
		    writer.println("</string>");
		    writer.flush();
		};
	}
	
}
