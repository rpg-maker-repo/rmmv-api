package com.trinary.rpgmaker.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.trinary.rpgmaker.persistence.entity.Role;
import com.trinary.rpgmaker.persistence.entity.User;

public class TokenBasedAuthenticationFilter implements Filter {

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
		
		if (!authHeaderParts[0].trim().equals("SimpleToken")) {
			next.doFilter(request, response);
			return;
		}
		
		// Token stuff here
		
		// Start temporary test stuff
		Role userRole = new Role();
		userRole.setName("USER");
		Role adminRole = new Role();
		adminRole.setName("ADMIN");
		
		List<Role> roles = new ArrayList<Role>();
		roles.add(userRole);
		roles.add(adminRole);
		
		User user = new User();
		user.setUsername("deusprogrammer");
		user.setRoles(roles);
		// End temporary test stuff
		
		next.doFilter(new UserRoleRequestWrapper(user, httpRequest), response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {}

}
