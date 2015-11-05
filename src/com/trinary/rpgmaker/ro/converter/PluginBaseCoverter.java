package com.trinary.rpgmaker.ro.converter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import com.trinary.ro.converter.ROConverter;
import com.trinary.rpgmaker.persistence.entity.PluginBase;
import com.trinary.rpgmaker.resource.PluginBaseResource;
import com.trinary.rpgmaker.ro.PluginBaseRO;
import com.trinary.rpgmaker.service.LinkGenerator;

public class PluginBaseCoverter extends ROConverter<PluginBaseRO, PluginBase> {
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
		
		return pluginBase;
	}

	@Override
	protected PluginBaseRO _addLinks(PluginBaseRO object) {
		try {
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("id", object.getId());
			
			Class<?> resource = PluginBaseResource.class;
			Method get = resource.getMethod("get", Long.class);
			Method getVersions = resource.getMethod("getVersions", Long.class);
			Method addVersions = resource.getMethod("addVersions", Long.class, List.class);
			object.addLink(generator.createLink(get, args).setRel("self"));
			object.addLink(generator.createLink(getVersions, args).setRel("get-versions"));
			object.addLink(generator.createLink(addVersions, args).setRel("add-versions"));
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