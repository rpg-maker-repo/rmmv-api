package com.trinary.rpgmaker.resource;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.trinary.rpgmaker.ro.PluginRO;
import com.trinary.rpgmaker.service.PluginService;

@Path("/v1/plugin")
@Produces(MediaType.APPLICATION_JSON)
public class PluginResource {
	@Inject
	PluginService service;
	
	@Path("/")
	@GET
	public Response getAll() {
		return Response.ok(service.getAll()).build();
	}
	
	@Path("/{id}")
	@GET
	public Response get(@PathParam("id") Long id) {
		return Response.ok(service.getById(id)).build();
	}
	
	@Path("/{id}/script")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response getScript(@PathParam("id") Long id) {
		return Response.ok(service.getScript(id)).build();
	}
	
	@Path("/{id}/dependency")
	@GET
	public Response getDependencies(@PathParam("id") Long id) {
		return Response.ok(service.getDependencies(id)).build();
	}
	
	@Path("/{id}/dependency")
	@POST
	public Response addDependencies(@PathParam("id") Long id, List<PluginRO> dependencies) {
		return Response.ok(service.addDependencies(id, dependencies)).build();
	}
}