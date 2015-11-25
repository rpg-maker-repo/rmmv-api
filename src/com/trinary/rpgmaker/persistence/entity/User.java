package com.trinary.rpgmaker.persistence.entity;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class User implements Principal {
	@Id
	@GeneratedValue
	long id;
	
	@Column
	Date dateCreated;
	
	@Column(unique=true)
	String username;
	
	@Column
	String password;
	
	@Column
	String salt;
	
	@ManyToMany(fetch=FetchType.EAGER)
	List<Role> roles;
	
	@OneToMany
	List<Plugin> plugins;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the dateCreated
	 */
	public Date getDateCreated() {
		return dateCreated;
	}

	/**
	 * @param dateCreated the dateCreated to set
	 */
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the salt
	 */
	public String getSalt() {
		return salt;
	}

	/**
	 * @param salt the salt to set
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}

	/**
	 * @return the plugins
	 */
	public List<Plugin> getPlugins() {
		return plugins;
	}

	/**
	 * @param plugins the plugins to set
	 */
	public void setPlugins(List<Plugin> plugins) {
		this.plugins = plugins;
	}

	/**
	 * @return the roles
	 */
	public List<Role> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	public List<String> getRoleNames() {
		List<String> roleNames = new ArrayList<String>();
		
		for (Role role : roles) {
			roleNames.add(role.getName());
		}
		
		return roleNames;
	}

	@Override
	public String getName() {
		return username;
	}
}