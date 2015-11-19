package com.trinary.rpgmaker.handler;

import javax.ejb.EJBAccessException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EJBAccessExceptionHandler implements ExceptionMapper<EJBAccessException> {

	@Override
	public Response toResponse(EJBAccessException e) {
		return Response.status(Status.FORBIDDEN).build();
	}

}
