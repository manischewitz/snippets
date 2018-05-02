package com.restful.shop.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.print.attribute.standard.RequestingUserName;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

@JacksonXmlRootElement(localName="order")
public class Order {

	@JacksonXmlProperty(isAttribute = true)
	private final long id;
	private final boolean cancelled;
	private final List<LineItem> lineItems;
	private final BigDecimal total;
	private final String date;
	private final Customer customer;
	
	@JsonCreator
	public Order(@JsonProperty("id") long id, 
			@JsonProperty("cancelled") boolean cancelled, 
			@JsonProperty("date") String date, 
			@JsonProperty("customer") Customer customer,
			@JsonProperty("lineItems") List<LineItem> lineItems) {
		this.id = id;
		this.cancelled = cancelled;
		this.lineItems = lineItems;
		//this.total = getTotal(new BigDecimal(0), lineItems, 0);
		this.total = lineItems.stream().collect(Collectors.reducing(new BigDecimal(0), LineItem::getCost, BigDecimal::add));
		this.date = date;
		this.customer = customer;
	}
	
	private static BigDecimal getTotal(BigDecimal price, List<LineItem> lineItems, int counter) {
		if (counter == lineItems.size()) {
			return price;
		}
		return getTotal(price.add(lineItems.get(counter).getCost()), lineItems, ++counter);
	}
	
	private Order(long id, boolean cancelled, List<LineItem> lineItems, BigDecimal total, String date, Customer customer) {
		this.id = id;
		this.cancelled = cancelled;
		this.lineItems = lineItems;
		this.total = total;
		this.date = date;
		this.customer = customer;
	}

	public Order cancel() {
		return new Order(id, true, lineItems, total, date, customer);
	}
	
	public Order addLineItem(LineItem lineItem) {
		lineItems.add(lineItem);
		return new Order(id, cancelled, date, customer, lineItems);
	}
	
	public Order addAll(Collection<LineItem> items) {
		lineItems.addAll(items);
		return new Order(id, cancelled, date, customer, lineItems);
	}
	
	public long getId() {
		return id;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public List<LineItem> getLineItems() {
		return Collections.unmodifiableList(lineItems);
	}
	
	public String getTotal() {
		return total.toString();
	}
	
	public String getDate() {
		return date;
	}
	
	public Customer getCustomer() {
		return customer;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, customer.getId(), lineItems, cancelled);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (cancelled != other.cancelled)
			return false;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id != other.id)
			return false;
		if (total == null) {
			if (other.total != null)
				return false;
		} else if (!total.equals(other.total))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Order [id=" + id + ", cancelled=" + cancelled + ", total=" + total + ", date=" + date + ", customer="
				+ customer + "]" + lineItems;
	}
	
	public static void main(String... strings) throws Exception {
		ObjectMapper m = new XmlMapper();
		m.enable(SerializationFeature.INDENT_OUTPUT);
		Customer customer = new Customer(1, "John", "Doe", "Main street", "Moscow", "Russia", "64128", "RU");
		LineItem item = new LineItem(-1, "Soap", new BigDecimal(200.0));
		   Order order = new Order(-1, false, new Date().toString(), customer, new ArrayList<LineItem>() { { add(item); } });
		System.out.println(m.writeValueAsString(order));
		Order order1 = m.readValue(m.writeValueAsString(order), Order.class);
		System.out.println(order1);
		
		Orders orders = new Orders();
		orders.add(order);
		System.out.println(m.writeValueAsString(orders));
		
	}
	
}
