package com.restful.shop.domain;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@SuppressWarnings("serial")
@JacksonXmlRootElement(localName="last-viewed")
public class LastViewed<T> implements Serializable {

	private final T target;
	private final List<LastView> people;
	
	@JsonCreator
	public LastViewed(@JsonProperty("target") T target, @JsonProperty("people") List<LastView> people) {
		this.target = target;
		this.people = people;
	}

	public T getTarget() {
		return target;
	}
	
	@JacksonXmlElementWrapper(useWrapping = false)
	public List<LastView> getPeople() {
		return Collections.unmodifiableList(people);
	}
	
	public static void main(String...strings) throws Exception {
		ObjectMapper m = new XmlMapper();
		m.enable(SerializationFeature.INDENT_OUTPUT);
		Customer customer = new Customer(1, "John", "Doe", "Main street", "Moscow", "Russia", "64128", "RU");
		Customer target = new Customer(2, "Anna", "Lee", "Main street", "Moscow", "Russia", "64128", "RU");
		
		LastView entity = new LastView(LocalDate.now().toString(), customer);
		LastViewed<Customer> lv = new LastViewed<>(target, new ArrayList<LastView>() { { add(entity ); } });
		System.out.println(m.writeValueAsString(lv));
		System.out.println(m.readValue(m.writeValueAsString(lv), LastViewed.class));
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LastViewed [target=");
		builder.append(target);
		builder.append(", people=");
		builder.append(people);
		builder.append("]");
		return builder.toString();
	}
	
}
