package com.trinary.rpgmaker.persistence.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.trinary.rpgmaker.persistence.entity.Role;
import com.trinary.rpgmaker.persistence.entity.User;

@Stateless
@Transactional
public class UserDao extends GenericDao<User, Long> {
	@PersistenceContext EntityManager em;
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	@Override
	public Class<User> getEntityClass() {
		return User.class;
	}

	public User getUserByName(String username) {
		TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
		query.setParameter("username", username);
		
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Role> getRoles(Long id) {
		User user = get(id);
		return new ArrayList<Role>(user.getRoles());
	}
	
	public User addRole(Long id, Role role) {
		User user = get(id);
		
		if (user == null) {
			return null;
		}
		
		user.getRoles().add(role);
		return update(user);
	}
}