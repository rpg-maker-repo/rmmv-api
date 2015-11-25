package com.trinary.rpgmaker.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.trinary.rpgmaker.ro.UserRO;
import com.trinary.rpgmaker.service.UserService;

@Path("/v1/user")
@Api
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@PermitAll
public class UserResource {
	@Inject UserService userService;
	@Context SecurityContext securityContext;

	@POST
	@ApiOperation(value = "Create a user")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Success", response=UserRO.class)
	})
	@RolesAllowed("SUPERUSER")
	public Response createUser(@ApiParam(value="User RO to be created") UserRO userRO) {
		userRO = userService.createUser(userRO);
		return Response.ok(userRO).build();
	}
	
	@GET
	@ApiOperation(value = "Get all users")
	public Response getAll() {		
		List<UserRO> users = userService.getAll();
		return Response.ok(users).build();
	}
	
	@Path("/{username}")
	@GET
	@ApiOperation(value = "Get a user by id")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Success", response=UserRO.class)
	})
	public Response get(@PathParam("username") String username) {
		UserRO user = userService.get(username);
		return Response.ok(user).build();
	}
	
	@Path("/{username}")
	@PUT
	@ApiOperation(value = "Update a user")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Success", response=UserRO.class)
	})
	public Response update(@PathParam("username") String username) {
		UserRO user = userService.update(username);
		return Response.ok(user).build();
	}
	
	@Path("/{username}/role")
	@POST
	@ApiOperation(value = "Add a named role to a user")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Success", response=UserRO.class)
	})
	@RolesAllowed("SUPERUSER")
	public Response addRole(@PathParam("username") String username, String roleName) {
		UserRO user = userService.addRole(username, roleName);
		return Response.ok(user).build();
	}
	
	@Path("/{username}/role")
	@GET
	@ApiOperation(value = "Get roles for named user")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Success", response=String.class, responseContainer="List")
	})
	public Response getRoles(@PathParam("username") String username) {
		List<String> roles = userService.getRoles(username);
		return Response.ok(roles).build();
	}
}