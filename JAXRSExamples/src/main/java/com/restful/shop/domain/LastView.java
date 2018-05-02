package com.restful.shop.domain;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="last-view")
public class LastView  implements Serializable  {

	private final String when;
	private final Customer who;
	
	@JsonCreator
	public LastView(@JsonProperty("when") String when, @JsonProperty("who") Customer who) {
		this.when = when.toString();
		this.who = who;
	}

	public String getWhen() {
		return when;
	}

	public Customer getWho() {
		return who;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LastView [when=");
		builder.append(when);
		builder.append(", who=");
		builder.append(who);
		builder.append("]");
		return builder.toString();
	}
	
	
	
	
}
