package com.trinary.rpgmaker.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.commons.codec.binary.Base64;

import com.trinary.rpgmaker.persistence.dao.PluginBaseDao;
import com.trinary.rpgmaker.persistence.dao.TagDao;
import com.trinary.rpgmaker.persistence.dao.UserDao;
import com.trinary.rpgmaker.persistence.entity.Plugin;
import com.trinary.rpgmaker.persistence.entity.PluginBase;
import com.trinary.rpgmaker.persistence.entity.Tag;
import com.trinary.rpgmaker.persistence.entity.User;
import com.trinary.rpgmaker.ro.PluginBaseRO;
import com.trinary.rpgmaker.ro.PluginRO;
import com.trinary.rpgmaker.ro.converter.PluginBaseConverter;
import com.trinary.rpgmaker.ro.converter.PluginConverter;

@RequestScoped
public class PluginBaseService {
	@Inject PluginBaseConverter pluginBaseConverter;
	@Inject PluginConverter pluginConverter;
	
	@EJB PluginBaseDao dao;
	@EJB TagDao tagDao;
	@EJB UserDao userDao;
	
	public PluginBaseRO save(PluginBaseRO plugin) {
		plugin.setDateCreated(new Date());
		PluginBase base = dao.save(pluginBaseConverter.convertRO(plugin));
		
		List<Tag> tags = new ArrayList<Tag>();
		if (plugin.getTags() != null) {
			for (String tagString : plugin.getTags()) {
				Tag tag = tagDao.findByName(tagString);
				if (tag == null) {
					tag = new Tag();
					tag.setValue(tagString);
					tag = tagDao.save(tag);
				}
				
				// Add this base plugin to this tag
				if (tag.getPlugins() == null) {
					tag.setPlugins(new ArrayList<PluginBase>());
				}
				tag.getPlugins().add(base);
				tag = tagDao.update(tag);
				
				tags.add(tag);
			}
			base.setTags(tags);
			base = dao.update(base);
		}
		
		return pluginBaseConverter.convertEntity(base);
	}
	
	public PluginBaseRO setOwner(PluginBaseRO plugin, Long userId) {
		PluginBase base = dao.get(plugin.getId());
		User owner = userDao.get(userId);
		base.setAuthor(owner);
		base = dao.update(base);
		userDao.addPlugin(owner.getUsername(), base.getId());
		return pluginBaseConverter.convertEntity(base);
	}
	
	public List<PluginBaseRO> getAll(Integer page, Integer pageSize, String search) {
		if (search == null) {
			return pluginBaseConverter.convertEntityList(dao.getAll(page, pageSize));
		}
		
		return pluginBaseConverter.convertEntityList(dao.search(search, page, pageSize));
	}
	
	public Long getCount() {
		return dao.getCount();
	}
	
	public PluginBaseRO getById(Long id) {
		return pluginBaseConverter.convertEntity(dao.get(id));
	}
	
	public List<PluginRO> getVersions(Long id) {
		return getVersions(id, null, null);
	}
	
	public List<PluginRO> getVersions(Long id, Integer page, Integer pageSize) {
		return pluginConverter.convertEntityList(dao.getVersions(id, page, pageSize));
	}
	
	public PluginRO addVersion(Long id, PluginRO versionRo) {
		// Set timestamp
		versionRo.setDateCreated(new Date());
		
		// Find hash
		MessageDigest md;
		if (versionRo.getScript() != null) {
			try {
				md = MessageDigest.getInstance("MD5");
				md.update(versionRo.getScript().getBytes());
				String digest = Base64.encodeBase64String(md.digest());
				versionRo.setHash(digest);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		
		Plugin version = pluginConverter.convertRO(versionRo);
		return pluginConverter.convertEntity(dao.addVersion(id, version));
	}
	
	public Long getVersionCount(Long id) {
		return dao.getVersionCount(id);
	}
	
	public PluginBaseRO addTags(Long id, List<String> tagStrings) {
		PluginBase base = dao.get(id);
		List<Tag> tags = new ArrayList<Tag>();
		PluginBaseRO baseRO = pluginBaseConverter.convertEntity(base);
		for (String tagString : tagStrings) {
			if (!baseRO.getTags().contains(tagString)) {
				Tag tag = tagDao.findByName(tagString);
				if (tag == null) {
					tag = new Tag();
					tag.setValue(tagString);
					tag = tagDao.save(tag);
				}
				
				// Add this base plugin to this tag
				if (tag.getPlugins() == null) {
					tag.setPlugins(new ArrayList<PluginBase>());
				}
				tag.getPlugins().add(base);
				tag = tagDao.update(tag);
				
				tags.add(tag);
			}
		}
		base.getTags().addAll(tags);
		dao.update(base);
		
		return pluginBaseConverter.convertEntity(base);
	}

	public List<PluginRO> getLatestVersions(Long id) {
		List<PluginRO> versions = getVersions(id);
		List<PluginRO> latestVersion = new ArrayList<PluginRO>();
		latestVersion.add(versions.get(versions.size() - 1));
		
		return latestVersion;
	}
	
	public List<String> getTags(Long id) {
		PluginBase base = dao.get(id);
		List<String> tagStrings = new ArrayList<String>();
		for (Tag tag : base.getTags()) {
			tagStrings.add(tag.getValue());
		}
		return tagStrings;
	}
}