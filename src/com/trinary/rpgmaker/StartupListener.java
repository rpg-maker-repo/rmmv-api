package com.trinary.rpgmaker;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.trinary.rpgmaker.ro.UserRO;
import com.trinary.rpgmaker.service.BootstrapService;

public class StartupListener implements ServletContextListener {
	@Inject BootstrapService userService;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// Create new roles if not already created.
		List<String> declaredRoles = userService.getDeclaredRoles();
		if (!declaredRoles.contains("SUPERUSER")) {
			userService.createRole("SUPERUSER");
		}
		if (!declaredRoles.contains("DEVELOPER")) {
			userService.createRole("DEVELOPER");
		}
		
		// Create the user if not already existing
		UserRO userRO = userService.get("root");
		if (userRO == null) {
			userRO = new UserRO();
			userRO.setUsername("root");
			userRO.setPassword("CHANGEME");
			userRO = userService.createUser(userRO);
		}
		
		// Add roles if not already added
		List<String> userRoles = userService.getRoles(userRO.getId());
		if (!userRoles.contains("SUPERUSER")) {
			userService.addRole(userRO.getId(), "SUPERUSER");
		}
		if (!userRoles.contains("DEVELOPER")) {
			userService.addRole(userRO.getId(), "DEVELOPER");
		}
	}
}