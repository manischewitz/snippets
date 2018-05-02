package com.restful.shop.persistense;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="customers")
public class CustomersEntity {

	private List<CustomerEntity> list;

	public List<CustomerEntity> getList() {
		return list;
	}
	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "customer")
	public void setList(List<CustomerEntity> list) {
		this.list = list;
	}
	
}
