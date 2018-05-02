package com.restful.shop.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;
import javax.xml.crypto.Data;

import org.glassfish.hk2.utilities.reflection.ParameterizedTypeImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.restful.shop.domain.Customer;
import com.restful.shop.domain.LastViewed;

@Provider
@Consumes("application/xml")
public class JacksonXMLUnmarshaller implements MessageBodyReader<Object>{

	@Context 
	private Providers providers;
	
	public  JacksonXMLUnmarshaller (@Context Providers providers) {
		this.providers = providers;
	}
	public  JacksonXMLUnmarshaller () { }
	
	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return type.isAnnotationPresent(JacksonXmlRootElement.class) || Collection.class.isAssignableFrom(type);
	}

	@Override
	public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {
		ContextResolver<ObjectMapper> resolver = providers.getContextResolver(ObjectMapper.class, mediaType);
		if (resolver != null) {
			
			if (type.equals(LastViewed.class) && genericType instanceof ParameterizedType) {
				ParameterizedType pti = ( ParameterizedType) genericType ;
				if (pti.getActualTypeArguments().length == 1 && pti.getActualTypeArguments()[0].equals(Customer.class))  {
					return resolver.getContext(type).readValue(entityStream, new TypeReference<LastViewed<Customer>>() {});
				}
			}
			return resolver.getContext(type).readValue(entityStream, type);
		} 
		throw new RuntimeException();
	}

	






}
