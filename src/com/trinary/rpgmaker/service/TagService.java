package com.trinary.rpgmaker.service;

import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.trinary.rpgmaker.persistence.dao.TagDao;
import com.trinary.rpgmaker.persistence.entity.Tag;
import com.trinary.rpgmaker.ro.PluginBaseRO;
import com.trinary.rpgmaker.ro.TagRO;
import com.trinary.rpgmaker.ro.converter.PluginBaseConverter;
import com.trinary.rpgmaker.ro.converter.TagConverter;

@RequestScoped
public class TagService {
	@EJB TagDao dao;
	@Inject PluginBaseConverter pluginConverter;
	@Inject TagConverter tagConverter;
	
	public Boolean tagExists(String tagString) {
		Tag tag = dao.findByName(tagString);
		return tag != null;
	}
	
	public void createTag(String tagString) {
		if (!tagExists(tagString)) {
			Tag tag = new Tag();
			tag.setValue(tagString);
			dao.save(tag);
		}
	}
	
	public List<TagRO> getTags() {
		List<Tag> tags = dao.getAll();
		return tagConverter.convertEntityList(tags);
	}
	
	public TagRO getTag(String tagString) {
		return tagConverter.convertEntity(dao.findByName(tagString));
	}

	public List<PluginBaseRO> getPlugins(String tagString) {
		Tag tag = dao.findByName(tagString);
		
		if (tag == null) {
			return Collections.emptyList();
		}
		
		return pluginConverter.convertEntityList(tag.getPlugins());
	}
}