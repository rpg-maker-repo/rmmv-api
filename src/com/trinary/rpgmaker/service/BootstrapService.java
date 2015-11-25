package com.trinary.rpgmaker.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.codec.binary.Base64;

import com.trinary.rpgmaker.persistence.dao.RoleDao;
import com.trinary.rpgmaker.persistence.dao.UserDao;
import com.trinary.rpgmaker.persistence.entity.Role;
import com.trinary.rpgmaker.persistence.entity.User;
import com.trinary.rpgmaker.ro.UserRO;
import com.trinary.rpgmaker.ro.converter.UserConverter;
import com.trinary.rpgmaker.security.token.TokenManager;

@Singleton
public class BootstrapService {
	@EJB UserDao userDao;
	@EJB RoleDao roleDao;
	@Inject TokenManager tokenManager;
	@Inject UserConverter userConverter;
	
	public List<UserRO> getAll() {
		return userConverter.convertEntityList(userDao.getAll());
	}
	
	public UserRO get(Long id) {
		return userConverter.convertEntity(userDao.get(id));
	}
	
	public UserRO get(String username) {
		return userConverter.convertEntity(userDao.getUserByName(username));
	}
	
	public UserRO createUser(UserRO userRO) {
		User user = userConverter.convertRO(userRO);
		user.setDateCreated(new Date());
		user.setSalt(generateSalt(user));
		
		user.setPassword(hashPassword(user.getPassword(), user.getSalt()));
		userDao.save(user);
		
		return userConverter.convertEntity(user);
	}
	
	public UserRO addRole(Long id, String roleName) {
		Role role = roleDao.getRoleByName(roleName);
		User user = userDao.get(id);
		
		if (role == null) {
			return userConverter.convertEntity(user);
		}
		
		user = userDao.addRole(id, role);
		return userConverter.convertEntity(user);
	}
	
	public List<String> getRoles(Long id) {
		User user = userDao.get(id);
		List<Role> roles = userDao.getRoles(id);
		user.setRoles(roles);
		return user.getRoleNames();
	}
	
	public String createRole(String roleName) {
		Role role = new Role();
		role.setName(roleName);
		roleDao.save(role);
		
		return roleName;
	}
	
	public List<String> getDeclaredRoles() {
		List<Role> roles = roleDao.getAll();
		List<String> roleNames = new ArrayList<String>();
		
		for (Role role : roles) {
			roleNames.add(role.getName());
		}
		
		return roleNames;
	}
	
	protected String hashPassword(String password, String salt) {
		MessageDigest md = null;
		String seperator = ":";
		
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			return password;
		}
		
		md.update(password.getBytes());
		md.update(seperator.getBytes());
		md.update(salt.getBytes());
		
		return Base64.encodeBase64String(md.digest());
	}
	
	protected String generateSalt(User user) {
		MessageDigest md = null;
		
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		sb
			.append(user.getUsername())
			.append(":")
			.append(user.getPassword())
			.append(":")
			.append(new Date().toString())
			.append(":")
			.append(user.getId())
			.append(":")
			.append(UUID.randomUUID().toString());
		
		md.update(sb.toString().getBytes());
		
		return Base64.encodeBase64String(md.digest());
	}
}