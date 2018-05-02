package com.restful.shop.persistense;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "products")
public class ProductsEntity {

	private List<ProductEntity> list;

	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "product")
	public List<ProductEntity> getList() {
		return list;
	}

	public void setList(List<ProductEntity> list) {
		this.list = list;
	}
	
}
