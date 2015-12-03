package com.trinary.rpgmaker.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.commons.codec.binary.Base64;

import com.trinary.rpgmaker.persistence.dao.RoleDao;
import com.trinary.rpgmaker.persistence.dao.UserDao;
import com.trinary.rpgmaker.persistence.entity.Role;
import com.trinary.rpgmaker.persistence.entity.User;
import com.trinary.rpgmaker.ro.AuthenticationRO;
import com.trinary.rpgmaker.ro.PluginBaseRO;
import com.trinary.rpgmaker.ro.TokenRO;
import com.trinary.rpgmaker.ro.UserRO;
import com.trinary.rpgmaker.ro.converter.PluginBaseConverter;
import com.trinary.rpgmaker.ro.converter.TokenConverter;
import com.trinary.rpgmaker.ro.converter.UserConverter;
import com.trinary.rpgmaker.security.token.Token;
import com.trinary.rpgmaker.security.token.TokenExpiredException;
import com.trinary.rpgmaker.security.token.TokenInvalidException;
import com.trinary.rpgmaker.security.token.TokenManager;

@RequestScoped
public class UserService {
	@EJB UserDao userDao;
	@EJB RoleDao roleDao;
	@Inject TokenManager tokenManager;
	@Inject UserConverter userConverter;
	@Inject TokenConverter tokenConverter;
	@Inject PluginBaseConverter pluginConverter;
	
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
	
	public TokenRO authenticateUser(AuthenticationRO authenticationRO) {
		User user = userDao.getUserByName(authenticationRO.getUsername());
		Token token = null;
		
		if (user.getPassword().equals(hashPassword(authenticationRO.getPassword(), user.getSalt()))) {
			token = tokenManager.createToken(user);
		}
		
		return tokenConverter.convertEntity(token);
	}
	
	public TokenRO unauthenticateUser(String tokenString) {
		return tokenConverter.convertEntity(tokenManager.releaseToken(tokenString));
	}
	
	public TokenRO checkToken(String tokenString) {
		try {
			return tokenConverter.convertEntity(tokenManager.authenticateToken(tokenString));
		} catch (TokenInvalidException e) {
			return null;
		} catch (TokenExpiredException e) {
			return null;
		}
	}
	
	public UserRO addRole(String username, String roleName) {
		User user = userDao.getUserByName(username);
		Role role = roleDao.getRoleByName(roleName);
		
		if (user.getRoleNames().contains(roleName)) {
			return userConverter.convertEntity(user);
		}
		
		if (role == null) {
			return userConverter.convertEntity(user);
		}
		
		user = userDao.addRole(user.getId(), role);
		return userConverter.convertEntity(user);
	}
	
	public List<String> getRoles(String username) {
		User user = userDao.getUserByName(username);
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

	public UserRO update(String username, UserRO userRo) {
		User user = userDao.getUserByName(username);
		user.setPassword(hashPassword(userRo.getPassword(), user.getSalt()));
		return userConverter.convertEntity(userDao.update(user));
	}

	// TODO Create get plugins by user
	public List<PluginBaseRO> getPlugins(String username, Integer page, Integer pageSize) {
		return pluginConverter.convertEntityList(userDao.getPlugins(username, page, pageSize));
	}

	// TODO Create update user roles
	public UserRO updateRoles(String username, List<String> roleStrings) {
		return null;
	}
}