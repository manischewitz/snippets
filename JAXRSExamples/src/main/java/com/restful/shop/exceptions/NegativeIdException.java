package com.restful.shop.exceptions;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NegativeIdException extends RuntimeException {

	private static final long serialVersionUID = -5185972194816313921L;
	
	public static final int CODE = 1;
	
	public NegativeIdException (String message) {
		super(message);
	}

}
