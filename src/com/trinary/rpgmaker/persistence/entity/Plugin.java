package com.trinary.rpgmaker.persistence.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Plugin {
	@Id
	@GeneratedValue
	long id;
	
	@Column
	Date dateCreated;
	
	@Column
	String version;
	
	@Column
	String compatibleRMVersion;
	
	@Column
	String hash;
	
	@Column
	@Lob
	String script;
	
	@ManyToMany
	List<Plugin> dependencies;
	
	@ManyToMany(mappedBy="dependencies")
	List<Plugin> dependedOnBy;
	
	@ManyToOne
	PluginBase base;

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
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the compatibleRMVersion
	 */
	public String getCompatibleRMVersion() {
		return compatibleRMVersion;
	}

	/**
	 * @param compatibleRMVersion the compatibleRMVersion to set
	 */
	public void setCompatibleRMVersion(String compatibleRMVersion) {
		this.compatibleRMVersion = compatibleRMVersion;
	}

	/**
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * @param hash the hash to set
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}

	/**
	 * @return the script
	 */
	public String getScript() {
		return script;
	}

	/**
	 * @param script the script to set
	 */
	public void setScript(String script) {
		this.script = script;
	}

	/**
	 * @return the dependencies
	 */
	public List<Plugin> getDependencies() {
		return dependencies;
	}

	/**
	 * @param dependencies the dependencies to set
	 */
	public void setDependencies(List<Plugin> dependencies) {
		this.dependencies = dependencies;
	}

	/**
	 * @return the dependedOnBy
	 */
	public List<Plugin> getDependedOnBy() {
		return dependedOnBy;
	}

	/**
	 * @param dependedOnBy the dependedOnBy to set
	 */
	public void setDependedOnBy(List<Plugin> dependedOnBy) {
		this.dependedOnBy = dependedOnBy;
	}

	/**
	 * @return the base
	 */
	public PluginBase getBase() {
		return base;
	}

	/**
	 * @param base the base to set
	 */
	public void setBase(PluginBase base) {
		this.base = base;
	}
}