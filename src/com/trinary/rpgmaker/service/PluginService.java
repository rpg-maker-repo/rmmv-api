package com.trinary.rpgmaker.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.trinary.rpgmaker.persistence.dao.PluginDao;
import com.trinary.rpgmaker.persistence.entity.Plugin;
import com.trinary.rpgmaker.ro.PluginBaseRO;
import com.trinary.rpgmaker.ro.PluginRO;
import com.trinary.rpgmaker.ro.converter.PluginBaseConverter;
import com.trinary.rpgmaker.ro.converter.PluginConverter;

@RequestScoped
public class PluginService {
	@EJB PluginDao dao;
	@Inject PluginConverter converter;
	@Inject PluginBaseConverter baseConverter;
	
	public PluginRO save(PluginRO pluginRo) {
		// Set timestamp
		pluginRo.setDateCreated(new Date());
		
		// Find hash
		MessageDigest md;
		if (pluginRo.getScript() != null) {
			try {
				md = MessageDigest.getInstance("MD5");
				md.update(pluginRo.getScript().getBytes());
				String digest = Base64.encodeBase64String(md.digest());
				pluginRo.setHash(digest);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		
		Plugin plugin = converter.convertRO(pluginRo);
		return converter.convertEntity(dao.save(plugin));
	}
	
	public List<PluginRO> getAll() {
		return converter.convertEntityList(dao.getAll());
	}
	
	public PluginRO getById(Long id) {
		return converter.convertEntity(dao.get(id));
	}
	
	public String getScript(Long id) {
		return new String(Base64.decodeBase64(dao.getScript(id)));
	}
	
	public PluginRO addDependencies(Long id, List<PluginRO> dependencies) {
		return converter.convertEntity(
				dao.addDependencies(
						id, 
						converter.convertROList(dependencies)));
	}
	
	public List<PluginRO> getDependencies(Long id) {
		return converter.convertEntityList(dao.getDependencies(id));
	}

	public PluginBaseRO getBase(Long id) {
		return baseConverter.convertEntity(dao.getBase(id));
	}

	public List<PluginRO> getAllWithHash(String hash) {
		return converter.convertEntityList(dao.getAllWithHash(hash));
	}
}