package com.trinary.rpgmaker.persistence.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.trinary.rpgmaker.persistence.entity.Plugin;
import com.trinary.rpgmaker.persistence.entity.PluginBase;

@Stateless
@Transactional
public class PluginDao extends GenericDao<Plugin, Long> {
	@PersistenceContext
	EntityManager em;
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	@Override
	public Class<Plugin> getEntityClass() {
		return Plugin.class;
	}
	
	public Plugin addDependencies(Long id, List<Plugin> dependencies) {
		Plugin plugin = get(id);
		
		if (plugin == null) {
			return null;
		}
		
		List<Plugin> plugins = new ArrayList<Plugin>();
		for (Plugin dependency : dependencies) {
			Plugin dep = get(dependency.getId());
			if (dep != null) {
				plugins.add(dep);
			}
		}
		plugin.getDependencies().addAll(plugins);
		return save(plugin);
	}
	
	public String getScript(Long id) {
		Plugin plugin = get(id);
		return plugin.getScript();
	}
	
	public List<Plugin> getDependencies(Long id) {
		Plugin plugin = get(id);
		List<Plugin> deps = new ArrayList<Plugin>(plugin.getDependencies());
		return deps;
	}
	
	public Plugin search() {
		return null;
	}

	public PluginBase getBase(Long id) {
		Plugin plugin = get(id);
		return plugin.getBase();
	}

	public List<Plugin> getAllWithHash(String hash) {
		TypedQuery<Plugin> query = this.getEntityManager().createQuery("SELECT p FROM Plugin p WHERE p.hash=:hash", Plugin.class);
		query.setParameter("hash", hash);
		return query.getResultList();
	}
}