package com.trinary.rpgmaker.persistence.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.trinary.rpgmaker.persistence.entity.Plugin;

@Stateless
public class PluginDao {
	@PersistenceContext
	EntityManager em;
	
	public Plugin save(Plugin plugin) {
		em.persist(plugin);
		return plugin;
	}
	
	public Plugin get(Long id) {
		return em.find(Plugin.class, id);
	}
	
	public List<Plugin> getAll() {
		TypedQuery<Plugin> query = em.createQuery("FROM Plugin p", Plugin.class);
		return query.getResultList();
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
	
	public List<Plugin> getDependencies(Long id) {
		Plugin plugin = get(id);
		List<Plugin> deps = new ArrayList<Plugin>(plugin.getDependencies());
		return deps;
	}
	
	public Plugin search() {
		return null;
	}
}