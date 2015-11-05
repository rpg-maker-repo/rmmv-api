package com.trinary.rpgmaker.persistence.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public abstract class GenericDao<T, U> {
	public abstract EntityManager getEntityManager();
	public abstract Class<T> getEntityClass();
	
	public T save(T object) {
		getEntityManager().persist(object);
		return object;
	}
	
	public T get(U id) {
		return getEntityManager().find(getEntityClass(), id);
	}
	
	public List<T> getAll() {
		return getAll(null, null);
	}
	
	public List<T> getAll(Integer page, Integer pageSize) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
        Root<T> rootEntry = cq.from(getEntityClass());
        CriteriaQuery<T> all = cq.select(rootEntry);
        TypedQuery<T> allQuery = getEntityManager().createQuery(all);
        
        if (page == null || page <= 0) {
        	return allQuery.getResultList();
        }
        
        if (pageSize == null) {
        	pageSize = 10;
        }
        
        return allQuery
        		.setFirstResult((page - 1) * pageSize)
        		.setMaxResults(pageSize)
        		.getResultList();
	}
}