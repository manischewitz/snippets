package com.restful.shop.handlers;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Provider
@Produces("application/xml")
public class JacksonXMLResolver implements ContextResolver<ObjectMapper> {

	private ObjectMapper mapper;
	
	public JacksonXMLResolver() {
		mapper = new XmlMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
	}
	
	@Override
	public ObjectMapper getContext(Class<?> type) {
		return mapper;
	}

}
