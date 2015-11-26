package com.trinary.rpgmaker.ro.converter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;

import com.trinary.ro.converter.ROConverter;
import com.trinary.rpgmaker.persistence.entity.PluginBase;
import com.trinary.rpgmaker.persistence.entity.Tag;
import com.trinary.rpgmaker.resource.PluginBaseResource;
import com.trinary.rpgmaker.ro.PluginBaseRO;
import com.trinary.rpgmaker.ro.PluginRO;
import com.trinary.rpgmaker.service.LinkGenerator;

public class PluginBaseConverter extends ROConverter<PluginBaseRO, PluginBase> {
	@EJB LinkGenerator generator;
	
	@Override
	protected PluginBase _convertRO(PluginBaseRO ro) {
		PluginBase pluginBase = new PluginBase();
		pluginBase.setId(ro.getId());
		pluginBase.setName(ro.getName());
		pluginBase.setDescription(ro.getDescription());
		pluginBase.setDateCreated(ro.getDateCreated());
		
		return pluginBase;
	}

	@Override
	protected PluginBaseRO _convertEntity(PluginBase entity) {
		PluginBaseRO pluginBase = new PluginBaseRO();
		pluginBase.setId(entity.getId());
		pluginBase.setName(entity.getName());
		pluginBase.setDescription(entity.getDescription());
		pluginBase.setDateCreated(entity.getDateCreated());
		
		pluginBase.setTags(new ArrayList<String>());
		for (Tag tag : entity.getTags()) {
			pluginBase.getTags().add(tag.getValue());
		}
		
		return pluginBase;
	}

	@Override
	protected PluginBaseRO _addLinks(PluginBaseRO object) {
		try {
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("id", object.getId());
			
			Class<?> resource = PluginBaseResource.class;
			Method get = resource.getMethod("get", Long.class);
			Method getVersions = resource.getMethod("getVersions", Long.class, Boolean.class);
			Method addVersion = resource.getMethod("addVersion", Long.class, PluginRO.class);
			object.addLink(generator.createLink(get, args).setRel("self"));
			object.addLink(generator.createLink(getVersions, args).setRel("get-versions"));
			object.addLink(generator.createLink(addVersion, args).setRel("add-versions"));
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