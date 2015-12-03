package com.trinary.rpgmaker.persistence.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.trinary.rpgmaker.persistence.entity.PluginBase;
import com.trinary.rpgmaker.persistence.entity.Role;
import com.trinary.rpgmaker.persistence.entity.User;

@Stateless
@Transactional
public class UserDao extends GenericDao<User, Long> {
	@PersistenceContext EntityManager em;
	@EJB PluginBaseDao pluginDao;
	
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
		
		if (user.getRoles() == null) {
			user.setRoles(new ArrayList<Role>());
		}
		
		user.getRoles().add(role);
		return update(user);
	}

	public List<PluginBase> getPlugins(String username, Integer page, Integer pageSize) {
		TypedQuery<PluginBase> query = em
				.createQuery("SELECT pb FROM PluginBase pb WHERE author.username=:username", PluginBase.class);
		query.setParameter("username", username);
		
		if (page != null && page >= 0) {
			if (pageSize == null || pageSize < 0) {
				pageSize = 10;
			}
			
			Integer offset = (page - 1) * pageSize;
			return query
				.setMaxResults(pageSize)
				.setFirstResult(offset)
				.getResultList();
		}
		
		return query.getResultList();
	}
	
	public User addPlugin(String username, Long pluginId) {
		PluginBase base = pluginDao.get(pluginId);
		User user = this.getUserByName(username);
		
		if (user.getPlugins() == null) {
			user.setPlugins(new ArrayList<PluginBase>());
		}
		
		user.getPlugins().add(base);
		return update(user);
	}
}