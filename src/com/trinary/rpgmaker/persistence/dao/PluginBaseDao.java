package com.trinary.rpgmaker.persistence.dao;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

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
	
	public Long getCount() {
		TypedQuery<Long> query = em.createQuery("SELECT COUNT(pb) FROM PluginBase pb", Long.class);
		return query.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<PluginBase> search(String search, Integer page, Integer pageSize) {
		FullTextEntityManager fem = Search.getFullTextEntityManager(em);
		QueryBuilder qb = fem.getSearchFactory().buildQueryBuilder().forEntity(PluginBase.class).get();
		Query luceneQuery = qb
				.keyword()
				.onFields("name", "description", "tags.value")
				.matching(search)
				.createQuery();
		
		FullTextQuery jpaQuery =
			    fem.createFullTextQuery(luceneQuery, PluginBase.class);
		
		if (page != null && page >= 0) {
			if (pageSize == null || pageSize < 0) {
				pageSize = 10;
			}
			
			Integer offset = (page - 1) * pageSize;
			return jpaQuery
				.setFirstResult(offset)
				.setMaxResults(pageSize)
				.getResultList();
		}
		
		return jpaQuery.getResultList();
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
	
	public Long getVersionCount(Long id) {
		PluginBase base = get(id);
		
		TypedQuery<Long> query = em.createQuery("SELECT COUNT(p) FROM Plugin p WHERE base = :base", Long.class);
		return query.setParameter("base", base).getSingleResult();
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