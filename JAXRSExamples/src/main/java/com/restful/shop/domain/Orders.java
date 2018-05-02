package com.restful.shop.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "orders")
public class Orders {

	private final List<Order> list;
	
	public Orders() {
		list = new ArrayList<>();
	}
	
	public Orders addAllCollection(Collection<Order> list) {
		list.addAll(list);
		return this;
	}
	
	public Orders add(Order order) {
		list.add(order);
		return this;
	}
	
	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "order")
	public List<Order> getOrders() {
		return Collections.unmodifiableList(list);
	}
	
}
