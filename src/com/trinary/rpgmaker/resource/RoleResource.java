package com.trinary.rpgmaker.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.trinary.rpgmaker.service.UserService;

@Path("/v1/role")
@Api
@Produces(MediaType.APPLICATION_JSON)
public class RoleResource {
	@Inject UserService userService;
	
	@GET
	@ApiOperation(value="Get all possible roles")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Success", response=String.class, responseContainer="List")
	})
	public Response getRoles() {
		return Response.ok(userService.getDeclaredRoles()).build();
	}
}