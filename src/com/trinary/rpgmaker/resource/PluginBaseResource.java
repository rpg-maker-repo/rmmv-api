package com.trinary.rpgmaker.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.trinary.rpgmaker.ro.PluginBaseRO;
import com.trinary.rpgmaker.ro.PluginRO;
import com.trinary.rpgmaker.service.PluginBaseService;

@Path("/v1/base")
@Api
@Produces(MediaType.APPLICATION_JSON)
public class PluginBaseResource {
	@Inject
	PluginBaseService service;
	
	@GET
	@ApiOperation(value = "Get all plugins")
	public Response getAll() {
		return Response.ok(service.getAll()).build();
	}
	
	@POST
	@ApiOperation(value = "Create a plugin")
	public Response create(PluginBaseRO base) {
		return Response.ok(service.save(base)).build();
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(value = "Find a plugin by id")
	public Response get(@PathParam("id") Long id) {
		return Response.ok(service.getById(id)).build();
	}
	
	@GET
	@Path("/{id}/version")
	@ApiOperation(value = "Get all plugin versions for plugin identified by id")
	public Response getVersions(@PathParam("id") Long id, @QueryParam("latest") Boolean latest) {
		if (latest != null && latest) {
			return Response.ok(service.getLatestVersions(id)).build();
		}
		
		return Response.ok(service.getVersions(id)).build();
	}
	
	@POST
	@Path("/{id}/version")
	@ApiOperation(value = "Create a version of plugin identified by id")
	public Response addVersion(@PathParam("id") Long id, PluginRO version) {
		return Response.ok(service.addVersion(id, version)).build();
	}
}