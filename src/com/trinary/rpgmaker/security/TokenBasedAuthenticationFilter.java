package com.trinary.rpgmaker.security;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.trinary.rpgmaker.persistence.entity.User;
import com.trinary.rpgmaker.security.token.Token;
import com.trinary.rpgmaker.security.token.TokenExpiredException;
import com.trinary.rpgmaker.security.token.TokenInvalidException;
import com.trinary.rpgmaker.security.token.TokenManager;
import com.trinary.rpgmaker.service.UserService;

public class TokenBasedAuthenticationFilter implements Filter {
	@Inject TokenManager manager;
	@Inject UserService userService;
	
	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain next) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		
		String authHeader = httpRequest.getHeader("Authorization");
		if (authHeader == null) {
			next.doFilter(request, response);
			return;
		}
		
		String[] authHeaderParts = authHeader.split(" ");
		
		if (!authHeaderParts[0].trim().equals("Bearer")) {
			next.doFilter(request, response);
			return;
		}
		
		Token token = null;
		try {
			token = manager.authenticateToken(authHeaderParts[1]);
		} catch (TokenInvalidException e) {
			e.printStackTrace();
		} catch (TokenExpiredException e) {
			e.printStackTrace();
		}
		
		if (token != null) {
			User user = (User)token.getPrincipal();
			System.out.println(user.getRoleNames());
			next.doFilter(new UserRoleRequestWrapper(user, httpRequest), response);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {}

}
