package com.trinary.rpgmaker.ro.converter;

import com.trinary.ro.converter.ROConverter;
import com.trinary.rpgmaker.persistence.entity.PluginBase;
import com.trinary.rpgmaker.ro.PluginBaseRO;

public class PluginBaseCoverter extends ROConverter<PluginBaseRO, PluginBase> {

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
		// TODO Auto-generated method stub
		return null;
	}

}
