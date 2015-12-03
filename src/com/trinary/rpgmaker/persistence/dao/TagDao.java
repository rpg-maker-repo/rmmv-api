package com.trinary.rpgmaker.persistence.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.trinary.rpgmaker.persistence.entity.Tag;

@Stateless
@Transactional
public class TagDao extends GenericDao<Tag, Long> {
	@PersistenceContext
	EntityManager em;

	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	@Override
	public Class<Tag> getEntityClass() {
		return Tag.class;
	}
	
	public Tag findByName(String name) {
		try {
			TypedQuery<Tag> query = em.createQuery("SELECT t FROM Tag t WHERE value=:name", Tag.class);
			query.setParameter("name", name);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}