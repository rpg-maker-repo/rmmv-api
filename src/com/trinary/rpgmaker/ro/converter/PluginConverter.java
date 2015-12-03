package com.trinary.rpgmaker.ro.converter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.codec.binary.Base64;

import com.trinary.ro.converter.ROConverter;
import com.trinary.rpgmaker.persistence.entity.Plugin;
import com.trinary.rpgmaker.resource.PluginResource;
import com.trinary.rpgmaker.ro.PluginRO;
import com.trinary.rpgmaker.service.LinkGenerator;

@ApplicationScoped
public class PluginConverter extends ROConverter<PluginRO, Plugin> {
	@EJB LinkGenerator generator;
	
	@Override
	protected Plugin _convertRO(PluginRO ro) {
		Plugin entity = new Plugin();
		
		entity.setId(ro.getId());
		entity.setVersion(ro.getVersion());
		entity.setCompatibleRMVersion(ro.getCompatibleRMVersion());
		entity.setHash(ro.getHash());
		entity.setDateCreated(ro.getDateCreated());
		entity.setFilename(ro.getFilename());
		if (ro.getScript() != null) {
			entity.setScript(Base64.encodeBase64String(ro.getScript().getBytes()));
		}
		
		return entity;
	}

	@Override
	protected PluginRO _convertEntity(Plugin entity) {
		PluginRO ro = new PluginRO();
		
		ro.setId(entity.getId());
		if (entity.getBase() != null) {
			ro.setName(entity.getBase().getName());
			ro.setDescription(entity.getBase().getDescription());
		}
		ro.setVersion(entity.getVersion());
		ro.setCompatibleRMVersion(entity.getCompatibleRMVersion());
		if (entity.getBase() != null && entity.getBase().getAuthor() != null) {
			ro.setAuthor(entity.getBase().getAuthor().getUsername());
		}
		ro.setHash(entity.getHash());
		ro.setDateCreated(entity.getDateCreated());
		ro.setFilename(entity.getFilename());
		
		return ro;
	}

	@Override
	protected PluginRO _addLinks(PluginRO object) {
		try {
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("id", object.getId());
			
			Class<?> resource = PluginResource.class;
			Method get = resource.getMethod("get", Long.class);
			Method getScript = resource.getMethod("getScript", Long.class);
			Method addDependencies = resource.getMethod("addDependencies", Long.class, List.class);
			Method getDependencies = resource.getMethod("getDependencies", Long.class);
			object.addLink(generator.createLink(get, args).setRel("self"));
			object.addLink(generator.createLink(getScript, args).setRel("get-script"));
			object.addLink(generator.createLink(addDependencies, args).setRel("add-dependencies"));
			object.addLink(generator.createLink(getDependencies, args).setRel("get-dependencies"));
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