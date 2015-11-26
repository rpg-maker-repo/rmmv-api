package com.trinary.rpgmaker.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

import com.trinary.rpgmaker.persistence.dao.TagDao;
import com.trinary.rpgmaker.persistence.entity.Tag;

@RequestScoped
public class TagService {
	@EJB TagDao dao;
	
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
	
	public List<String> getTags() {
		List<Tag> tags = dao.getAll();
		List<String> strings = new ArrayList<String>();
		for (Tag tag : tags) {
			strings.add(tag.getValue());
		}
		return strings;
	}
}