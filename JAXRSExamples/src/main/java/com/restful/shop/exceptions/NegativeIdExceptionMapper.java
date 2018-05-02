package com.restful.shop.exceptions;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NegativeIdExceptionMapper implements ExceptionMapper<NegativeIdException> {

	@Override
	public Response toResponse(NegativeIdException exception) {
		return Response.status(Response.Status.NOT_FOUND).entity(exception.getMessage()).type("text/plain").build();
	}

}
