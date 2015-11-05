package com.trinary.rpgmaker.persistence.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.trinary.rpgmaker.persistence.entity.Plugin;
import com.trinary.rpgmaker.persistence.entity.PluginBase;

@Stateless
public class PluginBaseDao extends GenericDao<PluginBase, Long> {
	@PersistenceContext
	EntityManager em;
	
	@EJB
	PluginDao pluginDao;
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	@Override
	public Class<PluginBase> getEntityClass() {
		return PluginBase.class;
	}

	public List<Plugin> getVersions(Long id) {
		PluginBase base = get(id);
		return new ArrayList<Plugin>(base.getVersions());
	}
	
	public PluginBase addVersions(Long id, List<Plugin> versions) {
		PluginBase base = get(id);
		
		List<Plugin> savedVersions = new ArrayList<Plugin>();
		for (Plugin version : versions) {
			if (version.getId() == 0) {
				version.setBase(base);
				version = pluginDao.save(version);
				savedVersions.add(version);
			} else {
				version = pluginDao.get(version.getId());
				version.setBase(base);
				version = pluginDao.update(version);
				savedVersions.add(version);
			}
		}
		base.getVersions().addAll(savedVersions);
		return save(base);
	}
}