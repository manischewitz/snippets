package com.restful.shop.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="line-item")
public class LineItem {

	private final long id;
	private final String product;
	private final BigDecimal cost;
	
	@JsonCreator
	public LineItem(@JsonProperty("id") long id, @JsonProperty("product") String product, @JsonProperty("cost") BigDecimal cost) {
		this.product = product;
		this.cost = cost;
		this.id = id;
	}
	
	public String getProduct() {
		return product;
	}
	
	public BigDecimal getCost() {
		return cost;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(product, cost);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LineItem other = (LineItem) obj;
		if (cost == null) {
			if (other.cost != null)
				return false;
		} else if (!cost.equals(other.cost))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "LineItem [product=" + product + ", cost=" + cost + "]";
	}

	public long getId() {
		return id;
	}
	
	
	
	
}
