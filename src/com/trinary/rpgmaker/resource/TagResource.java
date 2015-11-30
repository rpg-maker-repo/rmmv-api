package com.trinary.rpgmaker.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.trinary.rpgmaker.ro.PluginBaseRO;
import com.trinary.rpgmaker.ro.TagRO;
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
		@ApiResponse(code = 200, message = "Success", response=TagRO.class, responseContainer="List")
	})
	public Response getAll() {
		return Response.ok(service.getTags()).build();
	}
	
	@Path("/{tagString}")
	@GET
	@ApiOperation(value="Get tag defined by tagString")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Success", response=TagRO.class)
	})
	public Response get(@PathParam("tagString") String tagString) {
		return Response.ok(service.getTag(tagString)).build();
	}
	
	@Path("/{tagString}/plugin")
	@GET
	@ApiOperation(value="Get all plugins that fall under this tag")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Success", response=PluginBaseRO.class, responseContainer="List")
	})
	public Response getPlugins(@PathParam("tagString") String tagString) {
		return Response.ok(service.getPlugins(tagString)).build();
	}
}