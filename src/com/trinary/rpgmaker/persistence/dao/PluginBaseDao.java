package com.trinary.rpgmaker.persistence.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.trinary.rpgmaker.persistence.entity.Plugin;
import com.trinary.rpgmaker.persistence.entity.PluginBase;

@Stateless
@Transactional
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
	
	public Plugin addVersion(Long id, Plugin version) {
		PluginBase base = get(id);
		version.setBase(base);
		version = pluginDao.save(version);
		base.getVersions().add(version);
		save(base);
		
		return version;
	}
}