package com.trinary.rpgmaker.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.trinary.rpgmaker.ro.PluginRO;
import com.trinary.rpgmaker.service.PluginService;

@Path("/v1/plugin")
@Api
@Produces(MediaType.APPLICATION_JSON)
public class PluginResource {
	@Inject
	PluginService service;
	
	@Path("/")
	@GET
	@ApiOperation(value = "Get all plugin versions")
	public Response getAll(@ApiParam(value="Filter by script MD5") @QueryParam("hash") String hash) {
		if (hash == null) {
			return Response.ok(service.getAll()).build();
		}
		
		return Response.ok(service.getAllWithHash(hash)).build();
	}
	
	@Path("/{id}")
	@GET
	@ApiOperation(value = "Get one plugin version by id")
	public Response get(@PathParam("id") Long id) {
		return Response.ok(service.getById(id)).build();
	}
	
	@Path("/{id}/script")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(value = "Get script payload")
	public Response getScript(@PathParam("id") Long id) {
		return Response.ok(service.getScript(id)).build();
	}
	
	@Path("/{id}/base")
	@GET
	@ApiOperation(value = "Get base plugin of version identified by id")
	public Response getBase(@PathParam("id") Long id) {
		return Response.ok(service.getBase(id)).build();
	}
	
	@Path("/{id}/dependency")
	@GET
	@ApiOperation(value = "Get all dependencies for version identified by id")
	public Response getDependencies(@PathParam("id") Long id) {
		return Response.ok(service.getDependencies(id)).build();
	}
	
	@Path("/{id}/dependency")
	@POST
	@ApiOperation(value = "Add one to many dependencies to version identified by id")
	public Response addDependencies(@PathParam("id") Long id, List<PluginRO> dependencies) {
		return Response.ok(service.addDependencies(id, dependencies)).build();
	}
}