package com.trinary.rpgmaker.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.trinary.rpgmaker.service.TagService;

@Path("/v1/tag")
@Api
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TagResource {
	@Inject TagService service;
	
	@GET
	@ApiOperation(value="Get all defined tags")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Success", response=String.class, responseContainer="List")
	})
	public Response getAll() {
		return Response.ok(service.getTags()).build();
	}
}