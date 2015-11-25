package com.trinary.rpgmaker.ro.converter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;

import com.trinary.ro.converter.ROConverter;
import com.trinary.rpgmaker.persistence.entity.User;
import com.trinary.rpgmaker.resource.UserResource;
import com.trinary.rpgmaker.ro.UserRO;
import com.trinary.rpgmaker.service.LinkGenerator;

@ApplicationScoped
public class UserConverter extends ROConverter<UserRO, User> {
	@EJB LinkGenerator generator;
	
	@Override
	protected User _convertRO(UserRO ro) {
		User user = new User();
		user.setId(ro.getId());
		user.setDateCreated(ro.getDateCreated());
		user.setUsername(ro.getUsername());
		user.setPassword(ro.getPassword());
		
		return user;
	}

	@Override
	protected UserRO _convertEntity(User entity) {
		UserRO ro = new UserRO();
		ro.setId(entity.getId());
		ro.setUsername(entity.getUsername());
		ro.setDateCreated(entity.getDateCreated());
		
		return ro;
	}

	@Override
	protected UserRO _addLinks(UserRO object) {
		try {
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("id", object.getUsername());
			Method get      = UserResource.class.getMethod("get", String.class);
			Method getRoles = UserResource.class.getMethod("getRoles", String.class);
			Method addRole  = UserResource.class.getMethod("addRole", String.class, String.class);
			object.addLink(generator.createLink(get, args).setRel("self"));
			object.addLink(generator.createLink(getRoles, args).setRel("get-roles"));
			object.addLink(generator.createLink(addRole, args).setRel("add-role"));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		} catch (SecurityException e) {
			e.printStackTrace();
			return null;
		}
		return object;
	}

}