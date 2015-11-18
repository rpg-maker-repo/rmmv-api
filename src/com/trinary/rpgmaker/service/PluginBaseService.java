package com.trinary.rpgmaker.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.commons.codec.binary.Base64;

import com.trinary.rpgmaker.persistence.dao.PluginBaseDao;
import com.trinary.rpgmaker.persistence.entity.Plugin;
import com.trinary.rpgmaker.persistence.entity.PluginBase;
import com.trinary.rpgmaker.ro.PluginBaseRO;
import com.trinary.rpgmaker.ro.PluginRO;
import com.trinary.rpgmaker.ro.converter.PluginBaseConverter;
import com.trinary.rpgmaker.ro.converter.PluginConverter;

@RequestScoped
public class PluginBaseService {
	@Inject
	PluginBaseConverter pluginBaseConverter;
	@Inject
	PluginConverter pluginConverter;
	
	@EJB
	PluginBaseDao dao;
	
	public PluginBaseRO save(PluginBaseRO plugin) {
		plugin.setDateCreated(new Date());
		
		PluginBase base = pluginBaseConverter.convertRO(plugin);
		return pluginBaseConverter.convertEntity(dao.save(base));
	}
	
	public List<PluginBaseRO> getAll() {
		return pluginBaseConverter.convertEntityList(dao.getAll());
	}
	
	public PluginBaseRO getById(Long id) {
		return pluginBaseConverter.convertEntity(dao.get(id));
	}
	
	public List<PluginRO> getVersions(Long id) {
		return pluginConverter.convertEntityList(dao.getVersions(id));
	}
	
	public PluginRO addVersion(Long id, PluginRO versionRo) {
		// Set timestamp
		versionRo.setDateCreated(new Date());
		
		// Find hash
		MessageDigest md;
		if (versionRo.getScript() != null) {
			try {
				md = MessageDigest.getInstance("MD5");
				md.update(versionRo.getScript().getBytes());
				String digest = Base64.encodeBase64String(md.digest());
				versionRo.setHash(digest);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		
		Plugin version = pluginConverter.convertRO(versionRo);
		return pluginConverter.convertEntity(dao.addVersion(id, version));
	}

	public List<PluginRO> getLatestVersions(Long id) {
		List<PluginRO> versions = getVersions(id);
		List<PluginRO> latestVersion = new ArrayList<PluginRO>();
		latestVersion.add(versions.get(versions.size() - 1));
		
		return latestVersion;
	}
}