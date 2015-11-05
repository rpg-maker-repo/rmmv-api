package com.trinary.rpgmaker.resource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.trinary.rpgmaker.ro.PluginBaseRO;
import com.trinary.rpgmaker.ro.PluginRO;
import com.trinary.rpgmaker.service.PluginBaseService;

@Path("/v1/base")
@Produces(MediaType.APPLICATION_JSON)
public class PluginBaseResource {
	@Inject
	PluginBaseService service;
	
	@GET
	public Response getAll() {
		return Response.ok(service.getAll()).build();
	}
	
	@POST
	public Response create(PluginBaseRO base) {
		return Response.ok(service.save(base)).build();
	}
	
	@GET
	@Path("/{id}")
	public Response get(@PathParam("id") Long id) {
		return Response.ok(service.getById(id)).build();
	}
	
	@GET
	@Path("/{id}/version")
	public Response getVersions(@PathParam("id") Long id) {
		return Response.ok(service.getVersions(id)).build();
	}
	
	@POST
	@Path("/{id}/version")
	public Response addVersion(@PathParam("id") Long id, PluginRO version) {
		return Response.ok(service.addVersion(id, version)).build();
	}
}