package com.restful.shop.persistense;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@Entity(name = "PurchaseOrder")
@Table(name="orders")
@JacksonXmlRootElement(localName="order")
public class OrderEntity {

	@JacksonXmlProperty(isAttribute = true)
	private int id;
	private boolean cancelled;
	private List<LineItemEntity> lineItems = new ArrayList<LineItemEntity>();
	private BigDecimal total;
	private long date;
	private CustomerEntity customer;
	
	 @Id
	 @GeneratedValue
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void cancel(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	public List<LineItemEntity> getLineItems() {
		return Collections.unmodifiableList(lineItems);
	}
	
	public void setLineItems(List<LineItemEntity> lineItems) {
		this.lineItems = lineItems;
	}
	
	public BigDecimal getTotal() {
		return total;
	}
	
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	public Instant getDateAsInstant() {
		return Instant.ofEpochMilli(date);
	}
	
	public long getDate() {
		return date;
	}
	
	public void setDate(long date) {
		this.date = date;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	public CustomerEntity getCustomer() {
		return customer;
	}
	
	public void setCustomer(CustomerEntity customer) {
		this.customer = customer;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OrderEntity [id=");
		builder.append(id);
		builder.append(", cancelled=");
		builder.append(cancelled);
		builder.append(", lineItems=");
		builder.append(lineItems);
		builder.append(", total=");
		builder.append(total);
		builder.append(", date=");
		builder.append(date);
		builder.append(", customer=");
		builder.append(customer);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (cancelled ? 1231 : 1237);
		result = prime * result + ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + (int) (date ^ (date >>> 32));
		result = prime * result + id;
		result = prime * result + ((lineItems == null) ? 0 : lineItems.hashCode());
		result = prime * result + ((total == null) ? 0 : total.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderEntity other = (OrderEntity) obj;
		if (cancelled != other.cancelled)
			return false;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		if (date != other.date)
			return false;
		if (id != other.id)
			return false;
		if (lineItems == null) {
			if (other.lineItems != null)
				return false;
		} else if (!lineItems.equals(other.lineItems))
			return false;
		if (total == null) {
			if (other.total != null)
				return false;
		} else if (!total.equals(other.total))
			return false;
		return true;
	}
	
}
