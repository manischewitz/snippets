package com.restful.shop.services;

import java.net.URI;
import java.util.Date;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.client.Entity;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.restful.shop.domain.ChatMessage;

@Path("/chat")
public class ChatService {

	public static final int MAX_CHAT_MESSAGES = 10;
	private final Queue<ChatMessage> messagesDB = new ArrayBlockingQueue<>(MAX_CHAT_MESSAGES, false);
	private AtomicLong counter = new AtomicLong(0);
	private List<AsyncResponse> listeners = new LinkedList<AsyncResponse>();
	private ExecutorService writer = Executors.newSingleThreadExecutor();
	
	@Context
	private UriInfo uriInfo;
	
	@POST
	@Consumes("text/plain")
	public void post(String message) {
		final UriBuilder base = uriInfo.getBaseUriBuilder();
		writer.execute(() -> {
			synchronized(messagesDB) {
				long id = counter.incrementAndGet();
				ChatMessage created = new ChatMessage(id, message);
				if (messagesDB.size() == MAX_CHAT_MESSAGES) {
					messagesDB.poll();
				}
				messagesDB.add(created);
				listeners.forEach((AsyncResponse async) -> {
					try {
						send(base, async, created);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
				listeners.clear();
			}
		});
	}
	
	@GET
	public void receive(@PathParam("current") long nextId, @Suspended AsyncResponse async) {
		final UriBuilder base = uriInfo.getBaseUriBuilder();
		ChatMessage message = null;
		synchronized(messagesDB) {
			Iterator<ChatMessage> iterator = messagesDB.iterator();
			while (iterator.hasNext()) {
				ChatMessage next = iterator.next();
				if (next.getId() == nextId && iterator.hasNext()) {
					message = iterator.next();
					break;
				}
			}
			if (message == null) {
				queue(async);
			}
		}
		if (message != null) {
			send(base, async, message);
		}
	}

	private void queue(AsyncResponse async) {
		 listeners.add(async);
	}

	private void send(UriBuilder base, AsyncResponse async, ChatMessage created) {
		URI nextUri = base.clone().path(ChatService.class).queryParam("current", created.getId()).build();
		Link next = Link.fromUri(nextUri).rel("next").build();
		Response response = Response.ok(created.toString()).links(next).build();
		async.resume(response);
		
	}
	
	
	
}
