package com.trinary.rpgmaker.persistence.dao;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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
	
	public List<PluginBase> getAll(Integer page, Integer pageSize, String search) {
		// Temporary (need to figure out a good search algorithm).
		return getAll(page, pageSize);
	}

	public List<Plugin> getVersions(Long id) {
		return getVersions(id, null, null);
	}
	
	public List<Plugin> getVersions(Long id, Integer page, Integer pageSize) {
		PluginBase base = get(id);
		TypedQuery<Plugin> query = em.createQuery("SELECT p FROM Plugin p WHERE base = :base", Plugin.class);
		
		if (page != null && page >= 0) {
			if (pageSize == null || pageSize < 0) {
				pageSize = 10;
			}
			
			Integer offset = (page - 1) * pageSize;
			return query.setParameter("base", base)
				.setMaxResults(pageSize)
				.setFirstResult(offset)
				.getResultList();
		}
		
		return query.setParameter("base", base)
				.getResultList();
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