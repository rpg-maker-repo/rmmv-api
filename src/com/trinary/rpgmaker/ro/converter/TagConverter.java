package com.trinary.rpgmaker.ro.converter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;

import com.trinary.ro.converter.ROConverter;
import com.trinary.rpgmaker.persistence.entity.Tag;
import com.trinary.rpgmaker.resource.TagResource;
import com.trinary.rpgmaker.ro.TagRO;
import com.trinary.rpgmaker.service.LinkGenerator;

@ApplicationScoped
public class TagConverter extends ROConverter<TagRO, Tag> {
	@EJB LinkGenerator generator;

	@Override
	protected Tag _convertRO(TagRO ro) {
		Tag tag = new Tag();
		tag.setValue(ro.getValue());
		
		return tag;
	}

	@Override
	protected TagRO _convertEntity(Tag entity) {
		TagRO tag = new TagRO();
		tag.setValue(entity.getValue());
		
		return tag;
	}

	@Override
	protected TagRO _addLinks(TagRO object) {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("tagString", object.getValue());
		Method getPlugins;
		try {
			getPlugins = TagResource.class.getMethod("getPlugins", String.class);
			object.addLink(generator.createLink(getPlugins, args).setRel("get-plugins"));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		} catch (SecurityException e) {
			e.printStackTrace();
			return null;
		}
		
		return object;
	}

}