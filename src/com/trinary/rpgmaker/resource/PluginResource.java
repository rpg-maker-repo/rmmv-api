package com.trinary.rpgmaker.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.trinary.rpgmaker.ro.PluginBaseRO;
import com.trinary.rpgmaker.ro.PluginRO;
import com.trinary.rpgmaker.service.PluginService;

@Path("/v1/plugin")
@Api
@Produces(MediaType.APPLICATION_JSON)
@Stateless
@PermitAll
public class PluginResource {
	@Inject
	PluginService service;
	
	@Context
	SecurityContext securityContext;
	
	@GET
	@ApiOperation(value = "Get all plugin versions")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Success", response=PluginRO.class, responseContainer="List")
	})
	public Response getAll(@ApiParam(value="Filter by script MD5") @QueryParam("hash") String hash) {
		if (hash == null) {
			return Response.ok(service.getAll()).build();
		}
		
		return Response.ok(service.getAllWithHash(hash)).build();
	}
	
	@Path("/{id}")
	@GET
	@ApiOperation(value = "Get one plugin version by id")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Success", response=PluginRO.class)
	})
	public Response get(@PathParam("id") Long id) {
		return Response.ok(service.getById(id)).build();
	}
	
	@Path("/{id}/script")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(value = "Get script payload")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Success", response=String.class)
	})
	public Response getScript(@PathParam("id") Long id) {
		return Response.ok(service.getScript(id)).build();
	}
	
	@Path("/{id}/base")
	@GET
	@ApiOperation(value = "Get base plugin of version identified by id")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Success", response=PluginBaseRO.class)
	})
	public Response getBase(@PathParam("id") Long id) {
		return Response.ok(service.getBase(id)).build();
	}
	
	@Path("/{id}/dependency")
	@GET
	@ApiOperation(value = "Get all dependencies for version identified by id")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Success", response=PluginRO.class, responseContainer="List")
	})
	public Response getDependencies(@PathParam("id") Long id) {
		return Response.ok(service.getDependencies(id)).build();
	}
	
	@Path("/{id}/dependency")
	@POST
	@ApiOperation(
			value = "Add one to many dependencies to version identified by id",
			authorizations = {
					@Authorization(value = "basic")
			})
	@ApiResponses({
		@ApiResponse(code = 200, message = "Success", response=PluginRO.class)
	})
	@RolesAllowed("Developer")
	public Response addDependencies(@PathParam("id") Long id, List<PluginRO> dependencies) {
		return Response.ok(service.addDependencies(id, dependencies)).build();
	}
}