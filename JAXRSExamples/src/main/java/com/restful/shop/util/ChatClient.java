package com.restful.shop.util;

import java.util.Scanner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

public class ChatClient {
	
	public static final String BASE_URI = "http://localhost:8080/JAXRSExamples/api/";

	public static void main(String[] args) {
		try (Scanner in = new Scanner(System.in)) {
			System.out.println("Enter your name: ");
			final String name = in.nextLine();
			if (name == null || name.isEmpty()) {
				throw new RuntimeException("Invalid name.");
			}
			
			final Client client = new ResteasyClientBuilder().connectionPoolSize(3).build();
			final WebTarget target = client.target(BASE_URI + "chat");
			target.request().async().get(new InvocationCallback<Response>() {
	
				@Override
				public void completed(Response response) {
					Link next = response.getLink("next");
		            String message = response.readEntity(String.class);
		            System.out.print(">"+message+"\n");
		            System.out.print("> ");
		            client.target(next).request().async().get(this);
				}
	
				@Override
				public void failed(Throwable throwable) {
					 System.out.println("> Failed request with message " + throwable.getMessage());
				}
			});
		
			while (true) {
				System.out.print("> ");
				String message = in.nextLine();
				if (message.equals("!exit")) {
					break;
				}
				target.request().post(Entity.text(name + ": " + message));
			}
		} catch (Exception e) {
			System.out.println("Failed with message " + e.getMessage());
		}
	}

}
