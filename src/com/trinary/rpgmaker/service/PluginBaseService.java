package com.trinary.rpgmaker.service;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.trinary.rpgmaker.persistence.dao.PluginBaseDao;
import com.trinary.rpgmaker.persistence.entity.Plugin;
import com.trinary.rpgmaker.persistence.entity.PluginBase;
import com.trinary.rpgmaker.ro.PluginBaseRO;
import com.trinary.rpgmaker.ro.PluginRO;
import com.trinary.rpgmaker.ro.converter.PluginBaseCoverter;
import com.trinary.rpgmaker.ro.converter.PluginConverter;

@RequestScoped
public class PluginBaseService {
	@Inject
	PluginBaseCoverter pluginBaseConverter;
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
	
	public PluginBaseRO addVersions(Long id, List<PluginRO> versionRoList) {
		List<Plugin> versions = pluginConverter.convertROList(versionRoList);
		return pluginBaseConverter.convertEntity(dao.addVersions(id, versions));
	}
}