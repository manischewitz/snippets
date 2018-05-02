package com.restful.shop.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;
import javax.xml.crypto.Data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.restful.shop.domain.Customer;
import com.restful.shop.domain.LastViewed;

@Provider
@Produces("application/xml")
public class JacksonXMLMarshaller  implements MessageBodyWriter<Object> {

	@Context 
	private Providers providers;
	
	public JacksonXMLMarshaller (@Context Providers providers) {
		this.providers = providers;
	}
	
	public JacksonXMLMarshaller () { }
	
	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return type.isAnnotationPresent(JacksonXmlRootElement.class) || Collection.class.isAssignableFrom(type);
	}

	@Override
	public void writeTo(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
			throws IOException, WebApplicationException {
		ContextResolver<ObjectMapper> resolver = providers.getContextResolver(ObjectMapper.class, mediaType);
		if (resolver != null) {
			
			resolver.getContext(type).writeValue(entityStream, t);
		} else  {
			throw new RuntimeException();
		}
	}

}
