package com.trinary.rpgmaker.security;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.trinary.rpgmaker.persistence.entity.User;

public class UserRoleRequestWrapper extends HttpServletRequestWrapper {
	protected User user;
	protected HttpServletRequest originalRequest;

	public UserRoleRequestWrapper(User user, HttpServletRequest request) {
		super(request);
		this.user = user;
		this.originalRequest = request;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequestWrapper#getUserPrincipal()
	 */
	@Override
	public Principal getUserPrincipal() {
		if (user == null) {
			return originalRequest.getUserPrincipal();
		}
		
		return new Principal() {
			@Override
			public String getName() {
				return user.getUsername();
			}
		};
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequestWrapper#isUserInRole(java.lang.String)
	 */
	@Override
	public boolean isUserInRole(String role) {
		if (user.getRoles() == null || user.getRoles().isEmpty()) {
			return originalRequest.isUserInRole(role);
		}
		
		return user.getRoleNames().contains(role);
	}
}