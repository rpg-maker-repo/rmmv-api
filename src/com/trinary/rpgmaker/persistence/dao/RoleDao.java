package com.trinary.rpgmaker.persistence.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.trinary.rpgmaker.persistence.entity.Role;

@Stateless
@Transactional
public class RoleDao extends GenericDao<Role, Long> {
	@PersistenceContext EntityManager em;
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	@Override
	public Class<Role> getEntityClass() {
		return Role.class;
	}
	
	public Role getRoleByName(String roleName) {
		TypedQuery<Role> query = em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class);
		query.setParameter("name", roleName);
		
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}