package com.restful.shop.services;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.restful.shop.exceptions.EntityNotFoundExceptionMapper;
import com.restful.shop.exceptions.NegativeIdExceptionMapper;
import com.restful.shop.features.MaxAgeFeature;
import com.restful.shop.filters.ContentMD5Writer;
import com.restful.shop.handlers.JacksonXMLMarshaller;
import com.restful.shop.handlers.JacksonXMLResolver;
import com.restful.shop.handlers.JacksonXMLUnmarshaller;
import com.restful.shop.handlers.JavaMarshaller;
import com.restful.shop.resouces.CustomerResourse;
import com.restful.shop.resouces.OrderResourse;
import com.restful.shop.resouces.ProductResource;

@ApplicationPath("/api")
public class ShoppingApplication extends Application {

	private Set<Object> singletons = new HashSet<>();
	private Set<Class<?>> empty = new HashSet<Class<?>>();
	
	  public ShoppingApplication() {
	      singletons.add(new CustomerResource());
	      singletons.add(new LocalDatabaseService());
	      singletons.add(new CarResource());
	      singletons.add(new AdvancedCustomerService());
	      singletons.add(new PrettyCustomerService());
	      singletons.add(new JacksonCustomerResource());
	      singletons.add(new StoreResource());
	      singletons.add(new OrderService());
	      singletons.add(new JacksonXMLResolver());
	      singletons.add(new com.restful.shop.resouces.CustomerResourse());
	      singletons.add(new com.restful.shop.resouces.OrderResourse());
	      singletons.add(new com.restful.shop.resouces.ProductResource());
	      singletons.add(new com.restful.shop.resouces.StoreResource());
	      
	      empty.add(EntityNotFoundExceptionMapper.class);
	      empty.add(ContentMD5Writer.class);
	      empty.add(MaxAgeFeature.class);
	      empty.add(NegativeIdExceptionMapper.class);
	      empty.add(JavaMarshaller.class);
	      empty.add(JacksonXMLMarshaller.class);
	      empty.add(JacksonXMLUnmarshaller.class);
	  }
	
	@Override 
	  public Set<Class<?>> getClasses() {
	        return empty;
	    }
	  
	  protected ApplicationContext springContext;

	   @Context
	   protected ServletContext servletContext;

	   public Set<Object> getSingletons() {
	      
	      return singletons;
	   }
	  
}
