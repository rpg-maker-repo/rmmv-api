package com.trinary.rpgmaker.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.trinary.rpgmaker.ro.AuthenticationRO;
import com.trinary.rpgmaker.ro.TokenRO;
import com.trinary.rpgmaker.service.UserService;

@Path("/v1/token")
@Api
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@PermitAll
public class TokenResource {
	@Inject
	UserService userService;
	
	@POST
	@ApiOperation(value = "Create an access token")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Success", response=TokenRO.class)
	})
	public Response createToken(@ApiParam(value="Username and password") AuthenticationRO authenticationRO) {
		TokenRO token = userService.authenticateUser(authenticationRO);
		
		return Response.ok(token).build();
	}
	
	@Path("/{tokenString}")
	@DELETE
	@ApiOperation(value = "Release an access token")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Success", response=TokenRO.class)
	})
	public Response releaseToken(@ApiParam(value="Access token to release") @PathParam("tokenString") String tokenString) {
		TokenRO token = userService.unauthenticateUser(tokenString);
		return Response.ok(token).build();
	}
}