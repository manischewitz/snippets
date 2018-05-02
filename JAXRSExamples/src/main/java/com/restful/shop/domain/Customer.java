package com.restful.shop.domain;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import javax.ws.rs.BadRequestException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 *  Immutable, setter methods create new objects, getters never return null, empty strings and negative values.
 *  @author Pavel F.
 *  @version 1.0
 * 
 **/

@JacksonXmlRootElement(localName="customer")
public final class Customer implements Serializable {
	//Optional<Long> ID;
	
	private static final long serialVersionUID = -9191007161287102649L;

	@JacksonXmlProperty(isAttribute = true)
	private final long id;
	
	@JsonProperty("first-name")
	private final String firstName;
	
	@JsonProperty("last-name")
	private final String lastName;
	private final String street;
	private final String city;
	private final String state;
	private final String zip;
	private final String country;
	@JsonProperty("last-modified")
	private final String lastModified;
	
	@JsonCreator
	private Customer(@JsonProperty("id") long id, 
			@JsonProperty("first-name") String firstName, 
			@JsonProperty("last-name") String lastName, 
			@JsonProperty("street") String street, 
			@JsonProperty("city") String city, 
			@JsonProperty("state") String state, 
			@JsonProperty("zip") String zip, 
			@JsonProperty("country") String country,
			@JsonProperty("last-modified") String lastModified) {
		if (id < 1 || firstName == null || lastName == null || street == null ||
				city == null || state == null || zip == null || country == null || lastModified == null) {
			throw new IllegalArgumentException();
		}
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.country = country;
		this.lastModified = lastModified;
	}
	
	public Customer(long id, String firstName, String lastName, String street, String city, String state, 
			String zip, String country) {
		this(id, firstName, lastName, street, city, state, zip, country, System.currentTimeMillis()+"");
	}
	
	public Customer(String id, String firstName, String lastName, String street, String city, String state, 
			String zip, String country) {
		this(validateAndGetId(id), firstName, lastName, street, city, state, zip, country,  System.currentTimeMillis()+"");
	}
	
	@JsonIgnore
	public Date getLastModifiedDate() {
		return new Date(Long.parseLong(lastModified));
	}

	public long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZip() {
		return zip;
	}

	public String getCountry() {
		return country;
	}
	
	public Customer setFirstName(String name) {
		return new Customer(id, name, lastName, street, city, state, zip, country);
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", street=" + street
				+ ", city=" + city + ", state=" + state + ", zip=" + zip + ", country=" + country + "]";
	}

	@Override
	public int hashCode() {
		return (int) id;
	}
	
	private static long validateAndGetId(String number) {
		long primitive;
		if (number != null && !number.trim().isEmpty()) {
			primitive = Long.valueOf(number);
		} else {
			throw new IllegalArgumentException ();
		}
		return primitive;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id != other.id)
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
			return false;
		if (zip == null) {
			if (other.zip != null)
				return false;
		} else if (!zip.equals(other.zip))
			return false;
		return true;
	}

	public String getLastModified() {
		return lastModified;
	}

}
