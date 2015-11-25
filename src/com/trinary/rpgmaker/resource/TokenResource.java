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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import com.trinary.rpgmaker.persistence.entity.User;
import com.trinary.rpgmaker.ro.AuthenticationRO;
import com.trinary.rpgmaker.ro.TokenRO;
import com.trinary.rpgmaker.security.token.Token;
import com.trinary.rpgmaker.security.token.TokenExpiredException;
import com.trinary.rpgmaker.security.token.TokenInvalidException;
import com.trinary.rpgmaker.security.token.TokenManager;
import com.trinary.rpgmaker.service.UserService;

@Path("/v1/token")
@Api
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@PermitAll
public class TokenResource {
	@Inject UserService userService;
	@Inject TokenManager tokenManager;
	@Context SecurityContext securityContext;
	
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
	@GET
	@ApiOperation(value = "Refresh an access token")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Success", response=TokenRO.class)
	})
	public Response checkToken(@PathParam("tokenString") String tokenString) {
		TokenRO token = userService.checkToken(tokenString);
		
		if (token == null) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		return Response.ok(token).build();
	}
	
	@Path("/{tokenString}")
	@DELETE
	@ApiOperation(value = "Release an access token")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Success", response=TokenRO.class)
	})
	public Response releaseToken(@ApiParam(value="Access token to release") @PathParam("tokenString") String tokenString) {
		Token token = null;
		try {
			token = tokenManager.authenticateToken(tokenString);
		} catch (TokenInvalidException e) {
			return Response.status(Status.FORBIDDEN).build();
		} catch (TokenExpiredException e) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		User principal = (User)securityContext.getUserPrincipal();
		User tokenOwner = (User)token.getPrincipal();
		if (!securityContext.isUserInRole("SUPERUSER") && principal.getId() != tokenOwner.getId()) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		TokenRO tokenRO = userService.unauthenticateUser(tokenString);
		return Response.ok(tokenRO).build();
	}
}