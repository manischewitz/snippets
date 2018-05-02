package com.restful.shop.domain;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="chat-message")
public class ChatMessage {

	private final long id;
	private final String message;
	private final long date;
	
	@JsonCreator
	private ChatMessage(@JsonProperty("id") long id, @JsonProperty("message") String message, @JsonProperty("date") long date) {
		this.id = id;
		this.message = message;
		this.date = date;
	}
	
	public ChatMessage(long id, String message) {
		this(id, message, Instant.now().toEpochMilli());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ChatMessage [id=");
		builder.append(id);
		builder.append(", message=");
		builder.append(message);
		builder.append(", date=");
		builder.append(date);
		builder.append("]");
		return builder.toString();
	}

	@JacksonXmlProperty(isAttribute = true)
	public long getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	public long getDate() {
		return date;
	}
	
}
