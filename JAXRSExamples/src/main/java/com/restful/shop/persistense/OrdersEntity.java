package com.restful.shop.persistense;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.restful.shop.domain.Order;
import com.restful.shop.domain.Orders;

@JacksonXmlRootElement(localName = "orders")
public class OrdersEntity {

	private List<OrderEntity> list;
	
	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "order")
	public List<OrderEntity> getOrders() {
		return Collections.unmodifiableList(list);
	}

	public List<OrderEntity> getList() {
		return list;
	}

	public void setList(List<OrderEntity> list) {
		this.list = list;
	}
}
