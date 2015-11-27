package com.trinary.rpgmaker.resource;

import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
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
import com.trinary.rpgmaker.service.PluginBaseService;

@Path("/v1/base")
@Api
@Produces(MediaType.APPLICATION_JSON)
@PermitAll
public class PluginBaseResource {
	@Inject
	PluginBaseService service;
	
	@Context
	SecurityContext securityContext;
	
	@GET
	@ApiOperation(value = "Get all plugins")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Success", response=PluginBaseRO.class, responseContainer="List")
	})
	public Response getAll() {
		return Response.ok(service.getAll()).build();
	}
	
	@POST
	@ApiOperation(value = "Create a plugin",
			authorizations = {
			@Authorization(value = "basic")
	})
	@ApiResponses({
		@ApiResponse(code = 200, message = "Success", response=PluginBaseRO.class)
	})
	@RolesAllowed("DEVELOPER")
	public Response create(PluginBaseRO base) {
		return Response.ok(service.save(base)).build();
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(value = "Find a plugin by id")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Success", response=PluginBaseRO.class)
	})
	public Response get(@PathParam("id") Long id) {
		return Response.ok(service.getById(id)).build();
	}
	
	@GET
	@Path("/{id}/version")
	@ApiOperation(value = "Get all plugin versions for plugin identified by id")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Success", response=PluginRO.class, responseContainer="List")
	})
	public Response getVersions(@PathParam("id") Long id, @QueryParam("latest") Boolean latest) {
		if (latest != null && latest) {
			return Response.ok(service.getLatestVersions(id)).build();
		}
		
		return Response.ok(service.getVersions(id)).build();
	}
	
	@POST
	@Path("/{id}/version")
	@ApiOperation(value = "Create a version of plugin identified by id",
			authorizations = {
			@Authorization(value = "basic")
	})
	@ApiResponses({
		@ApiResponse(code = 200, message = "Success", response=PluginRO.class)
	})
	@RolesAllowed("DEVELOPER")
	public Response addVersion(@PathParam("id") Long id, PluginRO version) {
		return Response.ok(service.addVersion(id, version)).build();
	}
	
	@POST
	@Path("/{id}/tag")
	@ApiOperation(value = "Add tags to plugin identified by id")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Success", response=PluginRO.class)
	})
	@RolesAllowed("DEVELOPER")
	public Response addTag(@PathParam("id") Long id, List<String> tagStrings) {
		return Response.ok(service.addTags(id, tagStrings)).build();
	}
	
	@GET
	@Path("/{id}/tag")
	@ApiOperation(value = "Get tags for plugin identified by id")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Success", response=String.class, responseContainer="List")
	})
	public Response getTags(@PathParam("id") Long id) {
		return Response.ok(service.getTags(id)).build();
	}
}