package com.restful.shop.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="customers")
public class Customers {
	
	public List<Customer> list = new ArrayList<Customer>();
    
	
	
}
